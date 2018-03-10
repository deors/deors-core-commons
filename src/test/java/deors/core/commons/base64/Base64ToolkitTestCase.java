package deors.core.commons.base64;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import deors.core.commons.CommonsContext;
import deors.core.commons.base64.Base64Toolkit;

public class Base64ToolkitTestCase {

    private static final String NEW_LINE = "\n";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public Base64ToolkitTestCase() {

        super();
    }

    @Test
    public void testEncodeBytes() {

        assertArrayEquals(
            new byte[0],
            Base64Toolkit.encode(new byte[0]));
        assertArrayEquals(
            new byte[] {70, 65, 61, 61, 10},
            Base64Toolkit.encode(new byte[] {20}));
        assertArrayEquals(
            new byte[] {55, 65, 61, 61, 10},
            Base64Toolkit.encode(new byte[] {-20}));
        assertArrayEquals(
            new byte[] {101, 65, 61, 61, 10},
            Base64Toolkit.encode(new byte[] {120}));
        assertArrayEquals(
            new byte[] {105, 65, 61, 61, 10},
            Base64Toolkit.encode(new byte[] {-120}));
        assertArrayEquals(
            new byte[] {70, 68, 99, 61, 10},
            Base64Toolkit.encode(new byte[] {20, 55}));
        assertArrayEquals(
            new byte[] {55, 77, 107, 61, 10},
            Base64Toolkit.encode(new byte[] {-20, -55}));
        assertArrayEquals(
            new byte[] {101, 72, 77, 61, 10},
            Base64Toolkit.encode(new byte[] {120, 115}));
        assertArrayEquals(
            new byte[] {105, 73, 48, 61, 10},
            Base64Toolkit.encode(new byte[] {-120, -115}));
    }

    @Test
    public void testEncodeStringNull() {

        thrown.expect(NullPointerException.class);
        Base64Toolkit.encode((String) null);
    }

    @Test
    public void testEncodeStringNoEncoding()
        throws UnsupportedEncodingException {

        thrown.expect(UnsupportedEncodingException.class);
        Base64Toolkit.encode("string", "no-encoding");
    }

    @Test
    public void testEncodeString()
        throws UnsupportedEncodingException {

        assertEquals("KCkmJSQ=" + NEW_LINE, Base64Toolkit.encode("()&%$"));
        assertEquals("b3JpZ2luYWwgc3RyaW5n" + NEW_LINE, Base64Toolkit.encode("original string"));
        assertEquals("dGhpcyBpcyBhIGxvbmcsIGEgdmVyeSB2ZXJ5IGxvbmcsIG9yaWdpbmFsIHN0cmluZw==" + NEW_LINE, Base64Toolkit.encode("this is a long, a very very long, original string"));
        assertEquals("dGhpcyBpcyBhbiBpbmNyZWRpYmx5IGxvbmcgb3JpZ2luYWwgc3RyaW5nLCBidXQgYSByZWFsIGlu" + NEW_LINE + "Y3JlZGlibHkgdmVyeSB2ZXJ5IGxvbmcgb3JpZ2luYWwgc3RyaW5n" + NEW_LINE, Base64Toolkit.encode("this is an incredibly long original string, but a real incredibly very very long original string"));
    }

    @Test
    public void testEncodeStringEncoding()
        throws UnsupportedEncodingException {

        assertEquals("b3JpZ2luYWwgc3RyaW5n" + NEW_LINE, Base64Toolkit.encode("original string", "ISO-8859-1"));
        assertEquals("4ent8/rx" + NEW_LINE, Base64Toolkit.encode("αινσϊρ", "ISO-8859-1"));

        assertEquals("b3JpZ2luYWwgc3RyaW5n" + NEW_LINE, Base64Toolkit.encode("original string", "UTF-8"));
        assertEquals("w6HDqcOtw7PDusOx" + NEW_LINE, Base64Toolkit.encode("αινσϊρ", "UTF-8"));
    }

    @Test
    public void testDecodeBytes() {

        assertArrayEquals(
            new byte[0],
            Base64Toolkit.decode(new byte[0]));
        assertArrayEquals(
            new byte[0],
            Base64Toolkit.decode(new byte[] {65}));
        assertArrayEquals(
            new byte[] {20},
            Base64Toolkit.decode(new byte[] {70, 65, 61, 61, 10}));
        assertArrayEquals(
            new byte[] {-20},
            Base64Toolkit.decode(new byte[] {55, 65, 61, 61, 10}));
        assertArrayEquals(
            new byte[] {120},
            Base64Toolkit.decode(new byte[] {101, 65, 61, 61, 10}));
        assertArrayEquals(
            new byte[] {-120},
            Base64Toolkit.decode(new byte[] {105, 65, 61, 61, 10}));
        assertArrayEquals(
            new byte[] {20, 55},
            Base64Toolkit.decode(new byte[] {70, 68, 99, 61, 10}));
        assertArrayEquals(
            new byte[] {-20, -55},
            Base64Toolkit.decode(new byte[] {55, 77, 107, 61, 10}));
        assertArrayEquals(
            new byte[] {120, 115},
            Base64Toolkit.decode(new byte[] {101, 72, 77, 61, 10}));
        assertArrayEquals(
            new byte[] {-120, -115},
            Base64Toolkit.decode(new byte[] {105, 73, 48, 61, 10}));
    }

    @Test
    public void testDecodeBytesInvalid()
        throws IOException {

        assertArrayEquals(new byte[0], Base64Toolkit.decode("αινσϊρ".getBytes()));
        assertArrayEquals(new byte[0], Base64Toolkit.decode("()&%$".getBytes()));
    }

    @Test
    public void testDecodeStringNull() {

        thrown.expect(NullPointerException.class);
        Base64Toolkit.decode((String) null);
    }

    @Test
    public void testDecodeStringInvalid()
        throws IOException {

        assertEquals("", Base64Toolkit.decode("αινσϊρ"));
        assertEquals("", Base64Toolkit.decode("()&%$"));
    }

    @Test
    public void testDecodeStringNoEncoding()
        throws UnsupportedEncodingException {

        thrown.expect(UnsupportedEncodingException.class);
        Base64Toolkit.decode("string", "no-encoding");
    }

    @Test
    public void testDecodeString()
        throws UnsupportedEncodingException {

        assertEquals("()&%$", Base64Toolkit.decode("KCkmJSQ="));
        assertEquals("original string", Base64Toolkit.decode("b3JpZ2luYWwgc3RyaW5n"));
        assertEquals("this is a long, a very very long, original string", Base64Toolkit.decode("dGhpcyBpcyBhIGxvbmcsIGEgdmVyeSB2ZXJ5IGxvbmcsIG9yaWdpbmFsIHN0cmluZw=="));
        assertEquals("this is an incredibly long original string, but a real incredibly very very long original string", Base64Toolkit.decode("dGhpcyBpcyBhbiBpbmNyZWRpYmx5IGxvbmcgb3JpZ2luYWwgc3RyaW5nLCBidXQgYSByZWFsIGlu" + NEW_LINE + "Y3JlZGlibHkgdmVyeSB2ZXJ5IGxvbmcgb3JpZ2luYWwgc3RyaW5n"));
    }

    @Test
    public void testDecodeStringEncoding()
        throws UnsupportedEncodingException {

        assertEquals("original string", Base64Toolkit.decode("b3JpZ2luYWwgc3RyaW5n", "ISO-8859-1"));
        assertEquals("αινσϊρ", Base64Toolkit.decode("4ent8/rx", "ISO-8859-1"));

        assertEquals("original string", Base64Toolkit.decode("b3JpZ2luYWwgc3RyaW5n", "UTF-8"));
        assertEquals("αινσϊρ", Base64Toolkit.decode("w6HDqcOtw7PDusOx", "UTF-8"));
    }

    @Test
    public void testDecodeStreamsInvalid()
        throws IOException {

        thrown.expect(IOException.class);
        thrown.expectMessage(CommonsContext.getMessage("B64TK_ERR_READING_DATA"));
        ByteArrayInputStream bais = new ByteArrayInputStream("αινσϊρ".getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream(100);
        Base64Toolkit.decode(bais, baos);
    }
    @Test
    public void testIsBase64StringNoEncoding()
        throws UnsupportedEncodingException {

        thrown.expect(UnsupportedEncodingException.class);
        Base64Toolkit.isBase64("string", "no-encoding");
    }

    @Test
    public void testIsBase64String()
        throws UnsupportedEncodingException {

        assertTrue(Base64Toolkit.isBase64("b3JpZ2luYWwgc3RyaW5n"));
        assertTrue(Base64Toolkit.isBase64("4ent8/rx"));

        assertFalse(Base64Toolkit.isBase64("()&%$"));
        assertFalse(Base64Toolkit.isBase64(" \t\r\n\f"));
        assertFalse(Base64Toolkit.isBase64("="));
        assertFalse(Base64Toolkit.isBase64("=A"));
    }

    @Test
    public void testIsBase64StringEncoding()
        throws UnsupportedEncodingException {

        assertTrue(Base64Toolkit.isBase64("b3JpZ2luYWwgc3RyaW5n", "ISO-8859-1"));
        assertTrue(Base64Toolkit.isBase64("4ent8/rx", "ISO-8859-1"));
        assertFalse(Base64Toolkit.isBase64("()&%$", "ISO-8859-1"));

        assertTrue(Base64Toolkit.isBase64("b3JpZ2luYWwgc3RyaW5n", "UTF-8"));
        assertTrue(Base64Toolkit.isBase64("w6HDqcOtw7PDusOx", "UTF-8"));
        assertFalse(Base64Toolkit.isBase64("()&%$", "UTF-8"));
    }
}
