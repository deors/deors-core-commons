package deors.core.commons.template;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import deors.core.commons.CommonsContext;
import deors.core.commons.IOToolkit;
import deors.core.commons.template.Template;
import deors.core.commons.template.TemplateException;

public class TemplateTestCase {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static final String TEMPLATE_FILE_NAME = "/test.tmpl";

    public TemplateTestCase() {

        super();
    }

    @Test
    public void testConstructorNull()
        throws TemplateException {

        thrown.expect(NullPointerException.class);

        new Template(null);
    }

    @Test
    public void testNoTemplate()
        throws TemplateException {

        thrown.expect(TemplateException.class);
        thrown.expectMessage(CommonsContext.getMessage("TMPL_ERR_NEED_LOAD"));

        Template t = new Template();
        t.processTemplate(new HashMap<String, String>());
    }

    @Test
    public void testProcessTemplate()
        throws TemplateException {

        Template t = new Template(this.getClass().getResourceAsStream(TEMPLATE_FILE_NAME));

        assertNotNull(t.getTemplateSource());

        Map<String, String> h = new HashMap<String, String>();
        h.put("línea", "valor-de-línea");
        h.put("tiene", "valor-de-tiene");
        h.put("final", "valor-de-final");
        h.put("de", "valor-de-de");
        h.put("mas", "valor-de-mas");
        h.put("son", "valor-de-son");
        h.put("necesarias", "valor-de-necesarias");

        List<String> expected = new ArrayList<String>();
        expected.add("prueba de template");
        expected.add("esta línea no tiene tokens");
        expected.add("esta línea tiene [[ un carácter de apertura");
        expected.add("esta valor-de-línea tiene uno ] de cierre");
        expected.add("esta línea tiene un [token no cerrado");
        expected.add("esta línea valor-de-tiene un token");
        expected.add("esta línea valor-de-tiene tres [tokens] uno justo al valor-de-final");
        expected.add("una [[ mezla valor-de-de [ cosas");
        expected.add("pruebas valor-de-mas completas valor-de-son valor-de-necesarias final");
        expected.add("pruebas [[mas] completas valor-de-son valor-de-necesarias");

        List<String> processed = t.processTemplate(h);

        assertEquals(expected, processed);
    }

    @Test
    public void testProcessTemplateNoReplacements()
        throws TemplateException {

        Template t = new Template();
        t.setTemplateSource(this.getClass().getResourceAsStream(TEMPLATE_FILE_NAME));
        t.loadTemplate();

        assertNotNull(t.getTemplateSource());

        List<String> expected = new ArrayList<String>();
        expected.add("prueba de template");
        expected.add("esta línea no tiene tokens");
        expected.add("esta línea tiene [[ un carácter de apertura");
        expected.add("esta [línea] tiene uno ] de cierre");
        expected.add("esta línea tiene un [token no cerrado");
        expected.add("esta línea [tiene] un token");
        expected.add("esta línea [tiene] tres [tokens] uno justo al [final]");
        expected.add("una [[ mezla [de] [ cosas");
        expected.add("pruebas [mas] completas [son] [necesarias] final");
        expected.add("pruebas [[mas] completas [son] [necesarias]");

        List<String> processed = t.processTemplate(null);

        assertEquals(expected, processed);
    }

    @Test
    public void testProcessTemplatePrintWriter()
        throws TemplateException, IOException {

        Template t = new Template();
        t.setTemplateSource(this.getClass().getResourceAsStream(TEMPLATE_FILE_NAME));
        t.loadTemplate();

        assertNotNull(t.getTemplateSource());

        File file = IOToolkit.createTempFile(true);
        PrintWriter pw = new PrintWriter(file);

        t.processTemplate(null, pw);
        pw.close();

        byte[] expected = IOToolkit.readStream(this.getClass().getResourceAsStream(TEMPLATE_FILE_NAME));
        byte[] actual = IOToolkit.readFile(file);

        assertArrayEquals(expected, actual);

        file.delete();
    }

    @Test(expected = TemplateException.class)
    public void testLoadTemplateError()
        throws TemplateException, IOException {

        InputStream is = createMock(InputStream.class);

        is.close();
        expect(is.read(anyObject(byte[].class), eq(0), eq(8192))).andThrow(new IOException("error"));

        replay(is);

        new Template(is);
    }
}
