package deors.core.commons.threadpool;

/**
 * Implementation of a thread that can be controlled using a <code>MultiThreadPool</code> object
 * allowing this threads to be executed concurrently.
 *
 * @author deors
 * @version 1.0
 */
public interface MultiThread {

    /**
     * Returns the thread owner.
     *
     * @return the thread owner
     */
    MultiThreadPool<?> getOwner();

    /**
     * Changes the thread owner.
     *
     * @param owner the new thread owner
     */
    void setOwner(MultiThreadPool<?> owner);

    /**
     * Releases this thread to the pool.
     *
     * @throws InstantiationException the thread class is abstract
     * @throws IllegalAccessException the thread class constructor is not accessible
     */
    void release() throws InstantiationException, IllegalAccessException;
}
