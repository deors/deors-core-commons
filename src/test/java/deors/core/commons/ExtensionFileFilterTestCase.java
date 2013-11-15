package deors.core.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import deors.core.commons.ExtensionFileFilter;

public class ExtensionFileFilterTestCase {

    public ExtensionFileFilterTestCase() {

        super();
    }

    @Test
    public void testAcceptNull() {

        ExtensionFileFilter filter = new ExtensionFileFilter();

        assertFalse(filter.accept(null));
    }

    @Test
    public void testAcceptNotExist() {

        ExtensionFileFilter filter = new ExtensionFileFilter();

        assertFalse(filter.accept(new File("should-not-exist")));
    }

    @Test
    public void testAcceptfooension()
        throws URISyntaxException {

        ExtensionFileFilter filter = new ExtensionFileFilter();

        URL url1 = this.getClass().getResource("/emptyfile");
        File f1 = new File(url1.toURI());
        assertTrue(filter.accept(f1));
    }

    @Test
    public void testAcceptDirectory() {

        ExtensionFileFilter filter = new ExtensionFileFilter();

        assertTrue(filter.accept(new File(".")));
    }

    @Test
    public void testAcceptExtension()
        throws URISyntaxException {

        ExtensionFileFilter filter = new ExtensionFileFilter(".txt");
        URL url1 = this.getClass().getResource("/samplefile.txt");
        File f1 = new File(url1.toURI());
        URL url2 = this.getClass().getResource("/emptyfile.foo");
        File f2 = new File(url2.toURI());

        assertTrue(filter.accept(new File(".")));
        assertTrue(filter.accept(f1));
        assertFalse(filter.accept(f2));
    }

    @Test
    public void testAcceptExtensionArray()
        throws URISyntaxException {

        ExtensionFileFilter filter = new ExtensionFileFilter(new String[] {"txt", "foo"});
        URL url1 = this.getClass().getResource("/samplefile.txt");
        File f1 = new File(url1.toURI());
        URL url2 = this.getClass().getResource("/emptyfile.foo");
        File f2 = new File(url2.toURI());

        assertTrue(filter.accept(new File(".")));
        assertTrue(filter.accept(f1));
        assertTrue(filter.accept(f2));
    }

    @Test
    public void testAcceptExtensionList()
        throws URISyntaxException {

        List<String> list = new ArrayList<String>();
        list.add("txt");
        list.add("foo");
        ExtensionFileFilter filter = new ExtensionFileFilter(list);
        URL url1 = this.getClass().getResource("/samplefile.txt");
        File f1 = new File(url1.toURI());
        URL url2 = this.getClass().getResource("/emptyfile.foo");
        File f2 = new File(url2.toURI());

        assertTrue(filter.accept(new File(".")));
        assertTrue(filter.accept(f1));
        assertTrue(filter.accept(f2));
    }

    @Test
    public void testGetExtension()
        throws URISyntaxException {

        List<String> list = new ArrayList<String>();
        list.add("txt");
        list.add("foo");
        ExtensionFileFilter filter = new ExtensionFileFilter(list);
        URL url1 = this.getClass().getResource("/samplefile.txt");
        File f1 = new File(url1.toURI());
        URL url2 = this.getClass().getResource("/emptyfile.foo");
        File f2 = new File(url2.toURI());

        assertNull(filter.getExtension(null));
        assertEquals("txt", filter.getExtension(f1));
        assertEquals("foo", filter.getExtension(f2));
    }
}
