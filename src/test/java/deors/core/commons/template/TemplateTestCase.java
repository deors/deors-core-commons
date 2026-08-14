package deors.core.commons.template;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import deors.core.commons.CommonsContext;
import deors.core.commons.io.IOToolkit;

public class TemplateTestCase {

    private static final String TEMPLATE_1_FILE_NAME = "/test1.tmpl";
    private static final String TEMPLATE_2_FILE_NAME = "/test2.tmpl";

    public TemplateTestCase() {

        super();
    }

    @Test
    public void testConstructorNull()
        throws TemplateException {

        assertThrows(NullPointerException.class, () -> {

            new Template(null);
        });
    }

    @Test
    public void testNoTemplate()
        throws TemplateException {

        Exception ex = assertThrows(TemplateException.class, () -> {

            Template t = new Template();
            t.processTemplate(new HashMap<String, String>());
        });
        assertTrue(ex.getMessage().contains(CommonsContext.getMessage("TMPL_ERR_NEED_LOAD")));
    }

    @Test
    public void testProcessTemplateDefaultCharset()
        throws TemplateException {

        Template t = new Template(this.getClass().getResourceAsStream(TEMPLATE_2_FILE_NAME));

        assertNotNull(t.getTemplateSource());

        Map<String, String> replacements = new HashMap<String, String>();
        replacements.put("template", "plantilla");

        List<String> expected = new ArrayList<String>();
        expected.add("prueba de plantilla");
        expected.add("muy sencilla");

        List<String> processed = t.processTemplate(replacements);

        assertEquals(expected, processed);
    }

    @Test
    public void testProcessTemplateCharsetAsString()
        throws TemplateException {

        Template t = new Template(this.getClass().getResourceAsStream(TEMPLATE_1_FILE_NAME), "ISO-8859-1");

        assertNotNull(t.getTemplateSource());

        Map<String, String> replacements = new HashMap<String, String>();
        replacements.put("línea", "valor-de-línea");
        replacements.put("tiene", "valor-de-tiene");
        replacements.put("final", "valor-de-final");
        replacements.put("de", "valor-de-de");
        replacements.put("mas", "valor-de-mas");
        replacements.put("son", "valor-de-son");
        replacements.put("necesarias", "valor-de-necesarias");

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

        List<String> processed = t.processTemplate(replacements);

        assertEquals(expected, processed);
    }

    @Test
    public void testProcessTemplateCharsetAsObject()
        throws TemplateException {

        Template t = new Template(this.getClass().getResourceAsStream(TEMPLATE_1_FILE_NAME), Charset.forName("ISO-8859-1"));

        assertNotNull(t.getTemplateSource());

        Map<String, String> replacements = new HashMap<String, String>();
        replacements.put("línea", "valor-de-línea");
        replacements.put("tiene", "valor-de-tiene");
        replacements.put("final", "valor-de-final");
        replacements.put("de", "valor-de-de");
        replacements.put("mas", "valor-de-mas");
        replacements.put("son", "valor-de-son");
        replacements.put("necesarias", "valor-de-necesarias");

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

        List<String> processed = t.processTemplate(replacements);

        assertEquals(expected, processed);
    }

    @Test
    public void testProcessTemplateNoReplacements()
        throws TemplateException {

        Template t = new Template();
        t.setTemplateSource(this.getClass().getResourceAsStream(TEMPLATE_1_FILE_NAME));
        t.setTemplateCharset(Charset.forName("ISO-8859-1"));

        assertNotNull(t.getTemplateSource());
        assertNotNull(t.getTemplateCharset());

        t.loadTemplate();

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
        t.setTemplateSource(this.getClass().getResourceAsStream(TEMPLATE_1_FILE_NAME));
        t.setTemplateCharset(Charset.forName("ISO-8859-1"));

        assertNotNull(t.getTemplateSource());
        assertNotNull(t.getTemplateCharset());

        t.loadTemplate();

        File file = IOToolkit.createTempFile(true);
        PrintWriter pw = new PrintWriter(file, "ISO-8859-1");

        t.processTemplate(null, pw);
        pw.close();

        List<String> expected = IOToolkit.readTextStream(this.getClass().getResourceAsStream(TEMPLATE_1_FILE_NAME));
        List<String> actual = IOToolkit.readTextFile(file);

        assertEquals(expected, actual);

        file.delete();
    }

    @Test
    public void testLoadTemplateError()
        throws TemplateException {

        InputStream errorStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("simulated read error");
            }

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                throw new IOException("simulated read error");
            }
        };

        assertThrows(TemplateException.class, () -> new Template(errorStream));
    }
}
