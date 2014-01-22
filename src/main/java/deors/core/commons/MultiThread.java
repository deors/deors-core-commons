package deors.core.commons;

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
     * @throws java.lang.InstantiationException an instantiation exception
     * @throws java.lang.IllegalAccessException an illegal access exception
     */
    void release() throws InstantiationException, IllegalAccessException;
}
