package uic.template;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;
import selab.ui_composite_engine.util.FreeMarkerUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HtmlStrTemplateTest {
    @Test
    public void testHtmlTemplate() {
        Template template = FreeMarkerUtil.getInstance().getTemplate("ng-basic-template-operator.html.ftl");
        Writer stringWriter = new StringWriter();

        Map<String, Object> dataMap = new HashMap<>();
        List<String> htmlStrList = new ArrayList<>();

        htmlStrList.add("<input pInputText [placeholder]=\"placeHolder1\" [(ngModel)]=\"value1\" [ngModelOptions]=\"{standalone:\n" +
                "   true}\">");
        htmlStrList.add("<input pInputText [placeholder]=\"placeHolder2\" [(ngModel)]=\"value2\" [ngModelOptions]=\"{standalone:\n" +
                "   true}\">");
        htmlStrList.add("<p-button [label]=\"innerText1\" (onClick)=\"handleClick()\" [disabled]=\"unready\"></p-button>");
        dataMap.put("htmlStrList", htmlStrList);


        try {
            template.process(dataMap, stringWriter);
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String htmlStr = stringWriter.toString().trim()
                .replaceAll(" +", " ");
//                .replace("\n","");
        System.out.println(htmlStr);

    }

}
