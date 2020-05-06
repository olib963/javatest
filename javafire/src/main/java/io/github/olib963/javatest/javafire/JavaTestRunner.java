package io.github.olib963.javatest.javafire;

import io.github.olib963.javatest.reflection.ReflectionRunners;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.project.MavenProject;
import io.github.olib963.javatest.JavaTest;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

class JavaTestRunner {
    private final Optional<String> testRunners;
    private final Optional<String> configurationClass;
    private final ClassLoaderProvider classLoaderProvider;
    private final MavenProject project;

    JavaTestRunner(Optional<String> testRunners, Optional<String> configurationClass, ClassLoaderProvider classLoaderProvider, MavenProject project) {
        this.testRunners = testRunners;
        this.configurationClass = configurationClass;
        this.classLoaderProvider = classLoaderProvider;
        this.project = project;
    }

    Result run() {
        try {
            var classPathElements = resolveAllClassPathElements(project);
            var classLoader = lookupClassLoader(classPathElements);
            var r = testRunners.map(c -> instantiateTestRunners(loadClass(classLoader, c))).orElse(defaultRunners(classLoader));
            var configuration = configurationClass.map(c -> loadClass(classLoader, c));
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
            return new HashSet<>(project.getTestClasspathElements());
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

    private Class<?> loadClass(ClassLoader loader, String testRunners) {
        try {
            return loader.loadClass(testRunners);
        } catch (ClassNotFoundException e) {
            throw new InternalTestException(Status.EXECUTION_FAILURE,
                    "Could not load the  given class (" + testRunners + ")", e);
        }
    }


    private TestRunners instantiateTestRunners(Class<?> runnersClass) {
        try {
            if (!TestRunners.class.isAssignableFrom(runnersClass)) {
                throw new InternalTestException(Status.FAILURE,
                        "Given class (" + runnersClass.getName() + ") does not implement TestRunners");
            }
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

        private InternalTestException(Status status, String message, Throwable t) {
            this.result = new Result(status, message, Optional.of(t));
        }

        private InternalTestException(Status status, String message) {
            this.result = new Result(status, message, Optional.empty());
        }
    }

    enum Status {
        SUCCESS, FAILURE, EXECUTION_FAILURE
    }
}
