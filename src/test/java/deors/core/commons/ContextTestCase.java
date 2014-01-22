package deors.core.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Field;

import org.junit.Test;

public class ContextTestCase {

    public ContextTestCase() {

        super();
    }

    @Test
    public void testInit() {

        assertNotNull(CommonsContext.getContext());
    }

    @Test
    public void testNoBundle() throws NoSuchFieldException, IllegalAccessException {

        AbstractContext noBundle = new AbstractContext("nobundle", null) {
            @SuppressWarnings("unused")
            private static final long serialVersionUID = 2689951325683362420L;
        };

        Field bundleField = AbstractContext.class.getDeclaredField("bundle");
        bundleField.setAccessible(true);

        assertNull(bundleField.get(noBundle));
    }

    @Test
    public void testGetConfigurationProperty() {

        assertEquals(4096, CommonsContext.getConfigurationProperty("commons.defaultBufferSize", 1));

        assertEquals("yyyy/MM/dd", CommonsContext.getConfigurationProperty("commons.defaultDateFormat", ""));

        assertEquals(';', CommonsContext.getConfigurationProperty("inimgr.commentsStartsWith", '0'));

        assertEquals(";", CommonsContext.getConfigurationProperty("inimgr.commentsStartsWith", ";", new String[] {";", "#"}));
    }

    @Test
    public void testGetConfigurationPropertyDefaultValue() {

        assertEquals(200, CommonsContext.getConfigurationProperty("nokey", 200));

        assertEquals("xxx", CommonsContext.getConfigurationProperty("nokey", "xxx"));

        assertEquals(true, CommonsContext.getConfigurationProperty("nokey", true));

        assertEquals('0', CommonsContext.getConfigurationProperty("nokey", '0'));

        assertEquals(";", CommonsContext.getConfigurationProperty("nokey", ";", new String[] {";", "#"}));
    }

    @Test
    public void testGetConfigurationPropertyIntegerError() {

        assertEquals(1, CommonsContext.getConfigurationProperty("commons.defaultDateFormat", 1));
    }

    @Test
    public void testGetMessage() {

        assertEquals("error reading base 64 encoded data",
            CommonsContext.getMessage("B64TK_ERR_READING_DATA"));

        assertEquals("file F not found",
            CommonsContext.getMessage("INIMGR_ERR_FILE_NOT_FOUND", "F"));
    }

    @Test
    public void testGetMessageDefaultValue() {

        assertEquals("nokey",
            CommonsContext.getMessage("nokey"));

        assertEquals("nokey",
            CommonsContext.getMessage("nokey", "F"));

        assertEquals("nokey",
            CommonsContext.getMessage("nokey", "A", "B"));

        assertEquals("nokey",
            CommonsContext.getMessage("nokey", new String[] {"P", "A", "B"}));

        assertEquals("nokey",
            CommonsContext.getMessage("nokey", new String[] {"{1}", "{0}"}, new String[] {"A", "B"}));
    }

    @Test
    public void testGetConfigurationPropertyEmptyToCharError() {

        Context c = new DummyContext();

        assertEquals('0', c.getIConfigurationProperty("emptyKey", '0'));
    }

    @Test
    public void testBaseBundleConfigString() {

        Context c = new DummyContext();

        assertEquals("value1", c.getIConfigurationProperty("key1", "def"));

        assertEquals("4096", c.getIConfigurationProperty("defaultBufferSize", "def"));
    }

    @Test
    public void testBaseBundleConfigStringValidValues() {

        Context c = new DummyContext();

        assertEquals("value1", c.getIConfigurationProperty("key1", "def", new String[] {"value1", "value2"}));

        assertEquals(";", c.getIConfigurationProperty("commentsStartsWith", "def", new String[] {";", "#"}));

        assertEquals("4096", c.getIConfigurationProperty("defaultBufferSize", "def", new String[] {"1024", "4096"}));
    }

    @Test
    public void testBaseBundleConfigInt() {

        Context c = new DummyContext();

        assertEquals(5, c.getIConfigurationProperty("keyn", 1));

        assertEquals(4096, c.getIConfigurationProperty("defaultBufferSize", 1000));
    }

    @Test
    public void testBaseBundleConfigChar() {

        Context c = new DummyContext();

        assertEquals('v', c.getIConfigurationProperty("key1", 'd'));

        assertEquals('4', c.getIConfigurationProperty("defaultBufferSize", 'd'));
    }

    @Test
    public void testBaseBundleConfigBoolean() {

        Context c = new DummyContext();

        assertEquals(true, c.getIConfigurationProperty("logOnInitialization", false));

        assertEquals(true, c.getIConfigurationProperty("logOnSilentError", false));
    }

    @Test
    public void testBaseBundleMessage() {

        Context c = new DummyContext();

        assertEquals("value1", c.getIMessage("key1"));

        assertEquals("4096", c.getIMessage("defaultBufferSize"));
    }

    @Test
    public void testBaseBundleMessageReplacement() {

        Context c = new DummyContext();

        assertEquals("file file1 not found2", c.getIMessage("FILE_NOT_FOUND_2", "file1"));

        assertEquals("file file1 not found", c.getIMessage("FILE_NOT_FOUND", "file1"));
    }

    @Test
    public void testBaseBundleMessageReplacement2() {

        Context c = new DummyContext();

        assertEquals("output value2: A = B", c.getIMessage("OUTPUT_VALUE_2", "A", "B"));

        assertEquals("output value: A = B", c.getIMessage("OUTPUT_VALUE", "A", "B"));
    }

    @Test
    public void testBaseBundleMessageReplacements() {

        Context c = new DummyContext();

        assertEquals("SELECT2 A FROM B WHERE C", c.getIMessage("SELECT_FROM_WHERE_2", new String[] {"A", "B", "C"}));

        assertEquals("SELECT A FROM B WHERE C", c.getIMessage("SELECT_FROM_WHERE", new String[] {"A", "B", "C"}));
    }

    @Test
    public void testBaseBundleMessageTokens() {

        Context c = new DummyContext();

        assertEquals("SELECT2 A FROM B WHERE C", c.getIMessage("SELECT_FROM_WHERE_2", new String[] {"{0}", "{1}", "{2}"}, new String[] {"A", "B", "C"}));

        assertEquals("SELECT A FROM B WHERE C", c.getIMessage("SELECT_FROM_WHERE", new String[] {"{0}", "{1}", "{2}"}, new String[] {"A", "B", "C"}));
    }

    private static class DummyBaseContext extends AbstractContext {

        public DummyBaseContext() {

            super("basecontext", null);
        }
    }

    private static class DummyContext extends AbstractContext {

        public DummyContext() {

            super("testcontext", new DummyBaseContext());
        }
    }
}
