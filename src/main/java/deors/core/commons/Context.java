package deors.core.commons;

/**
 * Interface for context classes.<br>
 *
 * @author deors
 * @version 1.0
 */
public interface Context {

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
    String getIConfigurationProperty(String propertyName, String defaultValue);

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
    String getIConfigurationProperty(String propertyName, String defaultValue,
                                     String[] validValues);

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
    int getIConfigurationProperty(String propertyName, int defaultValue);

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
    char getIConfigurationProperty(String propertyName, char defaultValue);

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
    boolean getIConfigurationProperty(String propertyName, boolean defaultValue);

    /**
     * Returns a message from the properties file or the message key if the resource bundle could
     * not be located or the key is missing.
     *
     * @param messageKey the message key in the properties file
     *
     * @return the message in the properties file or the message key if the resource bundle could
     *         not be located or the key is missing
     */
    String getIMessage(String messageKey);

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
    String getIMessage(String messageKey, String replacementString);

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
    String getIMessage(String messageKey, String replacementString1,
                       String replacementString2);

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
    String getIMessage(String messageKey, String[] replacementStrings);

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
    String getIMessage(String messageKey, String[] replacementTokens,
                       String[] replacementStrings);
}
