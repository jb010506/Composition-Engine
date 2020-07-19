package selab.ui_composite_engine.configurer.porttype;

import selab.ui_composite_engine.component.OperationComponent;
import selab.ui_composite_engine.component.PageComponent;
import selab.ui_composite_engine.metadata.UicMetadata;

import java.util.HashMap;
import java.util.Map;

public class OutputPortConfigurerVisitor extends NgConfigurerVisitor {

    private final static String configTemplateName = "ng-config.porttype.ts.ftl";
    private final static String className = "OutputPort";
    private final static String pathName = "output-port";


    public void visit(PageComponent component){
        UicMetadata metadata = component.getUicMetadata();
        super.getDeclarationComponents().add(metadata.getClassName());
        super.getDependencyClassNames().add(metadata.getClassName()  + "Component");

        Map<String, String> pageRouting = new HashMap<>();
        if(metadata.isMainComponent()){
            pageRouting.put("path", "");
        }else{
            pageRouting.put("path", metadata.getPathName());
        }
        pageRouting.put("component", metadata.getClassName());
        pageRouting.put("outlet", "outputPort");
        super.getRoutings().add(pageRouting);
        super.getModuleImports().addAll(metadata.getModuleDependency().getModuleModules());
        super.getDependencyClassNames().addAll(metadata.getModuleDependency().getModuleModules());


        super.visit(component);
    }

    public void visit(OperationComponent component){
        UicMetadata metadata = component.getUicMetadata();
        this.getDeclarationComponents().add(metadata.getClassName());
        this.getDependencyClassNames().add(metadata.getClassName() + "Component");
        this.getEntryComponents().add(metadata.getClassName());
        this.getModuleImports().addAll(metadata.getModuleDependency().getModuleModules());
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
