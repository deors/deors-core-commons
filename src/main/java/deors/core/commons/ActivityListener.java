package deors.core.commons;

/**
 * Generic interface for processes that want to publish activity log events.
 *
 * @author deors
 * @version 1.0
 */
@FunctionalInterface
public interface ActivityListener {

    /**
     * Callback invoked each time a new activity message needs to be logged.
     *
     * @param message the activity message
     */
    void activityLog(String message);
}
