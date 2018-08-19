package deors.core.commons.threadpool;

import java.lang.reflect.InvocationTargetException;
import java.util.Stack;

/**
 * Thread pool implementation used to control the parallel execution of multi-threaded tasks.
 *
 * <p>Threads intending to use the pool must subclass the <code>MultiThread</code> interface
 * or the <code>AbstractMultiThread</code> abstract class.
 *
 * @author deors
 * @version 1.0
 *
 * @param <T> the type of the objects that will be pooled by any given instance of this class
 */
public final class MultiThreadPool<T extends MultiThread> {

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
     * @throws InstantiationException the thread class is abstract
     * @throws IllegalAccessException the thread class constructor is not accessible
     * @throws InvocationTargetException the thread class constructor throwed an exception
     * @throws NoSuchMethodException the thread class constructor does not exist
     * @throws SecurityException the thread class constructor access is denied
     *
     * @see MultiThreadPool#DEFAULT_POOL_SIZE
     */
    public MultiThreadPool(Class<T> seedClass)
        throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException {

        this(seedClass, DEFAULT_POOL_SIZE);
    }

    /**
     * Constructor that builds a thread pool of the given class (must be of <code>MultiThread</code>
     * type) with the given number of objects.
     *
     * @param seedClass the class used to build the pool
     * @param poolSize the pool size
     *
     * @throws InstantiationException the thread class is abstract
     * @throws IllegalAccessException the thread class constructor is not accessible
     * @throws InvocationTargetException the thread class constructor throwed an exception
     * @throws NoSuchMethodException the thread class constructor does not exist
     * @throws SecurityException the thread class constructor access is denied
     */
    public MultiThreadPool(Class<T> seedClass, int poolSize)
        throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException {

        super();

        this.seedClass = seedClass;

        for (int i = 0; i < poolSize; i++) {
            T thread = seedClass.getConstructor().newInstance();
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
     * method creates a new instance of the seed class and sets its owner to this thread pool. Then
     * the stack is notified that there is a new available thread.
     *
     * @throws InstantiationException the thread class is abstract
     * @throws IllegalAccessException the thread class constructor is not accessible
     * @throws InvocationTargetException the thread class constructor throwed an exception
     * @throws NoSuchMethodException the thread class constructor does not exist
     * @throws SecurityException the thread class constructor access is denied
     */
    void release()
        throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException {

        synchronized (threadStack) {
            T thread = seedClass.getConstructor().newInstance();
            thread.setOwner(this);
            threadStack.push(thread);
            threadStack.notifyAll();
        }
    }
}
