package selab.ui_composite_engine.configurer.porttype;

import selab.ui_composite_engine.component.PageComponent;
import selab.ui_composite_engine.metadata.UicMetadata;

import java.util.HashMap;
import java.util.Map;

public class NavigationPortConfigurerVisitor extends NgConfigurerVisitor {

    private final static String configTemplateName = "ng-config.porttype.ts.ftl";
    private final static String className = "NavigationPort";
    private final static String pathName = "navigation-port";

    public void visit(PageComponent component){
        UicMetadata metadata = component.getUicMetadata();
        super.getDependencyClassNames().add(metadata.getClassName() + "Component");

        Map<String, String> pageRouting = new HashMap<>();
        pageRouting.put("path", metadata.getPathName());
        pageRouting.put("component", metadata.getClassName());
        super.getRoutings().add(pageRouting);

        super.visit(component);
    }

    public String getConfigTemplateName() {
        return configTemplateName;
    }

    public String getClassName() {
        return className;
    }

    public String getPathName() {
        return pathName;
    }

}
