package selab.ui_composite_engine.operator;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import selab.ui_composite_engine.component.CompositeComponent;
import selab.ui_composite_engine.component.RenderingComponent;
import selab.ui_composite_engine.metadata.UicMetadata;
import selab.ui_composite_engine.util.FreeMarkerUtil;
import selab.ui_composite_engine.util.PathUtil;
import selab.ui_composite_engine.renderer.NgWebAppRenderer;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

public class NgFormTemplateOperator extends CompositeOperator{
    private String htmlTemplateName = "ng-form-template-operator.html.ftl";
    private String tsTemplateName = "ng-form-template-operator.ts.ftl";
    private String tsServiceName = "ng-form-template-operator.service.ftl";

    private Map<String, String> subComponentSelectorNameMap = new LinkedHashMap<>();
    private List<String>  subComponentNames = new ArrayList<>();

    @Override
    public void operate(CompositeComponent compositeComponent) {
        UicMetadata metadata = compositeComponent.getUicMetadata();

        List<RenderingComponent> renderingComponents = compositeComponent.getAllSubComponents();
        for(RenderingComponent component: renderingComponents){
            UicMetadata subMetadata = component.getUicMetadata();
            metadata.addSubMetadata(subMetadata);

            subComponentSelectorNameMap.put(subMetadata.getSelectorName(), component.getName());
            subComponentNames.add(component.getName());
        }

        metadata.setNgType("component");
        String componentName = getComponentName(compositeComponent);
        String name = componentName.toLowerCase().replace(".", "");
        metadata.setSelectorName(name);
        metadata.setPathName(name);
        metadata.setClassName(name.substring(0,1).toUpperCase() + name.substring(1));

    }

    private String getComponentName(CompositeComponent compositeComponent) {
        String name = compositeComponent.getName();
        String[] nameSplitted = name.split("/");
        return PathUtil.getFileNameWithoutExt(nameSplitted[nameSplitted.length - 1]);
    }

    @Override
    public void render(RenderingComponent renderingComponent) {

        UicMetadata uicMetadata = renderingComponent.getUicMetadata();

        // Html File
        Template template = FreeMarkerUtil.getInstance().getTemplate(htmlTemplateName);
        Writer stringWriter = new StringWriter();

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("subComponentSelectorNameMap", subComponentSelectorNameMap);

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


        // TS File
        template = FreeMarkerUtil.getInstance().getTemplate(tsTemplateName);
        stringWriter = new StringWriter();
        dataMap = new HashMap<>();

        dataMap.put("selectorName", uicMetadata.getSelectorName());
        dataMap.put("pathName", uicMetadata.getPathName());
        dataMap.put("className", uicMetadata.getClassName());
        dataMap.put("subComponentNames", subComponentNames);

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

        // TS Service
        template = FreeMarkerUtil.getInstance().getTemplate(tsServiceName);
        stringWriter = new StringWriter();
        dataMap = new HashMap<>();

        dataMap.put("className", uicMetadata.getClassName());
        dataMap.put("portTypeNamespace", uicMetadata.getInfoAsString("portTypeNamespace"));
        dataMap.put("portTypeLocalName", "pageInteractionPort");

        dataMap.put("partnerLink", uicMetadata.getInfoAsString("partnerLink"));
        if(uicMetadata.isMainComponent()){
            dataMap.put("startOperation", "start");
        }else{
            dataMap.put("startOperation", renderingComponent.getName());
        }

        try {
            template.process(dataMap, stringWriter);
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String serviceStr = stringWriter.toString().trim()
                .replaceAll(" +", " ");
        uicMetadata.setServiceStr(serviceStr);
        NgWebAppRenderer.getInstance().exportComponentService(serviceStr, uicMetadata.getPathName());

        // Css File
        NgWebAppRenderer.getInstance().exportComponentCss("", uicMetadata.getPathName());

        // Ts Dependencies
        NgWebAppRenderer.getInstance().addAllComponentTsDependency(uicMetadata.getTsDependency().getNodeModulesModules());
    }
}
