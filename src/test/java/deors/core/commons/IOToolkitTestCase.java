package deors.core.commons;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class IOToolkitTestCase {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public IOToolkitTestCase() {

        super();
    }

    @Test
    public void testCompareArrays() {

        byte[] ba1 = new byte[] {1, 5, -3, 18, 54, 69};
        byte[] ba2 = new byte[] {1, 5, -3, 18, 54, 69};
        byte[] ba3 = new byte[] {1, 18, 54, 69};
        byte[] ba4 = new byte[] {1, 5, -3, 18};
        byte[] ba5 = new byte[] {1, 5, -3, 25, 54, 69, 62};
        byte[] ba6 = new byte[] {1, 5, -3, 18, 54, 69, -97};

        // System.out.println("ba1 = {1, 5, -3, 18, 54, 69};");
        // System.out.println("ba2 = {1, 5, -3, 18, 54, 69};");
        // System.out.println("ba3 = {1, 18, 54, 69};");
        // System.out.println("ba4 = {1, 5, -3, 18};");
        // System.out.println("ba5 = {1, 5, -3, 25, 54, 69, 62};");
        // System.out.println("ba6 = {1, 5, -3, 18, 54, 69, -97};");

        assertTrue(IOToolkit.compareArrays(ba1, ba2));
        assertFalse(IOToolkit.compareArrays(ba1, ba3));
        assertFalse(IOToolkit.compareArrays(ba1, ba4));
        assertFalse(IOToolkit.compareArrays(ba1, ba5));
        assertFalse(IOToolkit.compareArrays(ba1, ba6));

        assertTrue(IOToolkit.compareArrays(ba2, ba1));
        assertFalse(IOToolkit.compareArrays(ba2, ba3));
        assertFalse(IOToolkit.compareArrays(ba2, ba4));
        assertFalse(IOToolkit.compareArrays(ba2, ba5));
        assertFalse(IOToolkit.compareArrays(ba2, ba6));

        assertFalse(IOToolkit.compareArrays(ba3, ba1));
        assertFalse(IOToolkit.compareArrays(ba3, ba2));
        assertFalse(IOToolkit.compareArrays(ba3, ba4));
        assertFalse(IOToolkit.compareArrays(ba3, ba5));
        assertFalse(IOToolkit.compareArrays(ba3, ba6));
    }

    @Test
    public void testCompareStreamsNull()
        throws IOException {

        thrown.expect(IOException.class);
        IOToolkit.compareStreams(null, null);
    }

    @Test
    public void testCompareStreamsNull2()
        throws IOException {

        thrown.expect(IOException.class);
        IOToolkit.compareStreams(new ByteArrayInputStream(new byte[0]), null);
    }

    @Test
    public void testCompareStreamsNull3()
        throws IOException {

        thrown.expect(IOException.class);
        IOToolkit.compareStreams(null, new ByteArrayInputStream(new byte[0]));
    }

    @Test
    public void testCompareStreams()
        throws IOException {

        byte[] ba1 = new byte[] {1, 5, -3, 18, 54, 69};
        byte[] ba2 = new byte[] {1, 5, -3, 18, 54, 69};
        byte[] ba3 = new byte[] {1, 18, 54, 69};
        byte[] ba4 = new byte[] {1, 5, -3, 18};
        byte[] ba5 = new byte[] {1, 5, -3, 25, 54, 69, 62};
        byte[] ba6 = new byte[] {1, 5, -3, 18, 54, 69, -97};

        // System.out.println("ba1 = {1, 5, -3, 18, 54, 69};");
        // System.out.println("ba2 = {1, 5, -3, 18, 54, 69};");
        // System.out.println("ba3 = {1, 18, 54, 69};");
        // System.out.println("ba4 = {1, 5, -3, 18};");
        // System.out.println("ba5 = {1, 5, -3, 25, 54, 69, 62};");
        // System.out.println("ba6 = {1, 5, -3, 18, 54, 69, -97};");

        testCompareStreamsCallT(ba1, ba2);
        testCompareStreamsCallF(ba1, ba3);
        testCompareStreamsCallF(ba1, ba4);
        testCompareStreamsCallF(ba1, ba5);
        testCompareStreamsCallF(ba1, ba6);

        testCompareStreamsCallT(ba2, ba1);
        testCompareStreamsCallF(ba2, ba3);
        testCompareStreamsCallF(ba2, ba4);
        testCompareStreamsCallF(ba2, ba5);
        testCompareStreamsCallF(ba2, ba6);

        testCompareStreamsCallF(ba3, ba1);
        testCompareStreamsCallF(ba3, ba2);
        testCompareStreamsCallF(ba3, ba4);
        testCompareStreamsCallF(ba3, ba5);
        testCompareStreamsCallF(ba3, ba6);

        testCompareStreamsCallF(ba4, ba6);
    }

    @Test
    public void testCompareStreamsDefaultBuffer()
        throws IOException {

        byte[] ba1 = new byte[] {1, 5, -3, 18, 54, 69};
        byte[] ba2 = new byte[] {1, 5, -3, 18, 54, 69};

        ByteArrayInputStream bais1 = new ByteArrayInputStream(ba1);
        ByteArrayInputStream bais2 = new ByteArrayInputStream(ba2);

        assertTrue(IOToolkit.compareStreams(bais1, bais2));
    }

    private void testCompareStreamsCallT(byte[] ba1, byte[] ba2)
        throws IOException {

        ByteArrayInputStream bais1 = new ByteArrayInputStream(ba1);
        ByteArrayInputStream bais2 = new ByteArrayInputStream(ba2);

        assertTrue(IOToolkit.compareStreams(bais1, bais2, 4));
    }

    private void testCompareStreamsCallF(byte[] ba1, byte[] ba2)
        throws IOException {

        ByteArrayInputStream bais1 = new ByteArrayInputStream(ba1);
        ByteArrayInputStream bais2 = new ByteArrayInputStream(ba2);

        assertFalse(IOToolkit.compareStreams(bais1, bais2, 4));
    }

    @Test
    public void testCopyStreamNull()
        throws IOException {

        thrown.expect(NullPointerException.class);
        IOToolkit.copyStream(null, null);
    }

    @Test
    public void testCopyStreamNull2()
        throws IOException {

        thrown.expect(NullPointerException.class);
        IOToolkit.copyStream(new ByteArrayInputStream(new byte[0]), null);
    }

    @Test
    public void testCopyStreamNull3()
        throws IOException {

        thrown.expect(IOException.class);
        IOToolkit.copyStream(null, new ByteArrayOutputStream());
    }

    @Test
    public void testCopyStream()
        throws IOException {

        byte[] ba = new byte[] {1, 5, -3, 18, 54, 69};
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOToolkit.copyStream(bais, baos);

        assertArrayEquals(ba, baos.toByteArray());
    }

    @Test
    public void testCreateTempFile()
        throws IOException {

        File f1 = IOToolkit.createTempFile(true);
        File f2 = IOToolkit.createTempFile(false);

        assertNotNull(f1);
        assertNotNull(f2);

        assertTrue(f1.canRead());
        assertTrue(f2.canRead());

        assertTrue(f1.canWrite());
        assertTrue(f2.canWrite());

        assertTrue(f1.isFile());
        assertTrue(f2.isFile());

        assertEquals(0, f1.length());
        assertEquals(0, f2.length());

        assertTrue(f1.delete());
        assertTrue(f2.delete());
    }

    @Test
    public void testPrintString()
        throws IOException {

        String s = "test string written to a print writer";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(baos);
        IOToolkit.printString(s, pw);
        pw.close();

        assertEquals(s, baos.toString());
    }

    @Test
    public void testReadStreamNull()
        throws IOException {

        thrown.expect(IOException.class);
        IOToolkit.readStream(null);
    }

    @Test
    public void testReadStream()
        throws IOException, URISyntaxException {

        String s = "sample file for unit test cases\r\nsecond line of the sample file\r\nthe last line - the fine ends with a new line\r\n";
        InputStream is = this.getClass().getResourceAsStream("/samplefile.txt");
        byte[] ba = IOToolkit.readStream(is);

        assertEquals(s, new String(ba));
    }

    @Test
    public void testReadFileNull()
        throws IOException {

        thrown.expect(NullPointerException.class);
        IOToolkit.readFile(null);
    }

    @Test
    public void testReadFile()
        throws IOException, URISyntaxException {

        String s = "sample file for unit test cases\r\nsecond line of the sample file\r\nthe last line - the fine ends with a new line\r\n";
        URL url = this.getClass().getResource("/samplefile.txt");
        File f = new File(url.toURI());
        byte[] ba = IOToolkit.readFile(f);

        assertEquals(s, new String(ba));
    }

    @Test
    public void testReadTextFileNull()
        throws IOException {

        thrown.expect(NullPointerException.class);
        IOToolkit.readTextFile(null);
    }

    @Test
    public void testReadTextFile()
        throws IOException, URISyntaxException {

        String s0 = "sample file for unit test cases";
        String s1 = "second line of the sample file";
        String s2 = "the last line - the fine ends with a new line";
        URL url = this.getClass().getResource("/samplefile.txt");
        File f = new File(url.toURI());
        List<String> contents = IOToolkit.readTextFile(f);

        assertEquals(3, contents.size());
        assertEquals(s0, contents.get(0));
        assertEquals(s1, contents.get(1));
        assertEquals(s2, contents.get(2));
    }

    @Test
    public void testWriteStreamNull()
        throws IOException {

        thrown.expect(IOException.class);
        IOToolkit.writeStream(null);
    }

    @Test
    public void testWriteStream()
        throws IOException {

        String s = "sample file for unit test cases\r\nsecond line of the sample file\r\nthe last line - the fine ends with a new line\r\n";
        InputStream is = this.getClass().getResourceAsStream("/samplefile.txt");
        File f = IOToolkit.writeStream(is);
        byte[] ba = IOToolkit.readFile(f);

        assertEquals(s, new String(ba));
        assertTrue(f.delete());
    }

    @Test
    public void testWriteFileNull()
        throws IOException {

        thrown.expect(NullPointerException.class);
        IOToolkit.writeFile(null);
    }

    @Test
    public void testWriteFile()
        throws IOException {

        String s = "sample file for unit test cases\r\nsecond line of the sample file\r\nthe last line - the fine ends with a new line\r\n";
        InputStream is = this.getClass().getResourceAsStream("/samplefile.txt");
        byte[] data = IOToolkit.readStream(is);
        File f = IOToolkit.writeFile(data);
        byte[] ba = IOToolkit.readFile(f);

        assertEquals(s, new String(ba));
        assertTrue(f.delete());
    }

    @Test
    public void testExtractExtensionNull() {

        assertNull(IOToolkit.extractExtension(null));
    }

    @Test
    public void testExtractExtensionNo() {

        assertEquals("", IOToolkit.extractExtension(new File("noextension")));
    }

    @Test
    public void testExtractExtensionNo2() {

        assertEquals("", IOToolkit.extractExtension(new File("noextension.")));
    }

    @Test
    public void testExtractExtension() {

        assertEquals("jpg", IOToolkit.extractExtension(new File("picture.jpg")));
        assertEquals("JPEG", IOToolkit.extractExtension(new File("Picture.JPEG")));
    }

    @Test
    public void testExtractNameNull() {

        assertNull(IOToolkit.extractName(null));
    }

    @Test
    public void testExtractNameNo() {

        assertEquals("", IOToolkit.extractName(new File(".noname")));
    }

    @Test
    public void testExtractNameWhole() {

        assertEquals("onlyname", IOToolkit.extractName(new File("onlyname")));
    }

    @Test
    public void testExtractName() {

        assertEquals("picture", IOToolkit.extractName(new File("picture.jpg")));
        assertEquals("Picture", IOToolkit.extractName(new File("Picture.JPEG")));
    }

    @Test
    public void testMakeSameExtensionNull() {

        assertNull(IOToolkit.makeSameExtension(null, null));
        assertNull(IOToolkit.makeSameExtension(null, new File("")));
        assertNull(IOToolkit.makeSameExtension(new File(""), null));
    }

    @Test
    public void testMakeSameExtensionNoParent() {

        assertEquals(
            new File("source.xlsx"), IOToolkit.makeSameExtension(
            new File("source.xls"),
            new File("getext.xlsx")));
        assertEquals(
            new File(".xlsx"), IOToolkit.makeSameExtension(
            new File(".xls"),
            new File("getext.xlsx")));
        assertEquals(
            new File("source."), IOToolkit.makeSameExtension(
            new File("source.xls"),
            new File("getext")));
        assertEquals(
            new File("source."), IOToolkit.makeSameExtension(
            new File("source.xls"),
            new File("getext.")));
    }

    @Test
    public void testMakeSameExtension() {

        assertEquals(
            new File("/temporal/source.xlsx"), IOToolkit.makeSameExtension(
            new File("/temporal/source.xls"),
            new File("/ignored/getext.xlsx")));
        assertEquals(
            new File("/temporal/.xlsx"), IOToolkit.makeSameExtension(
            new File("/temporal/.xls"),
            new File("/ignored/getext.xlsx")));
        assertEquals(
            new File("/temporal/source."), IOToolkit.makeSameExtension(
            new File("/temporal/source.xls"),
            new File("/ignored/getext")));
        assertEquals(
            new File("/temporal/source."), IOToolkit.makeSameExtension(
            new File("/temporal/source.xls"),
            new File("/ignored/getext.")));
    }
}
