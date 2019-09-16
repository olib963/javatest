package io.github.olib963.javatest.javafire;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import io.github.olib963.javatest.javafire.JavaTestRunner.Result;

import java.util.Optional;

/**
 * Run all of the JavaTest tests from the given test runners
 */
@Mojo(name = "test", requiresDependencyResolution = ResolutionScope.TEST, defaultPhase = LifecyclePhase.TEST)
public class JavaTestMojo extends AbstractMojo {

	/**
	 * The name of the class that implements TestRunners, providing the runners for testing
	 */
	@Parameter(property = "javafire.testRunners")
	private String testRunners;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		var testRunner = new JavaTestRunner(Optional.ofNullable(testRunners), new ThreadLocalClassLoaderProvider(), lookupProject());
		validateResult(testRunner.run());
	}

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