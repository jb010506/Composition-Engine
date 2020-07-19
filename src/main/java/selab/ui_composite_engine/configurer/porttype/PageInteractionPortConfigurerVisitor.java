package selab.ui_composite_engine.configurer.porttype;

import selab.ui_composite_engine.component.OperationComponent;
import selab.ui_composite_engine.metadata.UicMetadata;

public class PageInteractionPortConfigurerVisitor extends NgConfigurerVisitor {

    private final static String configTemplateName = "ng-config.porttype.ts.ftl";
    private final static String className = "PageInteractionPort";
    private final static String pathName = "page-interaction-port";

    public void visit(OperationComponent component){
        UicMetadata metadata = component.getUicMetadata();
        this.getDeclarationComponents().add(metadata.getClassName());
        this.getDependencyClassNames().add(metadata.getClassName() + "Component");
        this.getModuleImports().addAll(metadata.getModuleDependency().getModuleModules());  //module (ts_dependency)
        this.getDependencyClassNames().addAll(metadata.getModuleDependency().getModuleModules());

        super.visit(component);
    }


    @Override
    public String getConfigTemplateName() {
        return configTemplateName;
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
