package deors.core.commons.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TemplateExceptionTestCase {

    public TemplateExceptionTestCase() {

        super();
    }

    @Test
    public void testDefaultConstructor() {

        TemplateException te = new TemplateException();

        assertNull(te.getMessage());
        assertNull(te.getCause());
    }

    @Test
    public void testConstructorMessage() {

        String message = "the error message";
        TemplateException te = new TemplateException(message);

        assertEquals(message, te.getMessage());
        assertNull(te.getCause());
    }

    @Test
    public void testConstructorCause() {

        Throwable cause = new Exception("the root cause");
        TemplateException te = new TemplateException(cause);

        assertEquals("java.lang.Exception: " + cause.getMessage(), te.getMessage());
        assertEquals(cause, te.getCause());
    }

    @Test
    public void testConstructorMessageCause() {

        String message = "the error message";
        Throwable cause = new Exception("the root cause");
        TemplateException te = new TemplateException(message, cause);

        assertEquals(message, te.getMessage());
        assertEquals(cause, te.getCause());
    }
}
