package org.javatest.javafire;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.project.MavenProject;
import org.javatest.JavaTest;
import org.javatest.TestProvider;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class JavaTestRunner {
    private final String testProvider;
    private final ClassLoaderProvider classLoaderProvider;
    private final MavenProject project;

    JavaTestRunner(String testProvider, ClassLoaderProvider classLoaderProvider, MavenProject project) {
        this.testProvider = testProvider;
        this.classLoaderProvider = classLoaderProvider;
        this.project = project;
    }

    Result run() {
        Set<String> classPathElements;
        try {
            classPathElements = resolveAllClassPathElements(project);
        } catch (DependencyResolutionRequiredException e) {
            return new Result(Status.EXECUTION_FAILURE, "Could not resolve dependencies for maven project", e);
        }

        ClassLoader loader;
        try {
            loader = classLoaderProvider.classLoaderFor(classPathElements);
        } catch (ClassLoaderProvider.ClassLoadingException e) {
            return new Result(Status.EXECUTION_FAILURE, "Could not create a class loader", e);
        }
        Class<?> providerClass;
        try {
            providerClass = loader.loadClass(testProvider);
        } catch (ClassNotFoundException e) {
            return new Result(Status.EXECUTION_FAILURE, "Could not load the  given class (" + testProvider + ")", e);
        }
        if (!TestProvider.class.isAssignableFrom(providerClass)) {
            return new Result(Status.FAILURE, "Given class (" + providerClass.getName() + ") does not implement TestProvider");
        }
        TestProvider provider;
        try {
            provider = (TestProvider) providerClass.getConstructor(null).newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return new Result(Status.FAILURE, "Given class (" + providerClass.getName() + ") could not be instantiated", e);
        }
        var results = JavaTest.run(provider.testStream());
        if(results.succeeded) {
            return new Result(Status.SUCCESS, "All tests passed");
        } else {
            return new Result(Status.FAILURE, "Tests failed");
        }
    }

    private Set<String> resolveAllClassPathElements(MavenProject project) throws DependencyResolutionRequiredException {
        return Stream
                .concat(project.getTestClasspathElements().stream(),
                        project.getRuntimeClasspathElements().stream())
                .collect(Collectors.toSet());
    }

    static class Result {
        final Status status;
        final String description;
        final Optional<Throwable> cause;

        Result(Status status, String description) {
            this(status, description, Optional.empty());
        }

        Result(Status status, String description, Throwable cause) {
            this(status, description, Optional.of(cause));
        }

        private Result(Status status, String description, Optional<Throwable> cause) {
            this.status = status;
            this.description = description;
            this.cause = cause;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "status=" + status +
                    ", name='" + description + '\'' +
                    ", cause=" + cause +
                    '}';
        }
    }

    enum Status { SUCCESS, FAILURE, EXECUTION_FAILURE }
}
