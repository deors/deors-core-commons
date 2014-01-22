package deors.core.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;
import static org.powermock.api.easymock.PowerMock.mockStatic;

//import java.lang.reflect.Field;
//import java.util.Collections;
//import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ConfigurationToolkit.class)
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

        mockStatic(System.class);
        expect(System.getenv("YES_EXIST")).andReturn("envval");
        expect(System.getProperty("not.exist")).andReturn(null);
        replayAll();
        assertEquals("envval",
            ConfigurationToolkit.getConfigurationProperty("not.exist", "YES_EXIST", "otherval"));
        verifyAll();
    }

    @Test
    public void testBooleanPropertyEnvValue() {

        mockStatic(System.class);
        expect(System.getenv("YES_EXIST")).andReturn("TrUe");
        expect(System.getProperty("not.exist")).andReturn(null);
        replayAll();
        assertTrue(
            ConfigurationToolkit.getConfigurationProperty("not.exist", "YES_EXIST", false));
        verifyAll();
    }

//    private static void setEnv(Map<String, String> newEnv) {
//
//        try {
//            Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
//            Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
//            theEnvironmentField.setAccessible(true);
//            Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
//            env.putAll(newEnv);
//            Field theCaseInsensitiveEnvironmentField =
//                processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
//            theCaseInsensitiveEnvironmentField.setAccessible(true);
//            Map<String, String> cienv =
//                (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
//            cienv.putAll(newEnv);
//        } catch (NoSuchFieldException e) {
//            try {
//                Class[] classes = Collections.class.getDeclaredClasses();
//                Map<String, String> env = System.getenv();
//                for (Class cl : classes) {
//                    if ("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
//                        Field field = cl.getDeclaredField("m");
//                        field.setAccessible(true);
//                        Object obj = field.get(env);
//                        Map<String, String> map = (Map<String, String>) obj;
//                        map.clear();
//                        map.putAll(newEnv);
//                    }
//                }
//            } catch (Exception e2) {
//                e2.printStackTrace();
//            }
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
//    }
//
//    private static void addEnv(String envKey, String envValue) {
//
//        Map<String, String> env = System.getenv();
//        env.put(envKey, envValue);
//        setEnv(env);
//    }
//
//    private static void removeEnv(String envKey) {
//
//        Map<String, String> env = System.getenv();
//        env.remove(envKey);
//        setEnv(env);
//    }
}
