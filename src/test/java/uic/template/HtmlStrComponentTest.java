package uic.template;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HtmlStrComponentTest {
    @Test
    public void testButtonComponent() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
        Writer out = null;
        try {
            // step1 Get Template Path
            configuration.setDirectoryForTemplateLoading(new File("./templates"));

            // step2 Create data
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("component", "p-button");

            Map<String, Object> attributeMap = new HashMap<>();
            attributeMap.put("lable", "Operation: patient");
            dataMap.put("attributes", attributeMap);

            Map<String, Object> propertyBindings = new HashMap<>();
            propertyBindings.put("disabled", "unready1");
            dataMap.put("propertyBindings", propertyBindings);

            Map<String, Object> eventBindings = new HashMap<>();
            eventBindings.put("onClick", "handleClick");
            dataMap.put("onClick", eventBindings);
            dataMap.put("endTag", true);

            // step3 Get Template
            Template template = configuration.getTemplate("html-str-component.html.ftl");

            // step5 Set Output
            out = new OutputStreamWriter(System.out);
            // step6 Process
            template.process(dataMap, out);

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

    @Test
    public void testInputDirective() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
        Writer out = null;
        try {
            // step1 Get Template Path
            configuration.setDirectoryForTemplateLoading(new File("./templates"));

            // step2 Create data
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("hostListener", "input");
            dataMap.put("directive", "pInputText");

            Map<String, Object> attributeMap = new HashMap<>();
            attributeMap.put("placeholder", "name1");
            dataMap.put("attributes", attributeMap);

            dataMap.put("ngModel", "value1");

            // step3 Get Template
            Template template = configuration.getTemplate("html-str-component.html.ftl");

            // step5 Set Output
            out = new OutputStreamWriter(System.out);
            // step6 Process
            template.process(dataMap, out);

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

    @Test
    public void testCardComponent() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
        Writer out = null;
        try {
            // step1 Get Template Path
            configuration.setDirectoryForTemplateLoading(new File("./templates"));

            // step2 Create data
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("component", "p-card");

            List<String> interpolations = new ArrayList<>();
            interpolations.add("_content");
            dataMap.put("interpolations", interpolations);
            dataMap.put("endTag", true);

            dataMap.put("attributes", new HashMap<>());

            // step3 Get Template
            Template template = configuration.getTemplate("html-str-component.html.ftl");

            // step5 Set Output
            out = new OutputStreamWriter(System.out);
            // step6 Process
            template.process(dataMap, out);

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
