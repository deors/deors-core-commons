package deors.core.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.util.Calendar;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchedulerTaskTestCase {

    public SchedulerTaskTestCase() {

        super();
    }

    @Test
    public void testConstructor() {

        Calendar now = Calendar.getInstance();
        Calendar later = Calendar.getInstance();
        later.add(Calendar.HOUR_OF_DAY, 1);

        MyTask task = new MyTask("myTaskName", "myTaskDescription", now, later);

        assertEquals(task.getTaskName(), "myTaskName");
        assertEquals(task.getTaskDescription(), "myTaskDescription");
        assertEquals(task.getTaskStartTime(), now);
        assertEquals(task.getTaskStopTime(), later);
        assertNull(task.getTaskNextStartTime());
        assertNull(task.getTaskNextStopTime());

        assertFalse(task.isDaemonExecuted());
        assertFalse(task.isDaemonTask());
        assertFalse(task.isExecuting());
        assertFalse(task.isStarting());
        assertFalse(task.isStopping());

        assertEquals(task.toString(), "myTaskName (myTaskDescription)");
    }

    @Test
    public void testSetters() {

        Calendar now = Calendar.getInstance();
        Calendar later = Calendar.getInstance();
        later.add(Calendar.HOUR_OF_DAY, 1);

        MyTask task = new MyTask("myTaskName", "myTaskDescription", now, later);

        Calendar evenLater = Calendar.getInstance();
        later.add(Calendar.HOUR_OF_DAY, 2);

        task.setTaskName("myTaskName2");
        task.setTaskDescription("myTaskDescription2");
        task.setTaskStartTime(later);
        task.setTaskStopTime(evenLater);
        task.setTaskNextStartTime(later);
        task.setTaskNextStopTime(evenLater);

        assertEquals(task.getTaskName(), "myTaskName2");
        assertEquals(task.getTaskDescription(), "myTaskDescription2");
        assertEquals(task.getTaskStartTime(), later);
        assertEquals(task.getTaskStopTime(), evenLater);
        assertEquals(task.getTaskNextStartTime(), later);
        assertEquals(task.getTaskNextStopTime(), evenLater);
    }

    public static class MyTask
        extends SchedulerTask {

        private static final Logger LOG = LoggerFactory.getLogger(MyTask.class);

        private int count;

        public MyTask(String taskName, String taskDescription, Calendar taskStartTime,
                      Calendar taskStopTime) {

            super(taskName, taskDescription, taskStartTime, taskStopTime);
        }

        @Override
        protected void taskLogic() {

            LOG.info("task counts " + count++);

            if (count == 100) {
                taskAutoStop();
                return;
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException ie) {
            }
        }

        @Override
        protected void taskPrepareStart() throws Throwable {

            LOG.info("starting task");
        }

        @Override
        protected void taskPrepareStop() throws Throwable {

            LOG.info("stopping task");
        }
    }
}
