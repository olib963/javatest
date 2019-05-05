package org.javatest.javafire;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.project.MavenProject;
import org.javatest.JavaTest;
import org.javatest.TestSuite;
import org.javatest.javafire.ClassLoaderProvider.ClassLoadingException;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class JavaTestRunner {
	private final String testSuite;
	private final ClassLoaderProvider classLoaderProvider;
	private final MavenProject project;

	JavaTestRunner(String testSuite, ClassLoaderProvider classLoaderProvider, MavenProject project) {
		this.testSuite = testSuite;
		this.classLoaderProvider = classLoaderProvider;
		this.project = project;
	}

	Result run() {

		try {
			Set<String> classPathElements = resolveAllClassPathElements(project);
			Class<?> suiteClass = loadSuiteClass(lookupClassLoader(classPathElements));

			TestSuite suite = instantiateSuite(suiteClass);

			var results = JavaTest.run(suite.testStream());

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
		} catch (ClassLoadingException e) {
			// FIXME Add classPathElements to exception message
			throw new InternalTestException(Status.EXECUTION_FAILURE, "Could not create a class loader", e);
		}
	}

	private Class<?> loadSuiteClass(ClassLoader loader) throws InternalTestException {
		try {
			Class<?> suiteClass = loader.loadClass(testSuite);

			if (!TestSuite.class.isAssignableFrom(suiteClass)) {
				throw new InternalTestException(Status.FAILURE,
						"Given class (" + suiteClass.getName() + ") does not implement TestSuite");
			}

			return suiteClass;

		} catch (ClassNotFoundException e) {
			throw new InternalTestException(Status.EXECUTION_FAILURE,
					"Could not load the  given class (" + testSuite + ")", e);
		}
	}

	private TestSuite instantiateSuite(Class<?> providerClass) throws InternalTestException {
		try {
			return (TestSuite) providerClass.getConstructor(null).newInstance();
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException
				| NoSuchMethodException e) {
			throw new InternalTestException(Status.FAILURE,
					"Given class (" + providerClass.getName() + ") could not be instantiated", e);
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

	// TODO Consider splitting this whole class, as needed this exception class to control the flow (a lot going on!)
	@SuppressWarnings("serial")
	private static class InternalTestException extends Exception {

		private Status status;

		public InternalTestException(Status executionFailure, String message, Throwable t) {
			super(message, t);
			this.status = executionFailure;
		}

		public InternalTestException(Status executionFailure, String message) {
			super(message);
			this.status = executionFailure;
		}

		Result toResult() {
			Throwable cause = getCause();
			
			if (cause == null) {
				return new Result(status, getMessage());
			}
			else {
				return new Result(status, getMessage(), cause);		// TODO Bit cumbersome - use Optional here too?
			}
		}
	}

	enum Status {
		SUCCESS, FAILURE, EXECUTION_FAILURE
	}
}
