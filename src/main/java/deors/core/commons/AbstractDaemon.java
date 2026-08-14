package deors.core.commons;

/**
 * An abstract daemon.
 *
 * <p>Daemons created extending this class have to override the methods <code>daemonStart</code>,
 * <code>daemonStop</code> and <code>daemonLogic</code> to implement the daemon logic.
 *
 * <p>The daemons are not compatible with the Scheduler system, but are compatible with multi-thread
 * pools.
 *
 * @author deors
 * @version 1.0
 */
public abstract class AbstractDaemon
    extends Thread
    implements AutoCloseable {

    /**
     * The daemon thread.
     */
    protected volatile Thread daemonThread;

    /**
     * Default constructor.
     */
    public AbstractDaemon() {
        super();
    }

    /**
     * Starts the daemon. This method is the starting point for the daemon.
     */
    public void startDaemon() {

        daemonThread = new Thread(this);
        daemonThread.start();
    }

    /**
     * Stops the daemon. This method is used to stop executing the daemon logic.
     */
    public void stopDaemon() {

        daemonThread = null;
    }

    /**
     * Closes the daemon by stopping the execution logic.
     */
    @Override
    public void close() {

        stopDaemon();
    }

    /**
     * Daemon execution control. The method <code>daemonStart</code> is executed at the beginning,
     * then the method <code>daemonLogic</code> is executed while the daemon thread is equal to
     * the current execution thread, and finally, before the thread end, if the daemon thread is
     * <code>null</code>, the method <code>daemonStop</code> is executed.
     */
    public void run() {

        Thread tmpThread = Thread.currentThread();

        daemonStart();

        while (tmpThread == daemonThread) {
            daemonLogic();
        }

        if (daemonThread == null) {
            daemonStop();
        }
    }

    /**
     * The daemon logic. It is recommended that a delay is introduced at the end of the method
     * implementation so the thread does not consume the available CPU resources.
     */
    protected abstract void daemonLogic();

    /**
     * Starts the daemon. When an exception raises while starting the daemon, the daemon thread
     * should be assigned a <code>null</code> reference and the method should end.
     */
    protected abstract void daemonStart();

    /**
     * Stops the daemon.
     */
    protected abstract void daemonStop();
}
