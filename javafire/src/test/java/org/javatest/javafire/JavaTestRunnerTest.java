package org.javatest.javafire;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.project.MavenProject;
import org.javatest.*;

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

    private JavaTestRunner.Result runWithRunners(Class<?> testClass) throws ClassNotFoundException, ClassLoaderProvider.ClassLoadingException, DependencyResolutionRequiredException {
        var mavenProject = projectWithExpectedClasspathDependencies();
        var testRunners = testClass.getName();
        var classLoaderProvider = providerFor(testClass);
        return new JavaTestRunner(testRunners, classLoaderProvider, mavenProject).run();
    }

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
                test("Test when test runners class cannot be loaded", () -> {
                    var mavenProject = projectWithExpectedClasspathDependencies();
                    var runners = "org.foo.Bar";
                    var classLoader = mock(ClassLoader.class);
                    doThrow(ClassNotFoundException.class).when(classLoader).loadClass(runners);
                    var classLoaderProvider = providerFor(classLoader);
                    var result = new JavaTestRunner(runners, classLoaderProvider, mavenProject).run();
                    return that(result.status == JavaTestRunner.Status.EXECUTION_FAILURE, "Should return an execution failure");
                }),
                test("Test when test runners class does not extend TestRunners", () -> {
                    var result = runWithRunners(IncorrectTypeClass.class);
                    return that(result.status == JavaTestRunner.Status.FAILURE, "Should return a failure");
                }),
                test("Test when test runners class cannot be instantiated", () -> {
                    var result = runWithRunners(RunnersWithNoDefaultConstructor.class);
                    return that(result.status == JavaTestRunner.Status.FAILURE, "Should return a failure");
                }),
                test("Test when test runners contains a failing test", () -> {
                    var result = runWithRunners(RunnersWithFailingTest.class);
                    return that(result.status == JavaTestRunner.Status.FAILURE, "Should return a failure");
                }),
                test("Test when all tests from runners pass", () -> {
                    var result = runWithRunners(RunnersWithPassingTests.class);
                    return that(result.status == JavaTestRunner.Status.SUCCESS, "Should return a success");
                })
        );
    }

    public static class IncorrectTypeClass {}

    public static class RunnersWithNoDefaultConstructor implements TestRunners {
        private RunnersWithNoDefaultConstructor() {}

        @Override
        public Stream<TestRunner> runners() {
            return Stream.empty();
        }
    }

    public static class RunnersWithFailingTest implements TestRunners {
        @Override
        public Stream<TestRunner> runners() {
            return Stream.of(testStreamRunner(
                    Stream.of(test("Failure", () -> that(false, "Expected false"))),
                    Collections.emptyList()
            ));
        }
    }
    public static class RunnersWithPassingTests implements TestRunners {
        @Override
        public Stream<TestRunner> runners() {
            return Stream.of(testStreamRunner(
                    Stream.of(test("Success", () -> that(true, "Expected true"))),
                    Collections.emptyList()
            ));
        }
    }

}
