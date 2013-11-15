package deors.core.commons;

/**
 * Toolkit methods for working with objects.<br>
 *
 * @author deors
 * @version 1.0
 */
public final class ObjectToolkit {

    /**
     * Default constructor. This class is a toolkit and therefore it cannot be instantiated.
     */
    private ObjectToolkit() {
        super();
    }

    /**
     * Returns the first non-null object in the array.
     *
     * @param o an object array
     *
     * @return the first non-null object in the array
     */
    public static Object coalesce(Object[] o) {

        if (o == null || o.length == 0) {
            return null;
        }

        for (int i = 0, l = o.length; i < l; i++) {
            if (o[i] != null) {
                return o[i];
            }
        }

        return null;
    }
}
