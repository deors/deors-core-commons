package deors.core.commons;

/**
 * Toolkit methods for managing program configuration settings and preferences.
 *
 * @author deors
 * @version 1.0
 */
public final class ConfigurationToolkit {

    /**
     * Default constructor. This class is a toolkit and therefore it cannot be instantiated.
     */
    private ConfigurationToolkit() {

        super();
    }

    /**
     * Gets a string value for a configuration property looking at the given system property,
     * if not present looking at the given environment variable, and if not, returning
     * the given default value.
     *
     * @param sysKey the system property key
     * @param envKey the environment variable key
     * @param defValue the default value
     *
     * @return the configuration property value
     */
    @SuppressWarnings("PMD.ConfusingTernary")
    public static String getConfigurationProperty(String sysKey, String envKey, String defValue) {

        String retValue = defValue;
        String envValue = System.getenv(envKey);
        String sysValue = System.getProperty(sysKey);
        // system property prevails over environment variable
        if (sysValue != null) {
            retValue = sysValue;
        } else if (envValue != null) {
            retValue = envValue;
        }
        return retValue;
    }

    /**
     * Gets a boolean value for a configuration property looking at the given system property,
     * if not present looking at the given environment variable, and if not, returning
     * the given default value.
     *
     * @param sysKey the system property key
     * @param envKey the environment variable key
     * @param defValue the default value
     *
     * @return the configuration property value
     */
    @SuppressWarnings("PMD.ConfusingTernary")
    public static boolean getConfigurationProperty(String sysKey, String envKey, boolean defValue) {

        boolean retValue = defValue;
        String envValue = System.getenv(envKey);
        String sysValue = System.getProperty(sysKey);
        // system property prevails over environment variable
        if (sysValue != null) {
            retValue = Boolean.parseBoolean(sysValue);
        } else if (envValue != null) {
            retValue = Boolean.parseBoolean(envValue);
        }
        return retValue;
    }
}
