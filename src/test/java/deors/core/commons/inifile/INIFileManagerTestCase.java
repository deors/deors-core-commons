package deors.core.commons.inifile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

import deors.core.commons.io.IOToolkit;

public class INIFileManagerTestCase {

    public INIFileManagerTestCase() {

        super();
    }

    @Test
    public void testConstructorFileNotExists()
        throws IOException {

        Exception ex = assertThrows(FileNotFoundException.class, () -> {

            File f = IOToolkit.createTempFile(false);
            f.delete();

            new INIFileManager(f);
        });
        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    public void testConstructorDirectory()
        throws IOException {

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {

            File f = new File(".");

            new INIFileManager(f);
        });
        assertTrue(ex.getMessage().contains("is a directory"));
    }

    @Test
    public void testConstructorFile()
        throws IOException {

        INIFileManager ifm = new INIFileManager(new File("target/test-classes/scheduler.ini"));

        assertSame(1, ifm.getKeys().size());
        assertEquals("default", ifm.getKeys().get(0));
        assertEquals("default section is skipped", ifm.getValue("default"));

        assertSame(4, ifm.getKeys("task").size());
        assertEquals("start", ifm.getKeys("task").get(2));
        assertEquals("22:00:00", ifm.getValue("task", "start"));

        assertEquals("xx", ifm.getValue("notfound", "notfound", "xx"));

        assertSame(3, ifm.getSections().size());
    }

    @Test
    public void testConstructorFileByName()
        throws IOException {

        INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

        assertSame(1, ifm.getKeys().size());
        assertEquals("default", ifm.getKeys().get(0));
        assertEquals("default section is skipped", ifm.getValue("default"));

        assertSame(4, ifm.getKeys("task").size());
        assertEquals("start", ifm.getKeys("task").get(2));
        assertEquals("22:00:00", ifm.getValue("task", "start"));

        assertEquals("xx", ifm.getValue("notfound", "notfound", "xx"));

        assertSame(3, ifm.getSections().size());
    }

    @Test
    public void testAdd1()
        throws IOException {

        INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

        ifm.addEntry("otherdef", "othervalue");
        ifm.addEntry("section", "key", "value");

        assertSame(2, ifm.getKeys().size());
        assertEquals("otherdef", ifm.getKeys().get(1));
        assertEquals("othervalue", ifm.getValue("otherdef"));

        assertSame(1, ifm.getKeys("section").size());
        assertEquals("key", ifm.getKeys("section").get(0));
        assertEquals("value", ifm.getValue("section", "key"));
    }

    @Test
    public void testAdd2()
        throws IOException {

        INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

        boolean r1 = ifm.addEntry("otherdef", "othervalue");
        boolean r2 = ifm.addEntry("otherdef", "othervalue");

        assertTrue(r1);
        assertFalse(r2);
    }

    @Test
    public void testAddNoException1()
        throws IOException {

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {

            INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

            ifm.addEntry(null, "value");
        });
        assertTrue(ex.getMessage().contains("the key is null or blank"));
    }

    @Test
    public void testAddNoException2()
        throws IOException {

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {

            INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

            ifm.addEntry("", "value");
        });
        assertTrue(ex.getMessage().contains("the key is null or blank"));
    }

    @Test
    public void testAddNoException3()
        throws IOException {

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {

            INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

            ifm.addEntry("key", null);
        });
        assertTrue(ex.getMessage().contains("the value is null or blank"));
    }

    @Test
    public void testAddNoException4()
        throws IOException {

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {

            INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

            ifm.addEntry("key", "");
        });
        assertTrue(ex.getMessage().contains("the value is null or blank"));
    }

    @Test
    public void testAddWithComments()
        throws IOException {

        INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

        List<String> comments = new ArrayList<String>();
        comments.add("these are the comments");
        comments.add("for the section.key entry");

        ifm.addEntry("section", "key", "value", comments);

        assertSame(2, ifm.getComments("section", "key").size());
    }

    @Test
    public void testAddWithEmptyComments()
        throws IOException {

        INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

        List<String> comments = new ArrayList<String>();

        ifm.addEntry("section", "key", "value", comments);

        assertNull(ifm.getComments("section", "key"));
    }

    @Test
    public void testAddSection()
        throws IOException {

        INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

        assertFalse(ifm.addSection());
        assertTrue(ifm.addSection("newsection"));
    }

    @Test
    public void testAddSectionWithComments()
        throws IOException {

        INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

        List<String> comments = new ArrayList<String>();
        comments.add("these are the comments");
        comments.add("for the section newsection");

        assertTrue(ifm.addSection("newsection", comments));
        assertTrue(ifm.getComments("newsection", null).size() == 2);
    }

    @Test
    public void testAddSectionWithCommentsEmpty()
        throws IOException {

        INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

        List<String> comments = new ArrayList<String>();

        assertTrue(ifm.addSection("newsection", comments));
        assertNull(ifm.getComments("newsection", null));
    }

    @Test
    public void testDefaultKeyWithComments()
        throws IOException {

        INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

        List<String> comments = new ArrayList<String>();
        comments.add("these are the comments");
        comments.add("for the .key entry");

        assertTrue(ifm.addEntry(null, "key", "value", comments));
        assertSame(2, ifm.getComments("key").size());
    }

    @Test
    public void testGetWhenNoKey1()
        throws IOException {

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {

            INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

            ifm.getValue(null, null);
        });
        assertTrue(ex.getMessage().contains("the key is null or blank"));
    }

    @Test
    public void testGetWhenNoKey2()
        throws IOException {

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {

            INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

            ifm.getValue(null, "");
        });
        assertTrue(ex.getMessage().contains("the key is null or blank"));
    }

    @Test
    public void testHasEntry()
        throws IOException {

        INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

        assertTrue(ifm.hasEntry("default"));
        assertTrue(ifm.hasEntry("task", "start"));
    }

    @Test
    public void testHasEntryNoKey1()
        throws IOException {

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {

            INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

            assertTrue(ifm.hasEntry(null, null));
        });
        assertTrue(ex.getMessage().contains("the key is null or blank"));
    }

    @Test
    public void testHasEntryNoKey2()
        throws IOException {

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {

            INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

            assertTrue(ifm.hasEntry(null, ""));
        });
        assertTrue(ex.getMessage().contains("the key is null or blank"));
    }

    @Test
    public void testHasSection()
        throws IOException {

        INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

        assertTrue(ifm.hasSection());
        assertTrue(ifm.hasSection("task"));
        assertTrue(ifm.hasSection(null));
        assertTrue(ifm.hasSection(""));
    }

    @Test
    public void testHasValue()
        throws IOException {

        INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

        assertTrue(ifm.hasValue("default section is skipped"));
        assertTrue(ifm.hasValue(null, "default section is skipped"));
        assertTrue(ifm.hasValue("", "default section is skipped"));
        assertTrue(ifm.hasValue("task", "test task"));
        assertFalse(ifm.hasValue("notfound"));
    }

    @Test
    public void testHasValueNo1()
        throws IOException {

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {

            INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

            ifm.hasValue(null, null);
        });
        assertTrue(ex.getMessage().contains("the value is null or blank"));
    }

    @Test
    public void testHasValueNo2()
        throws IOException {

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {

            INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

            ifm.hasValue(null, "");
        });
        assertTrue(ex.getMessage().contains("the value is null or blank"));
    }

    @Test
    public void testRemoveEntry()
        throws IOException {

        INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

        assertTrue(ifm.removeEntry("default"));
        assertFalse(ifm.removeEntry("notfound"));
        assertTrue(ifm.removeEntry("task", "start"));
        assertFalse(ifm.removeEntry("notfound", "start"));
        assertFalse(ifm.removeEntry("task", "notfound"));
    }

    @Test
    public void testRemoveEntryNoKey1()
        throws IOException {

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {

            INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

            ifm.removeEntry(null);
        });
        assertTrue(ex.getMessage().contains("the key is null or blank"));
    }

    @Test
    public void testRemoveEntryNoKey2()
        throws IOException {

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {

            INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

            ifm.removeEntry("");
        });
        assertTrue(ex.getMessage().contains("the key is null or blank"));
    }

    @Test
    public void testRemoveSection()
        throws IOException {

        INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

        assertTrue(ifm.removeSection());
        assertFalse(ifm.removeSection());
        assertTrue(ifm.removeSection("task"));
        assertFalse(ifm.removeSection("notfound"));
    }

    @Test
    public void testUpdateEntry()
        throws IOException {

        INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

        ifm.updateEntry("default", "new value");
        ifm.updateEntry("task", "start", "new start");

        assertEquals("new value", ifm.getValue("default"));
        assertEquals("new start", ifm.getValue("task", "start"));

        assertFalse(ifm.updateEntry("notfound", "newvalue"));
        assertFalse(ifm.updateEntry("notfound", "notfound", "newvalue"));
    }

    @Test
    public void testUpdateWithComments()
        throws IOException {

        INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

        List<String> comments = new ArrayList<String>();
        comments.add("these are the comments");
        comments.add("for the section.key entry");

        ifm.addEntry("section", "key", "initvalue");
        ifm.updateEntry("section", "key", "value", comments);

        assertSame(2, ifm.getComments("section", "key").size());
    }

    @Test
    public void testUpdateWithEmptyComments()
        throws IOException {

        INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

        List<String> comments = new ArrayList<String>();

        ifm.addEntry("section", "key", "initvalue");
        ifm.updateEntry("section", "key", "value", comments);

        assertNull(ifm.getComments("section", "key"));
    }

    @Test
    public void testUpdateNoKey1()
        throws IOException {

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {

            INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

            ifm.updateEntry(null, "newvalue");
        });
        assertTrue(ex.getMessage().contains("the key is null or blank"));
    }

    @Test
    public void testUpdateNoKey2()
        throws IOException {

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {

            INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

            ifm.updateEntry("", "newvalue");
        });
        assertTrue(ex.getMessage().contains("the key is null or blank"));
    }

    @Test
    public void testUpdateNoValue1()
        throws IOException {

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {

            INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

            ifm.updateEntry("default", null);
        });
        assertTrue(ex.getMessage().contains("the value is null or blank"));
    }

    @Test
    public void testUpdateNoValue2()
        throws IOException {

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {

            INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

            ifm.updateEntry("default", "");
        });
        assertTrue(ex.getMessage().contains("the value is null or blank"));
    }

    @Test
    public void testUpdateFileNothingToDo()
        throws IOException {

        File f = File.createTempFile("deors.core.commons.", ".test");

        INIFileManager ifm = new INIFileManager(f);

        ifm.updateFile();

        f.delete();
    }

    @Test
    public void testUpdateFileNotWritable()
        throws IOException {

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {

            File f = File.createTempFile("deors.core.commons.", ".test");
            f.setReadOnly();

            INIFileManager ifm = new INIFileManager(f);

            ifm.addEntry("key", "value");

            ifm.updateFile();
        });
        assertTrue(ex.getMessage().contains("is not writable"));
    }

    @Test
    public void testUpdateFile()
        throws IOException {

        File f = IOToolkit.createTempFile(true);

        INIFileManager ifm = new INIFileManager(f);

        List<String> comments = new ArrayList<>();
        comments.add("some comments");

        ifm.addEntry("key", "value");
        ifm.addEntry(null, "key2", "value2", comments);

        ifm.addSection("section", comments);
        ifm.addEntry("section", "key3", "value3");
        ifm.addEntry("section", "key4", "value4", comments);

        ifm.addSection("blank");

        ifm.updateFile();

        f.delete();
    }

    @Test
    public void testUpdateSection()
        throws IOException {

        INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

        List<String> comments = new ArrayList<String>();
        comments.add("some comments");

        ifm.updateSection("task", comments);

        assertTrue(ifm.getComments("task", null).size() == 1);
        assertEquals("some comments", ifm.getComments("task", null).get(0));
    }

    @Test
    public void testUpdateSectionNo1()
        throws IOException {

        INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

        List<String> comments = new ArrayList<String>();
        comments.add("some comments");

        ifm.updateSection("notfound", comments);
    }

    @Test
    public void testUpdateSectionNo2()
        throws IOException {

        INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

        List<String> comments = new ArrayList<String>();

        ifm.updateSection("notfound", comments);
    }

    @Test
    public void testUpdateSectionNo3()
        throws IOException {

        INIFileManager ifm = new INIFileManager("target/test-classes/scheduler.ini");

        ifm.updateSection("notfound", null);
    }
}
