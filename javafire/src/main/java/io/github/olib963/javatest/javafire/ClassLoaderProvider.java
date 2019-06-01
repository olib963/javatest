package io.github.olib963.javatest.javafire;

import java.util.Set;

public interface ClassLoaderProvider {

    ClassLoader classLoaderFor(Set<String> classPathElements) throws ClassLoadingException;
    class ClassLoadingException extends Exception {
        public ClassLoadingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
