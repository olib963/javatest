package io.github.olib963.javatest.reflection;

public class FailingClassLoader extends ClassLoader {
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        throw new ClassNotFoundException("Never finds any classes");
    }
}
