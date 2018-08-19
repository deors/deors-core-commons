package deors.core.commons.threadpool;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiThreadPoolTestCase {

    public MultiThreadPoolTestCase() {

        super();
    }

    @Test
    public void testMultiThread()
        throws IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {

        MultiThreadPool<MyMultiThread> pool = new MultiThreadPool<>(MyMultiThread.class, 5);

        for (int i = 1; i <= 5; i++) {
            MyMultiThread thread = pool.getThread();
            thread.setThreadId(i);
            thread.start();
        }

        assertNull(pool.getThread(false));

        MyMultiThread lastThread = pool.getThread();
        assertNotNull(lastThread);
        assertEquals(pool, lastThread.getOwner());
    }

    @Test
    public void testMultiThreadDefaultSize()
        throws IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {

        MultiThreadPool<MyMultiThread> pool = new MultiThreadPool<>(MyMultiThread.class);

        for (int i = 1; i <= 10; i++) {
            MyMultiThread thread = pool.getThread();
            thread.setThreadId(i);
            thread.start();
        }

        assertNull(pool.getThread(false));

        MyMultiThread lastThread = pool.getThread();
        assertNotNull(lastThread);
        assertEquals(pool, lastThread.getOwner());
    }

    public static class MyMultiThread
        extends AbstractMultiThread {

        private static final Logger LOG = LoggerFactory.getLogger(MyMultiThread.class);

        private int threadId = -1;

        public MyMultiThread() {

            super();
        }

        public void setThreadId(int threadId) {

            this.threadId = threadId;
        }

        public void run() {

            try {
                for (int i = 1; i <= threadId; i++) {
                    LOG.info("thread " + threadId + " counts " + i);

                    try {
                        sleep(100);
                    } catch (InterruptedException ie) {
                    }
                }
            } finally {
                try {
                    release();
                } catch (IllegalAccessException | InstantiationException | InvocationTargetException
                         | NoSuchMethodException | SecurityException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
