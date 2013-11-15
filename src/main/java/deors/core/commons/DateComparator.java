package deors.core.commons;

import static deors.core.commons.CommonsContext.getMessage;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 * Comparator used to sort collections and lists containing objects that represent dates.<br>
 *
 * The comparator supports <code>java.util.Calendar</code>, <code>java.util.Date</code> and
 * <code>java.lang.String</code> objects.<br>
 *
 * @author deors
 * @version 1.0
 */
public final class DateComparator
    implements Comparator<Object>, Serializable {

    /**
     * Serialization ID.
     */
    private static final long serialVersionUID = 339885731403886648L;

    /**
     * The date format.
     */
    private String dateFormat;

    /**
     * The formatter.
     */
    private transient SimpleDateFormat formatter;

    /**
     * Default constructor. The default date/time format will be used.
     */
    public DateComparator() {

        this(CommonsContext.DEFAULT_DATE_TIME_FORMAT);
    }

    /**
     * Constructor that initializes the date format.
     *
     * @param dateFormat the date format
     */
    public DateComparator(String dateFormat) {

        super();
        setDateFormat(dateFormat);
    }

    /**
     * Compares two objects and returns <code>-1</code> if the first is less than the second,
     * <code>0</code> if both objects represent the same date, and <code>1</code> if the first
     * is greater than the second. The comparator supports <code>java.util.Calendar</code>,
     * <code>java.util.Date</code> and <code>java.lang.String</code> objects.<br>
     *
     * A <code>java.lang.IllegalArgumentException</code> is thrown if the objects are of
     * unsupported types or the strings are not valid dates.
     *
     * @param o1 the first object
     * @param o2 the second object
     *
     * @return <code>-1</code> if the first object is less than the second, <code>0</code> if
     *         both objects represent the same date, and <code>1</code> if the first object is
     *         greater than the second
     *
     * @see java.util.Comparator#compare(Object, Object)
     */
    public int compare(Object o1, Object o2) {

        Date d1 = null;
        Date d2 = null;

        if (o1 instanceof Date) {
            d1 = (Date) o1;
        } else if (o1 instanceof Calendar) {
            d1 = ((Calendar) o1).getTime();
        } else if (o1 instanceof String) {
            try {
                d1 = formatter.parse((String) o1);
            } catch (ParseException pe) {
                throw new IllegalArgumentException(
                    getMessage("DTCMP_ERR_INVALID_STRING", (String) o1), pe); //$NON-NLS-1$
            }
        } else {
            throw new IllegalArgumentException(getMessage("DTCMP_ERR_INVALID_CLASS")); //$NON-NLS-1$
        }

        if (o2 instanceof Date) {
            d2 = (Date) o2;
        } else if (o2 instanceof Calendar) {
            d2 = ((Calendar) o2).getTime();
        } else if (o2 instanceof String) {
            try {
                d2 = formatter.parse((String) o2);
            } catch (ParseException pe) {
                throw new IllegalArgumentException(
                    getMessage("DTCMP_ERR_INVALID_STRING", (String) o2), pe); //$NON-NLS-1$
            }
        } else {
            throw new IllegalArgumentException(getMessage("DTCMP_ERR_INVALID_CLASS")); //$NON-NLS-1$
        }

        if (d1.equals(d2)) {
            return 0;
        } else if (d1.before(d2)) {
            return -1;
        } else {
            return 1;
        }
    }

    /**
     * Returns the <code>dateFormat</code> property value.
     *
     * @return the property value
     *
     * @see DateComparator#dateFormat
     * @see DateComparator#setDateFormat(String)
     */
    public String getDateFormat() {

        return dateFormat;
    }

    /**
     * Changes the <code>dateFormat</code> property value and the formatter object.
     *
     * @param dateFormat the property new value
     *
     * @see DateComparator#dateFormat
     * @see DateComparator#getDateFormat()
     */
    public void setDateFormat(String dateFormat) {

        this.dateFormat = dateFormat;
        this.formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
        this.formatter.setLenient(false);
    }
}
