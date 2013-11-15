package deors.core.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import deors.core.commons.ObjectToolkit;

public class ObjectToolkitTestCase {

    public ObjectToolkitTestCase() {

        super();
    }

    @Test
    public void testCoalesce() {

        assertNull(ObjectToolkit.coalesce(null));
        assertNull(ObjectToolkit.coalesce(new Object[] {}));

        Object[] o = null;

        o = new Object[] {
            null,
            "first non null"
        };
        assertEquals("first non null", ObjectToolkit.coalesce(o));

        o = new Object[] {
            null,
            null
        };
        assertEquals(null, ObjectToolkit.coalesce(o));
    }
}
