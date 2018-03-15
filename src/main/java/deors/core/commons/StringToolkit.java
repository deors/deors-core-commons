package deors.core.commons;

import static deors.core.commons.CommonsContext.BLANK;
import static deors.core.commons.CommonsContext.SPACE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Toolkit methods for working with strings.
 *
 * <p>Most of the functions in this toolkit are replacements to functions in popular open source
 * libraries and are useful when relying on external, open source libraries, is not possible or
 * by doing so it brings on integration issues between other dependencies.
 *
 * @author deors
 * @version 1.0
 */
public final class StringToolkit {

    /**
     * Used in the trim method to trim leading characters.
     */
    public static final int TRIM_LEFT = 0;

    /**
     * Used in the trim method to trim trailing characters.
     */
    public static final int TRIM_RIGHT = 1;

    /**
     * Used in the trim method to trim both leading and trailing characters.
     */
    public static final int TRIM_BOTH = 2;

    /**
     * The separators used to tokenize words while capitalizing strings.
     */
    private static final String CAPITALIZATION_SEPARATORS = " \t\n\u000B\f\r"; //$NON-NLS-1$

    /**
     * The default replacement token.
     */
    public static final String DEFAULT_TOKEN = "{0}"; //$NON-NLS-1$

    /**
     * The default second replacement token.
     */
    public static final String DEFAULT_SECOND_TOKEN = "{1}"; //$NON-NLS-1$

    /**
     * The default third replacement token.
     */
    private static final String DEFAULT_THIRD_TOKEN = "{2}"; //$NON-NLS-1$

    /**
     * The default fourth replacement token.
     */
    private static final String DEFAULT_FOURTH_TOKEN = "{3}"; //$NON-NLS-1$

    /**
     * The character used to start a replacement token.
     */
    private static final char TOKEN_START = '{';

    /**
     * The character used to end a replacement token.
     */
    private static final char TOKEN_END = '}';

    /**
     * The HTML 'non-breaking-space' entity.
     */
    private static final String HTML_SPACE = "&nbsp;"; //$NON-NLS-1$

    /**
     * Four HTML 'non-breaking-space' entities, used to replace tab characters.
     */
    private static final String HTML_4_SPACES = "&nbsp;&nbsp;&nbsp;&nbsp;"; //$NON-NLS-1$

    /**
     * The HTML 'greater-than' entity.
     */
    private static final String HTML_GREATER_THAN = "&gt;"; //$NON-NLS-1$

    /**
     * The HTML 'lesser-than' entity.
     */
    private static final String HTML_LESSER_THAN = "&lt;"; //$NON-NLS-1$

    /**
     * The HTML ampersand entity.
     */
    private static final String HTML_AMPERSAND = "&amp;"; //$NON-NLS-1$

    /**
     * The HTML line break tag.
     */
    private static final String HTML_LINE_BREAK = "<br>"; //$NON-NLS-1$

    /**
     * The HTML entity and number prefix.
     */
    private static final String HTML_ENTITY_NUMBER = "&#"; //$NON-NLS-1$

    /**
     * The HTML entity suffix.
     */
    private static final char HTML_ENTITY_SEMICOLON = ';';

    /**
     * The character used to sign a surrounder is not used in tokenizing methods.
     */
    private static final char NO_SURROUNDER = '\u0000';

    /**
     * The decimal number zero as a char.
     */
    private static final char ZERO = '0'; //$NON-NLS-1$

    /**
     * Date formatter using the default date format.
     */
    private static final SimpleDateFormat DATE_FORMATTER;

    /**
     * Date formatter using the default time format.
     */
    private static final SimpleDateFormat TIME_FORMATTER;

    /**
     * Date formatter using the default date/time format.
     */
    private static final SimpleDateFormat DATE_TIME_FORMATTER;

    /**
     * Initializes the date formatters.
     */
    static {

        DATE_FORMATTER = new SimpleDateFormat(CommonsContext.DEFAULT_DATE_FORMAT, Locale.getDefault());
        TIME_FORMATTER = new SimpleDateFormat(CommonsContext.DEFAULT_TIME_FORMAT, Locale.getDefault());
        DATE_TIME_FORMATTER = new SimpleDateFormat(CommonsContext.DEFAULT_DATE_TIME_FORMAT, Locale.getDefault());
    }

    /**
     * Default constructor. This class is a toolkit and therefore it cannot be instantiated.
     */
    private StringToolkit() {
        super();
    }

    /**
     * Capitalizes a string. The first character in each word is upper-cased and the rest characters
     * are lower-cased. Each word is delimited by the characters corresponding to the
     * <code>/s</code> regular expression tag and capitalized using the rules of the default locale.
     * Actually, a <code>java.util.StringTokenizer</code> object is used to provide compatibility
     * with JDK v1.3. The tokenizer uses the string <code>" \t\n\u000B\f\r"</code> that is the string
     * expanded from the <code>/s</code> regular expression tag.
     *
     * @param source the string to be capitalized
     *
     * @return the capitalized string
     */
    public static String capitalize(String source) {

        return capitalize(source, Locale.getDefault());
    }

    /**
     * Capitalizes a string. The first character in each word is upper-cased and the rest characters
     * are lower-cased. Each word is delimited by the characters corresponding to the
     * <code>/s</code> regular expression tag and capitalized using the rules of the given locale.
     * Actually, a <code>java.util.StringTokenizer</code> object is used to provide compatibility
     * with JDK v1.3. The tokenizer uses the string <code>" \t\n\u000B\f\r"</code> that is the string
     * expanded from the <code>/s</code> regular expression tag.
     *
     * @param source the string to be capitalized
     * @param locale locale to be used for rules to capitalize words
     *
     * @return the capitalized string
     */
    public static String capitalize(String source, Locale locale) {

        if (source == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(source, CAPITALIZATION_SEPARATORS, true);

        String word = BLANK;
        while (st.hasMoreTokens()) {
            word = st.nextToken();

            if (word.length() == 1) {
                sb.append(word.toUpperCase(locale));
            } else {
                sb.append(word.substring(0, 1).toUpperCase(locale));
                sb.append(word.substring(1, word.length()).toLowerCase(locale));
            }
        }

        return sb.toString();
    }

    /**
     * Combines a list of strings into one single string using single
     * space characters to separate them.
     *
     * @param inputStrings the list of strings to be combined into one
     *
     * @return the strings combined
     */
    public static String combine(List<String> inputStrings) {

        return combine(inputStrings, SPACE);
    }

    /**
     * Combines a list of strings into one single string using the given EOL
     * char(s) to separate them (any arbitrary string is allowed).
     *
     * @param inputStrings the list of strings to be combined into one
     * @param eol the EOL char(s) to be used to separate them
     *
     * @return the strings combined
     */
    public static String combine(List<String> inputStrings, String eol) {

        if (inputStrings == null) {
            return null;
        }

        if (inputStrings.isEmpty()) {
            return BLANK;
        }

        boolean addEol = eol != null && !eol.isEmpty();

        StringBuilder combined = new StringBuilder();
        for (int i = 0, n = inputStrings.size(); i < n; i++) {
            combined.append(inputStrings.get(i));
            if (addEol && i < n - 1) {
                combined.append(eol);
            }
        }

        return combined.toString();
    }

    /**
     * Counts the number of times that the given character appears in the given string.
     *
     * @param source the source string
     * @param count the character to be counted
     *
     * @return the number of times that the given character appears in the given string
     */
    public static int count(String source, char count) {

        if (source == null || source.length() == 0) {
            return 0;
        }

        int counter = 0;
        char[] temp = source.toCharArray();

        for (int i = 0, n = temp.length; i < n; i++) {
            if (temp[i] == count) {
                counter++;
            }
        }

        return counter;
    }

    /**
     * Adds leading space characters to the given string until the result string has a given
     * length. If the original string length is greater than the final length, the result string is
     * truncated.
     *
     * @param source the string to be padded
     * @param length the target string length
     *
     * @return a string of the given length constructed from the given string with leading space
     *         characters
     */
    public static String padLeft(String source, int length) {

        return padLeft(source, length, ' ');
    }

    /**
     * Adds leading <code>pad</code> characters to the given string until the result string has a
     * given length. If the original string length is greater than the final length, the
     * result string is truncated.
     *
     * @param source the string to be padded
     * @param length the target string length
     * @param pad the character to be added
     *
     * @return a string of the given length constructed from the given string with leading
     *         <code>pad</code> characters
     */
    public static String padLeft(String source, int length, char pad) {

        String temp = (source == null) ? BLANK : source;

        int difLength = length - temp.length();

        if (difLength > 0) {
            temp = repeatCharacter(pad, difLength) + temp;
        } else {
            temp = temp.substring(0, length);
        }

        return temp;
    }

    /**
     * Adds trailing space characters to the given string until the result string has a given
     * length. If the original string length is greater than the final length, the result string is
     * truncated.
     *
     * @param source the string to be padded
     * @param length the target string length
     *
     * @return a string of the given length constructed from the given string with trailing space
     *         characters
     */
    public static String padRight(String source, int length) {

        return padRight(source, length, ' ');
    }

    /**
     * Adds trailing <code>pad</code> characters to the given string until the result string has a
     * given length. If the original string length is greater than the final length, the
     * result string is truncated.
     *
     * @param source the string to be padded
     * @param length the target string length
     * @param pad the character to be added
     *
     * @return a string of the given length constructed from the given string with trailing
     *         <code>pad</code> characters
     */
    public static String padRight(String source, int length, char pad) {

        String temp = (source == null) ? BLANK : source;

        int difLength = length - temp.length();

        if (difLength > 0) {
            temp = temp + repeatCharacter(pad, difLength);
        } else {
            temp = temp.substring(0, length);
        }

        return temp;
    }

    /**
     * Returns a list of strings of length less or equal than <code>width</code> created from
     * the <code>source</code> string.
     *
     * @param source the source string
     * @param width the paragraph width
     *
     * @return the source string as a list
     */
    public static List<String> paragraph(String source, int width) {

        List<String> retValue = new ArrayList<String>();

        if (source == null || width <= 0) {
            return null;
        }

        if (source.length() == 0 || source.length() <= width) {
            retValue.add(source);
            return retValue;
        }

        String token = source;

        while (token.length() != 0) {
            if (token.length() <= width) {
                retValue.add(token);
                break;
            }
            int befSpacePos = token.lastIndexOf(' ', width);
            if (befSpacePos == -1) {
                int aftSpacePos = token.indexOf(' ', width);
                if (aftSpacePos == -1) {
                    retValue.add(token);
                    token = BLANK;
                } else {
                    retValue.add(token.substring(0, aftSpacePos));
                    token = token.substring(aftSpacePos + 1);
                }
            } else {
                retValue.add(token.substring(0, befSpacePos));
                token = token.substring(befSpacePos + 1);
            }
        }

        return retValue;
    }

    /**
     * Searches in the string <code>source</code> the first occurrence of the character
     * <code>token</code> and removes it.
     *
     * @param source the string where to search
     * @param token the character to be searched for
     *
     * @return the string after the remove operation
     */
    public static String remove(String source, char token) {

        return remove(source, token, 0);
    }

    /**
     * Searches in the string <code>source</code> the first occurrence of the string
     * <code>token</code> and removes it.
     *
     * @param source the string where to search
     * @param token the string to be searched for
     *
     * @return the string after the remove operation
     */
    public static String remove(String source, String token) {

        return remove(source, token, 0);
    }

    /**
     * Searches in the string <code>source</code> the first occurrence, starting in the position
     * <code>begin</code>, of the character <code>token</code> and removes it.
     *
     * @param source the string where to search
     * @param token the character to be searched for
     * @param begin the starting position
     *
     * @return the string after the remove operation
     */
    public static String remove(String source, char token, int begin) {

        return remove(source, Character.toString(token), begin);
    }

    /**
     * Searches in the string <code>source</code> the first occurrence, starting in the position
     * <code>begin</code>, of the string <code>token</code> and removes it.
     *
     * @param source the string where to search
     * @param token the string to be searched for
     * @param begin the starting position
     *
     * @return the string after the remove operation
     */
    public static String remove(String source, String token, int begin) {

        if (source == null || token == null) {
            return null;
        }

        if (begin > source.length()) {
            return source;
        }

        int start = source.substring(begin).indexOf(token);

        if (start == -1) {
            return source;
        }

        StringBuilder sb = new StringBuilder(source.substring(0, begin + start));
        sb.append(source.substring(begin + start + token.length()));

        return sb.toString();
    }

    /**
     * Searches in the string <code>source</code> all occurrences of the character
     * <code>token</code> and removes them.
     *
     * @param source the string where to search
     * @param token the character to be searched for
     *
     * @return the string after the remove operation
     */
    public static String removeAll(String source, char token) {

        return removeAll(source, token, 0);
    }

    /**
     * Searches in the string <code>source</code> all occurrences of the string <code>token</code>
     * and removes them.
     *
     * @param source the string where to search
     * @param token the string to be searched for
     *
     * @return the string after the remove operation
     */
    public static String removeAll(String source, String token) {

        return removeAll(source, token, 0);
    }

    /**
     * Searches in the string <code>source</code> all occurrences, starting in the position
     * <code>begin</code>, of the character <code>token</code> and removes them.
     *
     * @param source the string where to search
     * @param token the character to be searched for
     * @param begin the starting position
     *
     * @return the string after the remove operation
     */
    public static String removeAll(String source, char token, int begin) {

        return removeAll(source, Character.toString(token), begin);
    }

    /**
     * Searches in the string <code>source</code> all occurrences, starting in the position
     * <code>begin</code>, of the string <code>token</code> and removes them.
     *
     * @param source the string where to search
     * @param token the string to be searched for
     * @param begin the starting position
     *
     * @return the string after the remove operation
     */
    public static String removeAll(String source, String token, int begin) {

        return replaceAll(source, token, null, begin);
    }

    /**
     * Searches in the string <code>source</code> the last occurrence of the character
     * <code>token</code> and removes it.
     *
     * @param source the string where to search
     * @param token the character to be searched for
     *
     * @return the string after the remove operation
     */
    public static String removeLast(String source, char token) {

        if (source == null) {
            return null;
        }

        return removeLast(source, token, source.length());
    }

    /**
     * Searches in the string <code>source</code> the last occurrence of the string
     * <code>token</code> and removes it.
     *
     * @param source the string where to search
     * @param token the string to be searched for
     *
     * @return the string after the remove operation
     */
    public static String removeLast(String source, String token) {

        if (source == null) {
            return null;
        }

        return removeLast(source, token, source.length());
    }

    /**
     * Searches in the string <code>source</code> the last occurrence, before the position
     * <code>end</code>, of the character <code>token</code> and removes it.
     *
     * @param source the string where to search
     * @param token the character to be searched for
     * @param end the ending position
     *
     * @return the string after the remove operation
     */
    public static String removeLast(String source, char token, int end) {

        return removeLast(source, Character.toString(token), end);
    }

    /**
     * Searches in the string <code>source</code> the last occurrence, before the position
     * <code>end</code>, of the string <code>token</code> and removes it.
     *
     * @param source the string where to search
     * @param token the string to be searched for
     * @param end the ending position
     *
     * @return the string after the remove operation
     */
    public static String removeLast(String source, String token, int end) {

        if (source == null || token == null) {
            return null;
        }

        if (end > source.length()) {
            return source;
        }

        int start = source.substring(0, end).lastIndexOf(token);

        if (start == -1) {
            return source;
        }

        StringBuilder sb = new StringBuilder(source.substring(0, start));
        sb.append(source.substring(start + token.length()));

        return sb.toString();
    }

    /**
     * Creates a string with the <code>repeat</code> character <code>length</code> times.
     *
     * @param repeat the character used to create the string
     * @param length the target string length
     *
     * @return a string with the <code>repeat</code> character <code>length</code> times
     */
    public static String repeatCharacter(char repeat, int length) {

        if (length <= 0) {
            return BLANK;
        }

        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(repeat);
        }

        return sb.toString();
    }

    /**
     * Searches in the string <code>source</code> the first occurrence of the default token and
     * replaces it for the string <code>replacement</code>.
     *
     * @param source the source string
     * @param replacement the replacement string
     *
     * @return the string after the replace operation
     *
     * @see StringToolkit#DEFAULT_TOKEN
     */
    public static String replace(String source, String replacement) {

        return replace(source, DEFAULT_TOKEN, replacement, 0);
    }

    /**
     * Searches in the string <code>source</code> the first occurrence of the character
     * <code>token</code> and replaces it for the string <code>replacement</code>.
     *
     * @param source the string where to search
     * @param token the character to be searched for
     * @param replacement the replacement string
     *
     * @return the string after the replace operation
     */
    public static String replace(String source, char token, String replacement) {

        return replace(source, token, replacement, 0);
    }

    /**
     * Searches in the string <code>source</code> the first occurrence of the string
     * <code>token</code> and replaces it for the string <code>replacement</code>.
     *
     * @param source the string where to search
     * @param token the string to be searched for
     * @param replacement the replacement string
     *
     * @return the string after the replace operation
     */
    public static String replace(String source, String token, String replacement) {

        return replace(source, token, replacement, 0);
    }

    /**
     * Searches in the string <code>source</code> the first occurrence, starting in the position
     * <code>begin</code>, of the character <code>token</code> and replaces it for the string
     * <code>replacement</code>.
     *
     * @param source the string where to search
     * @param token the character to be searched for
     * @param replacement the replacement string
     * @param begin the starting position
     *
     * @return the string after the replace operation
     */
    public static String replace(String source, char token, String replacement, int begin) {

        return replace(source, Character.toString(token), replacement, begin);
    }

    /**
     * Searches in the string <code>source</code> the first occurrence, starting in the position
     * <code>begin</code>, of the string <code>token</code> and replaces it for the string
     * <code>replacement</code>.
     *
     * @param source the string where to search
     * @param token the string to be searched for
     * @param replacement the replacement string
     * @param begin the starting position
     *
     * @return the string after the replace operation
     */
    public static String replace(String source, String token, String replacement, int begin) {

        if (source == null || token == null) {
            return null;
        }

        if (begin > source.length()) {
            return source;
        }

        int start = source.substring(begin).indexOf(token);

        if (start == -1) {
            return source;
        }

        StringBuilder sb = new StringBuilder(source.substring(0, begin + start));
        sb.append(replacement);
        sb.append(source.substring(begin + start + token.length()));

        return sb.toString();
    }

    /**
     * Searches in the string <code>source</code> all occurrences of the character
     * <code>token</code> and replaces them for the string <code>replacement</code>.
     *
     * @param source the string where to search
     * @param token the character to be searched for
     * @param replacement the replacement string
     *
     * @return the string after the replace operation
     */
    public static String replaceAll(String source, char token, String replacement) {

        return replaceAll(source, token, replacement, 0);
    }

    /**
     * Searches in the string <code>source</code> all occurrences of the string <code>token</code>
     * and replaces them for the string <code>replacement</code>.
     *
     * @param source the string where to search
     * @param token the string to be searched for
     * @param replacement the replacement string
     *
     * @return the string after the replace operation
     */
    public static String replaceAll(String source, String token, String replacement) {

        return replaceAll(source, token, replacement, 0);
    }

    /**
     * Searches in the string <code>source</code> all occurrences, starting in the position
     * <code>begin</code>, of the character <code>token</code> and replaces them for the string
     * <code>replacement</code>.
     *
     * @param source the string where to search
     * @param token the character to be searched for
     * @param replacement the replacement string
     * @param begin the starting position
     *
     * @return the string after the replace operation
     */
    public static String replaceAll(String source, char token, String replacement, int begin) {

        return replaceAll(source, Character.toString(token), replacement, begin);
    }

    /**
     * Searches in the string <code>source</code> all occurrences, starting in the position
     * <code>begin</code>, of the string <code>token</code> and replaces them for the string
     * <code>replacement</code>.
     *
     * @param source the string where to search
     * @param token the string to be searched for
     * @param replacement the replacement string
     * @param begin the starting position
     *
     * @return the string after the replace operation
     */
    public static String replaceAll(String source, String token, String replacement, int begin) {

        if (source == null || token == null) {
            return null;
        }

        if (begin > source.length()) {
            return source;
        }

        if (source.indexOf(token) == -1) {
            return source;
        }

        StringBuilder sb = new StringBuilder(source.substring(0, begin));

        int start = -1;
        int end = begin;
        while ((start = source.indexOf(token, end)) != -1) {
            sb.append(source.substring(end, start));
            if (replacement != null) {
                sb.append(replacement);
            }
            end = start + token.length();
        }

        if (end < source.length()) {
            sb.append(source.substring(end));
        }

        return sb.toString();
    }

    /**
     * Searches in the string <code>source</code> the last occurrence of the character
     * <code>token</code> and replaces it for the string <code>replacement</code>.
     *
     * @param source the string where to search
     * @param token the character to be searched for
     * @param replacement the replacement string
     *
     * @return the string after the replace operation
     */
    public static String replaceLast(String source, char token, String replacement) {

        if (source == null) {
            return null;
        }

        return replaceLast(source, token, replacement, source.length());
    }

    /**
     * Searches in the string <code>source</code> the last occurrence of the string
     * <code>token</code> and replaces it for the string <code>replacement</code>.
     *
     * @param source the string where to search
     * @param token the string to be searched for
     * @param replacement the replacement string
     *
     * @return the string after the replace operation
     */
    public static String replaceLast(String source, String token, String replacement) {

        if (source == null) {
            return null;
        }

        return replaceLast(source, token, replacement, source.length());
    }

    /**
     * Searches in the string <code>source</code> the last occurrence, before the position
     * <code>end</code>, of the character <code>token</code> and replaces it for the string
     * <code>replacement</code>.
     *
     * @param source the string where to search
     * @param token the character to be searched for
     * @param replacement the replacement string
     * @param end the ending position
     *
     * @return the string after the replace operation
     */
    public static String replaceLast(String source, char token, String replacement, int end) {

        return replaceLast(source, Character.toString(token), replacement, end);
    }

    /**
     * Searches in the string <code>source</code> the last occurrence, before the position
     * <code>end</code>, of the string <code>token</code> and replaces it for the string
     * <code>replacement</code>.
     *
     * @param source the string where to search
     * @param token the string to be searched for
     * @param replacement the replacement string
     * @param end the ending position
     *
     * @return the string after the replace operation
     */
    public static String replaceLast(String source, String token, String replacement, int end) {

        if (source == null || token == null) {
            return null;
        }

        if (end > source.length()) {
            return source;
        }

        int start = source.substring(0, end).lastIndexOf(token);

        if (start == -1) {
            return source;
        }

        StringBuilder sb = new StringBuilder(source.substring(0, start));
        sb.append(replacement);
        sb.append(source.substring(start + token.length()));

        return sb.toString();
    }

    /**
     * Performs a multiple string replacement in the string <code>source</code> replacing tokens
     * <code>{<i>n</i>}</code> with the strings in <code>replacements</code> array.<br>
     *
     * @param source the source string
     * @param replacements array with the replacement strings
     *
     * @return the string after the replace operation
     */
    public static String replaceMultiple(String source, String[] replacements) {

        final int index0 = 0;
        final int index1 = 1;
        final int index2 = 2;
        final int index3 = 3;
        final int index4 = 4;

        if (source == null || replacements == null || replacements.length == 0) {
            return null;
        }

        String[] tokens = new String[replacements.length];

        if (replacements.length == index1) {
            tokens[index0] = DEFAULT_TOKEN;
        } else if (replacements.length == index2) {
            tokens[index0] = DEFAULT_TOKEN;
            tokens[index1] = DEFAULT_SECOND_TOKEN;
        } else if (replacements.length == index3) {
            tokens[index0] = DEFAULT_TOKEN;
            tokens[index1] = DEFAULT_SECOND_TOKEN;
            tokens[index2] = DEFAULT_THIRD_TOKEN;
        } else if (replacements.length == index4) {
            tokens[index0] = DEFAULT_TOKEN;
            tokens[index1] = DEFAULT_SECOND_TOKEN;
            tokens[index2] = DEFAULT_THIRD_TOKEN;
            tokens[index3] = DEFAULT_FOURTH_TOKEN;
        } else {
            for (int i = 0; i < tokens.length; i++) {
                StringBuilder sb = new StringBuilder();
                sb.append(TOKEN_START);
                sb.append(i);
                sb.append(TOKEN_END);
                tokens[i] = sb.toString();
            }
        }

        return replaceMultiple(source, tokens, replacements, 0);
    }

    /**
     * Performs a multiple string replacement in the string <code>source</code> using two string
     * arrays, <code>tokens</code> and <code>replacements</code> with the strings to search for
     * and the strings used as replacements.
     *
     * @param source the source string
     * @param tokens array with the strings to be searched for
     * @param replacements array with the replacement strings
     *
     * @return the string after the replace operation
     */
    public static String replaceMultiple(String source, String[] tokens, String[] replacements) {

        return replaceMultiple(source, tokens, replacements, 0);
    }

    /**
     * Performs a multiple string replacement in the string <code>source</code> starting in the
     * position <code>begin</code> using two string arrays, <code>tokens</code> and
     * <code>replacements</code> with the strings to search for and the strings used as
     * replacements.<br>
     *
     * @param source the source string
     * @param tokens array with the strings to be searched for
     * @param replacements array with the replacement strings
     * @param begin the starting position
     *
     * @return the string after the replace operation or <code>null</code> if any of the input
     *         parameters are <code>null</code> or empty or if the two arrays have different lengths
     */
    public static String replaceMultiple(String source, String[] tokens, String[] replacements,
                                         int begin) {

        if (!checkReplaceMultipleParameters(source, tokens, replacements)) {
            return null;
        }

        if (begin > source.length()) {
            return source;
        }

        StringBuilder sb = new StringBuilder();

        int firstFoundIndex = -1;
        int start = -1;
        int end = begin;
        while ((firstFoundIndex = firstFoundString(source, tokens, end)) != -1) {
            start = source.indexOf(tokens[firstFoundIndex], end);
            sb.append(source.substring(end, start)).append(replacements[firstFoundIndex]);
            end = start + tokens[firstFoundIndex].length();
        }

        if (end < source.length()) {
            sb.append(source.substring(end));
        }

        return sb.toString();
    }

    /**
     * Performs a multiple string replacement in the string <code>source</code> using
     * the string pairs located in the map <code>replacementMap</code>.<br>
     *
     * @param source the source string
     * @param replacementMap map with token and replacement strings
     *
     * @return the string after the replace operation or <code>null</code> if any of the input
     *         parameters are <code>null</code> or empty
     */
    public static String replaceMultiple(String source, Map<String, String> replacementMap) {

        return replaceMultiple(source, replacementMap, 0);
    }

    /**
     * Performs a multiple string replacement in the string <code>source</code> starting in the
     * position <code>begin</code> using the string pairs located in the map
     * <code>replacementMap</code>.<br>
     *
     * @param source the source string
     * @param replacementMap map with token and replacement strings
     * @param begin the starting position
     *
     * @return the string after the replace operation or <code>null</code> if any of the input
     *         parameters are <code>null</code> or empty
     */
    public static String replaceMultiple(String source, Map<String, String> replacementMap,
                                         int begin) {

        if (source == null || replacementMap == null || replacementMap.isEmpty()) {
            return null;
        }

        if (begin > source.length()) {
            return source;
        }

        StringBuilder sb = new StringBuilder();

        Set<String> tokens = replacementMap.keySet();
        String firstFoundString = null;
        int start = -1;
        int end = begin;
        while ((firstFoundString = firstFoundString(source, tokens, end)) != null) {
            start = source.indexOf(firstFoundString, end);
            sb.append(source.substring(end, start)).append(replacementMap.get(firstFoundString));
            end = start + firstFoundString.length();
        }

        if (end < source.length()) {
            sb.append(source.substring(end));
        }

        return sb.toString();
    }

    /**
     * Checks parameters for replaceMultiple() method.
     *
     * @param source the source string
     * @param tokens array with the strings to be searched for
     * @param replacements array with the replacement strings
     *
     * @return whether parameters are valid
     */
    private static boolean checkReplaceMultipleParameters(String source, String[] tokens,
                                                          String[] replacements) {
        return source != null
            && tokens != null
            && replacements != null
            && tokens.length != 0
            && replacements.length != 0
            && tokens.length == replacements.length;
    }

    /**
     * Helper method used by the <code>replaceMultiple</code> methods. It returns the index of the
     * first string found in a given string starting in a given position.
     *
     * @param source the source string
     * @param tokens array with the strings to be searched for
     * @param begin the starting position
     *
     * @return the index of the first string found
     */
    private static int firstFoundString(String source, String[] tokens, int begin) {

        int firstFoundIndex = -1;
        int firstFoundPosition = Integer.MAX_VALUE;

        int j = -1;
        for (int i = 0; i < tokens.length; i++) {
            j = source.indexOf(tokens[i], begin);
            if (j != -1 && j < firstFoundPosition) {
                firstFoundPosition = j;
                firstFoundIndex = i;
            }
        }

        return firstFoundIndex;
    }

    /**
     * Helper method used by the <code>replaceMultiple</code> methods. It returns the index of the
     * first string found in a given string starting in a given position.
     *
     * @param source the source string
     * @param tokens set with the strings to be searched for
     * @param begin the starting position
     *
     * @return the first string found
     */
    private static String firstFoundString(String source, Set<String> tokens, int begin) {

        String firstFoundString = null;
        int firstFoundPosition = Integer.MAX_VALUE;

        int j = -1;
        Iterator<String> it = tokens.iterator();
        while (it.hasNext()) {
            String token = it.next();
            j = source.indexOf(token, begin);
            if (j != -1 && j < firstFoundPosition) {
                firstFoundPosition = j;
                firstFoundString = token;
            }
        }

        return firstFoundString;
    }

    /**
     * Tokenizes the string <code>source</code> considering two consecutive delimiters as having a
     * blank token between them.
     *
     * @param source the string to be tokenized
     * @param delimiters the delimiters used to separate the tokens
     *
     * @return a list that contains the tokens
     *
     * @see StringToolkit#NO_SURROUNDER
     */
    public static List<String> tokenize(String source, String delimiters) {

        return tokenize(source, delimiters, NO_SURROUNDER);
    }

    /**
     * Tokenizes the string <code>source</code> considering two consecutive delimiters as having a
     * blank token between them and using a surrounder character to include delimiters into a token.
     * If <code>surrounder</code> is <code>'\u0000'</code> the surrounding feature is not used.
     *
     * @param source the string to be tokenized
     * @param delimiters the delimiters used to separate the tokens
     * @param surrounder the surrounder character used to include delimiters into a token
     *
     * @return a list that contains the tokens
     *
     * @see StringToolkit#NO_SURROUNDER
     */
    public static List<String> tokenize(String source, String delimiters, char surrounder) {

        List<String> tokens = new ArrayList<String>();

        StringTokenizer st = new StringTokenizer(source, delimiters, true);

        boolean thisIsTheFirstItem = true;
        boolean lastItemWasADelimiter = false;

        while (st.hasMoreElements()) {
            String item = (String) st.nextElement();

            if (delimiters.indexOf(item) == -1) {
                if (surrounder == NO_SURROUNDER) {
                    tokens.add(item);
                } else {
                    if (item.startsWith(String.valueOf(surrounder))) {
                        if (item.endsWith(String.valueOf(surrounder))) {
                            tokens.add(item.substring(1, item.length() - 1));
                        } else {
                            StringBuilder buffer = new StringBuilder();
                            buffer.append(item.substring(1));
                            while (st.hasMoreElements()) {
                                String childItem = (String) st.nextElement();
                                if (childItem.endsWith(String.valueOf(surrounder))) {
                                    buffer.append(childItem.substring(0, childItem.length() - 1));
                                    tokens.add(buffer.toString());
                                    break;
                                }
                                buffer.append(childItem);
                            }
                        }
                    } else {
                        tokens.add(item);
                    }
                }
                lastItemWasADelimiter = false;
            } else {
                if (lastItemWasADelimiter || thisIsTheFirstItem || !st.hasMoreElements()) {
                    tokens.add(BLANK);
                }
                lastItemWasADelimiter = true;
            }

            if (thisIsTheFirstItem) {
                thisIsTheFirstItem = false;
            }
        }

        return tokens;
    }

    /**
     * Removes from the string <code>source</code> the leading and trailing space characters.
     *
     * @param source the string where to trim
     *
     * @return the trimmed string
     */
    public static String trim(String source) {

        return trim(source, ' ', TRIM_BOTH);
    }

    /**
     * Removes from the string <code>source</code> the leading and/or trailing <code>trim</code>
     * characters.
     *
     * @param source the string where to trim
     * @param trim the character to be trimmed
     * @param mode the trim mode
     *
     * @return the trimmed string
     *
     * @see StringToolkit#TRIM_LEFT
     * @see StringToolkit#TRIM_RIGHT
     * @see StringToolkit#TRIM_BOTH
     */
    public static String trim(String source, char trim, int mode) {

        int start = 0;
        int end = source.length();

        if (mode == TRIM_LEFT || mode == TRIM_BOTH) {
            while ((start < source.length()) && (source.charAt(start) == trim)) {
                start++;
            }
        }

        if (mode == TRIM_RIGHT || mode == TRIM_BOTH) {
            while ((end > 0) && (source.charAt(end - 1) == trim)) {
                end--;
            }
        }

        return (start > end) ? BLANK : source.substring(start, end);
    }

    /**
     * Removes from the string <code>source</code> the leading and/or trailing characters in the
     * array <code>trims</code>.
     *
     * @param source the string where to trim
     * @param trims the characters to be trimmed
     * @param mode the trim mode
     *
     * @return the trimmed string
     *
     * @see StringToolkit#TRIM_LEFT
     * @see StringToolkit#TRIM_RIGHT
     * @see StringToolkit#TRIM_BOTH
     */
    public static String trim(String source, char[] trims, int mode) {

        int start = 0;
        int end = source.length();

        if (mode == TRIM_LEFT || mode == TRIM_BOTH) {
            for (int i = start; i < end; i++) {
                boolean found = false;
                for (int j = 0, m = trims.length; j < m; j++) {
                    if (source.charAt(i) == trims[j]) {
                        found = true;
                        start++;
                        break;
                    }
                }
                if (!found) {
                    break;
                }
            }
        }

        if (mode == TRIM_RIGHT || mode == TRIM_BOTH) {
            for (int i = end - 1; i >= start; i--) {
                boolean found = false;
                for (int j = 0, m = trims.length; j < m; j++) {
                    if (source.charAt(i) == trims[j]) {
                        found = true;
                        end--;
                        break;
                    }
                }
                if (!found) {
                    break;
                }
            }
        }

        return source.substring(start, end);
    }

    /**
     * Returns a string with the hexadecimal representation of every byte in the given array.
     *
     * @param source the source bytes
     *
     * @return the string with the hexadecimal representation
     */
    public static String asHexadecimalString(byte[] source) {

        final int bytesPerCharacter = 2;
        final int zeroLeftMask = 0xff;
        final int zeroLeftLimit = 0x10;
        final int hexadecimalBase = 16;

        StringBuilder buffer = new StringBuilder(source.length * bytesPerCharacter);

        for (int i = 0; i < source.length; i++) {
            // source[i] is casted implicitly to int
            if ((source[i] & zeroLeftMask) < zeroLeftLimit) {
                buffer.append(ZERO);
            }

            buffer.append(Long.toString(source[i] & zeroLeftMask, hexadecimalBase));
        }

        return buffer.toString().toUpperCase(Locale.getDefault());
    }

    /**
     * Returns a byte array with the contents of an hexadecimal string (2 characters per byte).
     *
     * @param source the source string
     *
     * @return the resulting byte array
     */
    public static byte[] asHexadecimalArray(String source) {

        final int bytesPerCharacter = 2;
        final int hexadecimalBase = 16;

        if (source == null || source.length() == 0) {
            return new byte[] {};
        }

        byte[] hexArray = new byte[source.length() / bytesPerCharacter];

        for (int i = 0, n = 0; i < hexArray.length; i++, n += bytesPerCharacter) {
            hexArray[i] = (byte) Integer.parseInt(
                source.substring(n, n + bytesPerCharacter), hexadecimalBase);
        }

        return hexArray;
    }

    /**
     * Formats the given string for HTML viewing. It searches in the source string for reserved
     * characters in HTML syntax (i.e., <code>'&gt;'</code>, <code>'&lt;'</code> or
     * <code>'&amp;'</code>) and replaces them with the corresponding HTML entity, changes space
     * characters with non-breaking spaces (<code>&amp;nbsp;</code> entity), tab characters with
     * four non-breaking spaces and new-line characters with the <code>&lt;br&gt;</code> tag. It
     * also replaces the characters outside the ASCII 7-bit character set with the corresponding
     * unicode HTML entity.
     *
     * @param source the source string
     *
     * @return the string with the HTML formatting applied
     */
    public static String formatForHTMLViewing(String source) {

        final int firstExtendedCharacter = 127;

        StringBuilder sb = new StringBuilder();
        char[] c = source.toCharArray();

        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ' || c[i] == '\u000B') {
                sb.append(HTML_SPACE);
            } else if (c[i] == '\t') {
                sb.append(HTML_4_SPACES);
            } else if (c[i] == '>') {
                sb.append(HTML_GREATER_THAN);
            } else if (c[i] == '<') {
                sb.append(HTML_LESSER_THAN);
            } else if (c[i] == '&') {
                sb.append(HTML_AMPERSAND);
            } else if (c[i] == '\r' || c[i] == '\n') {
                if ((c[i] == '\r' && i > 0 && c[i - 1] != '\n')
                    || (c[i] == '\n' && i > 0 && c[i - 1] != '\r')) {
                    sb.append(HTML_LINE_BREAK);
                }
            } else if (c[i] >= firstExtendedCharacter) {
                // c[i] is casted implicitly to int
                sb.append(HTML_ENTITY_NUMBER);
                sb.append(Integer.toString(c[i]));
                sb.append(HTML_ENTITY_SEMICOLON);
            } else {
                sb.append(c[i]);
            }
        }

        return sb.toString();
    }

    /**
     * Formats the given string for use in XML documents. It searches in the source string
     * for reserved characters in XML syntax (i.e., <code>'&gt;'</code>, <code>'&lt;'</code> or
     * <code>'&amp;'</code>) and replaces them with the corresponding XML entity. It
     * also replaces the characters outside the ASCII 7-bit character set with the corresponding
     * unicode XML entity.
     *
     * @param source the source string
     *
     * @return the string with the XML formatting applied
     */
    public static String formatForXML(String source) {

        final int firstExtendedCharacter = 127;

        StringBuilder sb = new StringBuilder();
        char[] c = source.toCharArray();

        for (int i = 0; i < c.length; i++) {
            if (c[i] == '>') {
                sb.append(HTML_GREATER_THAN);
            } else if (c[i] == '<') {
                sb.append(HTML_LESSER_THAN);
            } else if (c[i] == '&') {
                sb.append(HTML_AMPERSAND);
            } else if (c[i] >= firstExtendedCharacter) {
                // c[i] is casted implicitly to int
                sb.append(HTML_ENTITY_NUMBER);
                sb.append(Integer.toString(c[i]));
                sb.append(HTML_ENTITY_SEMICOLON);
            } else {
                sb.append(c[i]);
            }
        }

        return sb.toString();
    }

    /**
     * Formats a list of strings as a single string, comma separated.
     *
     * @param list the list of strings to be formatted
     *
     * @return the dependencies list in a single string, comma separated
     */
    public static String formatListAsString(List<String> list) {

        final String separator = ", "; //$NON-NLS-1$

        return formatListAsStringGeneric(list, separator);
    }

    /**
     * Formats a list of strings as a single string, separated by new lines.
     *
     * @param list the list of strings to be formatted
     *
     * @return the list formatted in a single string, separated by new lines
     */
    public static String formatListAsMultiLineString(List<String> list) {

        final String separator = "\n"; //$NON-NLS-1$

        return formatListAsStringGeneric(list, separator);
    }

    /**
     * Formats a list of strings as a single string, separated by the given separator string.
     *
     * @param list the list of strings to be formatted
     * @param separator the separator string
     *
     * @return the list formatted in a single string, separated by the given separator string
     */
    public static String formatListAsStringGeneric(List<String> list, final String separator) {

        if (list == null || list.isEmpty()) {
            return BLANK;
        }

        StringBuilder buffer = new StringBuilder();

        for (String element : list) {
            if (buffer.length() != 0) {
                buffer.append(separator);
            }
            buffer.append(element);
        }

        return buffer.toString();
    }

    /**
     * Returns <code>true</code> if and only if any of the characters in the given invalid
     * characters string is found in the given source string.
     *
     * @param source the source string
     * @param invalidCharacters the invalid characters string
     *
     * @return whether any of the invalid characters is found in the source string
     */
    public static boolean hasInvalidCharacters(String source, String invalidCharacters) {

        if (source == null
            || source.length() == 0
            || invalidCharacters == null
            || invalidCharacters.length() == 0) {

            return false;
        }

        char[] invalids = invalidCharacters.toCharArray();
        for (char c : invalids) {
            if (source.indexOf(c) != -1) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the given date object formatted using the default date format.
     *
     * @param date the date to format
     *
     * @return the date formatted using the default date format
     */
    public static String formatWithDefaultDateFormat(Date date) {

        synchronized (DATE_FORMATTER) {
            return DATE_FORMATTER.format(date);
        }
    }

    /**
     * Returns the given date object formatted using the default time format.
     *
     * @param date the date to format
     *
     * @return the date formatted using the default time format
     */
    public static String formatWithDefaultTimeFormat(Date date) {

        synchronized (TIME_FORMATTER) {
            return TIME_FORMATTER.format(date);
        }
    }

    /**
     * Returns the given date object formatted using the default date/time format.
     *
     * @param date the date to format
     *
     * @return the date formatted using the default date/time format
     */
    public static String formatWithDefaultDateTimeFormat(Date date) {

        synchronized (DATE_TIME_FORMATTER) {
            return DATE_TIME_FORMATTER.format(date);
        }
    }
}
