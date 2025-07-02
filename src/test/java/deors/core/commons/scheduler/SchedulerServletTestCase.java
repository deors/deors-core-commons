package deors.core.commons.scheduler;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import deors.core.commons.io.IOToolkit;

@ExtendWith(MockitoExtension.class)
public class SchedulerServletTestCase {

    public SchedulerServletTestCase() {
        super();
    }

    @Test
    public void testServletCommandNull(@Mock HttpServletRequest request, @Mock HttpServletResponse response)
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");

        when(request.getParameter("command")).thenReturn(null);
        when(response.getWriter()).thenReturn(new PrintWriter(temp));

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.doPost(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue(s.contains("<title>Scheduler Command Center</title>"), "expected title not found");
            assertTrue(s.contains("<b>Scheduler not running</b><br/>"), "expected status not found");
            assertTrue(s.contains("<form id=\"commandForm\" name=\"commandForm\" method=\"post\" action=\"[ACTION]\">"), "expected form not found");
            assertTrue(s.contains("<input type=\"button\" name=\"start\" value=\"start\""), "expected button not found");
            assertTrue(s.contains("<b>Configuration parameters</b>"), "expected configuration header not found");
            assertFalse(s.contains("<b>Error(s) with configuration parameters</b><br/>"), "unexpected error message found");
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandEmpty(@Mocked HttpServletRequest request, @Mocked HttpServletResponse response)
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");

        new Expectations() {{
            request.getParameter("command");            result = "";
            response.getWriter();                       result = new PrintWriter(temp);
        }};

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue("expected title not found", s.contains("<title>Scheduler Command Center</title>"));
            assertTrue("expected status not found", s.contains("<b>Scheduler not running</b><br/>"));
            assertTrue("expected form not found", s.contains("<form id=\"commandForm\" name=\"commandForm\" method=\"post\" action=\"[ACTION]\">"));
            assertTrue("expected button not found", s.contains("<input type=\"button\" name=\"start\" value=\"start\""));
            assertTrue("expected configuration header not found", s.contains("<b>Configuration parameters</b>"));
            assertFalse("unexpected error message found", s.contains("<b>Error(s) with configuration parameters</b><br/>"));
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandHelp(@Mocked HttpServletRequest request, @Mocked HttpServletResponse response)
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");

        new Expectations() {{
            request.getParameter("command");            result = "help";
            response.getWriter();                       result = new PrintWriter(temp);
        }};

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue("expected title not found", s.contains("<title>Scheduler Command Center</title>"));
            assertTrue("expected status not found", s.contains("<b>Scheduler not running</b><br/>"));
            assertTrue("expected form not found", s.contains("<form id=\"commandForm\" name=\"commandForm\" method=\"post\" action=\"[ACTION]\">"));
            assertTrue("expected button not found", s.contains("<input type=\"button\" name=\"start\" value=\"start\""));
            assertTrue("expected configuration header not found", s.contains("<b>Configuration parameters</b>"));
            assertFalse("unexpected error message found", s.contains("<b>Error(s) with configuration parameters</b><br/>"));
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandStartEmpty(@Mocked HttpServletRequest request, @Mocked HttpServletResponse response)
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");

        new Expectations() {{
            request.getParameter("command");            result = "start";
            request.getParameter("iniFileName");        result = "";
            response.getWriter();                       result = new PrintWriter(temp);
        }};
        
        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue("expected title not found", s.contains("<title>Scheduler Command Center</title>"));
            assertTrue("expected status not found", s.contains("<b>Scheduler started</b><br/>"));

            assertFalse("unexpected task 'task' found", ss.existsTask("task"));
            assertFalse("unexpected task 'daemon' found", ss.existsTask("daemon"));
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandStartWithFile(@Mocked HttpServletRequest request, @Mocked HttpServletResponse response)
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");

        new Expectations() {{
            request.getParameter("command");            result = "start";
            request.getParameter("iniFileName");        result = "target/test-classes/scheduler.ini";
            response.getWriter();                       result = new PrintWriter(temp);
        }};

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue("expected title not found", s.contains("<title>Scheduler Command Center</title>"));
            assertTrue("expected status not found", s.contains("<b>Scheduler started</b><br/>"));

            assertTrue("expected task 'task' not found", ss.existsTask("task"));
            assertTrue("expected task 'daemon' not found", ss.existsTask("daemon"));
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandStartMissingFile(@Mocked HttpServletRequest request, @Mocked HttpServletResponse response)
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");

        new Expectations() {{
            request.getParameter("command");            result = "start";
            request.getParameter("iniFileName");        result = "target/test-classes/missing.ini";
            response.getWriter();                       result = new PrintWriter(temp);
        }};

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue("expected error message not found",
                s.contains("[scheduler] the configuration file is either missing or inaccessible:"));
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandStartAgain(
            @Mocked HttpServletRequest request1, @Mocked HttpServletResponse response1,
            @Mocked HttpServletRequest request2, @Mocked HttpServletResponse response2,
            @Mocked HttpServletRequest request3, @Mocked HttpServletResponse response3)
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp1 = File.createTempFile("deors.core.commons.", ".test");
        File temp2 = File.createTempFile("deors.core.commons.", ".test");
        File temp3 = File.createTempFile("deors.core.commons.", ".test");

        new Expectations() {{
            request1.getParameter("command");            result = "start";
            request1.getParameter("iniFileName");        result = "target/test-classes/scheduler.ini";
            response1.getWriter();                       result = new PrintWriter(temp1);

            request2.getParameter("command");            result = "stop";
            response2.getWriter();                       result = new PrintWriter(temp2);

            request3.getParameter("command");            result = "start";
            request3.getParameter("iniFileName");        result = "";
            response3.getWriter();                       result = new PrintWriter(temp3);
        }};

        SchedulerServlet ss = new SchedulerServlet();
        try {
            // stars scheduler with ini file including tasks
            ss.doGet(request1, response1);

            // stops scheduler
            ss.doGet(request2, response2);

            // starts again the scheduler without ini file - the tasks are maintained from previous execution
            ss.doGet(request3, response3);

            byte[] output = IOToolkit.readFile(temp3);
            String s = new String(output);

            assertTrue("expected title not found", s.contains("<title>Scheduler Command Center</title>"));
            assertTrue("expected status not found", s.contains("<b>Scheduler started</b><br/>"));

            assertTrue("expected task 'task' not found", ss.existsTask("task"));
            assertTrue("expected task 'daemon' not found", ss.existsTask("daemon"));
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp1.delete();
            temp2.delete();
            temp3.delete();
        }
    }

    @Test
    public void testServletCommandStartAlreadyInit(@Mocked HttpServletRequest request, @Mocked HttpServletResponse response, @Mocked ServletConfig config)
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");

        new Expectations() {{
            request.getParameter("command");            result = "start";
            response.getWriter();                       result = new PrintWriter(temp);
            config.getInitParameter("iniFileName");     result = "";
        }};

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue("expected title not found", s.contains("<title>Scheduler Command Center</title>"));
            assertTrue("expected status not found", s.contains("<b>Scheduler already started</b><br/>"));
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandStopNotInit(@Mocked HttpServletRequest request, @Mocked HttpServletResponse response)
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");

        new Expectations() {{
            request.getParameter("command");            result = "stop";
            response.getWriter();                       result = new PrintWriter(temp);
        }};

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue("expected title not found", s.contains("<title>Scheduler Command Center</title>"));
            assertTrue("expected status not found", s.contains("<b>Scheduler not running</b><br/>"));

            assertFalse("unexpected task 'task' found", ss.existsTask("task"));
            assertFalse("unexpected task 'daemon' found", ss.existsTask("daemon"));
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandStopIfInit(@Mocked HttpServletRequest request, @Mocked HttpServletResponse response, @Mocked ServletConfig config)
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");

        new Expectations() {{
            request.getParameter("command");            result = "stop";
            response.getWriter();                       result = new PrintWriter(temp);
            config.getInitParameter("iniFileName");     result = "";
        }};

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue("expected title not found", s.contains("<title>Scheduler Command Center</title>"));
            assertTrue("expected status not found", s.contains("<b>Scheduler stopped</b><br/>"));
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandStopTask(@Mocked HttpServletRequest request, @Mocked HttpServletResponse response, @Mocked ServletConfig config)
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");

        new Expectations() {{
            request.getParameter("command");            result = "stop";
            request.getParameter("taskName");           result = "task";
            response.getWriter();                       result = new PrintWriter(temp);
            config.getInitParameter("iniFileName");     result = "";
        }};

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
            ss.scheduleTask("task", MyTask.class, "description", null, null);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue("expected title not found", s.contains("<title>Scheduler Command Center</title>"));
            assertTrue("expected status not found", s.contains("<b>Task task was asked to stop</b><br/>"));
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandStopMissingTask(@Mocked HttpServletRequest request, @Mocked HttpServletResponse response, @Mocked ServletConfig config)
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");

        new Expectations() {{
            request.getParameter("command");            result = "stop";
            request.getParameter("taskName");           result = "task1";
            response.getWriter();                       result = new PrintWriter(temp);
            config.getInitParameter("iniFileName");     result = "";
        }};

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue("expected title not found", s.contains("<title>Scheduler Command Center</title>"));
            assertTrue("expected status not found", s.contains("Task task1 does not exist<br/>"));
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandRemoveNoTask(@Mocked HttpServletRequest request, @Mocked HttpServletResponse response, @Mocked ServletConfig config)
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");

        new Expectations() {{
            request.getParameter("command");            result = "remove";
            response.getWriter();                       result = new PrintWriter(temp);
            config.getInitParameter("iniFileName");     result = "";
        }};

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue("expected title not found", s.contains("<title>Scheduler Command Center</title>"));
            assertTrue("expected error header not found", s.contains("<b>Error(s) with configuration parameters</b><br/>"));
            assertTrue("expected error message not found", s.contains("Task name not informed<br/>"));
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandRemoveTask(@Mocked HttpServletRequest request, @Mocked HttpServletResponse response, @Mocked ServletConfig config)
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");

        new Expectations() {{
            request.getParameter("command");            result = "remove";
            request.getParameter("taskName");           result = "task1";
            response.getWriter();                       result = new PrintWriter(temp);
            config.getInitParameter("iniFileName");     result = "";
        }};

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);

            Calendar c1 = Calendar.getInstance();
            c1.add(Calendar.HOUR_OF_DAY, 1);
            Calendar c2 = Calendar.getInstance();
            c2.add(Calendar.HOUR_OF_DAY, 2);
            ss.scheduleTask("task1", MyTask.class, "description", c1, c2);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue("expected title not found", s.contains("<title>Scheduler Command Center</title>"));
            assertTrue("expected status not found", s.contains("<b>Task task1 was asked to stop and removed from scheduler</b><br/>"));
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandRemoveMissingTask(@Mocked HttpServletRequest request, @Mocked HttpServletResponse response, @Mocked ServletConfig config)
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");

        new Expectations() {{
            request.getParameter("command");            result = "remove";
            request.getParameter("taskName");           result = "task1";
            response.getWriter();                       result = new PrintWriter(temp);
            config.getInitParameter("iniFileName");     result = "";
        }};

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue("expected title not found", s.contains("<title>Scheduler Command Center</title>"));
            assertTrue("expected error header not found", s.contains("<b>Error(s) with configuration parameters</b><br/>"));
            assertTrue("expected error message not found", s.contains("Task task1 does not exist<br/>"));
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandKillNoTask(@Mocked HttpServletRequest request, @Mocked HttpServletResponse response, @Mocked ServletConfig config)
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");

        new Expectations() {{
            request.getParameter("command");            result = "kill";
            response.getWriter();                       result = new PrintWriter(temp);
            config.getInitParameter("iniFileName");     result = "";
        }};

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue("expected title not found", s.contains("<title>Scheduler Command Center</title>"));
            assertTrue("expected error header not found", s.contains("<b>Error(s) with configuration parameters</b><br/>"));
            assertTrue("expected error message not found", s.contains("Task name not informed<br/>"));
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandKillTask(@Mocked HttpServletRequest request, @Mocked HttpServletResponse response, @Mocked ServletConfig config)
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");

        new Expectations() {{
            request.getParameter("command");            result = "kill";
            request.getParameter("taskName");           result = "task1";
            response.getWriter();                       result = new PrintWriter(temp);
            config.getInitParameter("iniFileName");     result = "";
        }};

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
            ss.scheduleTask("task1", MyTask.class, "description", null, null);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue("expected title not found", s.contains("<title>Scheduler Command Center</title>"));
            assertTrue("expected status not found", s.contains("<b>Task task1 killed</b><br/>"));
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandKillMissingTask(@Mocked HttpServletRequest request, @Mocked HttpServletResponse response, @Mocked ServletConfig config)
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");

        new Expectations() {{
            request.getParameter("command");            result = "kill";
            request.getParameter("taskName");           result = "task1";
            response.getWriter();                       result = new PrintWriter(temp);
            config.getInitParameter("iniFileName");     result = "";
        }};

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue("expected title not found", s.contains("<title>Scheduler Command Center</title>"));
            assertTrue("expected error header not found", s.contains("<b>Error(s) with configuration parameters</b><br/>"));
            assertTrue("expected error message not found", s.contains("Task task1 does not exist<br/>"));
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandAddNoData(@Mocked HttpServletRequest request, @Mocked HttpServletResponse response, @Mocked ServletConfig config)
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");

        new Expectations() {{
            request.getParameter("command");            result = "add";
            response.getWriter();                       result = new PrintWriter(temp);
            config.getInitParameter("iniFileName");     result = "";
        }};

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue("expected title not found", s.contains("<title>Scheduler Command Center</title>"));
            assertTrue("expected error header not found", s.contains("<b>Error(s) with configuration parameters</b><br/>"));
            assertTrue("expected error message for task name not found", s.contains("Task name not informed<br/>"));
            assertTrue("expected error message for task class not found", s.contains("Task class name not informed<br/>"));
            assertTrue("expected error message for task description not found", s.contains("Task description not informed<br/>"));
            assertTrue("expected error message for task start not found", s.contains("Task start time not informed<br/>"));
            assertTrue("expected error message for task stop not found", s.contains("Task stop time not informed<br/>"));
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandAddBadDates(@Mocked HttpServletRequest request, @Mocked HttpServletResponse response, @Mocked ServletConfig config)
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");

        new Expectations() {{
            request.getParameter("command");            result = "add";
            request.getParameter("taskStartTime");      result = "bad";
            request.getParameter("taskStopTime");       result = "bad";
            response.getWriter();                       result = new PrintWriter(temp);
            config.getInitParameter("iniFileName");     result = "";
        }};

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue("expected title not found", s.contains("<title>Scheduler Command Center</title>"));
            assertTrue("expected error header not found", s.contains("<b>Error(s) with configuration parameters</b><br/>"));
            assertTrue("expected error message for task name not found", s.contains("Task name not informed<br/>"));
            assertTrue("expected error message for task class not found", s.contains("Task class name not informed<br/>"));
            assertTrue("expected error message for task description not found", s.contains("Task description not informed<br/>"));
            assertTrue("expected error message for task start not found", s.contains("Task start time not valid<br/>"));
            assertTrue("expected error message for task stop not found", s.contains("Task stop time not valid<br/>"));
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandAddBadClass(@Mocked HttpServletRequest request, @Mocked HttpServletResponse response, @Mocked ServletConfig config)
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");

        new Expectations() {{
            request.getParameter("command");            result = "add";
            request.getParameter("taskName");           result = "task1";
            request.getParameter("taskClassName");      result = "bad";
            request.getParameter("taskDescription");    result = "description";
            request.getParameter("taskStartTime");      result = "*";
            request.getParameter("taskStopTime");       result = "*";
            response.getWriter();                       result = new PrintWriter(temp);
            config.getInitParameter("iniFileName");     result = "";
        }};

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue("expected title not found", s.contains("<title>Scheduler Command Center</title>"));
            assertTrue("expected error header not found", s.contains("<b>Error(s) with configuration parameters</b><br/>"));
            assertTrue("expected error message not found", s.contains("class bad not found: java.lang.ClassNotFoundException: bad<br/>"));
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandAddOk(@Mocked HttpServletRequest request, @Mocked HttpServletResponse response, @Mocked ServletConfig config)
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");

        new Expectations() {{
            request.getParameter("command");            result = "add";
            request.getParameter("taskName");           result = "task1";
            request.getParameter("taskClassName");      result = "deors.core.commons.scheduler.SchedulerServletTestCase$MyTask";
            request.getParameter("taskDescription");    result = "description";
            request.getParameter("taskStartTime");      result = "*";
            request.getParameter("taskStopTime");       result = "*";
            response.getWriter();                       result = new PrintWriter(temp);
            config.getInitParameter("iniFileName");     result = "";
        }};

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue("expected title not found", s.contains("<title>Scheduler Command Center</title>"));
            assertTrue("expected status not found", s.contains("<b>Task task1 scheduled</b><br/>"));
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandScheduleNoData(@Mocked HttpServletRequest request, @Mocked HttpServletResponse response, @Mocked ServletConfig config)
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");

        new Expectations() {{
            request.getParameter("command");            result = "schedule";
            response.getWriter();                       result = new PrintWriter(temp);
            config.getInitParameter("iniFileName");     result = "";
        }};

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue("expected title not found", s.contains("<title>Scheduler Command Center</title>"));
            assertTrue("expected error header not found", s.contains("<b>Error(s) with configuration parameters</b><br/>"));
            assertTrue("expected error message for task name not found", s.contains("Task name not informed<br/>"));
            assertTrue("expected error message for task start not found", s.contains("Task start time not informed<br/>"));
            assertTrue("expected error message for task stop not found", s.contains("Task stop time not informed<br/>"));
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandScheduleBadDates(@Mocked HttpServletRequest request, @Mocked HttpServletResponse response, @Mocked ServletConfig config)
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");

        new Expectations() {{
            request.getParameter("command");            result = "schedule";
            request.getParameter("taskStartTime");      result = "bad";
            request.getParameter("taskStopTime");       result = "bad";
            response.getWriter();                       result = new PrintWriter(temp);
        }};

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue("expected title not found", s.contains("<title>Scheduler Command Center</title>"));
            assertTrue("expected error header not found", s.contains("<b>Error(s) with configuration parameters</b><br/>"));
            assertTrue("expected error message for task name not found", s.contains("Task name not informed<br/>"));
            assertTrue("expected error message for task start not found", s.contains("Task start time not valid<br/>"));
            assertTrue("expected error message for task stop not found", s.contains("Task stop time not valid<br/>"));
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandScheduleMissingTask(@Mocked HttpServletRequest request, @Mocked HttpServletResponse response, @Mocked ServletConfig config)
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");

        new Expectations() {{
            request.getParameter("command");            result = "schedule";
            request.getParameter("taskName");           result = "task1";
            request.getParameter("taskStartTime");      result = "*";
            request.getParameter("taskStopTime");       result = "*";
            response.getWriter();                       result = new PrintWriter(temp);
            config.getInitParameter("iniFileName");     result = "";
        }};

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue("expected title not found", s.contains("<title>Scheduler Command Center</title>"));
            assertTrue("expected error header not found", s.contains("<b>Error(s) with configuration parameters</b><br/>"));
            assertTrue("expected error message not found", s.contains("Task task1 does not exist<br/>"));
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandScheduleOk(@Mocked HttpServletRequest request, @Mocked HttpServletResponse response, @Mocked ServletConfig config)
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");

        new Expectations() {{
            request.getParameter("command");            result = "schedule";
            request.getParameter("taskName");           result = "task1";
            request.getParameter("taskStartTime");      result = "*";
            request.getParameter("taskStopTime");       result = "*";
            response.getWriter();                       result = new PrintWriter(temp);
            config.getInitParameter("iniFileName");     result = "";
        }};

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
            ss.scheduleTask("task1", MyTask.class, "description", null, null);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue("expected title not found", s.contains("<title>Scheduler Command Center</title>"));
            assertTrue("expected status not found", s.contains("<b>Task task1 scheduled</b><br/>"));
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletResponseNotInitializedNoMessages(@Mocked HttpServletRequest request, @Mocked HttpServletResponse response)
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException {

        File temp = File.createTempFile("deors.core.commons.", ".test");

        new Expectations() {{
            request.getRequestURI();                    result = "/testURI";
            response.getWriter();                       result = new PrintWriter(temp);
        }};

        Method mCreate = SchedulerServlet.class.getDeclaredMethod("createServletResponse", HttpServletRequest.class, HttpServletResponse.class, List.class, List.class, boolean.class);
        mCreate.setAccessible(true);

        Field fInit = SchedulerServlet.class.getDeclaredField("initialized");
        fInit.setAccessible(true);

        List<String> messages = new ArrayList<String>();
        List<String> errors = new ArrayList<String>();

        SchedulerServlet ss = new SchedulerServlet();
        try {
            // the scheduler servlet is marked as not initialized and its response is created
            fInit.setBoolean(ss, false);
            mCreate.invoke(ss, request, response, messages, errors, true);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue("expected title not found", s.contains("<title>Scheduler Command Center</title>"));
            assertTrue("expected status not found", s.contains("<b>Scheduler not running</b><br/>"));
            assertTrue("expected form not found", s.contains("<form id=\"commandForm\" name=\"commandForm\" method=\"post\" action=\"/testURI\">"));
            assertTrue("expected button not found", s.contains("<input type=\"button\" name=\"start\" value=\"start\""));
            assertTrue("expected configuration header not found", s.contains("<b>Configuration parameters</b>"));
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletResponseNotInitializedWithMessages(@Mocked HttpServletRequest request, @Mocked HttpServletResponse response)
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {

        File temp = File.createTempFile("deors.core.commons.", ".test");

        new Expectations() {{
            request.getRequestURI();                    result = "/testURI";
            response.getWriter();                       result = new PrintWriter(temp);
        }};

        Method mCreate = SchedulerServlet.class.getDeclaredMethod("createServletResponse", HttpServletRequest.class, HttpServletResponse.class, List.class, List.class, boolean.class);
        mCreate.setAccessible(true);

        List<String> messages = new ArrayList<String>();
        messages.add("message test 1");
        messages.add("message test 2");

        List<String> errors = new ArrayList<String>();
        errors.add("error test 1");
        errors.add("error test 2");

        SchedulerServlet ss = new SchedulerServlet();
        try {
            // the scheduler servlet response is created with specific messages
            mCreate.invoke(ss, request, response, messages, errors, true);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue("expected title not found", s.contains("<title>Scheduler Command Center</title>"));
            assertTrue("expected status not found", s.contains("<b>Scheduler not running</b><br/>"));
            assertTrue("expected form not found", s.contains("<form id=\"commandForm\" name=\"commandForm\" method=\"post\" action=\"/testURI\">"));
            assertTrue("expected button not found", s.contains("<input type=\"button\" name=\"start\" value=\"start\""));
            assertTrue("expected configuration header not found", s.contains("<b>Configuration parameters</b>"));
            assertTrue("expected message 'test 1' not found", s.contains("<b>message test 1</b><br/>"));
            assertTrue("expected message 'test 2' not found", s.contains("<b>message test 2</b><br/>"));
            assertTrue("expected error header not found", s.contains("<b>Error(s) with configuration parameters</b><br/>"));
            assertTrue("expected message 'error 1' not found", s.contains("error test 1<br/>"));
            assertTrue("expected message 'error 2' not found", s.contains("error test 2<br/>"));
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletResponseInitialized(@Mocked HttpServletRequest request, @Mocked HttpServletResponse response)
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException {

        File temp = File.createTempFile("deors.core.commons.", ".test");

        new Expectations() {{
            request.getRequestURI();                    result = "/testURI";
            response.getWriter();                       result = new PrintWriter(temp);
        }};

        Method mCreate = SchedulerServlet.class.getDeclaredMethod("createServletResponse", HttpServletRequest.class, HttpServletResponse.class, List.class, List.class, boolean.class);
        mCreate.setAccessible(true);

        Field fInit = SchedulerServlet.class.getDeclaredField("initialized");
        fInit.setAccessible(true);

        Field fSch = SchedulerServlet.class.getDeclaredField("sch");
        fSch.setAccessible(true);

        List<String> messages = new ArrayList<String>();
        List<String> errors = new ArrayList<String>();

        Scheduler sch = new Scheduler();
        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.HOUR_OF_DAY, 1);
        Calendar c2 = Calendar.getInstance();
        c2.add(Calendar.HOUR_OF_DAY, 2);
        sch.scheduleTask("task1", MyTask.class, "description1", null, null);
        sch.scheduleTask("task2", MyTask.class, "description2", c1, c2);

        SchedulerServlet ss = new SchedulerServlet();
        try {
            // the scheduler servlet is marked as initialized, the scheduler instance
            // is set directly on the servlet instance and its response is created
            fInit.setBoolean(ss, true);
            fSch.set(ss, sch);
            mCreate.invoke(ss, request, response, messages, errors, true);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue("expected message for task1 header not found", s.contains("Task <b>task1</b> (idle)"));
            assertTrue("expected message for task1 is a deamon not found", s.contains("&nbsp;&nbsp;info: task is a <i>daemon</i>"));
            assertTrue("expected button to start task1 not found", s.contains("onclick=\"taskStart('task1')\"/>&nbsp;&nbsp;"));
            assertTrue("expected message for task2 header not found", s.contains("Task <b>task2</b> (idle)"));
            assertTrue("expected message for task2 is scheduled not found", s.contains("&nbsp;&nbsp;info: task is scheduled from "));
            assertTrue("expected button to start task2 not found", s.contains("onclick=\"taskStart('task2')\"/>&nbsp;&nbsp;"));
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletInitWithFile(@Mocked ServletConfig config)
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        new Expectations() {{
            config.getInitParameter("iniFileName");     result = "target/test-classes/scheduler.ini";
        }};

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);

            assertTrue("expected task 'task' not found", ss.existsTask("task"));
            assertTrue("expected task 'daemon' not found", ss.existsTask("daemon"));
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
        }
    }

    @Test
    public void testServletInitNoFile(@Mocked ServletConfig config)
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);

            assertFalse("unexpected task 'task' found", ss.existsTask("task"));
            assertFalse("unexpected task 'daemon' found", ss.existsTask("daemon"));
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
        }
    }

    @Test
    public void testServletInitBlankFile(@Mocked ServletConfig config)
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        new Expectations() {{
            config.getInitParameter("iniFileName");     result = "";
        }};

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);

            assertFalse("unexpected task 'task' found", ss.existsTask("task"));
            assertFalse("unexpected task 'daemon' found", ss.existsTask("daemon"));
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
        }
    }

    @Test
    public void testServletInitInvalidFile(@Mocked ServletConfig config)
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        new Expectations() {{
            config.getInitParameter("iniFileName");     result = "target/test-classes/scheduler-err1.ini";
        }};

        thrown.expect(ServletException.class);
        thrown.expectMessage("[scheduler] the configuration file content is not valid: java.lang.IllegalArgumentException: class name for task task not found");

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
        }
    }

    private void testSleep() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException ie) {
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
