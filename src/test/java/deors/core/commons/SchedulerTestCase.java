package deors.core.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.spi.LoggingEvent;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchedulerTestCase {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public SchedulerTestCase() {

        super();
    }

    @Test
    public void testSchedule() {

        Calendar start = Calendar.getInstance();
        start.add(Calendar.MILLISECOND, 100);
        Calendar stop = Calendar.getInstance();
        stop.add(Calendar.MILLISECOND, 900);

        Scheduler sch = new Scheduler();
        sch.scheduleTask(
            "testSchedule",
            MyTask.class,
            "taskDescription",
            start, stop);

        assertTrue(sch.existsTask("testSchedule"));
        assertFalse(sch.existsTask("otherTaskName"));

        sch.startScheduler();

        try {
            Thread.sleep(300);
        } catch (InterruptedException ie) {
        }

        SchedulerTask task = sch.getTask("testSchedule");

        assertNotNull(task);
        assertTrue(task instanceof MyTask);
        assertFalse(task.isDaemonTask());
        assertFalse(task.isDaemonExecuted());
        assertTrue(task.isExecuting());

        try {
            Thread.sleep(1300);
        } catch (InterruptedException ie) {
        }

        assertFalse(task.isExecuting());

        task = sch.getTask("otherTaskName");
        assertNull(task);
    }

    @Test
    public void testDaemon() {

        Scheduler sch = new Scheduler();
        sch.scheduleTask(
            "testDaemon",
            MyTask.class,
            "taskDescription",
            null, null);

        SchedulerTask task = sch.getTask("testDaemon");

        assertNotNull(task);
        assertTrue(task instanceof MyTask);
        assertTrue(task.isDaemonTask());
        assertFalse(task.isExecuting());
        assertFalse(task.isDaemonExecuted());

        sch.startScheduler();

        try {
            Thread.sleep(300);
        } catch (InterruptedException ie) {
        }

        assertTrue(task.isExecuting());
        assertFalse(task.isDaemonExecuted());

        try {
            Thread.sleep(1500);
        } catch (InterruptedException ie) {
        }

        assertFalse(task.isExecuting());
        assertTrue(task.isDaemonExecuted());
        assertNull(task.getTaskNextStartTime());
        assertNull(task.getTaskNextStopTime());
    }

    @Test
    public void testStop() {

        Calendar start = Calendar.getInstance();
        start.add(Calendar.MILLISECOND, 100);
        Calendar stop = Calendar.getInstance();
        stop.add(Calendar.MILLISECOND, 900);

        Scheduler sch = new Scheduler();
        sch.scheduleTask(
            "testStop",
            MyTask.class,
            "taskDescription",
            start, stop);
        sch.startScheduler();

        try {
            Thread.sleep(300);
        } catch (InterruptedException ie) {
        }

        SchedulerTask task = sch.getTask("testStop");

        assertTrue(task.isExecuting());

        sch.stopTask("testStop");

        try {
            Thread.sleep(300);
        } catch (InterruptedException ie) {
        }

        assertFalse(task.isExecuting());
    }

    @Test
    public void testStopAndRemove() {

        Calendar start = Calendar.getInstance();
        start.add(Calendar.MILLISECOND, 100);
        Calendar stop = Calendar.getInstance();
        stop.add(Calendar.MILLISECOND, 900);

        Scheduler sch = new Scheduler();
        sch.scheduleTask(
            "testStopAndRemove",
            MyTask.class,
            "taskDescription",
            start, stop);
        sch.startScheduler();

        try {
            Thread.sleep(300);
        } catch (InterruptedException ie) {
        }

        SchedulerTask task = sch.getTask("testStopAndRemove");

        assertTrue(task.isExecuting());

        sch.stopAndRemoveTask("testStopAndRemove");

        try {
            Thread.sleep(300);
        } catch (InterruptedException ie) {
        }

        assertFalse(task.isExecuting());
        assertFalse(sch.existsTask("testStopAndRemove"));
    }

    @Test
    public void testStopAll() {

        Calendar start = Calendar.getInstance();
        start.add(Calendar.MILLISECOND, 100);
        Calendar stop = Calendar.getInstance();
        stop.add(Calendar.MILLISECOND, 900);

        Scheduler sch = new Scheduler();
        sch.scheduleTask(
            "testStopAll",
            MyTask.class,
            "taskDescription",
            start, stop);
        sch.startScheduler();

        try {
            Thread.sleep(300);
        } catch (InterruptedException ie) {
        }

        SchedulerTask task = sch.getTask("testStopAll");

        assertTrue(task.isExecuting());

        sch.stopAllTasks();

        try {
            Thread.sleep(300);
        } catch (InterruptedException ie) {
        }

        assertFalse(task.isExecuting());
    }

    @Test
    public void testReschedule() {

        Calendar start = Calendar.getInstance();
        start.add(Calendar.MILLISECOND, 100);
        Calendar stop = Calendar.getInstance();
        stop.add(Calendar.MILLISECOND, 900);

        Scheduler sch = new Scheduler();
        sch.scheduleTask(
            "testReschedule",
            MyTask.class,
            "taskDescription",
            start, stop);

        sch.scheduleTask(
            "testReschedule",
            MyTask.class,
            "taskDescription",
            start, stop);

        sch.startScheduler();

        try {
            Thread.sleep(300);
        } catch (InterruptedException ie) {
        }

        sch.scheduleTask(
            "testReschedule",
            MyTask.class,
            "taskDescription",
            start, stop);

        SchedulerTask task = sch.getTask("testReschedule");

        assertNotNull(task);
        assertTrue(task instanceof MyTask);
    }

    @Test
    public void testRescheduleDaemon() {

        Scheduler sch = new Scheduler();
        sch.scheduleTask(
            "testRescheduleDaemon",
            MyTask.class,
            "taskDescription",
            null, null);

        sch.scheduleTask(
            "testRescheduleDaemon",
            MyTask.class,
            "taskDescription",
            null, null);

        sch.startScheduler();

        try {
            Thread.sleep(300);
        } catch (InterruptedException ie) {
        }

        sch.scheduleTask(
            "testRescheduleDaemon",
            MyTask.class,
            "taskDescription",
            null, null);

        SchedulerTask task = sch.getTask("testRescheduleDaemon");

        assertNotNull(task);
        assertTrue(task instanceof MyTask);
    }

    @Test
    public void testScheduleErrorNoClass() {

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("task to be scheduled is new and the class name or the description is not informed");

        Scheduler sch = new Scheduler();
        sch.scheduleTask(
            "taskName",
            (Class<?>) null,
            "taskDescription",
            null, null);
    }

    @Test
    public void testScheduleErrorNoDescription() {

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("task to be scheduled is new and the class name or the description is not informed");

        Scheduler sch = new Scheduler();
        sch.scheduleTask(
            "testScheduleErrorNoDescription",
            MyTask.class,
            "",
            null, null);
    }

    @Test
    public void testScheduleErrorNoDescription2() {

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("task to be scheduled is new and the class name or the description is not informed");

        Scheduler sch = new Scheduler();
        sch.scheduleTask(
            "testScheduleErrorNoDescription2",
            MyTask.class,
            null,
            null, null);
    }

    @Test
    public void testScheduleErrorInvalidClass() {

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("class deors.core.commons.SchedulerTestCase$InvalidTask not valid: java.lang.NoSuchMethodException: deors.core.commons.SchedulerTestCase$InvalidTask.<init>(java.lang.String, java.lang.String, java.util.Calendar, java.util.Calendar)");

        Scheduler sch = new Scheduler();
        sch.scheduleTask(
            "testScheduleErrorInvalidClass",
            InvalidTask.class,
            "taskDescription",
            null, null);
    }

    @Test
    public void testScheduleErrorInvalidClass2() {

        thrown.expect(ClassCastException.class);

        Scheduler sch = new Scheduler();
        sch.scheduleTask(
            "testScheduleErrorInvalidClass2",
            Invalid2Task.class,
            "taskDescription",
            null, null);
    }

    @Test
    public void testScheduleErrorInvalidClass3() {

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("catch this!");

        Scheduler sch = new Scheduler();
        sch.scheduleTask(
            "testScheduleErrorInvalidClass3",
            Invalid3Task.class,
            "taskDescription",
            null, null);
    }

    @Test
    public void testScheduleByName() {

        Calendar start = Calendar.getInstance();
        start.add(Calendar.MILLISECOND, 100);
        Calendar stop = Calendar.getInstance();
        stop.add(Calendar.MILLISECOND, 900);

        Scheduler sch = new Scheduler();
        sch.scheduleTask(
            "testScheduleByName",
            "deors.core.commons.SchedulerTestCase$MyTask",
            "taskDescription",
            start, stop);

        assertTrue(sch.existsTask("testScheduleByName"));
        assertFalse(sch.existsTask("otherTaskName"));

        sch.startScheduler();

        try {
            Thread.sleep(300);
        } catch (InterruptedException ie) {
        }

        SchedulerTask task = sch.getTask("testScheduleByName");

        assertNotNull(task);
        assertTrue(task instanceof MyTask);
        assertFalse(task.isDaemonTask());
        assertFalse(task.isDaemonExecuted());
        assertTrue(task.isExecuting());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
        }

        assertFalse(task.isExecuting());

        task = sch.getTask("otherTaskName");
        assertNull(task);
    }

    @Test
    public void testScheduleByNameErrorNoClass() {

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("task to be scheduled is new and the class name or the description is not informed");

        Scheduler sch = new Scheduler();
        sch.scheduleTask(
            "testScheduleByNameErrorNoClass",
            "",
            "taskDescription",
            null, null);
    }

    @Test
    public void testScheduleByNameErrorNoClass2() {

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("task to be scheduled is new and the class name or the description is not informed");

        Scheduler sch = new Scheduler();
        sch.scheduleTask(
            "testScheduleByNameErrorNoClass2",
            (String) null,
            "taskDescription",
            null, null);
    }

    @Test
    public void testScheduleByNameErrorNotFound() {

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("class NotFoundTask not found: java.lang.ClassNotFoundException: NotFoundTask");

        Scheduler sch = new Scheduler();
        sch.scheduleTask(
            "testScheduleByNameErrorNotFound",
            "NotFoundTask",
            "taskDescription",
            null, null);
    }

    @Test
    public void testScheduleFileName()
        throws IOException {

        Scheduler sch = new Scheduler("target/test-classes/scheduler.ini");

        SchedulerTask task = sch.getTask("task");
        assertNotNull(task);

        SchedulerTask daemon = sch.getTask("daemon");
        assertNotNull(daemon);
    }

    @Test
    public void testScheduleFileError1()
        throws IOException {

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("class name for task task not found");

        new Scheduler("target/test-classes/scheduler-err1.ini");
    }

    @Test
    public void testScheduleFileError2()
        throws IOException {

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("description for task task not found");

        new Scheduler("target/test-classes/scheduler-err2.ini");
    }

    @Test
    public void testScheduleFileError3()
        throws IOException {

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("start time for task task not found");

        new Scheduler("target/test-classes/scheduler-err3.ini");
    }

    @Test
    public void testScheduleFileError4()
        throws IOException {

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("stop time for task task not found");

        new Scheduler("target/test-classes/scheduler-err4.ini");
    }

    @Test
    public void testScheduleFileError5()
        throws IOException {

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("task start time not valid");

        new Scheduler("target/test-classes/scheduler-err5.ini");
    }

    @Test
    public void testScheduleFileError6()
        throws IOException {

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("task stop time not valid");

        new Scheduler("target/test-classes/scheduler-err6.ini");
    }

    @Test
    public void testScheduleFileError7()
        throws IOException {

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("task stop time not valid");

        new Scheduler("target/test-classes/scheduler-err7.ini");
    }

    @Test
    public void testScheduleClassLoader() {

        URL tij = this.getClass().getResource("/classloader/taskinjar/TaskInJar.jar");
        ParentLastURLClassLoader cl = new ParentLastURLClassLoader(
            new URL[] {tij}, this.getClass().getClassLoader());

        Calendar start = Calendar.getInstance();
        start.add(Calendar.MILLISECOND, 100);
        Calendar stop = Calendar.getInstance();
        stop.add(Calendar.MILLISECOND, 900);

        Scheduler sch = new Scheduler();
        sch.setSchedulerClassLoader(cl);
        sch.scheduleTask(
            "testScheduleClassLoader",
            "deors.core.commons.TaskInJar",
            "taskDescription",
            start, stop);

        assertTrue(sch.existsTask("testScheduleClassLoader"));
        assertFalse(sch.existsTask("otherTaskName"));

        sch.startScheduler();

        try {
            Thread.sleep(300);
        } catch (InterruptedException ie) {
        }

        SchedulerTask task = sch.getTask("testScheduleClassLoader");

        assertNotNull(task);
        assertEquals("deors.core.commons.TaskInJar", task.getClass().getCanonicalName());
        assertFalse(task.isDaemonTask());
        assertFalse(task.isDaemonExecuted());
        assertTrue(task.isExecuting());

        try {
            Thread.sleep(1300);
        } catch (InterruptedException ie) {
        }

        assertFalse(task.isExecuting());
    }

    @Test
    public void testMainFile()
        throws IOException {

        Scheduler.main(new String[] {"target/test-classes/scheduler.ini"});
    }

    @Test
    public void testMainNoFile()
        throws IOException {

        Scheduler.main(new String[] {});
    }

    @Test
    public void testMainMissingFile()
        throws IOException {

        Scheduler.main(new String[] {"target/test-classes/missing.ini"});
    }

    @Test
    public void testMainErrorFile()
        throws IOException {

        Scheduler.main(new String[] {"target/test-classes/scheduler-err1.ini"});
    }

    @Test
    public void testScheduleErrorStarting() {

        Log4jMemoryAppender.clear();

        Scheduler sch = new Scheduler();
        sch.scheduleTask(
            "testScheduleErrorStarting",
            "deors.core.commons.SchedulerTestCase$ErrorStartingTask",
            "taskDescription",
            null, null);

        sch.startScheduler();

        try {
            Thread.sleep(300);
        } catch (InterruptedException ie) {
        }

        List<LoggingEvent> events = Log4jMemoryAppender.getEventList();
        for (LoggingEvent e : events) {
            System.out.println(e.getMessage().toString());
        }

        assertMessageEndingWithExists("error starting testScheduleErrorStarting: java.lang.Exception: 5 is not my favorite number");

        Log4jMemoryAppender.clear();
    }

    @Test
    public void testScheduleErrorStopping() {

        Log4jMemoryAppender.clear();

        Scheduler sch = new Scheduler();
        sch.scheduleTask(
            "testScheduleErrorStopping",
            "deors.core.commons.SchedulerTestCase$ErrorStoppingTask",
            "taskDescription",
            null, null);

        sch.startScheduler();

        try {
            Thread.sleep(500);
        } catch (InterruptedException ie) {
        }

        assertMessageEndingWithExists("error stopping testScheduleErrorStopping: java.lang.Exception: 5 is not my favorite number");

        Log4jMemoryAppender.clear();
    }

    @Test
    public void testScheduleErrorRunning() {

        Log4jMemoryAppender.clear();

        Scheduler sch = new Scheduler();
        sch.scheduleTask(
            "testScheduleErrorRunning",
            "deors.core.commons.SchedulerTestCase$ErrorRunningTask",
            "taskDescription",
            null, null);

        sch.startScheduler();

        try {
            Thread.sleep(500);
        } catch (InterruptedException ie) {
        }

        assertMessageEndingWithExists("unexpected error running testScheduleErrorRunning: java.lang.RuntimeException: 5 is not my favorite number");

        Log4jMemoryAppender.clear();
    }

    @Test
    public void testScheduleKill() {

        Scheduler sch = new Scheduler();
        sch.scheduleTask(
            "testScheduleKill",
            "deors.core.commons.SchedulerTestCase$MyTask",
            "taskDescription",
            null, null);

        assertTrue(sch.existsTask("testScheduleKill"));

        sch.startScheduler();

        try {
            Thread.sleep(100);
        } catch (InterruptedException ie) {
        }

        SchedulerTask task = sch.getTask("testScheduleKill");

        assertNotNull(task);
        assertTrue(task instanceof MyTask);
        assertTrue(task.isDaemonTask());
        assertFalse(task.isDaemonExecuted());
        assertTrue(task.isExecuting());

        sch.killTask("testScheduleKill");

        try {
            Thread.sleep(100);
        } catch (InterruptedException ie) {
        }

        task = sch.getTask("testScheduleKill");

        assertNull(task);
    }

    private void assertMessageEndingWithExists(String message) {

        synchronized (Log4jMemoryAppender.class) {

            List<LoggingEvent> events = Log4jMemoryAppender.getEventList();

            boolean found = false;

            for (LoggingEvent event : events) {
                if (event.getMessage().toString().endsWith(message)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }
    }

    public static class InvalidTask {

        public InvalidTask() {

            super();
        }
    }

    public static class Invalid2Task {

        public Invalid2Task(String taskName, String taskDescription, Calendar taskStartTime,
                             Calendar taskStopTime) {

            super();
        }
    }

    public static class Invalid3Task
        extends SchedulerTask {

        public Invalid3Task(String taskName, String taskDescription, Calendar taskStartTime,
                             Calendar taskStopTime) {

            super(taskName, taskDescription, taskStartTime, taskStopTime);
            throw new IllegalArgumentException("catch this!");
        }

        protected void taskLogic() {
        }

        protected void taskPrepareStart() throws Throwable {
        }

        protected void taskPrepareStop() throws Throwable {
        }
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

            LOG.info("MyTask " + this.getTaskName() + " task counts " + count++);

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

            LOG.info("MyTask " + this.getTaskName() + " starting task");
        }

        @Override
        protected void taskPrepareStop() throws Throwable {

            LOG.info("MyTask " + this.getTaskName() + " stopping task");
        }
    }

    public static class ErrorStartingTask
        extends SchedulerTask {

        private static final Logger LOG = LoggerFactory.getLogger(ErrorStartingTask.class);

        private int count;

        public ErrorStartingTask(String taskName, String taskDescription, Calendar taskStartTime,
                                 Calendar taskStopTime) {

            super(taskName, taskDescription, taskStartTime, taskStopTime);
        }

        @Override
        protected void taskLogic() {

            LOG.info("ErrorStartingTask task counts " + count++);

            if (count == 10) {
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

            LOG.info("ErrorStartingTask starting task");

            for (int n = 0; n < 10; n++) {
                LOG.info("ErrorStartingTask start task counts " + count++);

                if (n == 5) {
                    throw new Exception("5 is not my favorite number");
                }
            }
        }

        @Override
        protected void taskPrepareStop() throws Throwable {

            LOG.info("ErrorStartingTask stopping task");
        }
    }

    public static class ErrorStoppingTask
        extends SchedulerTask {

        private static final Logger LOG = LoggerFactory.getLogger(ErrorStoppingTask.class);

        private int count;

        public ErrorStoppingTask(String taskName, String taskDescription, Calendar taskStartTime,
                                 Calendar taskStopTime) {

            super(taskName, taskDescription, taskStartTime, taskStopTime);
        }

        @Override
        protected void taskLogic() {

            LOG.info("ErrorStoppingTask task counts " + count++);

            if (count == 10) {
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

            LOG.info("ErrorStoppingTask starting task");
        }

        @Override
        protected void taskPrepareStop() throws Throwable {

            LOG.info("ErrorStoppingTask stopping task");

            for (int n = 0; n < 10; n++) {
                LOG.info("ErrorStartingTask stop task counts " + count++);

                if (n == 5) {
                    throw new Exception("5 is not my favorite number");
                }
            }
        }
    }

    public static class ErrorRunningTask
        extends SchedulerTask {

        private static final Logger LOG = LoggerFactory.getLogger(ErrorRunningTask.class);

        private int count;

        public ErrorRunningTask(String taskName, String taskDescription, Calendar taskStartTime,
                                Calendar taskStopTime) {

            super(taskName, taskDescription, taskStartTime, taskStopTime);
        }

        @Override
        protected void taskLogic() {

            LOG.info("ErrorRunningTask task counts " + count++);

            if (count == 5) {
                throw new RuntimeException("5 is not my favorite number");
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException ie) {
            }
        }

        @Override
        protected void taskPrepareStart() throws Throwable {

            LOG.info("ErrorRunningTask starting task");
        }

        @Override
        protected void taskPrepareStop() throws Throwable {

            LOG.info("ErrorRunningTask stopping task");
        }
    }
}
