package deors.core.commons.threadpool;

import java.lang.reflect.InvocationTargetException;

/**
 * Implementation of a thread that can be controlled using a <code>MultiThreadPool</code> object
 * allowing this threads to be executed concurrently.
 *
 * <p>When used within a thread pool, each thread needs to release itself to the pool when its run
 * method ends (typically in the <code>finally</code> block.
 *
 * <p>The class default constructor must be implemented and the owner must be set before running
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
     * Default constructor.
     */
    public AbstractMultiThread() {
        super();
    }

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
     * @throws InstantiationException the thread class is abstract
     * @throws IllegalAccessException the thread class constructor is not accessible
     * @throws InvocationTargetException the thread class constructor throwed an exception
     * @throws NoSuchMethodException the thread class constructor does not exist
     * @throws SecurityException the thread class constructor access is denied
     */
    public void release()
        throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException {
        if (owner != null) {
            owner.release();
        }
    }
}
