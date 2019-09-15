package io.github.olib963.javatest.javafire;

import io.github.olib963.javatest.reflection.ReflectionRunners;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.project.MavenProject;
import io.github.olib963.javatest.JavaTest;
import io.github.olib963.javatest.TestRunners;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class JavaTestRunner {
    private final Optional<String> testRunners;
    private final ClassLoaderProvider classLoaderProvider;
    private final MavenProject project;

    JavaTestRunner(Optional<String> testRunners, ClassLoaderProvider classLoaderProvider, MavenProject project) {
        this.testRunners = testRunners;
        this.classLoaderProvider = classLoaderProvider;
        this.project = project;
    }

    Result run() {
        try {
            var classPathElements = resolveAllClassPathElements(project);
            var classLoader = lookupClassLoader(classPathElements);
            TestRunners r = testRunners.map(c -> instantiateTestRunners(loadRunnersClass(classLoader, c))).orElse(defaultRunners(classLoader));
            var results = JavaTest.run(r.runners());

            if (results.succeeded) {
                return new Result(Status.SUCCESS, "All tests passed");
            } else {
                return new Result(Status.FAILURE, "Tests failed");
            }
        } catch (InternalTestException e) {
            return e.result;
        }
    }

    private Set<String> resolveAllClassPathElements(MavenProject project) {
        try {
            return Stream
                    .concat(project.getTestClasspathElements().stream(), project.getRuntimeClasspathElements().stream())
                    .collect(Collectors.toSet());

        } catch (DependencyResolutionRequiredException e) {
            throw new InternalTestException(Status.EXECUTION_FAILURE,
                    "Could not resolve dependencies for maven project", e);
        }
    }

    private ClassLoader lookupClassLoader(Set<String> classPathElements) {
        try {
            return classLoaderProvider.classLoaderFor(classPathElements);
        } catch (ClassLoaderProvider.ClassLoadingException e) {
            throw new InternalTestException(Status.EXECUTION_FAILURE, "Could not create a class loader", e);
        }
    }

    private Class<?> loadRunnersClass(ClassLoader loader, String testRunners) {
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

    private TestRunners instantiateTestRunners(Class<?> runnersClass) {
        try {
            return (TestRunners) runnersClass.getConstructor(null).newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                | NoSuchMethodException e) {
            throw new InternalTestException(Status.FAILURE,
                    "Given class (" + runnersClass.getName() + ") could not be instantiated", e);
        }
    }

    private TestRunners defaultRunners(ClassLoader classLoader) {
        var testCompileSourceRoots = project.getTestCompileSourceRoots().stream().map(File::new).collect(Collectors.toList());
        return ReflectionRunners.forTestSourceRoots(testCompileSourceRoots, classLoader);
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

    private static class InternalTestException extends RuntimeException {

        final Result result;

        InternalTestException(Status status, String message, Throwable t) {
            this.result = new Result(status, message, t);
        }

        InternalTestException(Status status, String message) {
            this.result = new Result(status, message);
        }
    }

    enum Status {
        SUCCESS, FAILURE, EXECUTION_FAILURE
    }
}
