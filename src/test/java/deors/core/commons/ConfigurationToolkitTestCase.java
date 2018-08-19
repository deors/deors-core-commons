package deors.core.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
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

    @Test
    public void testStringPropertyEnvValue() {

        new Expectations(System.class) {{
            System.getenv("YES_EXIST");
            result = "envval";
            System.getProperty("not.exist");
            result = null;
        }};

        assertEquals("envval",
            ConfigurationToolkit.getConfigurationProperty("not.exist", "YES_EXIST", "otherval"));
    }
 
    @Test
    public void testBooleanPropertyEnvValue() {

        new Expectations(System.class) {{
            System.getenv("YES_EXIST");
            result = "True";
            System.getProperty("not.exist");
            result = null;
        }};

        assertTrue(
            ConfigurationToolkit.getConfigurationProperty("not.exist", "YES_EXIST", false));
    }
}
