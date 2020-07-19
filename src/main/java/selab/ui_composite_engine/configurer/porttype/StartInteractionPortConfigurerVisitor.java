package selab.ui_composite_engine.configurer.porttype;

import selab.ui_composite_engine.component.PageComponent;
import selab.ui_composite_engine.metadata.UicMetadata;

import java.util.HashMap;
import java.util.Map;

public class StartInteractionPortConfigurerVisitor extends NgConfigurerVisitor {

    private final static String configTemplates = "ng-config.porttype.ts.ftl";
    private final static String className = "StartInteractionPort";
    private final static String pathName = "start-interaction-port";

    public void visit(PageComponent component){
        UicMetadata metadata = component.getUicMetadata();
        if(metadata.isMainComponent()) {
            this.getDeclarationComponents().add(metadata.getClassName());
            this.getDependencyClassNames().add(metadata.getClassName() + "Component");

            Map<String, String> pageRouting = new HashMap<>();
            pageRouting.put("path", "main");
            pageRouting.put("component", metadata.getClassName());
            this.getRoutings().add(pageRouting);
        }

        super.visit(component);
    }


    @Override
    public String getConfigTemplateName() {
        return configTemplates;
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public String getPathName() {
        return pathName;
    }
}
