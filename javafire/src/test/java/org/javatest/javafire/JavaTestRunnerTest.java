package org.javatest.javafire;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.project.MavenProject;
import org.javatest.JavaTest;
import org.javatest.tests.Test;
import org.javatest.tests.TestProvider;

import java.util.*;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

public class JavaTestRunnerTest implements TestProvider {

    // Running tests
    public static void main(String... args) {
        if (!JavaTest.run(new JavaTestRunnerTest()).succeeded) {
            throw new RuntimeException("Tests failed!");
        }
        System.out.println("Tests passed");
    }

    // Test definitions
    private static final List<String> RUNTIME_ELEMENTS = List.of("Runtime dependency");
    private static final List<String> TEST_ELEMENTS = List.of("Test dependency");
    private static final Set<String> ALL_ELEMENTS = union(RUNTIME_ELEMENTS, TEST_ELEMENTS);

    private static Set<String> union(List<String> a, List<String> b) {
        Set<String> all = new HashSet<>(a);
        all.addAll(b);
        return all;
    }

    private MavenProject projectWithExpectedClasspathDependencies() throws DependencyResolutionRequiredException {
        var project = mock(MavenProject.class);
        when(project.getRuntimeClasspathElements()).thenReturn(RUNTIME_ELEMENTS);
        when(project.getTestClasspathElements()).thenReturn(TEST_ELEMENTS);
        return project;
    }

    private ClassLoaderProvider providerFor(ClassLoader classLoader) throws ClassLoaderProvider.ClassLoadingException {
        var provider = mock(ClassLoaderProvider.class);
        when(provider.classLoaderFor(ALL_ELEMENTS)).thenReturn(classLoader);
        return provider;
    }
    private ClassLoaderProvider providerFor(Class<?> classToLoad) throws ClassLoaderProvider.ClassLoadingException, ClassNotFoundException {
        var provider = mock(ClassLoaderProvider.class);
        var classLoader = mock(ClassLoader.class);
        doReturn(classToLoad).when(classLoader).loadClass(eq(classToLoad.getName()));
        when(provider.classLoaderFor(ALL_ELEMENTS)).thenReturn(classLoader);
        return provider;
    }

    private JavaTestRunner.Result runWithTestProvider(Class<?> testClass) throws ClassNotFoundException, ClassLoaderProvider.ClassLoadingException, DependencyResolutionRequiredException {
        var mavenProject = projectWithExpectedClasspathDependencies();
        var testProvider = testClass.getName();
        var classLoaderProvider = providerFor(testClass);
        return new JavaTestRunner(testProvider, classLoaderProvider, mavenProject).run();
    }

    // TODO how do we want to handle setups and tear downs.
    @Override
    public Stream<Test> testStream() {
        return Stream.of(
                test("Test failure to get runtime classpath elements", () -> {
                    var mavenProject = mock(MavenProject.class);
                    doThrow(DependencyResolutionRequiredException.class).when(mavenProject).getRuntimeClasspathElements();
                    var result = new JavaTestRunner(null, null, mavenProject).run();
                    return that(result.status == JavaTestRunner.Status.EXECUTION_FAILURE, "Should return an execution failure");
                }),
                test("Test failure to get test classpath elements", () -> {
                    var mavenProject = mock(MavenProject.class);
                    doThrow(DependencyResolutionRequiredException.class).when(mavenProject).getTestClasspathElements();
                    var result = new JavaTestRunner(null, null, mavenProject).run();
                    return that(result.status == JavaTestRunner.Status.EXECUTION_FAILURE, "Should return an execution failure");
                }),
                test("Test failure load classpath elements", () -> {
                    var mavenProject = projectWithExpectedClasspathDependencies();
                    var classLoaderProvider = mock(ClassLoaderProvider.class);
                    doThrow(ClassLoaderProvider.ClassLoadingException.class).when(classLoaderProvider).classLoaderFor(eq(ALL_ELEMENTS));
                    var result = new JavaTestRunner(null, classLoaderProvider, mavenProject).run();
                    return that(result.status == JavaTestRunner.Status.EXECUTION_FAILURE, "Should return an execution failure");
                }),
                test("Test when test provider class cannot be loaded", () -> {
                    var mavenProject = projectWithExpectedClasspathDependencies();
                    var testProvider = "org.foo.Bar";
                    var classLoader = mock(ClassLoader.class);
                    doThrow(ClassNotFoundException.class).when(classLoader).loadClass(testProvider);
                    var classLoaderProvider = providerFor(classLoader);
                    var result = new JavaTestRunner(testProvider, classLoaderProvider, mavenProject).run();
                    return that(result.status == JavaTestRunner.Status.EXECUTION_FAILURE, "Should return an execution failure");
                }),
                test("Test when test provider class does not extend TestProvider", () -> {
                    var result = runWithTestProvider(IncorrectTypeClass.class);
                    return that(result.status == JavaTestRunner.Status.FAILURE, "Should return a failure");
                }),
                test("Test when test provider class cannot be instantiated", () -> {
                    var result = runWithTestProvider(ProviderWithNoDefaultConstructor.class);
                    return that(result.status == JavaTestRunner.Status.FAILURE, "Should return a failure");
                }),
                test("Test when test provider contains a failing test", () -> {
                    var result = runWithTestProvider(ProviderWithFailingTest.class);
                    return that(result.status == JavaTestRunner.Status.FAILURE, "Should return a failure");
                }),
                test("Test when all tests from a provider pass", () -> {
                    var result = runWithTestProvider(ProviderWithPassingTests.class);
                    return that(result.status == JavaTestRunner.Status.SUCCESS, "Should return a success");
                })
        );
    }

    public static class IncorrectTypeClass {}

    public static class ProviderWithNoDefaultConstructor implements TestProvider {
        private ProviderWithNoDefaultConstructor() {}

        @Override
        public Stream<Test> testStream() {
            return Stream.empty();
        }
    }
    // TODO make these tests run silently by turning off logging
    public static class ProviderWithFailingTest implements  TestProvider {
        @Override
        public Stream<Test> testStream() {
            return Stream.of(test("Failure", () -> that(false)));
        }
    }
    public static class ProviderWithPassingTests implements  TestProvider {
        @Override
        public Stream<Test> testStream() {
            return Stream.of(test("Success", () -> that(true)));
        }
    }

}