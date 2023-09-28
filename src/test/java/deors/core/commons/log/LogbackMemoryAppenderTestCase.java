package deors.core.commons.log;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.spi.ILoggingEvent;

public class LogbackMemoryAppenderTestCase {

    @Test
    public void testLogSomething() {

        synchronized (LogbackMemoryAppender.class) {
            LogbackMemoryAppender.clear();

            Logger testLog = LoggerFactory.getLogger(LogbackMemoryAppenderTestCase.class);

            testLog.info("info message");
            testLog.debug("debug message");

            assertMessageExists("info message");
            assertMessageExists("debug message");

            LogbackMemoryAppender.clear();
        }
    }

    private void assertMessageExists(String message) {

        List<ILoggingEvent> events = LogbackMemoryAppender.getEventList();

        boolean found = false;

        for (ILoggingEvent event : events) {

            if (message.equals(event.getMessage().toString())) {
                found = true;
                break;
            }
        }
        assertTrue("expected message not found", found);
    }
}
