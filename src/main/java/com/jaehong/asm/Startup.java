package com.jaehong.asm;

import java.io.IOException;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

/**
 * This is the "agent class" that initializes the JMockit "Java agent". It is not intended for use in client code.
 *
 * @see #premain(String, Instrumentation)
 * @see #agentmain(String, Instrumentation)
 */
public final class Startup {
    public static boolean initializing;
    private static Instrumentation instrumentation;
    private static boolean initializedOnDemand;

    private Startup() {
    }

    /**
     * This method must only be called by the JVM, to provide the instrumentation object.
     * In order for this to occur, the JVM must be started with "-javaagent:jmockit.jar" as a command line parameter
     * (assuming the jar file is in the current directory).
     * <p>
     * It is also possible to load other <em>instrumentation tools</em> at this time, by having set the "jmockit-tools"
     * and/or "jmockit-mocks" system properties in the JVM command line.
     * There are two types of instrumentation tools:
     * <ol>
     * <li>A {@link ClassFileTransformer class file transformer}, which will be instantiated and added to the JVM
     * instrumentation service. Such a class must have a no-args constructor.</li>
     * <li>An <em>external mock</em>, which should be a {@code MockUp} subclass with a no-args constructor.
     * </ol>
     *
     * @param agentArgs not used
     * @param inst      the instrumentation service provided by the JVM
     */
    public static void premain(String agentArgs, Instrumentation inst) throws IOException {
        initialize(true, inst);
    }

    private static void initialize(boolean applyStartupMocks, Instrumentation inst) throws IOException {
        if (instrumentation == null) {
            instrumentation = inst;
        }
    }

    /**
     * This method must only be called by the JVM, to provide the instrumentation object.
     * This occurs only when the JMockit Java agent gets loaded on demand, through the Attach API.
     * <p>
     * For additional details, see the {@link #premain(String, Instrumentation)} method.
     *
     * @param agentArgs not used
     * @param inst      the instrumentation service provided by the JVM
     */
    public static void agentmain(@SuppressWarnings("unused") String agentArgs, Instrumentation inst)
            throws IOException {
        if (!inst.isRedefineClassesSupported()) {
            throw new UnsupportedOperationException(
                    "This JRE must be started in debug mode, or with -javaagent:<proper path>/jmockit.jar");
        }

        initialize(false, inst);

        ClassLoader customCL = (ClassLoader) System.getProperties().remove("jmockit-customCL");
    }

    public static Instrumentation instrumentation() {
        verifyInitialization();
        assert instrumentation != null;
        return instrumentation;
    }

    public static void verifyInitialization() {
        if (getInstrumentation() == null) {
            new AgentLoader().loadAgent();
            initializedOnDemand = true;
        }
    }

    private static Instrumentation getInstrumentation() {
        return instrumentation;
    }

    public static boolean initializeIfPossible() {
        System.out.println("initializeIfPossible");
        if (getInstrumentation() == null) {
            ClassLoader currentCL = Startup.class.getClassLoader();
            boolean usingCustomCL = currentCL != ClassLoader.getSystemClassLoader();

            if (usingCustomCL) {
                //noinspection UseOfPropertiesAsHashtable
                System.getProperties().put("jmockit-customCL", currentCL);
            }

            try {
                System.out.println("loadAgent");
                new AgentLoader().loadAgent();

                return true;
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }

            return false;
        }

        return true;
    }

    public static void retransformClass(Class<?> aClass) {
        try {
            instrumentation().retransformClasses(aClass);
        } catch (UnmodifiableClassException ignore) {
        }
    }

    public static void redefineMethods(Class<?> classToRedefine, byte[] modifiedClassfile) {
        redefineMethods(new ClassDefinition(classToRedefine, modifiedClassfile));
    }

    public static void redefineMethods(ClassDefinition... classDefs) {
        try {
            instrumentation().redefineClasses(classDefs);
        } catch (ClassNotFoundException e) {
            // should never happen
            throw new RuntimeException(e);
        } catch (UnmodifiableClassException e) {
            throw new RuntimeException(e);
        } catch (InternalError ignore) {
            // If a class to be redefined hasn't been loaded yet, the JVM may get a NoClassDefFoundError during
            // redefinition. Unfortunately, it then throws a plain InternalError instead.
            for (ClassDefinition classDef : classDefs) {
                detectMissingDependenciesIfAny(classDef.getDefinitionClass());
            }

            // If the above didn't throw upon detecting a NoClassDefFoundError, then ignore the original error and
            // continue, in order to prevent secondary failures.
        }
    }

    private static void detectMissingDependenciesIfAny(Class<?> mockedClass) {
        try {
            Class.forName(mockedClass.getName(), false, mockedClass.getClassLoader());
        } catch (NoClassDefFoundError e) {
            throw new RuntimeException("Unable to mock " + mockedClass + " due to a missing dependency", e);
        } catch (ClassNotFoundException ignore) {
            // Shouldn't happen since the mocked class would already have been found in the classpath.
        }
    }
}
