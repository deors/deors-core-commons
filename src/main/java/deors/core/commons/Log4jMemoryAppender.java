package deors.core.commons;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Implementation of a Log4j appender that writes log messages to a static list
 * (in memory) that can be obtained any time for example for external verification.
 *
 * @author deors
 * @version 1.0
 */
public class Log4jMemoryAppender
    extends AppenderSkeleton {

    /**
     * The list where log events are stored.
     */
    private static final List<LoggingEvent> EVENT_LIST = new LinkedList<>();

    /**
     * Default constructor.
     */
    public Log4jMemoryAppender() {

        super();
    }

    /**
     * Adds the given log event to the list in memory.
     *
     * @param event the log event to be logged
     */
    @Override
    protected void append(LoggingEvent event) {

        EVENT_LIST.add(event);
    }

    /**
     * Clears the log events already stored.
     */
    public static void clear() {

        EVENT_LIST.clear();
    }

    /**
     * Closes the appender. This implementation does nothing.
     */
    @Override
    public void close() {

        // nothing to do
    }

    /**
     * Returns a copy of the list of log events for external verification.
     *
     * @return a copy of the list of logged events
     */
    public static List<LoggingEvent> getEventList() {

        return new ArrayList<LoggingEvent>(EVENT_LIST);
    }

    /**
     * Whether the appender requires a layout. This implementation always return
     * <code>false</code>.
     *
     * @return <code>false</code>, as this appender does not require a layout
     */
    @Override
    public boolean requiresLayout() {

        return false;
    }
}
