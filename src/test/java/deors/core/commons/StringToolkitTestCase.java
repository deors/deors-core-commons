package deors.core.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertArrayEquals;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import deors.core.commons.StringToolkit;

public class StringToolkitTestCase {

    public StringToolkitTestCase() {

        super();
    }

    @Test
    public void testCapitalize() {

        assertNull(StringToolkit.capitalize(null));

        String s = "capitalization string test";
        String r = "Capitalization String Test";
        assertEquals(r, StringToolkit.capitalize(s));
    }

    @Test
    public void testCount() {

        assertEquals(0, StringToolkit.count(null, 'i'));
        assertEquals(0, StringToolkit.count("", 'i'));

        String s = "counting i characters in this string";
        assertEquals(5, StringToolkit.count(s, 'i'));
    }

    @Test
    public void testPadLeft() {

        String s = null;

        s = "sdc.tests string 1";
        assertEquals("  sdc.tests string 1", StringToolkit.padLeft(s, 20));
        assertEquals("sdc.tests ", StringToolkit.padLeft(s, 10));
        assertEquals("oosdc.tests string 1", StringToolkit.padLeft(s, 20, 'o'));

        s = "sdc.tests str 2";
        assertEquals("     sdc.tests str 2", StringToolkit.padLeft(s, 20));
        assertEquals("sdc.tests ", StringToolkit.padLeft(s, 10));
        assertEquals("ooooosdc.tests str 2", StringToolkit.padLeft(s, 20, 'o'));

        s = "";
        assertEquals("xxxxxxxxxx", StringToolkit.padLeft(s, 10, 'x'));

        s = null;
        assertEquals("xxxxxxxxxx", StringToolkit.padLeft(s, 10, 'x'));
    }

    @Test
    public void testPadRight() {

        String s = null;

        s = "sdc.tests string 1";
        assertEquals("sdc.tests string 1  ", StringToolkit.padRight(s, 20));
        assertEquals("sdc.tests ", StringToolkit.padRight(s, 10));
        assertEquals("sdc.tests string 1oo", StringToolkit.padRight(s, 20, 'o'));

        s = "sdc.tests str 2";
        assertEquals("sdc.tests str 2     ", StringToolkit.padRight(s, 20));
        assertEquals("sdc.tests ", StringToolkit.padRight(s, 10));
        assertEquals("sdc.tests str 2ooooo", StringToolkit.padRight(s, 20, 'o'));

        s = "";
        assertEquals("xxxxxxxxxx", StringToolkit.padRight(s, 10, 'x'));

        s = null;
        assertEquals("xxxxxxxxxx", StringToolkit.padRight(s, 10, 'x'));
    }

    @Test
    public void testParagraphBasic() {

        assertNull(StringToolkit.paragraph(null, 0));
        assertNull(StringToolkit.paragraph("", 0));
        assertNull(StringToolkit.paragraph("", -5));

        List<String> l = new ArrayList<String>();

        l.add("");
        assertEquals(l, StringToolkit.paragraph("", 10));

        l.clear();
        l.add("xx");
        assertEquals(l, StringToolkit.paragraph("xx", 10));
        assertEquals(l, StringToolkit.paragraph("xx", 2));
    }

    @Test
    public void testParagraphLongParagraph() {

        List<String> l = new ArrayList<String>();
        String s = null;

        l.add("example of a string");
        l.add("that is very long");
        l.add("and it is separated");
        l.add("by the space");
        l.add("character thus each");
        l.add("line will not exceed");
        l.add("the maximum");
        l.add("requested width.");
        s = "example of a string that is very long and "
            + "it is separated by the space character "
            + "thus each line will not exceed the maximum "
            + "requested width.";
        assertEquals(l, StringToolkit.paragraph(s, 20));
    }

    @Test
    public void testParagraphLongWords() {

        List<String> l = new ArrayList<String>();
        String s = null;

        l.add("example of a string");
        l.add("that is very");
        l.add("loooooooooooooooooooooooong");
        s = "example of a string that is very "
            + "loooooooooooooooooooooooong";
        assertEquals(l, StringToolkit.paragraph(s, 20));

        l.clear();
        l.add("example of a string");
        l.add("that is very");
        l.add("loooooooooooooooooooooooong.");
        l.add("incredible!");
        s = "example of a string that is very "
            + "loooooooooooooooooooooooong. incredible!";
        assertEquals(l, StringToolkit.paragraph(s, 20));
    }

    @Test
    public void testRemove() {

        assertNull(StringToolkit.remove(null, '\''));
        assertNull(StringToolkit.remove(null, "'"));
        assertNull(StringToolkit.remove("x", null));

        String s = null;

        s = "short string";
        assertEquals(s, StringToolkit.remove(s, "'", 25));
        assertEquals(s, StringToolkit.remove(s, "'"));
        assertEquals(s, StringToolkit.remove(s, '\'', 25));
        assertEquals(s, StringToolkit.remove(s, '\''));

        s = "remove test of 'quotes' **";
        assertEquals("remove test of quotes' **", StringToolkit.remove(s, "'"));
        assertEquals("remove test of 'quotes **", StringToolkit.remove(s, "'", 20));
        assertEquals("remove test of quotes' **", StringToolkit.remove(s, '\''));
        assertEquals("remove test of 'quotes **", StringToolkit.remove(s, '\'', 20));
    }

    @Test
    public void testRemoveAll() {

        assertNull(StringToolkit.removeAll(null, '\''));
        assertNull(StringToolkit.removeAll(null, "'"));
        assertNull(StringToolkit.removeAll("x", null));

        String s = null;

        s = "short string";
        assertEquals(s, StringToolkit.removeAll(s, "'", 25));
        assertEquals(s, StringToolkit.removeAll(s, "'"));
        assertEquals(s, StringToolkit.removeAll(s, '\'', 25));
        assertEquals(s, StringToolkit.removeAll(s, '\''));

        s = "remove test of 'quotes' **";
        assertEquals("remove test of quotes **", StringToolkit.removeAll(s, "'"));
        assertEquals("remove test of 'quotes **", StringToolkit.removeAll(s, "'", 20));
        assertEquals("remove test of quotes **", StringToolkit.removeAll(s, '\''));
        assertEquals("remove test of 'quotes **", StringToolkit.removeAll(s, '\'', 20));

        s = "remove test of last char";
        assertEquals("emove test of last cha", StringToolkit.removeAll(s, "r"));
    }

    @Test
    public void testRemoveLast() {

        assertNull(StringToolkit.removeLast(null, '\''));
        assertNull(StringToolkit.removeLast(null, '\'', 0));
        assertNull(StringToolkit.removeLast(null, "'"));
        assertNull(StringToolkit.removeLast("x", null));

        String s = null;

        s = "short string";
        assertEquals(s, StringToolkit.removeLast(s, "'", 25));
        assertEquals(s, StringToolkit.removeLast(s, "'"));
        assertEquals(s, StringToolkit.removeLast(s, '\'', 25));
        assertEquals(s, StringToolkit.removeLast(s, '\''));

        s = "remove test of 'quotes' **";
        assertEquals("remove test of 'quotes **", StringToolkit.removeLast(s, "'"));
        assertEquals("remove test of quotes' **", StringToolkit.removeLast(s, "'", 20));
        assertEquals("remove test of 'quotes **", StringToolkit.removeLast(s, '\''));
        assertEquals("remove test of quotes' **", StringToolkit.removeLast(s, '\'', 20));
    }

    @Test
    public void testRepeatCharacter() {

        assertEquals("", StringToolkit.repeatCharacter('.', 0));
        assertEquals("", StringToolkit.repeatCharacter('.', -1));
        assertEquals(".", StringToolkit.repeatCharacter('.', 1));
        assertEquals(".....", StringToolkit.repeatCharacter('.', 5));
    }

    @Test
    public void testReplaceDefault() {

        String s = null;

        s = "change test of {0} [default token]";
        assertEquals("change test of xx [default token]", StringToolkit.replace(s, "xx"));
    }

    @Test
    public void testReplace() {

        assertNull(StringToolkit.replace(null, "'", "''"));
        assertNull(StringToolkit.replace("x", null, "''"));

        assertEquals("x", StringToolkit.replace("x", "'", null));

        String s = null;

        s = "short string";
        assertEquals(s, StringToolkit.replace(s, "'", "''", 25));
        assertEquals(s, StringToolkit.replace(s, "'", "''"));

        s = "change test of 'quotes' **";
        assertEquals("change test of ''quotes' **", StringToolkit.replace(s, "'", "''"));

        s = "change test of ''quotes'' **";
        assertEquals("change test of 'quotes'' **", StringToolkit.replace(s, "''", "'"));
    }

    @Test
    public void testReplaceChar() {

        assertNull(StringToolkit.replace(null, '\'', "''"));

        assertEquals("x", StringToolkit.replace("x", '\'', null));

        String s = null;

        s = "short string";
        assertEquals(s, StringToolkit.replace(s, '\'', "''", 25));
        assertEquals(s, StringToolkit.replace(s, '\'', "''"));

        s = "change test of 'quotes' **";
        assertEquals("change test of ''quotes' **", StringToolkit.replace(s, '\'', "''"));
    }

    @Test
    public void testReplaceAll() {

        assertNull(StringToolkit.replaceAll(null, "'", "''"));
        assertNull(StringToolkit.replaceAll("x", null, "''"));

        assertEquals("x", StringToolkit.replaceAll("x", "'", null));

        String s = null;

        s = "short string";
        assertEquals(s, StringToolkit.replaceAll(s, "'", "''", 25));
        assertEquals(s, StringToolkit.replaceAll(s, "'", "''"));

        s = "change test of 'quotes' **";
        assertEquals("change test of ''quotes'' **", StringToolkit.replaceAll(s, "'", "''"));

        s = "change test of ''quotes'' **";
        assertEquals("change test of 'quotes' **", StringToolkit.replaceAll(s, "''", "'"));

        s = "change rr and last char";
        assertEquals("change XX and last chaX", StringToolkit.replaceAll(s, "r", "X"));
    }

    @Test
    public void testReplaceAllChar() {

        assertNull(StringToolkit.replaceAll(null, '\'', "''"));

        assertEquals("x", StringToolkit.replaceAll("x", '\'', null));

        String s = null;

        s = "short string";
        assertEquals(s, StringToolkit.replaceAll(s, '\'', "''", 25));
        assertEquals(s, StringToolkit.replaceAll(s, '\'', "''"));

        s = "change test of 'quotes' **";
        assertEquals("change test of ''quotes'' **", StringToolkit.replaceAll(s, '\'', "''"));
    }

    @Test
    public void testReplaceLast() {

        assertNull(StringToolkit.replaceLast(null, "'", "''"));
        assertNull(StringToolkit.replaceLast("x", null, "''"));

        assertEquals("x", StringToolkit.replaceLast("x", "'", null));

        assertNull(StringToolkit.replaceLast(null, "'", "''", 0));
        assertNull(StringToolkit.replaceLast("x", null, "''", 0));

        assertEquals("x", StringToolkit.replaceLast("x", "'", null, 0));

        String s = null;

        s = "short string";
        assertEquals(s, StringToolkit.replaceLast(s, "'", "''", 25));
        assertEquals(s, StringToolkit.replaceLast(s, "'", "''"));

        s = "change test of 'quotes' **";
        assertEquals("change test of 'quotes'' **", StringToolkit.replaceLast(s, "'", "''"));

        s = "change test of ''quotes'' **";
        assertEquals("change test of ''quotes' **", StringToolkit.replaceLast(s, "''", "'"));
    }

    @Test
    public void testReplaceLastChar() {

        assertNull(StringToolkit.replaceLast(null, '\'', "''"));

        assertEquals("x", StringToolkit.replaceLast("x", '\'', null));

        String s = null;

        s = "short string";
        assertEquals(s, StringToolkit.replaceLast(s, '\'', "''", 25));
        assertEquals(s, StringToolkit.replaceLast(s, '\'', "''"));

        s = "change test of 'quotes' **";
        assertEquals("change test of 'quotes'' **", StringToolkit.replaceLast(s, '\'', "''"));
    }

    @Test
    public void testReplaceMultipleDefault() {

        assertNull(StringToolkit.replaceMultiple(null, new String[] {}));
        assertNull(StringToolkit.replaceMultiple("x", (String[]) null));
        assertNull(StringToolkit.replaceMultiple("x", new String[] {}));

        String s = null;

        s = "change test of {0} {1} {2} {3} {4} {5} {6} [default tokens]";
        assertEquals("change test of 1st {1} {2} {3} {4} {5} {6} [default tokens]",
            StringToolkit.replaceMultiple(s, new String[] {"1st"}));
        assertEquals("change test of 1st 2nd {2} {3} {4} {5} {6} [default tokens]",
            StringToolkit.replaceMultiple(s, new String[] {"1st", "2nd"}));
        assertEquals("change test of 1st 2nd 3rd {3} {4} {5} {6} [default tokens]",
            StringToolkit.replaceMultiple(s, new String[] {"1st", "2nd", "3rd"}));
        assertEquals("change test of 1st 2nd 3rd 4th {4} {5} {6} [default tokens]",
            StringToolkit.replaceMultiple(s, new String[] {"1st", "2nd", "3rd", "4th"}));
        assertEquals("change test of 1st 2nd 3rd 4th 5th 6th {6} [default tokens]",
            StringToolkit.replaceMultiple(s, new String[] {"1st", "2nd", "3rd", "4th", "5th", "6th"}));
    }

    @Test
    public void testReplaceMultiple() {

        assertNull(StringToolkit.replaceMultiple(null, null, null));
        assertNull(StringToolkit.replaceMultiple(null, new String[] {}, null));
        assertNull(StringToolkit.replaceMultiple(null, null, new String[] {}));
        assertNull(StringToolkit.replaceMultiple(null, new String[] {}, new String[] {}));
        assertNull(StringToolkit.replaceMultiple("x", null, null));
        assertNull(StringToolkit.replaceMultiple("x", new String[] {}, null));
        assertNull(StringToolkit.replaceMultiple("x", null, new String[] {}));
        assertNull(StringToolkit.replaceMultiple("x", new String[] {}, new String[] {}));
        assertNull(StringToolkit.replaceMultiple("x", new String[] {"x"}, new String[] {}));
        assertNull(StringToolkit.replaceMultiple("x", new String[] {"x"}, new String[] {"x", "x"}));

        String s = null;

        s = "short string";
        assertEquals(s, StringToolkit.replaceMultiple(s, new String[] {"x"}, new String[] {"x"}, 25));
        assertEquals(s, StringToolkit.replaceMultiple(s, new String[] {"x"}, new String[] {"x"}));

        s = "change test of 'quotes' **";
        assertEquals("c<|>ge test of ''quoottes'' **", StringToolkit.replaceMultiple(s,
            new String[] {"'", "han", "ot"}, new String[] {"''", "<|>", "oott"}));

        s = "change test of ''quotes'' **";
        assertEquals("change tst of 'quots' **", StringToolkit.replaceMultiple(s,
            new String[] {"''", "tes"}, new String[] {"'", "ts"}));

        s = "multiple change test of last chars";
        assertEquals("multiple change teYt of laYt chaXY", StringToolkit.replaceMultiple(s,
            new String[] {"r", "s"}, new String[] {"X", "Y"}));
    }

    @Test
    public void testReplaceMultipleMap() {

        Map<String, String> emptyMap = new HashMap<String, String>();

        assertNull(StringToolkit.replaceMultiple(null, (Map<String, String>) null));
        assertNull(StringToolkit.replaceMultiple("x", (Map<String, String>) null));
        assertNull(StringToolkit.replaceMultiple(null, emptyMap));
        assertNull(StringToolkit.replaceMultiple("x", emptyMap));
        assertNull(StringToolkit.replaceMultiple(null, (Map<String, String>) null, 0));
        assertNull(StringToolkit.replaceMultiple("x", (Map<String, String>) null, 0));
        assertNull(StringToolkit.replaceMultiple(null, emptyMap, 0));
        assertNull(StringToolkit.replaceMultiple("x", emptyMap, 0));

        String s1 = "short string";
        Map<String, String> mapX = new HashMap<String, String>();
        mapX.put("x", "x");

        assertEquals(s1, StringToolkit.replaceMultiple(s1, mapX, 25));
        assertEquals(s1, StringToolkit.replaceMultiple(s1, mapX));

        String s2 = "change test of 'quotes' **";
        Map<String, String> mapQ1 = new HashMap<String, String>();
        mapQ1.put("'", "''");
        mapQ1.put("han", "<|>");
        mapQ1.put("ot", "oott");

        assertEquals("c<|>ge test of ''quoottes'' **", StringToolkit.replaceMultiple(s2, mapQ1));

        String s3 = "change test of ''quotes'' **";
        Map<String, String> mapQ2 = new HashMap<String, String>();
        mapQ2.put("''", "'");
        mapQ2.put("tes", "ts");

        assertEquals("change tst of 'quots' **", StringToolkit.replaceMultiple(s3, mapQ2));

        String s4 = "multiple change test of last chars";
        Map<String, String> mapQ3 = new HashMap<String, String>();
        mapQ3.put("r", "X");
        mapQ3.put("s", "Y");

        assertEquals("multiple change teYt of laYt chaXY", StringToolkit.replaceMultiple(s4, mapQ3));
    }

    @Test
    public void testTokenize() {

        String s = null;
        List<String> expected = new ArrayList<String>();

        s = "a test string";
        expected.clear();
        expected.add("a");
        expected.add("test");
        expected.add("string");
        assertEquals(expected, StringToolkit.tokenize(s, " "));

        s = "another;;test";
        expected.clear();
        expected.add("another");
        expected.add("");
        expected.add("test");
        assertEquals(expected, StringToolkit.tokenize(s, ";"));

        s = ";this test starts and end with blanks;";
        expected.clear();
        expected.add("");
        expected.add("this test starts and end with blanks");
        expected.add("");
        assertEquals(expected, StringToolkit.tokenize(s, ";"));

        s = "three;delimiters,fight to rule this string";
        expected.clear();
        expected.add("three");
        expected.add("delimiters");
        expected.add("fight");
        expected.add("to");
        expected.add("rule");
        expected.add("this");
        expected.add("string");
        assertEquals(expected, StringToolkit.tokenize(s, ";, "));

        s = "\"extrange case";
        expected.clear();
        assertEquals(expected, StringToolkit.tokenize(s, " ", '\"'));

        s = "test;;\"of\";csv,file;\"with separators;. ,\"\"everything in the same field\";"
            + "another\"field;the last two fields are blanks;\"\";";
        expected.clear();
        expected.add("test");
        expected.add("");
        expected.add("of");
        expected.add("csv,file");
        expected.add("with separators;. ,\"\"everything in the same field");
        expected.add("another\"field");
        expected.add("the last two fields are blanks");
        expected.add("");
        expected.add("");
        assertEquals(expected, StringToolkit.tokenize(s, ";", '\"'));
    }

    @Test
    public void testTrim() {

        String s = null;

        s = " ";
        assertEquals("", StringToolkit.trim(s));
        assertEquals("", StringToolkit.trim(s, new char[] {'0', ' '}, StringToolkit.TRIM_BOTH));

        s = "   trim with spaces   ";
        assertEquals("trim with spaces", StringToolkit.trim(s));
        assertEquals("trim with spaces", StringToolkit.trim(s, ' ', StringToolkit.TRIM_BOTH));
        assertEquals("trim with spaces   ", StringToolkit.trim(s, ' ', StringToolkit.TRIM_LEFT));
        assertEquals("   trim with spaces", StringToolkit.trim(s, ' ', StringToolkit.TRIM_RIGHT));

        s = "0.001testing trim100.0";
        assertEquals("1testing trim100.0", StringToolkit.trim(s, new char[] {'0', '.'},
            StringToolkit.TRIM_LEFT));
        assertEquals("0.001testing trim1", StringToolkit.trim(s, new char[] {'0', '.'},
            StringToolkit.TRIM_RIGHT));
        assertEquals("1testing trim1", StringToolkit.trim(s, new char[] {'0', '.'},
            StringToolkit.TRIM_BOTH));

        s = "/***/";
        assertEquals("", StringToolkit.trim(s, new char[] {'/', '*'},
            StringToolkit.TRIM_LEFT));
        assertEquals("", StringToolkit.trim(s, new char[] {'/', '*'},
            StringToolkit.TRIM_RIGHT));
        assertEquals("", StringToolkit.trim(s, new char[] {'/', '*'},
            StringToolkit.TRIM_BOTH));
    }

    @Test
    public void testAsHexadecimalString()
        throws UnsupportedEncodingException {

        assertEquals("",
            StringToolkit.asHexadecimalString(new byte[] {}));

        String s = "test of hexadecimal string conversion\u000C";
        byte[] b = s.getBytes("ISO-8859-1");
        String r = "74657374206F662068657861646563696D616C207374"
            + "72696E6720636F6E76657273696F6E0C";
        assertEquals(r, StringToolkit.asHexadecimalString(b));
    }

    @Test
    public void testAsHexadecimalArray()
        throws UnsupportedEncodingException {

        assertArrayEquals(new byte[] {},
            StringToolkit.asHexadecimalArray(null));
        assertArrayEquals(new byte[] {},
            StringToolkit.asHexadecimalArray(""));

        String s = "74657374206F662068657861646563696D616C207374"
            + "72696E6720636F6E76657273696F6E0C";
        String r = "test of hexadecimal string conversion\u000C";
        assertEquals(r, new String(
            StringToolkit.asHexadecimalArray(s), "ISO-8859-1"));
    }

    @Test
    public void testFormatForHTMLViewing() {

        String s = "string formatted for HTML\r"
            + "<tag-name>\n\r  \u000Bmultiple spaces\n"
            + "&\ttab space\r\nαινσϊΥ";
        String r = "string&nbsp;formatted&nbsp;for&nbsp;HTML<br>"
            + "&lt;tag-name&gt;<br>&nbsp;&nbsp;&nbsp;multiple"
            + "&nbsp;spaces<br>&amp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "tab&nbsp;space<br>&#225;&#233;&#237;"
            + "&#243;&#250;&#213;";
        assertEquals(r, StringToolkit.formatForHTMLViewing(s));

        String s2 = "\r\nstring formatted for HTML\r"
            + "<tag-name>\n\r  \u000Bmultiple spaces\n"
            + "&\ttab space\r\nαινσϊΥ";
        String r2 = "string&nbsp;formatted&nbsp;for&nbsp;HTML<br>"
            + "&lt;tag-name&gt;<br>&nbsp;&nbsp;&nbsp;multiple"
            + "&nbsp;spaces<br>&amp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "tab&nbsp;space<br>&#225;&#233;&#237;"
            + "&#243;&#250;&#213;";
        assertEquals(r2, StringToolkit.formatForHTMLViewing(s2));

        String s3 = "\n\rstring formatted for HTML\r"
            + "<tag-name>\n\r  \u000Bmultiple spaces\n"
            + "&\ttab space\r\nαινσϊΥ";
        String r3 = "string&nbsp;formatted&nbsp;for&nbsp;HTML<br>"
            + "&lt;tag-name&gt;<br>&nbsp;&nbsp;&nbsp;multiple"
            + "&nbsp;spaces<br>&amp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "tab&nbsp;space<br>&#225;&#233;&#237;"
            + "&#243;&#250;&#213;";
        assertEquals(r3, StringToolkit.formatForHTMLViewing(s3));
    }

    @Test
    public void testFormatForXML() {

        String s = "string formatted for XML\r"
            + "<tag-name>\n\r  multiple spaces\n"
            + "&\ttab space\r\nαινσϊΥ";
        String r = "string formatted for XML\r"
            + "&lt;tag-name&gt;\n\r  multiple spaces\n"
            + "&amp;\ttab space\r\n&#225;&#233;&#237;"
            + "&#243;&#250;&#213;";
        assertEquals(r, StringToolkit.formatForXML(s));
    }

    @Test
    public void testFormatListAsString() {

        List<String> l1 = new ArrayList<String>();
        assertEquals("", StringToolkit.formatListAsString(l1));

        List<String> l2 = new ArrayList<String>();
        l2.add("one");
        assertEquals("one", StringToolkit.formatListAsString(l2));

        List<String> l3 = new ArrayList<String>();
        l3.add("one");
        l3.add("two");
        assertEquals("one, two", StringToolkit.formatListAsString(l3));
    }

    @Test
    public void testHasInvalidCharacters() {

        assertFalse(StringToolkit.hasInvalidCharacters(null, "x"));
        assertFalse(StringToolkit.hasInvalidCharacters("", "x"));
        assertFalse(StringToolkit.hasInvalidCharacters("x", null));
        assertFalse(StringToolkit.hasInvalidCharacters("x", ""));

        assertTrue(StringToolkit.hasInvalidCharacters("prueba", "p"));
        assertFalse(StringToolkit.hasInvalidCharacters("prueba", "x"));
    }

    @Test
    public void testFormatDefaultDateFormat() {

        Calendar now = Calendar.getInstance();
        now.set(2009, 9, 15, 13, 30, 0);
        now.set(Calendar.MILLISECOND, 0);
        String s = "2009/10/15";

        assertEquals(s, StringToolkit.formatWithDefaultDateFormat(now.getTime()));
    }

    @Test
    public void testFormatDefaultTimeFormat() {

        Calendar now = Calendar.getInstance();
        now.set(2009, 9, 15, 13, 30, 0);
        now.set(Calendar.MILLISECOND, 0);
        String s = "13:30:00";

        assertEquals(s, StringToolkit.formatWithDefaultTimeFormat(now.getTime()));
    }

    @Test
    public void testFormatDefaultDateTimeFormat() {

        Calendar now = Calendar.getInstance();
        now.set(2009, 9, 15, 13, 30, 0);
        now.set(Calendar.MILLISECOND, 0);
        String s = "2009/10/15 13:30:00";

        assertEquals(s, StringToolkit.formatWithDefaultDateTimeFormat(now.getTime()));
    }
}
