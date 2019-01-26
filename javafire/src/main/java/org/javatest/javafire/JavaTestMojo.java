package org.javatest.javafire;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

/**
 * Run all of the JavaTest tests from the given test provider
 */
@Mojo(name = "test", requiresDependencyResolution = ResolutionScope.TEST, defaultPhase = LifecyclePhase.TEST)
public class JavaTestMojo extends AbstractMojo {

    /**
     * The name of the class that implements TestProvider to run
     */
    @Parameter(required = true)
    private String testProvider;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        MavenProject project;
        try {
            project = (MavenProject) getPluginContext().get("project");
        } catch (Exception e) {
            throw new MojoExecutionException("The maven project did not exist in the context." +
                    " This should not happen, something is wrong with the maven API", e);
        }
        var result = new JavaTestRunner(testProvider, new ThreadLocalClassLoaderProvider(), project).run();
        if (result.status == JavaTestRunner.Status.EXECUTION_FAILURE) {
            throw result.cause
                    .map(cause -> new MojoExecutionException(result.description, cause))
                    .orElseGet(() -> new MojoExecutionException(result.description));
        } else if (result.status == JavaTestRunner.Status.FAILURE) {
            throw result.cause
                    .map(cause -> new MojoFailureException(result.description, cause))
                    .orElseGet(() -> new MojoFailureException(result.description));
        }
    }

}
