package deors.core.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import deors.core.commons.IOToolkit;
import deors.core.commons.ParentLastURLClassLoader;

public class ParentLastURLClassLoaderTestCase {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public ParentLastURLClassLoaderTestCase() {

        super();
    }

    @SuppressWarnings("resource")
	@Test
    public void testNull() {

        thrown.expect(NullPointerException.class);
        new ParentLastURLClassLoader(null);
    }

    @Test
    public void testNoParent() {

        URL empty = this.getClass().getResource("/classloader/empty.jar");
        ParentLastURLClassLoader plcl = new ParentLastURLClassLoader(new URL[] {empty}, null);

        assertNotNull(plcl);
    }

    @Test
    public void testNoFactory() {

        URL empty = this.getClass().getResource("/classloader/empty.jar");
        ParentLastURLClassLoader plcl = new ParentLastURLClassLoader(new URL[] {empty}, null, null);

        assertNotNull(plcl);
    }

    @SuppressWarnings("resource")
    @Test
    public void testLoadClass()
        throws ClassNotFoundException,
               NoSuchMethodException,
               IllegalAccessException,
               InstantiationException,
               InvocationTargetException {

        URL version1 = this.getClass().getResource("/classloader/version1/HelloWorld-version-1.jar");
        ParentLastURLClassLoader plcl1 = new ParentLastURLClassLoader(new URL[] {version1});

        Class<?> c1 = plcl1.loadClass("deors.core.commons.HelloWorld");
        Object o1 = c1.newInstance();
        Method m1 = c1.getMethod("getVersion", (Class[]) null);
        String r1 = (String) m1.invoke(o1, (Object[]) null);

        assertEquals("HelloWorld version 1", r1);

        URLClassLoader ucl1 = new URLClassLoader(new URL[] {version1});
        c1 = ucl1.loadClass("deors.core.commons.HelloWorld");
        o1 = c1.newInstance();
        m1 = c1.getMethod("getVersion", (Class[]) null);
        r1 = (String) m1.invoke(o1, (Object[]) null);

        assertEquals("HelloWorld version 0", r1);

        URL version2 = this.getClass().getResource("/classloader/version2/HelloWorld-version-2.jar");
        ParentLastURLClassLoader plcl2 = new ParentLastURLClassLoader(new URL[] {version2});

        Class<?> c2 = plcl2.loadClass("deors.core.commons.HelloWorld");
        Object o2 = c2.newInstance();
        Method m2 = c2.getMethod("getVersion", (Class[]) null);
        String r2 = (String) m2.invoke(o2, (Object[]) null);

        assertEquals("HelloWorld version 2", r2);

        URLClassLoader ucl2 = new URLClassLoader(new URL[] {version2});
        c2 = ucl2.loadClass("deors.core.commons.HelloWorld");
        o2 = c2.newInstance();
        m2 = c2.getMethod("getVersion", (Class[]) null);
        r2 = (String) m2.invoke(o1, (Object[]) null);

        assertEquals("HelloWorld version 0", r2);
    }

    @SuppressWarnings("resource")
    @Test
    public void testLoadClassDelegated()
        throws ClassNotFoundException,
               NoSuchMethodException,
               IllegalAccessException,
               InstantiationException,
               InvocationTargetException {

        URL empty = this.getClass().getResource("/classloader/empty.jar");
        ParentLastURLClassLoader plcl = new ParentLastURLClassLoader(new URL[] {empty});

        Class<?> c = plcl.loadClass("deors.core.commons.HelloUniverse");
        Object o = c.newInstance();
        Method m = c.getMethod("getVersion", (Class[]) null);
        String r = (String) m.invoke(o, (Object[]) null);

        assertEquals("HelloUniverse only version", r);

        URLClassLoader ucl = new URLClassLoader(new URL[] {empty});
        c = ucl.loadClass("deors.core.commons.HelloUniverse");
        o = c.newInstance();
        m = c.getMethod("getVersion", (Class[]) null);
        r = (String) m.invoke(o, (Object[]) null);

        assertEquals("HelloUniverse only version", r);
    }

    @SuppressWarnings("resource")
    @Test
    public void testLoadClassDelegated2()
        throws ClassNotFoundException,
               NoSuchMethodException,
               IllegalAccessException,
               InstantiationException,
               InvocationTargetException {

        URL version1 = this.getClass().getResource("/classloader/version1/HelloWorld-version-1.jar");
        ParentLastURLClassLoader plcl1 = new ParentLastURLClassLoader(new URL[] {version1});
        URL version2 = this.getClass().getResource("/classloader/version2/HelloWorld-version-2.jar");
        ParentLastURLClassLoader plcl2 = new ParentLastURLClassLoader(new URL[] {version2}, plcl1);

        Class<?> c = plcl2.loadClass("deors.core.commons.HelloWorld");
        Object o = c.newInstance();
        Method m = c.getMethod("getVersion", (Class[]) null);
        String r = (String) m.invoke(o, (Object[]) null);

        assertEquals("HelloWorld version 2", r);

        URLClassLoader ucl = new URLClassLoader(new URL[] {version2}, plcl1);
        c = ucl.loadClass("deors.core.commons.HelloWorld");
        o = c.newInstance();
        m = c.getMethod("getVersion", (Class[]) null);
        r = (String) m.invoke(o, (Object[]) null);

        assertEquals("HelloWorld version 1", r);
    }

    @SuppressWarnings("resource")
    @Test
    public void testGetResource()
        throws IOException {

        URL version1 = this.getClass().getResource("/classloader/version1/HelloWorld-version-1.jar");
        ParentLastURLClassLoader plcl1 = new ParentLastURLClassLoader(new URL[] {version1});

        InputStream is1 = plcl1.getResourceAsStream("classloader/sample.txt");
        String r1 = new String(IOToolkit.readStream(is1));

        assertEquals("version 1\r\n", r1);

        URLClassLoader ucl1 = new URLClassLoader(new URL[] {version1});

        is1 = ucl1.getResourceAsStream("classloader/sample.txt");
        r1 = new String(IOToolkit.readStream(is1));

        assertEquals("version 0\r\n", r1);

        URL version2 = this.getClass().getResource("/classloader/version2/HelloWorld-version-2.jar");
        ParentLastURLClassLoader plcl2 = new ParentLastURLClassLoader(new URL[] {version2});

        InputStream is2 = plcl2.getResourceAsStream("classloader/sample.txt");
        String r2 = new String(IOToolkit.readStream(is2));

        assertEquals("version 2\r\n", r2);

        URLClassLoader ucl2 = new URLClassLoader(new URL[] {version2});

        is2 = ucl2.getResourceAsStream("classloader/sample.txt");
        r2 = new String(IOToolkit.readStream(is2));

        assertEquals("version 0\r\n", r2);
    }

    @SuppressWarnings("resource")
    @Test
    public void testGetResourceDelegated()
        throws IOException {

        URL empty = this.getClass().getResource("/classloader/empty.jar");
        ParentLastURLClassLoader plcl = new ParentLastURLClassLoader(new URL[] {empty});

        InputStream is = plcl.getResourceAsStream("classloader/sample.txt");
        String r = new String(IOToolkit.readStream(is));

        assertEquals("version 0\r\n", r);

        URLClassLoader ucl = new URLClassLoader(new URL[] {empty});

        is = ucl.getResourceAsStream("classloader/sample.txt");
        r = new String(IOToolkit.readStream(is));

        assertEquals("version 0\r\n", r);
    }
}
