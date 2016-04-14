/*
 * Copyright (c) 2006 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package com.jaehong.asm;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import com.sun.tools.attach.spi.AttachProvider;
import sun.tools.attach.*;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public final class AgentLoader
{
   private static final AttachProvider ATTACH_PROVIDER = new AttachProvider() {
      @Override public String name() { return null; }
      @Override public String type() { return null; }
      @Override public VirtualMachine attachVirtualMachine(String id) { return null; }
      @Override public List<VirtualMachineDescriptor> listVirtualMachines() { return null; }
   };

   private final String jarFilePath;

   AgentLoader()
   {
      jarFilePath = new PathToAgentJar().getPathToJarFile();
      System.out.println("jarFilePath  " + jarFilePath);
   }

   public void loadAgent()
   {
      VirtualMachine vm;

      if (AttachProvider.providers().isEmpty()) {
            System.out.println("getVirtualMachineImplementationFromEmbeddedOnes");
            vm = getVirtualMachineImplementationFromEmbeddedOnes();
      }
      else {
         System.out.println("attachToRunningVM");
         vm = attachToRunningVM();
      }

      loadAgentAndDetachFromRunningVM(vm);
   }

   private static VirtualMachine getVirtualMachineImplementationFromEmbeddedOnes()
   {
      Class<? extends VirtualMachine> vmClass = findVirtualMachineClassAccordingToOS();
      Class<?>[] parameterTypes = {AttachProvider.class, String.class};
      String pid = getProcessIdForRunningVM();

      try {
         // This is only done with Reflection to avoid the JVM pre-loading all the XyzVirtualMachine classes.
         Constructor<? extends VirtualMachine> vmConstructor = vmClass.getConstructor(parameterTypes);
         VirtualMachine newVM = vmConstructor.newInstance(ATTACH_PROVIDER, pid);
         return newVM;
      }
      catch (NoSuchMethodException e)     { throw new RuntimeException(e); }
      catch (InvocationTargetException e) { throw new RuntimeException(e); }
      catch (InstantiationException e)    { throw new RuntimeException(e); }
      catch (IllegalAccessException e)    { throw new RuntimeException(e); }
      catch (NoClassDefFoundError e) {
         throw new IllegalStateException("Native library for Attach API not available in this JRE", e);
      }
      catch (UnsatisfiedLinkError e) {
         throw new IllegalStateException("Native library for Attach API not available in this JRE", e);
      }
   }

   private static Class<? extends VirtualMachine> findVirtualMachineClassAccordingToOS()
   {
      if (File.separatorChar == '\\') {
         return WindowsVirtualMachine.class;
      }

      String osName = System.getProperty("os.name");

      if (osName.startsWith("Linux") || osName.startsWith("LINUX")) {
         return LinuxVirtualMachine.class;
      }
      else if (osName.startsWith("Mac OS X")) {
         return BsdVirtualMachine.class;
      }
      else if (osName.startsWith("Solaris")) {
         return SolarisVirtualMachine.class;
      }

      throw new IllegalStateException("Cannot use Attach API on unknown OS: " + osName);
   }


   private static String getProcessIdForRunningVM()
   {
      String nameOfRunningVM = ManagementFactory.getRuntimeMXBean().getName();
      int p = nameOfRunningVM.indexOf('@');
      return nameOfRunningVM.substring(0, p);
   }


   private String getHelpMessageForNonHotSpotVM()
   {
      String vmName = System.getProperty("java.vm.name");
      String helpMessage = "To run on " + vmName;

      if (vmName.contains("J9")) {
         helpMessage += ", add <IBM SDK>/lib/tools.jar to the runtime classpath (before jmockit), or";
      }

      return helpMessage + " use -javaagent:" + jarFilePath;
   }


   private static VirtualMachine attachToRunningVM()
   {
      String pid = getProcessIdForRunningVM();

      try {
         return VirtualMachine.attach(pid);
      }
      catch (AttachNotSupportedException e) {
         throw new RuntimeException(e);
      }
      catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   private void loadAgentAndDetachFromRunningVM(VirtualMachine vm)
   {
      try {
         System.out.println("loadAgentAndDetachFromRunningVM " + jarFilePath);
         vm.loadAgent(jarFilePath, null);
         vm.detach();
      }
      catch (AgentLoadException e) {
         throw new IllegalStateException(e);
      }
      catch (AgentInitializationException e) {
         throw new IllegalStateException(e);
      }
      catch (IOException e) {
         throw new RuntimeException(e);
      }
   }
}
