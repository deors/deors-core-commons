package deors.core.commons;

import static deors.core.commons.CommonsContext.getMessage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Toolkit methods for encoding and decoding data in Base 64.<br>
 *
 * @author deors
 * @version 1.0
 */
public final class Base64Toolkit {

    /**
     * Representation of the end of input data.
     */
    private static final int END_OF_INPUT = -1;

    /**
     * Representation of an invalid Base 64 character.
     */
    private static final int BASE_64_INVALID = -1;

    /**
     * Representation of a Base 64 whitespace character.
     */
    private static final int BASE_64_WHITESPACE = -2;

    /**
     * Representation of a Base 64 padding character.
     */
    private static final int BASE_64_PADDING = -3;

    /**
     * The padding character.
     */
    private static final char PADDING_CHARACTER = '=';

    /**
     * The decoded string packet size.
     */
    private static final int DECODED_STRING_PACKET_SIZE = 3;

    /**
     * The encoded string packet size.
     */
    private static final int ENCODED_STRING_PACKET_SIZE = 4;

    /**
     * The platform-specific new line character(s).
     */
    private static final String NEW_LINE = System.getProperty("line.separator"); //$NON-NLS-1$

    /**
     * The Base 64 character matrix.
     */
    private static final byte[] BASE_64_CHARS = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
        'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
        'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
        'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
        'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
        'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
        'w', 'x', 'y', 'z', '0', '1', '2', '3',
        '4', '5', '6', '7', '8', '9', '+', '/'};

    /**
     * The reverse Base 64 character matrix length.
     */
    private static final int REVERSE_BASE_64_CHARS_LENGTH = 256;

    /**
     * The reverse Base 64 character matrix.
     */
    private static final byte[] REVERSE_BASE_64_CHARS = new byte[REVERSE_BASE_64_CHARS_LENGTH];

    /**
     * Initializes the reverse Base 64 character matrix.
     */
    static {
        for (int i = 0; i < REVERSE_BASE_64_CHARS.length; i++) {
            REVERSE_BASE_64_CHARS[i] = BASE_64_INVALID;
        }

        for (byte i = 0; i < BASE_64_CHARS.length; i++) {
            REVERSE_BASE_64_CHARS[BASE_64_CHARS[i]] = i;
        }

        REVERSE_BASE_64_CHARS[' '] = BASE_64_WHITESPACE;
        REVERSE_BASE_64_CHARS['\n'] = BASE_64_WHITESPACE;
        REVERSE_BASE_64_CHARS['\r'] = BASE_64_WHITESPACE;
        REVERSE_BASE_64_CHARS['\t'] = BASE_64_WHITESPACE;
        REVERSE_BASE_64_CHARS['\f'] = BASE_64_WHITESPACE;
        REVERSE_BASE_64_CHARS[PADDING_CHARACTER] = BASE_64_PADDING;
    }

    /**
     * Default constructor. This class is a toolkit and therefore it cannot be instantiated.
     */
    private Base64Toolkit() {
        super();
    }

    /**
     * Encodes a string using the platform's default charset and returns the result into another
     * string created using the platform's default charset.
     *
     * @param string the data to be encoded
     *
     * @return the encoded data
     */
    public static String encode(String string) {

        return new String(encode(string.getBytes()));
    }

    /**
     * Encodes a string using the given charset and returns the result into another string created
     * using the same charset.
     *
     * @param string the data to be encoded
     * @param encoding the character encoding that will be used to transform between bytes and
     *        characters
     *
     * @return the encoded data
     *
     * @throws java.io.UnsupportedEncodingException the character encoding is not supported
     */
    public static String encode(String string, String encoding)
        throws java.io.UnsupportedEncodingException {

        return new String(encode(string.getBytes(encoding)), encoding);
    }

    /**
     * Encodes a byte array and returns the encoded data into another byte array. If the encoding
     * process fails the method returns an empty array.
     *
     * @param bytes the data to be encoded
     *
     * @return the encoded data or an empty array if the encoding process fails
     */
    public static byte[] encode(byte[] bytes) {

        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

        int length = bytes.length;
        int mod = length % DECODED_STRING_PACKET_SIZE;

        if (mod != 0) {
            length += DECODED_STRING_PACKET_SIZE - mod;
        }

        length *= ENCODED_STRING_PACKET_SIZE / DECODED_STRING_PACKET_SIZE;

        ByteArrayOutputStream baos = new ByteArrayOutputStream(length);

        try {
            encode(bais, baos);
        } catch (IOException ioe) {
            return new byte[0];
        }

        return baos.toByteArray();
    }

    /**
     * Encodes data from an input stream and writes the encoded data into the given output stream.
     *
     * @param is the data input stream
     * @param os the encoded data output stream
     *
     * @throws java.io.IOException an I/O exception
     */
    public static void encode(InputStream is, OutputStream os)
        throws java.io.IOException {

        final int packetByte1 = 0;
        final int packetByte2 = 1;
        final int packetByte3 = 2;
        final int charZero = 0x30;
        final int charLessThan = 0x3c;
        final int charQuestion = 0x3f;
        final int shift2 = 2;
        final int shift4 = 4;
        final int shift6 = 6;
        final int maxLineWidth = 76;

        int[] inBuffer = new int[DECODED_STRING_PACKET_SIZE];
        int lineWidth = 0;

        boolean done = false;

        while (!done) {
            inBuffer[packetByte1] = is.read();

            if (inBuffer[packetByte1] == END_OF_INPUT) {
                break;
            }

            inBuffer[packetByte2] = is.read();
            inBuffer[packetByte3] = is.read();

            os.write(BASE_64_CHARS[inBuffer[packetByte1] >> shift2]);

            if (inBuffer[packetByte2] == END_OF_INPUT) {

                os.write(BASE_64_CHARS[(inBuffer[packetByte1] << shift4) & charLessThan]);
                os.write(PADDING_CHARACTER);
                os.write(PADDING_CHARACTER);
                done = true;

            } else {

                os.write(BASE_64_CHARS[
                    ((inBuffer[packetByte1] << shift4) & charZero)
                    | (inBuffer[packetByte2] >> shift4)]);

                if (inBuffer[packetByte3] == END_OF_INPUT) {

                    os.write(BASE_64_CHARS[(inBuffer[packetByte2] << shift2) & charLessThan]);
                    os.write(PADDING_CHARACTER);
                    done = true;

                } else {

                    os.write(BASE_64_CHARS[
                        ((inBuffer[packetByte2] << shift2) & charLessThan)
                        | (inBuffer[packetByte3] >> shift6)]);
                    os.write(BASE_64_CHARS[inBuffer[packetByte3] & charQuestion]);
                }
            }

            lineWidth += ENCODED_STRING_PACKET_SIZE;

            if (lineWidth >= maxLineWidth) {

                os.write(NEW_LINE.getBytes());
                lineWidth = 0;
            }
        }

        if (lineWidth >= 1) {

            os.write(NEW_LINE.getBytes());
            lineWidth = 0;
        }

        os.flush();
    }

    /**
     * Decodes an encoded string using the platform's default charset and returns the result into
     * another string created using the platform's default charset.
     *
     * @param string the data to be decoded
     *
     * @return the decoded data
     */
    public static String decode(String string) {

        return new String(decode(string.getBytes()));
    }

    /**
     * Decodes an encoded string using the given charset and returns the result into another string
     * created using the same charset.
     *
     * @param string the data to be decoded
     * @param encoding the character encoding that will be used to transform between bytes and
     *        characters
     *
     * @return the decoded data
     *
     * @throws java.io.UnsupportedEncodingException the character encoding is not supported
     */
    public static String decode(String string, String encoding)
        throws java.io.UnsupportedEncodingException {

        return new String(decode(string.getBytes(encoding)), encoding);
    }

    /**
     * Decodes an encoded byte array and returns the decoded data into another byte array. If the
     * decoding process fails the method returns an empty array.
     *
     * @param bytes the data to be decoded
     *
     * @return the decoded data or an empty array if the decoding process fails
     */
    public static byte[] decode(byte[] bytes) {

        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

        int length = bytes.length;
        int mod = length % ENCODED_STRING_PACKET_SIZE;

        if (mod != 0) {
            length += ENCODED_STRING_PACKET_SIZE - mod;
        }

        length = length * DECODED_STRING_PACKET_SIZE / ENCODED_STRING_PACKET_SIZE;

        ByteArrayOutputStream baos = new ByteArrayOutputStream(length);

        try {
            decode(bais, baos);
        } catch (IOException ioe) {
            return new byte[0];
        }

        return baos.toByteArray();
    }

    /**
     * Decodes encoded data from an input stream and writes the decoded data into the given output
     * stream.
     *
     * @param is the encoded data input stream
     * @param os the decoded data output stream
     *
     * @throws java.io.IOException an I/O exception
     */
    public static void decode(InputStream is, OutputStream os) throws java.io.IOException {

        final int packetByte1 = 0;
        final int packetByte2 = 1;
        final int packetByte3 = 2;
        final int packetByte4 = 3;
        final int shift2 = 2;
        final int shift4 = 4;
        final int shift6 = 6;

        int[] inBuffer = new int[ENCODED_STRING_PACKET_SIZE];

        boolean done = false;

        while (!done) {
            inBuffer[packetByte1] = readBase64(is);

            if (inBuffer[packetByte1] == END_OF_INPUT) {
                break;
            }

            inBuffer[packetByte2] = readBase64(is);

            if (inBuffer[packetByte2] == END_OF_INPUT) {
                break;
            }

            inBuffer[packetByte3] = readBase64(is);
            inBuffer[packetByte4] = readBase64(is);

            os.write(inBuffer[packetByte1] << shift2 | inBuffer[packetByte2] >> shift4);

            if (inBuffer[packetByte3] == END_OF_INPUT) {

                done = true;

            } else {

                os.write(inBuffer[packetByte2] << shift4 | inBuffer[packetByte3] >> shift2);

                if (inBuffer[packetByte4] == END_OF_INPUT) {

                    done = true;

                } else {

                    os.write(inBuffer[packetByte3] << shift6 | inBuffer[packetByte4]);
                }
            }
        }

        os.flush();
    }

    /**
     * Reads next byte of data from a Base 64 encoded input stream.
     *
     * @param is the encoded data input stream
     *
     * @return the byte read from the stream
     *
     * @throws java.io.IOException the stream contains an invalid Base 64 character
     */
    private static int readBase64(InputStream is) throws java.io.IOException {

        int read;
        int numPadding = 0;

        do {
            read = is.read();

            if (read == END_OF_INPUT) {
                return END_OF_INPUT;
            }

            byte index = (byte) read;

            if (index < 0) {
                throw new IOException(getMessage("B64TK_ERR_READING_DATA")); //$NON-NLS-1$
            }

            read = REVERSE_BASE_64_CHARS[index];

            if (read == BASE_64_INVALID || (numPadding > 0 && read > BASE_64_INVALID)) {
                throw new IOException(getMessage("B64TK_ERR_READING_DATA")); //$NON-NLS-1$
            }

            if (read == BASE_64_PADDING) {
                numPadding++;
            }
        } while (read <= BASE_64_INVALID);

        return read;
    }

    /**
     * Checks whether data is in Base 64 format using platform's default charset to convert between
     * bytes and characters.
     *
     * @param string the encoded data
     *
     * @return <code>true</code> if the input data is in Base 64 format or <code>false</code>
     *         otherwise
     */
    public static boolean isBase64(String string) {

        return isBase64(string.getBytes());
    }

    /**
     * Checks whether data is in Base 64 format using the given charset to convert between bytes and
     * characters.
     *
     * @param string the encoded data
     * @param encoding the character encoding used to transform between bytes and characters
     *
     * @return <code>true</code> if the input data is in Base 64 format or <code>false</code>
     *         otherwise
     *
     * @throws java.io.UnsupportedEncodingException the character encoding is not supported
     */
    public static boolean isBase64(String string, String encoding)
        throws java.io.UnsupportedEncodingException {

        return isBase64(string.getBytes(encoding));
    }

    /**
     * Checks whether data is in Base 64 format.
     *
     * @param bytes the encoded data
     *
     * @return <code>true</code> if the input data is in Base 64 format or <code>false</code>
     *         otherwise
     */
    public static boolean isBase64(byte[] bytes) {

        try {
            return isBase64(new ByteArrayInputStream(bytes));
        } catch (IOException ioe) {
            return false;
        }
    }

    /**
     * Checks whether data is in Base 64 format.
     *
     * @param is the encoded data input stream
     *
     * @return <code>true</code> if the input data is in Base 64 format or <code>false</code>
     *         otherwise
     *
     * @throws java.io.IOException an I/O exception
     */
    public static boolean isBase64(InputStream is)
        throws java.io.IOException {

        boolean isBase64 = true;
        long numBase64Chars = 0;
        int numPadding = 0;
        int read;

        while ((read = is.read()) != -1) {
            read = REVERSE_BASE_64_CHARS[read];

            if (read == BASE_64_INVALID) {
                isBase64 = false;
                break;
            } else if (read == BASE_64_WHITESPACE) {
                continue;
            } else if (read == BASE_64_PADDING) {
                numPadding++;
                numBase64Chars++;
            } else if (numPadding > 0) {
                isBase64 = false;
                break;
            } else {
                numBase64Chars++;
            }
        }

        // the stream does not contain valid characters
        // or the stream size is not a multiple of the packet size
        if (isBase64
            && (numBase64Chars == 0 || numBase64Chars % ENCODED_STRING_PACKET_SIZE != 0)) {
            isBase64 = false;
        }

        return isBase64;
    }
}
