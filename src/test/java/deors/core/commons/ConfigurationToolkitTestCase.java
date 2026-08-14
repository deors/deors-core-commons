package deors.core.commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ConfigurationToolkitTestCase {

    @Test
    public void testStringPropertyDefaultValue() {

        assertEquals("defval",
            ConfigurationToolkit.getConfigurationProperty("not.exist", "NOT_EXIST", "defval"));
    }

    @Test
    public void testBooleanPropertyDefaultValue() {

        assertTrue(
            ConfigurationToolkit.getConfigurationProperty("not.exist", "NOT_EXIST", true));
    }

    @Test
    public void testStringPropertySystemValue() {

        System.setProperty("yes.exist", "sysval");
        assertEquals("sysval",
            ConfigurationToolkit.getConfigurationProperty("yes.exist", "NOT_EXIST", "otherval"));
        System.setProperty("yes.exist", "");
    }

    @Test
    public void testBooleanPropertySystemValue() {

        System.setProperty("yes.exist", "true");
        assertTrue(
            ConfigurationToolkit.getConfigurationProperty("yes.exist", "NOT_EXIST", false));
        System.setProperty("yes.exist", "");
    }
}

