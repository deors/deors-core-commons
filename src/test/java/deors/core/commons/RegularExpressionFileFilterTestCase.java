package deors.core.commons;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;

public class RegularExpressionFileFilterTestCase {

    public RegularExpressionFileFilterTestCase() {

        super();
    }

    @Test
    public void testAcceptNull() {

        RegularExpressionFileFilter filter = new RegularExpressionFileFilter();
        assertFalse(filter.accept(null));
    }

    @Test
    public void testAcceptNoExist() {

        RegularExpressionFileFilter filter = new RegularExpressionFileFilter();
        assertFalse(filter.accept(new File("no.exist")));
    }

    @Test
    public void testAcceptDefault()
        throws URISyntaxException {

        RegularExpressionFileFilter filter = new RegularExpressionFileFilter();
        URL url1 = this.getClass().getResource("/samplefile.txt");
        File f1 = new File(url1.toURI());
        URL url2 = this.getClass().getResource("/emptyfile.foo");
        File f2 = new File(url2.toURI());

        assertTrue(filter.accept(new File(".")));
        assertTrue(filter.accept(f1));
        assertTrue(filter.accept(f2));
    }

    @Test
    public void testAcceptRegexp()
        throws URISyntaxException {

        RegularExpressionFileFilter filter = new RegularExpressionFileFilter(".*txt");
        URL url1 = this.getClass().getResource("/samplefile.txt");
        File f1 = new File(url1.toURI());
        URL url2 = this.getClass().getResource("/emptyfile.foo");
        File f2 = new File(url2.toURI());

        assertTrue(filter.accept(new File(".")));
        assertTrue(filter.accept(f1));
        assertFalse(filter.accept(f2));
    }
}
