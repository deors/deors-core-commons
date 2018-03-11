package deors.core.commons;

/**
 * The Commons library context.
 *
 * <p>The context acts as a bridge between the library classes, the configuration
 * properties and the messages stored in the resource bundle created from
 * <code>deors.core.commons.configuration.properties</code> properties file).
 *
 * <p>The system property <code>deors.core.commons.configuration</code> can be used to
 * inject an alternate properties file.
 *
 * <p>The class is a singleton.
 *
 * @author deors
 * @version 1.0
 */
public final class CommonsContext extends AbstractContext {

    /**
     * The only class instance.
     */
    private static CommonsContext contextInstance = new CommonsContext();

    /**
     * The resource bundle name.
     */
    private static final String BUNDLE_NAME = "deors.core.commons.configuration"; //$NON-NLS-1$

    /**
     * A blank string.
     */
    public static final String BLANK = ""; //$NON-NLS-1$

    /**
     * A string containing a single space character.
     */
    public static final String SPACE = " "; //$NON-NLS-1$

    /**
     * Default buffer size. Configurable in the properties file using the key
     * <code>commons.defaultBufferSize</code>. Default value is <code>4096</code>.
     *
     * @see CommonsContext#getConfigurationProperty(String, int)
     */
    public static final int DEFAULT_BUFFER_SIZE =
        getConfigurationProperty("commons.defaultBufferSize", 4096); //$NON-NLS-1$

    /**
     * Default date format. Configurable in the properties file using the key
     * <code>commons.defaultDateFormat</code>. Default value is <code>yyyy/MM/dd</code>.
     *
     * @see CommonsContext#getConfigurationProperty(String, String)
     */
    public static final String DEFAULT_DATE_FORMAT =
        getConfigurationProperty("commons.defaultDateFormat", "yyyy/MM/dd"); //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * Default time format. Configurable in the properties file using the key
     * <code>commons.defaultTimeFormat</code>. Default value is <code>HH:mm:ss</code>.
     *
     * @see CommonsContext#getConfigurationProperty(String, String)
     */
    public static final String DEFAULT_TIME_FORMAT =
        getConfigurationProperty("commons.defaultTimeFormat", "HH:mm:ss"); //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * Default date &amp; time format. Configurable in the properties file using the key
     * <code>commons.defaultDateTimeFormat</code>. Default value is <code>yyyy/MM/dd HH:mm:ss</code>.
     *
     * @see CommonsContext#getConfigurationProperty(String, String)
     */
    public static final String DEFAULT_DATE_TIME_FORMAT =
        getConfigurationProperty("commons.defaultDateTimeFormat", "yyyy/MM/dd HH:mm:ss"); //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * Prefix used in temporary files. Configurable in the properties file using the key
     * <code>commons.tempFilePrefix</code>. Default value is <code>commons.</code>.
     *
     * @see CommonsContext#getConfigurationProperty(String, String)
     */
    public static final String TEMP_FILE_PREFIX =
        getConfigurationProperty("commons.tempFilePrefix", "commons."); //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * Suffix used in temporary files. Configurable in the properties file using the key
     * <code>commons.tempFileSuffix</code>. Default value is <code>.temp</code>.
     *
     * @see CommonsContext#getConfigurationProperty(String, String)
     */
    public static final String TEMP_FILE_SUFFIX =
        getConfigurationProperty("commons.tempFileSuffix", ".temp"); //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * Default constructor.
     */
    private CommonsContext() {

        super(System.getProperty(BUNDLE_NAME, BUNDLE_NAME), null);
    }

    /**
     * Static method that returns the only class instance.
     *
     * @return the only class instance
     */
    public static CommonsContext getContext() {
        return contextInstance;
    }

    /**
     * Returns a configuration property named by the given key or the default value if the resource
     * bundle could not be located or the key is missing.
     *
     * @param propertyName the property name
     * @param defaultValue the default value
     *
     * @return the property value or the default value if the resource bundle could not be located
     *         or the key is missing
     */
    public static String getConfigurationProperty(String propertyName, String defaultValue) {

        return contextInstance.getIConfigurationProperty(propertyName, defaultValue);
    }

    /**
     * Returns a configuration property named by the given key or the default value if the resource
     * bundle could not be located or the key is missing. The given array defines the property valid
     * values. If the configured value is not in the array the default value is returned.
     *
     * @param propertyName the property name
     * @param defaultValue the default value
     * @param validValues array with the valid values
     *
     * @return the property value or the default value if the resource bundle could not be located
     *         or the key is missing or the value is not a valid one
     */
    public static String getConfigurationProperty(String propertyName, String defaultValue,
                                                  String[] validValues) {

        return contextInstance.getIConfigurationProperty(propertyName, defaultValue, validValues);
    }

    /**
     * Returns a configuration property named by the given key converting the value to an integer
     * value or the default value if the resource bundle could not be located or the key is missing
     * or the value could not be converted to an integer value.
     *
     * @param propertyName the property name
     * @param defaultValue the default value
     *
     * @return the property value or the default value if the resource bundle could not be located
     *         or the key is missing or the value could not be converted to an integer value
     */
    public static int getConfigurationProperty(String propertyName, int defaultValue) {

        return contextInstance.getIConfigurationProperty(propertyName, defaultValue);
    }

    /**
     * Returns a configuration property named by the given key converting the first character to a
     * char value or the default value if the resource bundle could not be located or the key is
     * missing or the value could not be converted to a char value.
     *
     * @param propertyName the property name
     * @param defaultValue the default value
     *
     * @return the property value or the default value if the resource bundle could not be located
     *         or the key is missing or the value could not be converted to a char value
     */
    public static char getConfigurationProperty(String propertyName, char defaultValue) {

        return contextInstance.getIConfigurationProperty(propertyName, defaultValue);
    }

    /**
     * Returns a configuration property named by the given key converting the value to a boolean
     * value or the default value if the resource bundle could not be located or the key is missing.
     *
     * @param propertyName the property name
     * @param defaultValue the default value
     *
     * @return the property value or the default value if the resource bundle could not be located
     *         or the key is missing
     */
    public static boolean getConfigurationProperty(String propertyName, boolean defaultValue) {

        return contextInstance.getIConfigurationProperty(propertyName, defaultValue);
    }

    /**
     * Returns a message from the properties file or the message key if the resource bundle could
     * not be located or the key is missing.
     *
     * @param messageKey the message key in the properties file
     *
     * @return the message in the properties file or the message key if the resource bundle could
     *         not be located or the key is missing
     */
    public static String getMessage(String messageKey) {

        return contextInstance.getIMessage(messageKey);
    }

    /**
     * Returns a message from the properties file replacing the default token with the given
     * replacement string or the message key if the resource bundle could not be located or the key
     * is missing.
     *
     * @param messageKey the message key in the properties file
     * @param replacementString string to replace the default token
     *
     * @return the message in the properties file with the replacement applied or the message key if
     *         the resource bundle could not be located or the key is missing
     *
     * @see StringToolkit#replace(String, String)
     */
    public static String getMessage(String messageKey, String replacementString) {

        return contextInstance.getIMessage(messageKey, replacementString);
    }

    /**
     * Returns a message from the properties file replacing the first and second token with the given
     * replacement strings or the message key if the resource bundle could not be located or the key
     * is missing.
     *
     * @param messageKey the message key in the properties file
     * @param replacementString1 the first replacement string
     * @param replacementString2 the second replacement string
     *
     * @return the message in the properties file with the replacement applied or the message key if
     *         the resource bundle could not be located or the key is missing
     *
     * @see StringToolkit#replaceMultiple(String, String[])
     */
    public static String getMessage(String messageKey, String replacementString1,
                                    String replacementString2) {

        return contextInstance.getIMessage(messageKey, replacementString1, replacementString2);
    }

    /**
     * Returns a message from the properties file replacing tokens in the form
     * <code>{<i>n</i>}</code> with the given replacements strings or the message key if the
     * resource bundle could not be located or the key is missing.
     *
     * @param messageKey the message key in the properties file
     * @param replacementStrings strings to replace the tokens
     *
     * @return the message in the properties file with the replacements applied or the message key
     *         if the resource bundle could not be located or the key is missing
     *
     * @see StringToolkit#replaceMultiple(String, String[])
     */
    public static String getMessage(String messageKey, String[] replacementStrings) {

        return contextInstance.getIMessage(messageKey, replacementStrings);
    }

    /**
     * Returns a message from the properties file replacing the given tokens with the given
     * replacements strings or the message key if the resource bundle could not be located or the
     * key is missing.
     *
     * @param messageKey the message key in the properties file
     * @param replacementTokens tokens that will be replaced from the message
     * @param replacementStrings strings to replace the tokens
     *
     * @return the message in the properties file with the replacements applied or the message key
     *         if the resource bundle could not be located or the key is missing
     *
     * @see StringToolkit#replaceMultiple(String, String[], String[])
     */
    public static String getMessage(String messageKey, String[] replacementTokens,
                                    String[] replacementStrings) {

        return contextInstance.getIMessage(messageKey, replacementTokens, replacementStrings);
    }
}
