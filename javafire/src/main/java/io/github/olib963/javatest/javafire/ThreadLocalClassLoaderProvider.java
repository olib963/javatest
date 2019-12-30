package io.github.olib963.javatest.javafire;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ThreadLocalClassLoaderProvider implements ClassLoaderProvider {

    @Override
    public ClassLoader classLoaderFor(Set<String> classPathElements) throws ClassLoadingException {
        var runtimeUrls = toURLs(classPathElements);
        var loader = new URLClassLoader(runtimeUrls.toArray(new URL[0]), Thread.currentThread().getContextClassLoader());
        // TODO not sure this is correct. I had to set the context class loader such that the test classpath is available to test code (e.g. JUnit
        // jupiter engine). Hopefully there is a better way to edit the classpath though?
        Thread.currentThread().setContextClassLoader(loader);
        return loader;
    }

    // Wrap and throw exception for any malformed URL
    private List<URL> toURLs(Set<String> classPathElements) throws ClassLoadingException {
        var urls = new ArrayList<URL>(classPathElements.size());

        for (final String classPathElement : classPathElements) {
            try {
                urls.add(new File(classPathElement).toURI().toURL());
            } catch (MalformedURLException e) {
                throw new ClassLoadingException("Could not create URL for classpath element <" + classPathElement + ">",
                        e);
            }
        }
        return urls;
    }
}