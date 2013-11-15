package deors.core.commons;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The generic library/module context.<br>
 *
 * The context acts as a bridge between the library classes, the configuration properties
 * and the messages stored in resource bundles.<br>
 *
 * @author deors
 * @version 1.0
 */
public abstract class AbstractContext implements Context {

    /**
     * The resource bundle.
     */
    private ResourceBundle bundle;

    /**
     * The base context used as a back-up for properties
     * and configuration settings.
     */
    private final Context baseContext;

    /**
     * The logger.
     */
    protected static final Logger LOG = LoggerFactory.getLogger(AbstractContext.class);

    /**
     * Constructor that initializes the resource bundle
     * and optionally the base context.
     *
     * @param bundleName the resource bundle name
     * @param baseContext the base context
     */
    public AbstractContext(String bundleName, Context baseContext) {

        super();

        try {
            bundle = ResourceBundle.getBundle(bundleName);
        } catch (MissingResourceException mre) {
            LOG.error("error context not initialized"); //$NON-NLS-1$
        }

        this.baseContext = baseContext;
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
    public String getIConfigurationProperty(String propertyName, String defaultValue) {

        String retValue = defaultValue;
        boolean lookInBase = true;

        if (bundle != null) {
            try {
                retValue = bundle.getString(propertyName);
                lookInBase = false;
            } catch (MissingResourceException mre) {
                lookInBase = true;
            }
        }

        if (lookInBase && baseContext != null) {
            retValue = baseContext.getIConfigurationProperty(
                propertyName, defaultValue);
        }

        return retValue;
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
    public String getIConfigurationProperty(String propertyName, String defaultValue,
                                            String[] validValues) {

        String retValue = defaultValue;
        boolean lookInBase = true;

        if (bundle != null) {
            try {
                String configuredValue = bundle.getString(propertyName);

                for (int i = 0, n = validValues.length; i < n; i++) {
                    if (configuredValue.equals(validValues[i])) {
                        retValue = configuredValue;
                        lookInBase = false;
                    }
                }
            } catch (MissingResourceException mre) {
                lookInBase = true;
            }
        }

        if (lookInBase && baseContext != null) {
            retValue = baseContext.getIConfigurationProperty(
                propertyName, defaultValue, validValues);
        }

        return retValue;
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
    public int getIConfigurationProperty(String propertyName, int defaultValue) {

        int retValue = defaultValue;
        boolean lookInBase = true;

        if (bundle != null) {
            try {
                retValue = Integer.parseInt(bundle.getString(propertyName));
                lookInBase = false;
            } catch (MissingResourceException mre) {
                lookInBase = true;
            } catch (NumberFormatException nfe) {
                lookInBase = true;
            }
        }

        if (lookInBase && baseContext != null) {
            retValue = baseContext.getIConfigurationProperty(
                propertyName, defaultValue);
        }

        return retValue;
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
    public char getIConfigurationProperty(String propertyName, char defaultValue) {

        char retValue = defaultValue;
        boolean lookInBase = true;

        if (bundle != null) {
            try {
                retValue = bundle.getString(propertyName).toCharArray()[0];
                lookInBase = false;
            } catch (MissingResourceException mre) {
                lookInBase = true;
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                lookInBase = true;
            }
        }

        if (lookInBase && baseContext != null) {
            retValue = baseContext.getIConfigurationProperty(
                propertyName, defaultValue);
        }

        return retValue;
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
    public boolean getIConfigurationProperty(String propertyName, boolean defaultValue) {

        boolean retValue = defaultValue;
        boolean lookInBase = true;

        if (bundle != null) {
            try {
                retValue = Boolean.valueOf(bundle.getString(propertyName)).booleanValue();
                lookInBase = false;
            } catch (MissingResourceException mre) {
                lookInBase = true;
            }
        }

        if (lookInBase && baseContext != null) {
            retValue = baseContext.getIConfigurationProperty(
                propertyName, defaultValue);
        }

        return retValue;
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
    public String getIMessage(String messageKey) {

        String retValue = messageKey;
        boolean lookInBase = true;

        if (bundle != null) {
            try {
                retValue = bundle.getString(messageKey);
                lookInBase = false;
            } catch (MissingResourceException mre) {
                lookInBase = true;
            }
        }

        if (lookInBase && baseContext != null) {
            retValue = baseContext.getIMessage(messageKey);
        }

        return retValue;
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
    public String getIMessage(String messageKey, String replacementString) {

        String retValue = messageKey;
        boolean lookInBase = true;

        if (bundle != null) {
            try {
                String temp = bundle.getString(messageKey);
                retValue = StringToolkit.replace(temp, replacementString);
                lookInBase = false;
            } catch (MissingResourceException mre) {
                lookInBase = true;
            }
        }

        if (lookInBase && baseContext != null) {
            retValue = baseContext.getIMessage(messageKey, replacementString);
        }

        return retValue;
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
    public String getIMessage(String messageKey, String replacementString1,
                              String replacementString2) {

        String retValue = messageKey;
        boolean lookInBase = true;

        if (bundle != null) {
            try {
                String temp = bundle.getString(messageKey);
                retValue = StringToolkit.replaceMultiple(
                    temp, new String[] {replacementString1, replacementString2});
                lookInBase = false;
            } catch (MissingResourceException mre) {
                lookInBase = true;
            }
        }

        if (lookInBase && baseContext != null) {
            retValue = baseContext.getIMessage(
                messageKey, replacementString1, replacementString2);
        }

        return retValue;
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
    public String getIMessage(String messageKey, String[] replacementStrings) {

        String retValue = messageKey;
        boolean lookInBase = true;

        if (bundle != null) {
            try {
                String temp = bundle.getString(messageKey);
                retValue = StringToolkit.replaceMultiple(temp, replacementStrings);
                lookInBase = false;
            } catch (MissingResourceException mre) {
                lookInBase = true;
            }
        }

        if (lookInBase && baseContext != null) {
            retValue = baseContext.getIMessage(messageKey, replacementStrings);
        }

        return retValue;
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
    public String getIMessage(String messageKey, String[] replacementTokens,
                              String[] replacementStrings) {

        String retValue = messageKey;
        boolean lookInBase = true;

        if (bundle != null) {
            try {
                String temp = bundle.getString(messageKey);
                retValue = StringToolkit.replaceMultiple(temp, replacementTokens, replacementStrings);
                lookInBase = false;
            } catch (MissingResourceException mre) {
                lookInBase = true;
            }
        }

        if (lookInBase && baseContext != null) {
            retValue = baseContext.getIMessage(
                messageKey, replacementTokens, replacementStrings);
        }

        return retValue;
    }
}
