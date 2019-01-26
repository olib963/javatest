package org.javatest.javafire;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;

public class ThreadLocalClassLoaderProvider implements ClassLoaderProvider {

    @Override
    public ClassLoader classLoaderFor(Set<String> classPathElements) throws ClassLoadingException {
        var runtimeUrls = new URL[classPathElements.size()];
        int i = 0;
        for (final String classPathElement : classPathElements) {
            try {
                runtimeUrls[i] = new File(classPathElement).toURI().toURL();
            } catch (MalformedURLException e) {
                throw new ClassLoadingException("Could not create URL for classpath element <" + classPathElement + ">", e);
            }
            i++;
        }
        return new URLClassLoader(runtimeUrls, Thread.currentThread().getContextClassLoader());
    }
}
