package org.javatest.javafire;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.project.MavenProject;
import org.javatest.JavaTest;
import org.javatest.Test;
import org.javatest.TestSuite;

import java.util.*;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.javatest.JavaTest.*;

public class JavaTestRunnerTest implements TestSuite {

    // Running tests
    public static void main(String... args) {
        if (!JavaTest.runTests(new JavaTestRunnerTest().testStream()).succeeded) {
            throw new RuntimeException("Tests failed!");
        }
        System.out.println("Tests passed");
    }

    // SimpleTest definitions
    private static final List<String> RUNTIME_ELEMENTS = List.of("Runtime dependency");
    private static final List<String> TEST_ELEMENTS = List.of("SimpleTest dependency");
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

    private JavaTestRunner.Result runWithTestSuite(Class<?> testClass) throws ClassNotFoundException, ClassLoaderProvider.ClassLoadingException, DependencyResolutionRequiredException {
        var mavenProject = projectWithExpectedClasspathDependencies();
        var testSuite = testClass.getName();
        var classLoaderProvider = providerFor(testClass);
        return new JavaTestRunner(testSuite, classLoaderProvider, mavenProject).run();
    }

    @Override
    public Stream<Test> testStream() {
        return Stream.of(
                test("SimpleTest failure to get runtime classpath elements", () -> {
                    var mavenProject = mock(MavenProject.class);
                    doThrow(DependencyResolutionRequiredException.class).when(mavenProject).getRuntimeClasspathElements();
                    var result = new JavaTestRunner(null, null, mavenProject).run();
                    return that(result.status == JavaTestRunner.Status.EXECUTION_FAILURE, "Should return an execution failure");
                }),
                test("SimpleTest failure to get test classpath elements", () -> {
                    var mavenProject = mock(MavenProject.class);
                    doThrow(DependencyResolutionRequiredException.class).when(mavenProject).getTestClasspathElements();
                    var result = new JavaTestRunner(null, null, mavenProject).run();
                    return that(result.status == JavaTestRunner.Status.EXECUTION_FAILURE, "Should return an execution failure");
                }),
                test("SimpleTest failure load classpath elements", () -> {
                    var mavenProject = projectWithExpectedClasspathDependencies();
                    var classLoaderProvider = mock(ClassLoaderProvider.class);
                    doThrow(ClassLoaderProvider.ClassLoadingException.class).when(classLoaderProvider).classLoaderFor(eq(ALL_ELEMENTS));
                    var result = new JavaTestRunner(null, classLoaderProvider, mavenProject).run();
                    return that(result.status == JavaTestRunner.Status.EXECUTION_FAILURE, "Should return an execution failure");
                }),
                test("SimpleTest when test suite class cannot be loaded", () -> {
                    var mavenProject = projectWithExpectedClasspathDependencies();
                    var testSuite = "org.foo.Bar";
                    var classLoader = mock(ClassLoader.class);
                    doThrow(ClassNotFoundException.class).when(classLoader).loadClass(testSuite);
                    var classLoaderProvider = providerFor(classLoader);
                    var result = new JavaTestRunner(testSuite, classLoaderProvider, mavenProject).run();
                    return that(result.status == JavaTestRunner.Status.EXECUTION_FAILURE, "Should return an execution failure");
                }),
                test("SimpleTest when test suite class does not extend TestSuite", () -> {
                    var result = runWithTestSuite(IncorrectTypeClass.class);
                    return that(result.status == JavaTestRunner.Status.FAILURE, "Should return a failure");
                }),
                test("SimpleTest when test suite class cannot be instantiated", () -> {
                    var result = runWithTestSuite(SuiteWithNoDefaultConstructor.class);
                    return that(result.status == JavaTestRunner.Status.FAILURE, "Should return a failure");
                }),
                test("SimpleTest when test suite contains a failing test", () -> {
                    var result = runWithTestSuite(ProviderWithFailingTest.class);
                    return that(result.status == JavaTestRunner.Status.FAILURE, "Should return a failure");
                }),
                test("SimpleTest when all tests from a suite pass", () -> {
                    var result = runWithTestSuite(SuiteWithPassingTests.class);
                    return that(result.status == JavaTestRunner.Status.SUCCESS, "Should return a success");
                })
        );
    }

    public static class IncorrectTypeClass {}

    public static class SuiteWithNoDefaultConstructor implements TestSuite {
        private SuiteWithNoDefaultConstructor() {}

        @Override
        public Stream<Test> testStream() {
            return Stream.empty();
        }
    }
    // TODO make these tests run silently by turning off logging
    public static class ProviderWithFailingTest implements TestSuite {
        @Override
        public Stream<Test> testStream() {
            return Stream.of(test("Failure", () -> that(false, "Expected false")));
        }
    }
    public static class SuiteWithPassingTests implements TestSuite {
        @Override
        public Stream<Test> testStream() {
            return Stream.of(test("Success", () -> that(true, "Expected true")));
        }
    }

}
