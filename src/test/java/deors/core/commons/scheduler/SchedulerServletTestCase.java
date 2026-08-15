package deors.core.commons.scheduler;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.mockito.Mockito.when;

import deors.core.commons.io.IOToolkit;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class SchedulerServletTestCase {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ServletConfig config;

    @Mock
    private HttpServletRequest request2;

    @Mock
    private HttpServletResponse response2;

    @Mock
    private HttpServletRequest request3;

    @Mock
    private HttpServletResponse response3;

    public SchedulerServletTestCase() {

        super();
    }

    @BeforeEach
    public void setUp() {

        SchedulerServlet.stopAllTasks();
        SchedulerServlet.resetScheduler();
    }

    @Test
    public void testServletCommandNull()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");
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
    public void testServletCommandEmpty()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");
        when(request.getParameter("command")).thenReturn("");
        when(response.getWriter()).thenReturn(new PrintWriter(temp));

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.doGet(request, response);

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
    public void testServletCommandHelp()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");
        when(request.getParameter("command")).thenReturn("help");
        when(response.getWriter()).thenReturn(new PrintWriter(temp));

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.doGet(request, response);

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
    public void testServletCommandStartEmpty()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");
        when(request.getParameter("command")).thenReturn("start");
        when(request.getParameter("iniFileName")).thenReturn("");
        when(response.getWriter()).thenReturn(new PrintWriter(temp));
        
        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue(s.contains("<title>Scheduler Command Center</title>"), "expected title not found");
            assertTrue(s.contains("<b>Scheduler started</b><br/>"), "expected status not found");

            assertFalse(ss.existsTask("task"), "unexpected task 'task' found");
            assertFalse(ss.existsTask("daemon"), "unexpected task 'daemon' found");
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandStartWithFile()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");
        when(request.getParameter("command")).thenReturn("start");
        when(request.getParameter("iniFileName")).thenReturn("target/test-classes/scheduler.ini");
        when(response.getWriter()).thenReturn(new PrintWriter(temp));

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue(s.contains("<title>Scheduler Command Center</title>"), "expected title not found");
            assertTrue(s.contains("<b>Scheduler started</b><br/>"), "expected status not found");

            assertTrue(ss.existsTask("task"), "expected task 'task' not found");
            assertTrue(ss.existsTask("daemon"), "expected task 'daemon' not found");
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandStartMissingFile()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");
        when(request.getParameter("command")).thenReturn("start");
        when(request.getParameter("iniFileName")).thenReturn("target/test-classes/missing.ini");
        when(response.getWriter()).thenReturn(new PrintWriter(temp));

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue(s.contains("[scheduler] the configuration file is either missing or inaccessible:"), "expected error message not found");
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandStartAgain()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp1 = File.createTempFile("deors.core.commons.", ".test");
        File temp2 = File.createTempFile("deors.core.commons.", ".test");
        File temp3 = File.createTempFile("deors.core.commons.", ".test");
        when(request.getParameter("command")).thenReturn("start");
        when(request.getParameter("iniFileName")).thenReturn("target/test-classes/scheduler.ini");
        when(response.getWriter()).thenReturn(new PrintWriter(temp1));
        when(request2.getParameter("command")).thenReturn("stop");
        when(response2.getWriter()).thenReturn(new PrintWriter(temp2));
        when(request3.getParameter("command")).thenReturn("start");
        when(request3.getParameter("iniFileName")).thenReturn("");
        when(response3.getWriter()).thenReturn(new PrintWriter(temp3));

        SchedulerServlet ss = new SchedulerServlet();
        try {
            // stars scheduler with ini file including tasks
            ss.doGet(request, response);

            // stops scheduler
            ss.doGet(request2, response2);

            // starts again the scheduler without ini file - the tasks are maintained from previous execution
            ss.doGet(request3, response3);

            byte[] output = IOToolkit.readFile(temp3);
            String s = new String(output);

            assertTrue(s.contains("<title>Scheduler Command Center</title>"), "expected title not found");
            assertTrue(s.contains("<b>Scheduler started</b><br/>"), "expected status not found");

            assertTrue(ss.existsTask("task"), "expected task 'task' not found");
            assertTrue(ss.existsTask("daemon"), "expected task 'daemon' not found");
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
    public void testServletCommandStartAlreadyInit()
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");
        when(request.getParameter("command")).thenReturn("start");
        when(response.getWriter()).thenReturn(new PrintWriter(temp));
        when(config.getInitParameter("iniFileName")).thenReturn("");

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue(s.contains("<title>Scheduler Command Center</title>"), "expected title not found");
            assertTrue(s.contains("<b>Scheduler already started</b><br/>"), "expected status not found");
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandStopNotInit()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");
        when(request.getParameter("command")).thenReturn("stop");
        when(response.getWriter()).thenReturn(new PrintWriter(temp));

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue(s.contains("<title>Scheduler Command Center</title>"), "expected title not found");
            assertTrue(s.contains("<b>Scheduler not running</b><br/>"), "expected status not found");

            assertFalse(ss.existsTask("task"), "unexpected task 'task' found");
            assertFalse(ss.existsTask("daemon"), "unexpected task 'daemon' found");
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandStopIfInit()
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");
        when(request.getParameter("command")).thenReturn("stop");
        when(response.getWriter()).thenReturn(new PrintWriter(temp));
        when(config.getInitParameter("iniFileName")).thenReturn("");

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue(s.contains("<title>Scheduler Command Center</title>"), "expected title not found");
            assertTrue(s.contains("<b>Scheduler stopped</b><br/>"), "expected status not found");
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandStopTask()
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");
        when(request.getParameter("command")).thenReturn("stop");
        when(request.getParameter("taskName")).thenReturn("task");
        when(response.getWriter()).thenReturn(new PrintWriter(temp));
        when(config.getInitParameter("iniFileName")).thenReturn("");

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
            ss.scheduleTask("task", MyTask.class, "description", null, null);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue(s.contains("<title>Scheduler Command Center</title>"), "expected title not found");
            assertTrue(s.contains("<b>Task task was asked to stop</b><br/>"), "expected status not found");
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandStopMissingTask()
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");
        when(request.getParameter("command")).thenReturn("stop");
        when(request.getParameter("taskName")).thenReturn("task1");
        when(response.getWriter()).thenReturn(new PrintWriter(temp));
        when(config.getInitParameter("iniFileName")).thenReturn("");

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue(s.contains("<title>Scheduler Command Center</title>"), "expected title not found");
            assertTrue(s.contains("Task task1 does not exist<br/>"), "expected status not found");
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandRemoveNoTask()
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");
        when(request.getParameter("command")).thenReturn("remove");
        when(response.getWriter()).thenReturn(new PrintWriter(temp));
        when(config.getInitParameter("iniFileName")).thenReturn("");

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue(s.contains("<title>Scheduler Command Center</title>"), "expected title not found");
            assertTrue(s.contains("<b>Error(s) with configuration parameters</b><br/>"), "expected error header not found");
            assertTrue(s.contains("Task name not informed<br/>"), "expected error message not found");
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandRemoveTask()
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");
        when(request.getParameter("command")).thenReturn("remove");
        when(request.getParameter("taskName")).thenReturn("task1");
        when(response.getWriter()).thenReturn(new PrintWriter(temp));
        when(config.getInitParameter("iniFileName")).thenReturn("");

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

            assertTrue(s.contains("<title>Scheduler Command Center</title>"), "expected title not found");
            assertTrue(s.contains("<b>Task task1 was asked to stop and removed from scheduler</b><br/>"), "expected status not found");
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandRemoveMissingTask()
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");
        when(request.getParameter("command")).thenReturn("remove");
        when(request.getParameter("taskName")).thenReturn("task1");
        when(response.getWriter()).thenReturn(new PrintWriter(temp));
        when(config.getInitParameter("iniFileName")).thenReturn("");

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue(s.contains("<title>Scheduler Command Center</title>"), "expected title not found");
            assertTrue(s.contains("<b>Error(s) with configuration parameters</b><br/>"), "expected error header not found");
            assertTrue(s.contains("Task task1 does not exist<br/>"), "expected error message not found");
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandKillNoTask()
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");
        when(request.getParameter("command")).thenReturn("kill");
        when(response.getWriter()).thenReturn(new PrintWriter(temp));
        when(config.getInitParameter("iniFileName")).thenReturn("");

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue(s.contains("<title>Scheduler Command Center</title>"), "expected title not found");
            assertTrue(s.contains("<b>Error(s) with configuration parameters</b><br/>"), "expected error header not found");
            assertTrue(s.contains("Task name not informed<br/>"), "expected error message not found");
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandKillTask()
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");
        when(request.getParameter("command")).thenReturn("kill");
        when(request.getParameter("taskName")).thenReturn("task1");
        when(response.getWriter()).thenReturn(new PrintWriter(temp));
        when(config.getInitParameter("iniFileName")).thenReturn("");

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
            ss.scheduleTask("task1", MyTask.class, "description", null, null);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue(s.contains("<title>Scheduler Command Center</title>"), "expected title not found");
            assertTrue(s.contains("<b>Task task1 killed</b><br/>"), "expected status not found");
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandKillMissingTask()
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");
        when(request.getParameter("command")).thenReturn("kill");
        when(request.getParameter("taskName")).thenReturn("task1");
        when(response.getWriter()).thenReturn(new PrintWriter(temp));
        when(config.getInitParameter("iniFileName")).thenReturn("");

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue(s.contains("<title>Scheduler Command Center</title>"), "expected title not found");
            assertTrue(s.contains("<b>Error(s) with configuration parameters</b><br/>"), "expected error header not found");
            assertTrue(s.contains("Task task1 does not exist<br/>"), "expected error message not found");
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandAddNoData()
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");
        when(request.getParameter("command")).thenReturn("add");
        when(response.getWriter()).thenReturn(new PrintWriter(temp));
        when(config.getInitParameter("iniFileName")).thenReturn("");

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue(s.contains("<title>Scheduler Command Center</title>"), "expected title not found");
            assertTrue(s.contains("<b>Error(s) with configuration parameters</b><br/>"), "expected error header not found");
            assertTrue(s.contains("Task name not informed<br/>"), "expected error message for task name not found");
            assertTrue(s.contains("Task class name not informed<br/>"), "expected error message for task class not found");
            assertTrue(s.contains("Task description not informed<br/>"), "expected error message for task description not found");
            assertTrue(s.contains("Task start time not informed<br/>"), "expected error message for task start not found");
            assertTrue(s.contains("Task stop time not informed<br/>"), "expected error message for task stop not found");
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandAddBadDates()
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");
        when(request.getParameter("command")).thenReturn("add");
        when(request.getParameter("taskStartTime")).thenReturn("bad");
        when(request.getParameter("taskStopTime")).thenReturn("bad");
        when(response.getWriter()).thenReturn(new PrintWriter(temp));
        when(config.getInitParameter("iniFileName")).thenReturn("");

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue(s.contains("<title>Scheduler Command Center</title>"), "expected title not found");
            assertTrue(s.contains("<b>Error(s) with configuration parameters</b><br/>"), "expected error header not found");
            assertTrue(s.contains("Task name not informed<br/>"), "expected error message for task name not found");
            assertTrue(s.contains("Task class name not informed<br/>"), "expected error message for task class not found");
            assertTrue(s.contains("Task description not informed<br/>"), "expected error message for task description not found");
            assertTrue(s.contains("Task start time not valid<br/>"), "expected error message for task start not found");
            assertTrue(s.contains("Task stop time not valid<br/>"), "expected error message for task stop not found");
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandAddBadClass()
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");
        when(request.getParameter("command")).thenReturn("add");
        when(request.getParameter("taskName")).thenReturn("task1");
        when(request.getParameter("taskClassName")).thenReturn("bad");
        when(request.getParameter("taskDescription")).thenReturn("description");
        when(request.getParameter("taskStartTime")).thenReturn("*");
        when(request.getParameter("taskStopTime")).thenReturn("*");
        when(response.getWriter()).thenReturn(new PrintWriter(temp));
        when(config.getInitParameter("iniFileName")).thenReturn("");

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue(s.contains("<title>Scheduler Command Center</title>"), "expected title not found");
            assertTrue(s.contains("<b>Error(s) with configuration parameters</b><br/>"), "expected error header not found");
            assertTrue(s.contains("class bad not found: java.lang.ClassNotFoundException: bad<br/>"), "expected error message not found");
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandAddOk()
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");
        when(request.getParameter("command")).thenReturn("add");
        when(request.getParameter("taskName")).thenReturn("task1");
        when(request.getParameter("taskClassName")).thenReturn("deors.core.commons.scheduler.SchedulerServletTestCase$MyTask");
        when(request.getParameter("taskDescription")).thenReturn("description");
        when(request.getParameter("taskStartTime")).thenReturn("*");
        when(request.getParameter("taskStopTime")).thenReturn("*");
        when(response.getWriter()).thenReturn(new PrintWriter(temp));
        when(config.getInitParameter("iniFileName")).thenReturn("");

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue(s.contains("<title>Scheduler Command Center</title>"), "expected title not found");
            assertTrue(s.contains("<b>Task task1 scheduled</b><br/>"), "expected status not found");
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandScheduleNoData()
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");
        when(request.getParameter("command")).thenReturn("schedule");
        when(response.getWriter()).thenReturn(new PrintWriter(temp));
        when(config.getInitParameter("iniFileName")).thenReturn("");

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue(s.contains("<title>Scheduler Command Center</title>"), "expected title not found");
            assertTrue(s.contains("<b>Error(s) with configuration parameters</b><br/>"), "expected error header not found");
            assertTrue(s.contains("Task name not informed<br/>"), "expected error message for task name not found");
            assertTrue(s.contains("Task start time not informed<br/>"), "expected error message for task start not found");
            assertTrue(s.contains("Task stop time not informed<br/>"), "expected error message for task stop not found");
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandScheduleBadDates()
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");
        when(request.getParameter("command")).thenReturn("schedule");
        when(request.getParameter("taskStartTime")).thenReturn("bad");
        when(request.getParameter("taskStopTime")).thenReturn("bad");
        when(response.getWriter()).thenReturn(new PrintWriter(temp));

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue(s.contains("<title>Scheduler Command Center</title>"), "expected title not found");
            assertTrue(s.contains("<b>Error(s) with configuration parameters</b><br/>"), "expected error header not found");
            assertTrue(s.contains("Task name not informed<br/>"), "expected error message for task name not found");
            assertTrue(s.contains("Task start time not valid<br/>"), "expected error message for task start not found");
            assertTrue(s.contains("Task stop time not valid<br/>"), "expected error message for task stop not found");
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandScheduleMissingTask()
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");
        when(request.getParameter("command")).thenReturn("schedule");
        when(request.getParameter("taskName")).thenReturn("task1");
        when(request.getParameter("taskStartTime")).thenReturn("*");
        when(request.getParameter("taskStopTime")).thenReturn("*");
        when(response.getWriter()).thenReturn(new PrintWriter(temp));
        when(config.getInitParameter("iniFileName")).thenReturn("");

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue(s.contains("<title>Scheduler Command Center</title>"), "expected title not found");
            assertTrue(s.contains("<b>Error(s) with configuration parameters</b><br/>"), "expected error header not found");
            assertTrue(s.contains("Task task1 does not exist<br/>"), "expected error message not found");
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletCommandScheduleOk()
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        File temp = File.createTempFile("deors.core.commons.", ".test");
        when(request.getParameter("command")).thenReturn("schedule");
        when(request.getParameter("taskName")).thenReturn("task1");
        when(request.getParameter("taskStartTime")).thenReturn("*");
        when(request.getParameter("taskStopTime")).thenReturn("*");
        when(response.getWriter()).thenReturn(new PrintWriter(temp));
        when(config.getInitParameter("iniFileName")).thenReturn("");

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);
            ss.scheduleTask("task1", MyTask.class, "description", null, null);
            ss.doGet(request, response);

            byte[] output = IOToolkit.readFile(temp);
            String s = new String(output);

            assertTrue(s.contains("<title>Scheduler Command Center</title>"), "expected title not found");
            assertTrue(s.contains("<b>Task task1 scheduled</b><br/>"), "expected status not found");
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletResponseNotInitializedNoMessages()
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException {

        File temp = File.createTempFile("deors.core.commons.", ".test");
        when(request.getRequestURI()).thenReturn("/testURI");
        when(response.getWriter()).thenReturn(new PrintWriter(temp));

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

            assertTrue(s.contains("<title>Scheduler Command Center</title>"), "expected title not found");
            assertTrue(s.contains("<b>Scheduler not running</b><br/>"), "expected status not found");
            assertTrue(s.contains("<form id=\"commandForm\" name=\"commandForm\" method=\"post\" action=\"/testURI\">"), "expected form not found");
            assertTrue(s.contains("<input type=\"button\" name=\"start\" value=\"start\""), "expected button not found");
            assertTrue(s.contains("<b>Configuration parameters</b>"), "expected configuration header not found");
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletResponseNotInitializedWithMessages()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {

        File temp = File.createTempFile("deors.core.commons.", ".test");
        when(request.getRequestURI()).thenReturn("/testURI");
        when(response.getWriter()).thenReturn(new PrintWriter(temp));

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

            assertTrue(s.contains("<title>Scheduler Command Center</title>"), "expected title not found");
            assertTrue(s.contains("<b>Scheduler not running</b><br/>"), "expected status not found");
            assertTrue(s.contains("<form id=\"commandForm\" name=\"commandForm\" method=\"post\" action=\"/testURI\">"), "expected form not found");
            assertTrue(s.contains("<input type=\"button\" name=\"start\" value=\"start\""), "expected button not found");
            assertTrue(s.contains("<b>Configuration parameters</b>"), "expected configuration header not found");
            assertTrue(s.contains("<b>message test 1</b><br/>"), "expected message 'test 1' not found");
            assertTrue(s.contains("<b>message test 2</b><br/>"), "expected message 'test 2' not found");
            assertTrue(s.contains("<b>Error(s) with configuration parameters</b><br/>"), "expected error header not found");
            assertTrue(s.contains("error test 1<br/>"), "expected message 'error 1' not found");
            assertTrue(s.contains("error test 2<br/>"), "expected message 'error 2' not found");
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletResponseInitialized()
        throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, IOException {

        File temp = File.createTempFile("deors.core.commons.", ".test");
        when(request.getRequestURI()).thenReturn("/testURI");
        when(response.getWriter()).thenReturn(new PrintWriter(temp));

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

            assertTrue(s.contains("Task <b>task1</b> (idle)"), "expected message for task1 header not found");
            assertTrue(s.contains("&nbsp;&nbsp;info: task is a <i>daemon</i>"), "expected message for task1 is a deamon not found");
            assertTrue(s.contains("onclick=\"taskStart('task1')\"/>&nbsp;&nbsp;"), "expected button to start task1 not found");
            assertTrue(s.contains("Task <b>task2</b> (idle)"), "expected message for task2 header not found");
            assertTrue(s.contains("&nbsp;&nbsp;info: task is scheduled from "), "expected message for task2 is scheduled not found");
            assertTrue(s.contains("onclick=\"taskStart('task2')\"/>&nbsp;&nbsp;"), "expected button to start task2 not found");
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
            temp.delete();
        }
    }

    @Test
    public void testServletInitWithFile()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, ServletException {
        when(config.getInitParameter("iniFileName")).thenReturn("target/test-classes/scheduler.ini");

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);

            assertTrue(ss.existsTask("task"), "expected task 'task' not found");
            assertTrue(ss.existsTask("daemon"), "expected task 'daemon' not found");
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
        }
    }

    @Test
    public void testServletInitNoFile()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, ServletException {

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);

            assertFalse(ss.existsTask("task"), "unexpected task 'task' found");
            assertFalse(ss.existsTask("daemon"), "unexpected task 'daemon' found");
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
        }
    }

    @Test
    public void testServletInitBlankFile()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, ServletException {
        when(config.getInitParameter("iniFileName")).thenReturn("");

        SchedulerServlet ss = new SchedulerServlet();
        try {
            ss.init(config);

            assertFalse(ss.existsTask("task"), "unexpected task 'task' found");
            assertFalse(ss.existsTask("daemon"), "unexpected task 'daemon' found");
        } finally {
            ss.stopAllTasks();
            ss.resetScheduler();
            testSleep();
        }
    }

    @Test
    public void testServletInitInvalidFile()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, ServletException {
        when(config.getInitParameter("iniFileName")).thenReturn("target/test-classes/scheduler-err1.ini");

        Exception ex = assertThrows(ServletException.class, () -> {

            SchedulerServlet ss = new SchedulerServlet();
            try {
                ss.init(config);
            } finally {
                ss.stopAllTasks();
                ss.resetScheduler();
                testSleep();
            }
            });
        assertTrue(ex.getMessage().contains("[scheduler] the configuration file content is not valid: java.lang.IllegalArgumentException: class name for task task not found"));
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
