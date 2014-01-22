package deors.core.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractDaemonTestCase {

    public AbstractDaemonTestCase() {

        super();
    }

    @Test
    public void testDaemon() {

        MyDaemon daemon = new MyDaemon();
        daemon.startDaemon();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ie) {
        }

        assertEquals(100, daemon.getCount());
    }

    @Test
    public void testDaemonStop() {

        MyDaemon daemon = new MyDaemon();
        daemon.startDaemon();

        try {
            Thread.sleep(100);
        } catch (InterruptedException ie) {
        }

        daemon.stopDaemon();

        assertTrue(daemon.getCount() != 100);
    }

    public static class MyDaemon
        extends AbstractDaemon {

        private static final Logger LOG = LoggerFactory.getLogger(MyDaemon.class);

        private int count;

        public MyDaemon() {

            super();
        }

        public int getCount() {

            return count;
        }

        @Override
        protected void daemonStart() {

            LOG.info("starting daemon");
        }

        @Override
        protected void daemonStop() {

            LOG.info("stopping daemon");
        }

        @Override
        protected void daemonLogic() {

            LOG.info("daemon logic executed " + (count + 1) + " times");

            if (++count == 100) {
                daemonThread = null;
                return;
            }

            try {
                this.sleep(10);
            } catch (InterruptedException ie) {
            }
        }
    }
}
