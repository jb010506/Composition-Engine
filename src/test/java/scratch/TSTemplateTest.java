package scratch;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.File;
import java.util.HashMap;
import java.util.Map;



public class TSTemplateTest {

    @Test
    public void testMain() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
        Writer out = null;
        try {
            // step1 Get Template Path
            configuration.setDirectoryForTemplateLoading(new File("./test_template"));
//            configuration.setTemplateLoader(new ClassTemplateLoader(MainTS.class, "/draft"));
            // step2 Create data
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("ComponenetName", "MainComponent");
            // step3 Get Template
            Template template = configuration.getTemplate("page-component.ts.ftl");

            // step5 Set Output
//            File docFile = new File(CLASS_PATH + "\\" + "AutoCodeDemo.java");
//            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));
            out = new OutputStreamWriter(System.out);
            // step6 Process
            template.process(dataMap, out);

            System.out.println("AutoCodeDemo.java success!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.flush();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
