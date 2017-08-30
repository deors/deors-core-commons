package deors.core.commons;

/**
 * Implementation of a thread that can be controlled using a <code>MultiThreadPool</code> object
 * allowing this threads to be executed concurrently.
 *
 * When used within a thread pool, each thread needs to release itself to the pool when its run
 * method ends (typically in the <code>finally</code> block.
 *
 * The class default constructor must be implemented and the owner must be set before running
 * the thread.
 *
 * @author deors
 * @version 1.0
 */
public abstract class AbstractMultiThread
    extends Thread
    implements MultiThread {

    /**
     * The thread owner.
     */
    protected MultiThreadPool<?> owner;

    /**
     * Returns the thread owner.
     *
     * @return the thread owner
     */
    public MultiThreadPool<?> getOwner() {
        return owner;
    }

    /**
     * Changes the thread owner.
     *
     * @param owner the new thread owner
     */
    public void setOwner(MultiThreadPool<?> owner) {
        this.owner = owner;
    }

    /**
     * Releases this thread to the pool.
     *
     * @throws java.lang.InstantiationException an instantiation exception
     * @throws java.lang.IllegalAccessException an illegal access exception
     */
    public void release()
        throws java.lang.InstantiationException, java.lang.IllegalAccessException {
        if (owner != null) {
            owner.release();
        }
    }
}
