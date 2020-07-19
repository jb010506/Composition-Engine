package selab.ui_composite_engine.operator;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.jsoup.helper.StringUtil;
import selab.ui_composite_engine.component.CompositeComponent;
import selab.ui_composite_engine.component.RenderingComponent;
import selab.ui_composite_engine.metadata.UicMetadata;
import selab.ui_composite_engine.uic.loader.PrimeNgLoader;
import selab.ui_composite_engine.util.FreeMarkerUtil;
import selab.ui_composite_engine.renderer.NgWebAppRenderer;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

public class NgOperationStrcombOperator extends CompositeOperator {

    private static final String htmlTemplateName = "ng-operation-strcomb-operator.html.ftl";
    private static final String tsTemplateName = "ng-operation-strcomb-operator.ts.ftl";

    List<String> htmlStrList = new ArrayList<>();
    List<String> cssStrList = new ArrayList<>();
    Map<String, String> tsInputInitMap = new LinkedHashMap<>();
    Map<String, String> tsNgModelInitMap = new LinkedHashMap<>();
    Map<String, String> tsNgModelParamMap = new LinkedHashMap<>();
    Map<String, String> tsAttributeStringMap = new LinkedHashMap<>();


    @Override
    public void operate(CompositeComponent compositeComponent){
        List<RenderingComponent> renderingComponents = compositeComponent.getAllSubComponents();
        UicMetadata metadata = compositeComponent.getUicMetadata();
        metadata.setSelectorName(compositeComponent.getName());

        for(RenderingComponent component: renderingComponents){
            UicMetadata subMetadata = component.getUicMetadata();
            metadata.addSubMetadata(subMetadata);
            this.htmlStrList.add(subMetadata.getHtmlStr());
            this.cssStrList.add(subMetadata.getCssStr());
            this.tsNgModelInitMap.putAll(subMetadata.getTsNgModelInitMap());
            this.tsInputInitMap.putAll(subMetadata.getTsInputInitMap());
            this.tsNgModelParamMap.putAll(subMetadata.getTsNgModelParamMap());
            this.tsAttributeStringMap.putAll(subMetadata.getTsAttributeStringMap());
        }
        metadata.setNgType("component");
        String name = compositeComponent.getName().toLowerCase().replace(".", "");
        metadata.setSelectorName(name);
        metadata.setPathName(name);
        metadata.setClassName(name.substring(0,1).toUpperCase() + name.substring(1));
    }

    @Override
    public void render(RenderingComponent renderingComponent) {

        UicMetadata uicMetadata = renderingComponent.getUicMetadata();

        // Html File
        Template template = FreeMarkerUtil.getInstance().getTemplate(htmlTemplateName);
        Writer stringWriter = new StringWriter();

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("htmlStrList", htmlStrList);

        if(uicMetadata.isHandleClickNeeded()){ // Use p-button as default to handle
            dataMap.put("handleClickLabel", renderingComponent.getName());
            uicMetadata.addSubMetadata(PrimeNgLoader.getButtonUic());
        }

        try {
            template.process(dataMap, stringWriter);
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String htmlStr = stringWriter.toString().trim()
                .replaceAll(" +", " ");
        uicMetadata.setHtmlStr(htmlStr);
        NgWebAppRenderer.getInstance().exportComponentHtml(htmlStr, uicMetadata.getPathName());


        // Ts File
        template = FreeMarkerUtil.getInstance().getTemplate(tsTemplateName);
        stringWriter = new StringWriter();

        dataMap = new HashMap<>();
        dataMap.put("selectorName", uicMetadata.getSelectorName());
        dataMap.put("pathName", uicMetadata.getPathName());
        dataMap.put("className", uicMetadata.getClassName());
        dataMap.put("operationName", renderingComponent.getName());
        dataMap.put("tsAttributeStringMap", tsAttributeStringMap);
        dataMap.put("tsInputInitMap", tsInputInitMap);
        dataMap.put("tsNgModelInitMap", tsNgModelInitMap);
        dataMap.put("tsNgModelParamMap", tsNgModelParamMap);

        try {
            template.process(dataMap, stringWriter);
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String tsStr = stringWriter.toString().trim()
                .replaceAll(" +", " ");

        uicMetadata.setTsStr(tsStr);
        NgWebAppRenderer.getInstance().exportComponentTs(tsStr, uicMetadata.getPathName());

        // Css File
        String cssStr = StringUtil.join(this.cssStrList, "\n");
        uicMetadata.setCssStr(cssStr);
        NgWebAppRenderer.getInstance().exportComponentCss(cssStr, uicMetadata.getPathName());

        // Ts Dependencies
        NgWebAppRenderer.getInstance().addAllComponentTsDependency(uicMetadata.getTsDependency().getNodeModulesModules());

    }
}
