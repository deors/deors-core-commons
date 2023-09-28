package deors.core.commons.log;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * Implementation of a Logback appender that writes log messages to a static
 * list (in memory) that can be obtained any time for example for external
 * verification during unit tests.
 *
 * @author deors
 * @version 1.0
 */
public class LogbackMemoryAppender
        extends AppenderBase<ILoggingEvent> {

    /**
     * The list where log events are stored.
     */
    private static final List<ILoggingEvent> EVENT_LIST = new LinkedList<>();

    /**
     * Default constructor.
     */
    public LogbackMemoryAppender() {

        super();
    }

    /**
     * Adds the given log event to the list in memory.
     *
     * @param event the log event to be logged
     */
    @Override
    protected void append(ILoggingEvent event) {
System.out.println("***"+event.toString());
        EVENT_LIST.add(event);
    }

    /**
     * Clears the log events already stored.
     */
    public static void clear() {

        EVENT_LIST.clear();
    }

    /**
     * Returns a copy of the list of log events for external verification.
     *
     * @return a copy of the list of logged events
     */
    public static List<ILoggingEvent> getEventList() {

        return Collections.unmodifiableList(EVENT_LIST);
    }
}
