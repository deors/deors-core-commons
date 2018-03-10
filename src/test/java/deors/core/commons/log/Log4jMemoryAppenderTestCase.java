package deors.core.commons.log;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Test;

import deors.core.commons.log.Log4jMemoryAppender;

public class Log4jMemoryAppenderTestCase {

    @Test
    public void testLogSomething() {

        Log4jMemoryAppender.clear();

        Logger testLog = LogManager.getLogger(Log4jMemoryAppenderTestCase.class);

        testLog.info("info message");
        testLog.debug("debug message");

        assertMessageExists("info message");
        assertMessageExists("debug message");

        Log4jMemoryAppender.clear();
    }

    private void assertMessageExists(String message) {

        synchronized (Log4jMemoryAppender.class) {

            List<LoggingEvent> events = Log4jMemoryAppender.getEventList();

            boolean found = false;

            for (LoggingEvent event : events) {
                if (message.equals(event.getMessage().toString())) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }
    }
}
