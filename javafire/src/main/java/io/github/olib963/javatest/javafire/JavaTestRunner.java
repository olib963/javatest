package io.github.olib963.javatest.javafire;

import io.github.olib963.javatest.RunConfiguration;
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
            var runnerProvider = testRunners
                    .map(c -> instantiateAs(loadClass(classLoader, c), TestRunners.class))
                    .orElse(defaultRunners(classLoader));
            var configurationProvider = configurationClass
                    .map(c -> instantiateAs(loadClass(classLoader, c), RunConfigurationProvider.class))
                    .orElse(RunConfiguration::defaultConfig);
            var results = JavaTest.run(runnerProvider.runners(), configurationProvider.config());

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

    private Class<?> loadClass(ClassLoader loader, String classToLoad) {
        try {
            return loader.loadClass(classToLoad);
        } catch (ClassNotFoundException e) {
            throw new InternalTestException(Status.EXECUTION_FAILURE,
                    "Could not load the  given class (" + classToLoad + ")", e);
        }
    }

    private <T> T instantiateAs(Class<?> classToInstantiate, Class<T> toInstantiateAs) {
        try {
            if (!toInstantiateAs.isAssignableFrom(classToInstantiate)) {
                throw new InternalTestException(Status.FAILURE,
                        "Given class (" + classToInstantiate.getName() + ") does not implement " + toInstantiateAs.getName());
            }
            return toInstantiateAs.cast(classToInstantiate.getConstructor(null).newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                | NoSuchMethodException e) {
            throw new InternalTestException(Status.FAILURE,
                    "Given class (" + classToInstantiate.getName() + ") could not be instantiated", e);
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
