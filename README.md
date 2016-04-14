# study-asm
ASM is an all purpose Java bytecode manipulation and analysis framework.

## class
* boolean isInterface()
* String getName()
* String getSuperClass()
* String[] getInterfaces()
* Method getDeclaedmethod(String name, String... parameterTypes)
* List<Method> getDeclaredmethods()
* Method getConstructor(String... parameterTypes)
* boolean hasDeclaredMethod(String methodName, String... args)
* bolean hasMethod(String methodName, String... parameterTypes)
* boolean hasElclosingMethod(String methodName, String... parameterTypes)
* boolean hasConstructor(String... parameterTypeArray)
* boolean hasField(String name, String type)
* boolean hasField(String name)
* void weave(String adviceClassName)
* void addField(String accessorTypeName)
* addGetter(String getterTypeName, String fieldName)
* addSetter(String setterTypeName, String fieldName)
* addSetter(String setterTypeName, String fieldName, boolean removeFinalFlag)
* addInterceptor(...)

## method
* String getName()
* String[] getParameterTypes()
* String getReturnType()
* int getModifiers()
* boolean isConstrucotr()
* MethodDescriptor getDescriptor()
* int addInterceptor(...)
