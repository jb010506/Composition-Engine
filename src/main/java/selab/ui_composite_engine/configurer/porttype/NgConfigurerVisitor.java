package selab.ui_composite_engine.configurer.porttype;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import selab.ui_composite_engine.component.PageComponent;
import selab.ui_composite_engine.metadata.PackageDependency;
import selab.ui_composite_engine.renderer.NgWebAppRenderer;
import selab.ui_composite_engine.configurer.ConfigurerVisitor;
import selab.ui_composite_engine.util.FreeMarkerUtil;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

abstract class NgConfigurerVisitor extends ConfigurerVisitor {
    private HashSet<String> dependencyClassNames = new HashSet<>();
    private HashSet<String> declarationComponents = new HashSet<>();
    private HashSet<String> entryComponents = new HashSet<>();
    private HashSet<String> moduleImports = new HashSet<>();
    private List<Map<String, String>> routings = new ArrayList<>();
    private Set<String> tsStyles = new HashSet<>();

    private PackageDependency packageDependency;


    protected Map<String, Object> getTemplateDataMap(){
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("dependencyClassNames", dependencyClassNames);
        dataMap.put("declarationComponents", declarationComponents);
        dataMap.put("entryComponents", entryComponents);
        dataMap.put("moduleImports", moduleImports);
        dataMap.put("routings", routings);
        dataMap.put("className", this.getClassName());
        return dataMap;
    }

    public void visit(PageComponent component){
        this.packageDependency = component.getUicMetadata().getPackageDependency();
        this.tsStyles = component.getUicMetadata().getTsDependency().getNodeModulesStyles();
        super.visit(component);
    }

    public void process(){
        Template template = FreeMarkerUtil.getInstance().getTemplate(this.getConfigTemplateName());
        Writer stringWriter = new StringWriter();

        Map<String, Object> dataMap = this.getTemplateDataMap();

        try {
            template.process(dataMap, stringWriter);
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String str = stringWriter.toString().trim()
                .replaceAll(" +", " ");

        NgWebAppRenderer.getInstance().exportPortTypeConfig(str, getPathName(), getClassName());

        NgWebAppRenderer.getInstance().addPackageDependencies(packageDependency);
        NgWebAppRenderer.getInstance().addNgStyles(this.tsStyles);

    }

    public HashSet<String> getDependencyClassNames() {
        return dependencyClassNames;
    }

    public HashSet<String> getDeclarationComponents() {
        return declarationComponents;
    }

    public HashSet<String> getEntryComponents() {
        return entryComponents;
    }

    public HashSet<String> getModuleImports() {
        return moduleImports;
    }

    public List<Map<String, String>> getRoutings() {
        return routings;
    }

    public abstract String getConfigTemplateName();

    public abstract String getClassName();

    public abstract String getPathName();
}
