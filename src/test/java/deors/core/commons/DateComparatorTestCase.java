package deors.core.commons;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DateComparatorTestCase {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public DateComparatorTestCase() {

        super();
    }

    @Test
    public void testNullConstructor() {

        thrown.expect(NullPointerException.class);
        new DateComparator(null);
    }

    @Test
    public void testNullCompare() {

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(CommonsContext.getMessage("DTCMP_ERR_INVALID_CLASS"));
        DateComparator dc = new DateComparator();
        dc.compare(null, Calendar.getInstance().getTime());
    }

    @Test
    public void testNullCompare2() {

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(CommonsContext.getMessage("DTCMP_ERR_INVALID_CLASS"));
        DateComparator dc = new DateComparator();
        dc.compare(Calendar.getInstance().getTime(), null);
    }

    @Test
    public void testInvalidClass() {

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(CommonsContext.getMessage("DTCMP_ERR_INVALID_CLASS"));
        DateComparator dc = new DateComparator();
        dc.compare(new Object(), Calendar.getInstance().getTime());
    }

    @Test
    public void testInvalidClass2() {

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(CommonsContext.getMessage("DTCMP_ERR_INVALID_CLASS"));
        DateComparator dc = new DateComparator();
        dc.compare(Calendar.getInstance().getTime(), new Object());
    }

    @Test
    public void testInvalidStringDefaultFormat() {

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(CommonsContext.getMessage("DTCMP_ERR_INVALID_STRING", "2009/2/30 1:00:00"));
        DateComparator dc = new DateComparator();
        dc.compare(Calendar.getInstance().getTime(), "2009/2/30 1:00:00");
    }

    @Test
    public void testInvalidStringOtherFormat() {

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(CommonsContext.getMessage("DTCMP_ERR_INVALID_STRING", "20/9/2330 25:35:00"));
        DateComparator dc = new DateComparator("dd/MM/yyyy HH:mm:ss");
        dc.compare("20/9/2330 25:35:00", Calendar.getInstance().getTime());
    }

    @Test
    public void testFormat() {

        DateComparator dc = new DateComparator();
        assertEquals(CommonsContext.DEFAULT_DATE_TIME_FORMAT, dc.getDateFormat());

        dc = new DateComparator("dd/MM/yyyy HH:mm:ss");
        assertEquals("dd/MM/yyyy HH:mm:ss", dc.getDateFormat());
    }

    @Test
    public void testDateDate() {

        DateComparator dc = new DateComparator();
        Calendar now = Calendar.getInstance();
        Date d1 = now.getTime();
        Date d2 = new Date(now.getTime().getTime());

        // d1 equals d2
        assertEquals(0, dc.compare(d1, d2));
        assertEquals(0, dc.compare(d2, d1));

        now.add(Calendar.SECOND, 1);
        Date d3 = now.getTime();

        // d1 is less than d3
        assertEquals(-1, dc.compare(d1, d3));
        // d3 is greater than d1
        assertEquals(1, dc.compare(d3, d1));
    }

    @Test
    public void testDateCalendar() {

        DateComparator dc = new DateComparator();
        Calendar now = Calendar.getInstance();
        Date d = new Date(now.getTime().getTime());

        // now equals d
        assertEquals(0, dc.compare(now, d));
        assertEquals(0, dc.compare(d, now));

        now.add(Calendar.SECOND, 1);

        // now is greater than d
        assertEquals(1, dc.compare(now, d));
        // d is less than now
        assertEquals(-1, dc.compare(d, now));
    }

    @Test
    public void testCalendarStringDefaultFormat() {

        DateComparator dc = new DateComparator();
        Calendar now = Calendar.getInstance();
        now.set(2009, 9, 15, 13, 30, 0);
        now.set(Calendar.MILLISECOND, 0);
        String s1 = "2009/10/15 13:30:00";
        String s2 = "2009/10/15 13:31:00";

        // now equals s1
        assertEquals(0, dc.compare(now, s1));
        // s2 is greater than now
        assertEquals(1, dc.compare(s2, now));
    }

    @Test
    public void testCalendarStringOtherFormat() {

        DateComparator dc = new DateComparator("dd/MM/yyyy");
        Calendar now = Calendar.getInstance();
        now.set(2009, 0, 15, 0, 0, 0);
        now.set(Calendar.MILLISECOND, 0);
        String s1 = "15/1/2009";
        String s2 = "15/2/2009";

        // now equals s1
        assertEquals(0, dc.compare(now, s1));
        // s2 is greater than now
        assertEquals(1, dc.compare(s2, now));
    }
}
