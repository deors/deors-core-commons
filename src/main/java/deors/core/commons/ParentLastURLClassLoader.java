package deors.core.commons;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

/**
 * An URL-based class loader implementation that performs the search
 * for classes and resources using a parent-last policy.<br>
 *
 * @author deors
 * @version 1.0
 */
public final class ParentLastURLClassLoader
    extends URLClassLoader {

    /**
     * Constructor that sets the URL's that will be used to search for classes.
     *
     * @param urls the URL's used to search for classes
     */
    public ParentLastURLClassLoader(URL[] urls) {
        super(urls);
    }

    /**
     * Constructor that sets the URL's that will be used to search for classes and the parent class
     * loader.
     *
     * @param urls the URL's used to search for classes
     * @param parent the parent class loader
     */
    public ParentLastURLClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    /**
     * Constructor that sets the URL's that will be used to search for classes, the parent class
     * loader and the URL stream handler factory.
     *
     * @param urls the URL's used to search for classes
     * @param parent the parent class loader
     * @param factory the URL stream handler factory
     */
    public ParentLastURLClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }

    /**
     * Gets a resource searching it by the given path only in the URLs defined when the loader
     * is instantiated.
     *
     * @param name the path name of the resource
     *
     * @return the URL that points to the resource or <code>null</code> if it was not found
     *
     * @see java.lang.ClassLoader#getResource(String)
     */
    public URL getResource(String name) {

        // in JDK URLClassLoader class, the search is delegated
        // to the parent class loader and if not found,
        // the loader searches in the URLs;
        // this implementation searches for the resource
        // in the URLs and if not found, delegates
        // in the parent class loader

        URL url = findResource(name);
        if (url == null) {
            return super.getResource(name);
        }
        return url;
    }

    /**
     * Loads a class searching it by the given fully qualified name only in the URLs defined when
     * the loader is instantiated. The class can or cannot be resolved (linked) once it is loaded.
     *
     * @param name the fully qualified name of the class
     * @param resolve whether to resolve the class once it is loaded
     *
     * @return the class object
     *
     * @throws ClassNotFoundException the class was not found
     *
     * @see java.lang.ClassLoader#loadClass(java.lang.String, boolean)
     */
    protected Class<?> loadClass(String name, boolean resolve)
        throws ClassNotFoundException {

        // checks if the class is already loaded
        Class<?> c = findLoadedClass(name);

        if (c == null) {
            // in JDK URLClassLoader class the search is delegated
            // to the parent class loader and if not found,
            // the loader searches in the URLs;
            // this implementation searches for the class
            // in the URLs and if not found, delegates
            // in the parent class loader
            try {
                c = findClass(name);
            } catch (ClassNotFoundException cnfe) {
                return super.loadClass(name, false);
            }
        }

        if (resolve) {
            resolveClass(c);
        }

        return c;
    }

    /**
     * Loads a class searching it by the given fully qualified name only in the URL's defined when
     * the loader is instantiated. The class is not resolved (linked).
     *
     * @param name the fully qualified name of the class
     *
     * @return the class object
     *
     * @throws ClassNotFoundException the class was not found
     *
     * @see java.lang.ClassLoader#loadClass(java.lang.String)
     */
    public Class<?> loadClass(String name)
        throws ClassNotFoundException {

        return loadClass(name, false);
    }
}
