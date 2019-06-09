package io.github.olib963.javatest.javafire;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.project.MavenProject;
import io.github.olib963.javatest.JavaTest;
import io.github.olib963.javatest.TestRunners;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class JavaTestRunner {
    private final String testRunners;
    private final ClassLoaderProvider classLoaderProvider;
    private final MavenProject project;

    JavaTestRunner(String testRunners, ClassLoaderProvider classLoaderProvider, MavenProject project) {
        this.testRunners = testRunners;
        this.classLoaderProvider = classLoaderProvider;
        this.project = project;
    }

    Result run() {

        try {
            Set<String> classPathElements = resolveAllClassPathElements(project);
            Class<?> runnersClass = loadRunnersClass(lookupClassLoader(classPathElements));
            TestRunners r = instantiateTestRunners(runnersClass);
            var results = JavaTest.run(r.runners());

            if (results.succeeded) {
                return new Result(Status.SUCCESS, "All tests passed");
            } else {
                return new Result(Status.FAILURE, "Tests failed");
            }
        } catch (InternalTestException e) {
            return e.toResult();
        }
    }

    private Set<String> resolveAllClassPathElements(MavenProject project) throws InternalTestException {
        try {
            return Stream
                    .concat(project.getTestClasspathElements().stream(), project.getRuntimeClasspathElements().stream())
                    .collect(Collectors.toSet());

        } catch (DependencyResolutionRequiredException e) {
            throw new InternalTestException(Status.EXECUTION_FAILURE,
                    "Could not resolve dependencies for maven project", e);
        }
    }

    private ClassLoader lookupClassLoader(Set<String> classPathElements) throws InternalTestException {
        try {
            return classLoaderProvider.classLoaderFor(classPathElements);
        } catch (ClassLoaderProvider.ClassLoadingException e) {
            throw new InternalTestException(Status.EXECUTION_FAILURE, "Could not create a class loader", e);
        }
    }

    private Class<?> loadRunnersClass(ClassLoader loader) throws InternalTestException {
        try {
            Class<?> runnersClass = loader.loadClass(testRunners);

            if (!TestRunners.class.isAssignableFrom(runnersClass)) {
                throw new InternalTestException(Status.FAILURE,
                        "Given class (" + runnersClass.getName() + ") does not implement TestRunners");
            }

            return runnersClass;

        } catch (ClassNotFoundException e) {
            throw new InternalTestException(Status.EXECUTION_FAILURE,
                    "Could not load the  given class (" + testRunners + ")", e);
        }
    }

    private TestRunners instantiateTestRunners(Class<?> runnersClass) throws InternalTestException {
        try {
            return (TestRunners) runnersClass.getConstructor(null).newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                | NoSuchMethodException e) {
            throw new InternalTestException(Status.FAILURE,
                    "Given class (" + runnersClass.getName() + ") could not be instantiated", e);
        }
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
            return "Result{" + "status=" + status + ", name='" + description + '\'' + ", cause=" + cause + '}';
        }
    }

    private static class InternalTestException extends Exception {

        private final Status status;

        public InternalTestException(Status executionFailure, String message, Throwable t) {
            super(message, t);
            this.status = executionFailure;
        }

        public InternalTestException(Status executionFailure, String message) {
            super(message);
            this.status = executionFailure;
        }

        Result toResult() {
            return Optional.ofNullable(getCause())
                    .map(c -> new Result(status, getMessage(), c))
                    .orElseGet(() -> new Result(status, getMessage()));
        }
    }

    enum Status {
        SUCCESS, FAILURE, EXECUTION_FAILURE
    }
}