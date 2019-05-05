package org.javatest.javafire;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.javatest.javafire.JavaTestRunner.Result;

/**
 * Run all of the JavaTest tests from the given test provider
 */
@Mojo(name = "test", requiresDependencyResolution = ResolutionScope.TEST, defaultPhase = LifecyclePhase.TEST)
public class JavaTestMojo extends AbstractMojo {

	/**
	 * The name of the class that implements TestSuite to run
	 */
	@Parameter(required = true)
	private String testSuite;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {

		var testRunner = new JavaTestRunner(testSuite, new ThreadLocalClassLoaderProvider(), lookupProject());
		
		validateResult(testRunner.run());
	}

	
	// private methods to aid readability

	private MavenProject lookupProject() throws MojoExecutionException {
		try {
			return (MavenProject) getPluginContext().get("project");
		} catch (Exception e) {
			throw new MojoExecutionException("The maven project did not exist in the context."
					+ " This should not happen, something is wrong with the Maven API", e);
		}
	}
	
	private void validateResult(Result result) throws MojoExecutionException, MojoFailureException {

		if (result.status == JavaTestRunner.Status.EXECUTION_FAILURE) {
			throw result.cause.map(cause -> new MojoExecutionException(result.description, cause))
					.orElseGet(() -> new MojoExecutionException(result.description));
		}
		
		if (result.status == JavaTestRunner.Status.FAILURE) {
			throw result.cause.map(cause -> new MojoFailureException(result.description, cause))
					.orElseGet(() -> new MojoFailureException(result.description));
		}
	}
}