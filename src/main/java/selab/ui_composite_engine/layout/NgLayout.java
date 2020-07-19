package selab.ui_composite_engine.layout;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import selab.ui_composite_engine.model.LayoutWrapper;
import selab.ui_composite_engine.renderer.NgWebAppRenderer;
import selab.ui_composite_engine.model.LDLWrapper;
import selab.ui_composite_engine.util.FreeMarkerUtil;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;
import java.util.logging.Logger;

public class NgLayout implements Layout{

    private static final Logger LOG = Logger.getLogger(NgLayout.class.getName());

    private final static String configTemplateName = "ng-config.layout.ts.ftl";

    private String layoutName;
    private LayoutWrapper ldlWrapper;

    private Map<String, Object> dataMap = new HashMap<>();
    private List<String> declarationComponents = new ArrayList<>();
    private List<String> entryComponents = new ArrayList<>();
    private List<String> moduleImports = new ArrayList<>();
    private List<Map<String, String>> routings = new ArrayList<>();
    private List<String> dependencyClassNames = new ArrayList<>();

    public NgLayout(String layoutName, LayoutWrapper ldlWrapper) {
        this.layoutName = layoutName;
        this.ldlWrapper = ldlWrapper;
    }

    @Override
    public void render() {
        NgWebAppRenderer writer = NgWebAppRenderer.getInstance();

        try {
            writer.buildLayout(this.ldlWrapper.getLayoutPath(),"");
            writer.buildLayoutAssets(this.ldlWrapper.getAssetsPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Template template = FreeMarkerUtil.getInstance().getTemplate(configTemplateName);
        Writer stringWriter = new StringWriter();

        this.process();

        try{
            template.process(dataMap, stringWriter);
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String str = stringWriter.toString().trim()
                .replaceAll(" +", " ");
        writer.exportLayoutConfig(str);

        // Ts Dependencies
        try {
            writer.exportLayoutTsDependencies(
                    this.ldlWrapper.getTsDependency().getNodeModulesComponents(),
                    this.ldlWrapper.getTsDependency().getTsImportPaths());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Package Dependencies
        writer.addPackageDependencies(ldlWrapper.getPackageDependency());

        // Styles Dependencies
        writer.addNgStyles(ldlWrapper.getTsDependency().getNodeModulesStyles());


        Set<String> srcs =new HashSet<>();
        srcs.add("/Users/william/Desktop/composite_UI_engine/test_data/LDL/sample2/layout/scss/style.scss");



        // Copy Scss Imports Files
        try {
            //writer.addScssFiles(ldlWrapper.getUicStyles().getScssImportsFilePaths());
            writer.moveScssFiles(this.ldlWrapper.getLayoutPath()+"/scss");
        } catch (IOException e) {
            e.printStackTrace();
        }




    }

    private void process(){

        this.declarationComponents.addAll(this.ldlWrapper.getDeclarationComponentNames());
        this.declarationComponents.addAll(this.ldlWrapper.getEntryComponentNames());

        Set<String> unprocessedDependencyClassNames = new HashSet<>();
        unprocessedDependencyClassNames.addAll(this.ldlWrapper.getDeclarationComponentNames());
        unprocessedDependencyClassNames.addAll(this.ldlWrapper.getEntryComponentNames());

        for(String componentName: unprocessedDependencyClassNames){
            this.dependencyClassNames.add(componentName + "Component");
        }

        this.moduleImports.addAll(this.ldlWrapper.getModuleDependency().getModuleModules());
        for(String moduleName: this.ldlWrapper.getModuleDependency().getModuleModules()){
            this.dependencyClassNames.add(moduleName.replace(".forRoot()",""));
        }

        this.routings.addAll(this.ldlWrapper.getComponentRoutings());

        this.dataMap.put("dependencyClassNames", dependencyClassNames);
        this.dataMap.put("declarationComponents", declarationComponents);
        this.dataMap.put("entryComponents", entryComponents);
        this.dataMap.put("moduleImports", moduleImports);
        this.dataMap.put("routings", routings);
    }
}
