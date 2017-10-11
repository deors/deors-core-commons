package deors.core.commons;

import java.util.Stack;

/**
 * Thread pool implementation used to control the parallel execution of multi-threaded tasks.
 *
 * Threads intending to use the pool must subclass the <code>AbstractMultiThread</code>
 * abstract class.
 *
 * @author deors
 * @version 1.0
 *
 * @param <T> the type of the objects that will be pooled by any given instance of this class
 */
public final class MultiThreadPool<T extends AbstractMultiThread> {

    /**
     * Stack with the available threads in the pool.
     */
    private final Stack<T> threadStack = new Stack<T>();

    /**
     * The seed class used to build the pool.
     */
    private Class<T> seedClass;

    /**
     * The default pool size.
     */
    private static final int DEFAULT_POOL_SIZE = 10;

    /**
     * Constructor that builds a thread pool of the given class (must be of <code>MultiThread</code>
     * type) with the default pool size.
     *
     * @param seedClass the class used to build the pool
     *
     * @throws InstantiationException an instantiation exception
     * @throws IllegalAccessException an illegal access exception
     *
     * @see MultiThreadPool#DEFAULT_POOL_SIZE
     */
    public MultiThreadPool(Class<T> seedClass)
        throws InstantiationException, IllegalAccessException {

        this(seedClass, DEFAULT_POOL_SIZE);
    }

    /**
     * Constructor that builds a thread pool of the given class (must be of <code>MultiThread</code>
     * type) with the given number of objects.
     *
     * @param seedClass the class used to build the pool
     * @param poolSize the pool size
     *
     * @throws InstantiationException an instantiation exception
     * @throws IllegalAccessException an illegal access exception
     */
    public MultiThreadPool(Class<T> seedClass, int poolSize)
        throws InstantiationException, IllegalAccessException {

        super();

        this.seedClass = seedClass;

        for (int i = 0; i < poolSize; i++) {
            T thread = seedClass.newInstance();
            thread.setOwner(this);
            threadStack.push(thread);
        }
    }

    /**
     * Gets a thread from the pool waiting if there is none available.
     *
     * @return an available thread
     */
    public T getThread() {
        return getThread(true);
    }

    /**
     * Gets a thread from the pool choosing whether to wait if there is none available. If there is
     * no available thread and we choose to not wait for one, the method returns <code>null</code>.
     *
     * @param wait whether to wait for an available thread
     *
     * @return an available thread or <code>null</code> if there is none and we choose to not wait
     *         for one
     */
    public T getThread(boolean wait) {

        try {
            while (true) {
                synchronized (threadStack) {
                    if (threadStack.size() > 0) {
                        return threadStack.pop();
                    }

                    if (wait) {
                        threadStack.wait();
                    } else {
                        return null;
                    }
                }
            }
        } catch (InterruptedException ie) {
            return null;
        }
    }

    /**
     * Releases a thread to the pool. This method is invoked when a thread ends its execution. The
     * method creates a new instance of the seed class and sets its owner to this thread pool. Tnen
     * the stack is notified that there is a new available thread.
     *
     * @throws InstantiationException an instantiation exception
     * @throws IllegalAccessException an illegal access exception
     */
    void release()
        throws InstantiationException, IllegalAccessException {

        synchronized (threadStack) {
            T thread = seedClass.newInstance();
            thread.setOwner(this);
            threadStack.push(thread);
            threadStack.notifyAll();
        }
    }
}
