package org.javatest.javafire;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.project.MavenProject;
import org.javatest.JavaTest;
import org.javatest.TestProvider;
import org.javatest.javafire.ClassLoaderProvider.ClassLoadingException;

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

		try {
			Set<String> classPathElements = resolveAllClassPathElements(project);
			Class<?> providerClass = loadProviderClass(lookupClassLoader(classPathElements));

			TestProvider provider = getTestProvider(providerClass);

			var results = JavaTest.run(provider.testStream());

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

	private Class<?> loadProviderClass(ClassLoader loader) throws InternalTestException {
		try {
			Class<?> providerClass = loader.loadClass(testProvider);

			if (!TestProvider.class.isAssignableFrom(providerClass)) {
				throw new InternalTestException(Status.FAILURE,
						"Given class (" + providerClass.getName() + ") does not implement TestProvider");
			}

			return providerClass;

		} catch (ClassNotFoundException e) {
			throw new InternalTestException(Status.EXECUTION_FAILURE,
					"Could not load the  given class (" + testProvider + ")", e);
		}
	}

	private TestProvider getTestProvider(Class<?> providerClass) throws InternalTestException {
		try {
			return (TestProvider) providerClass.getConstructor(null).newInstance();
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
	static private class InternalTestException extends Exception {

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
