package selab.ui_composite_engine.renderer;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import selab.ui_composite_engine.Configuration;
import selab.ui_composite_engine.metadata.PackageDependency;
import selab.ui_composite_engine.util.FreeMarkerUtil;
import selab.ui_composite_engine.util.PathUtil;

import java.io.*;
import java.util.*;

public class NgWebAppRenderer {
    private final static String COMPONENTS_OUTPUT_PATH = "src/app/components";
    private final static String LAYOUT_OUTPUT_PATH = "src/app/layout";
    private final static String SCSS_OUTPUT_PATH = "src/scss";

    // Singleton
    private static NgWebAppRenderer instance = null;

    public static NgWebAppRenderer getInstance(){
        if(instance == null)
            throw new NullPointerException("NgWebAppRenderer is not initialized yet!");
        return instance;
    }

    /**
     * The instance should be initialized before calling getInstance()
     * The difference from classical singleton pattern
     */
    public NgWebAppRenderer(String webAppPath, String webAppName) throws IOException {
        this.webAppName = webAppName;

        this.webAppPath = PathUtil.combinePath(webAppPath, this.webAppName);
        this.buildNgFramework();
        NgWebAppRenderer.instance = this;

        this.bpelPath = "";
        this.wsdlPath = "";

    }


    // All the typescript dependency paths, e.x. import/export {*} from $dependency_path
    private HashSet<String> componentsTsDependencies = new HashSet<>();

    // All the classNames and classPaths for portTypeConfig
    // e.x  0 = {HashMap$Node@2153} "pathName" -> "navigation-port"
    //      1 = {HashMap$Node@2154} "className" -> "NavigationPort"
    private List<Map<String, String>> portTypeConfigs = new ArrayList<>();

    // All the node package Dependencies that need to be imported in package.json
    private Map<String, String> npmDependencies = new HashMap<>();
    private Map<String, String> npmDevDependencies = new HashMap<>();

    // All the styles that need to be imported in angular.json
    private Set<String> ngStyles = new LinkedHashSet<>();

    // All the scss style files that need to be imported in style.scss
    private Set<String> scssImports = new LinkedHashSet<>();

    private String webAppPath; // File Path to generate webapp NG source code
    private String webAppName; // WebApp Name, which is the lowercase of bpel-wsdl name

    private String bpelPath; // The path to the bpel file
    private String wsdlPath; // The path to the wsdl file

    //

    /**
     * Copy angular framework source code to target path
     */
    private void buildNgFramework() throws IOException {
        File srcDir = new File(Configuration.ANGULAR_SRC_DIR_PATH);
        File desDir = new File(this.webAppPath);
        FileUtils.copyDirectory(srcDir, desDir);
    }

    /**
     * Copy layout source code to target path
     */
    public void buildLayout(String layoutPath, String i) throws IOException {
        System.out.println(this.webAppPath + " "+ this.LAYOUT_OUTPUT_PATH+i);
        File srcDir = new File(layoutPath);
        File desDir = new File(PathUtil.combinePath(this.webAppPath, this.LAYOUT_OUTPUT_PATH+i));
        FileUtils.copyDirectory(srcDir, desDir);
    }

    /**
     * Copy layout assets source code to target path
     */
    public void buildLayoutAssets(String assetsPath) throws IOException {
        File srcDir = new File(assetsPath);
        File desDir = new File(PathUtil.combinePath(this.webAppPath, "src/assets"));
        FileUtils.copyDirectory(srcDir, desDir);
    }

    /**
     * Export html file for UI Components
     */
    public void exportComponentHtml(String htmlStr, String pathName){
        try {
            FileUtils.writeStringToFile(new File(PathUtil.combinePath(webAppPath, COMPONENTS_OUTPUT_PATH,
                    pathName, pathName +".component.html")), htmlStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Export typescript file for UI Components
     */
    public void exportComponentTs(String tsStr, String pathName) {
        try {
            FileUtils.writeStringToFile(new File(PathUtil.combinePath(webAppPath, COMPONENTS_OUTPUT_PATH,
                    pathName, pathName +".component.ts")), tsStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.componentsTsDependencies.add("./" + PathUtil.combinePath(pathName, pathName +".component"));
    }

    /**
     * Export typescript service file for UI Components
     */
    public void exportComponentService(String tsStr, String pathName) {
        try {
            FileUtils.writeStringToFile(new File(PathUtil.combinePath(webAppPath, COMPONENTS_OUTPUT_PATH,
                    pathName, pathName +".service.ts")), tsStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Export css file for UI Components
     */
    public void exportComponentCss(String tsStr, String pathName) {
        try {
            FileUtils.writeStringToFile(new File(PathUtil.combinePath(webAppPath, COMPONENTS_OUTPUT_PATH,
                    pathName, pathName +".component.css")), tsStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add typescript dependency paths (files), the program can find dependent classes/modules from these paths
     */
    public void addAllComponentTsDependency(Set<String> tsDependencies){
        for(String str: tsDependencies){
            if(!str.contains("@angular"))
                // Hot fix, UI Component shouldn't dependent on @angular
                // Otherwise, it will have class name confliction
                this.componentsTsDependencies.add(str);
            else if(str.contains("@angular/material")){
                this.componentsTsDependencies.add(str);
            }
        }
    }

    /**
     * Export PortTypeConfig (config.xxx-port.ts) string for portTypeVisitors
     */
    public void exportPortTypeConfig(String str, String pathName, String className) {
        try {
            FileUtils.writeStringToFile(new File(PathUtil.combinePath(webAppPath, "src/app",
                    "config." + pathName + ".ts")), str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, String> config = new HashMap<>();
        config.put("className", className);
        config.put("pathName", pathName);
        this.portTypeConfigs.add(config);
    }

    //

    /**
     * Export LayoutConfig string for layout
     * LayoutConfig is a single file (config.layout.ts)
     */
    public void exportLayoutConfig(String str) {
        try {
            FileUtils.writeStringToFile(new File(PathUtil.combinePath(webAppPath, "src/app",
                    "config.layout.ts")), str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Export layout dependencies to tsdependencies.ts
     */
    public void exportLayoutTsDependencies(Set<String> paths, Map<String, String> classPaths) throws IOException {
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("classPaths", classPaths);
        dataMap.put("paths", paths);
        this.exportTsDependencies(PathUtil.combinePath(webAppPath, LAYOUT_OUTPUT_PATH,
                "tsdependencies.ts"), dataMap);
    }

    /**
     * Add package dependencies, called by LayoutConfiger & PortTypeConfiger
     */
    public void addPackageDependencies(PackageDependency packageDependency) {
        this.npmDependencies.putAll(packageDependency.getNpmDependencies());
        this.npmDevDependencies.putAll(packageDependency.getNpmDevDependencies());
    }

    /**
     * Add styles' paths that would be included in Angular.json
     */
    public void addNgStyles(Set<String> ngStyles) {
        this.ngStyles.addAll(ngStyles);
    }

    /**
     * Add styles' "imported paths" that would be included in styles.scss
     */
    public void addScssStyles(Set<String> scssStyles){
        scssImports.addAll(scssStyles);
    }

    /**
     * Add styles' file paths that would be copied into webapp folder
     */
    public void addScssFiles(Set<String> scssImportsFilePaths) throws IOException {
        for(String filePath: scssImportsFilePaths){
            File srcFile = new File(filePath);
            File destFile = new File(PathUtil.combinePath(webAppPath, SCSS_OUTPUT_PATH, srcFile.getName()));
            FileUtils.copyFile(srcFile, destFile);
        }
    }

    public void moveScssFiles(String src) throws IOException {


        File srcFile = new File(src);
        File destFile = new File(PathUtil.combinePath(webAppPath, SCSS_OUTPUT_PATH));

        FileUtils.copyDirectory(srcFile, destFile);

    }

    public void flush() throws IOException {

        // Export config.webapp.ts
        Template template = FreeMarkerUtil.getInstance().getTemplate("ng-config.webapp.ts.ftl");
        Writer writer = new FileWriter(new File(PathUtil.combinePath(webAppPath, "src/app",
                "config.webapp.ts")));
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("wsdlPath", this.wsdlPath);
        dataMap.put("bpelPath", this.bpelPath);
        this.processTemplate(template, dataMap, writer);

        // Export app.config.ts
        template = FreeMarkerUtil.getInstance().getTemplate("ng-app.config.ts.ftl");
        writer = new FileWriter(new File(PathUtil.combinePath(webAppPath, "src/app",
                "app.config.ts")));
        dataMap = new HashMap<>();
        dataMap.put("configs", this.portTypeConfigs);
        this.processTemplate(template, dataMap, writer);

        // Export components tsdependencies  (Import & Export Paths)
        dataMap = new HashMap<>();
        dataMap.put("paths", this.componentsTsDependencies);
        exportTsDependencies(PathUtil.combinePath(webAppPath, COMPONENTS_OUTPUT_PATH,
                "tsdependencies.ts"), dataMap);

        // Export package.json
        template = FreeMarkerUtil.getInstance().getTemplate("ng-package.json.ftl");
        writer = new FileWriter(new File(PathUtil.combinePath(webAppPath,
                "package.json")));
        dataMap = new HashMap<>();
        dataMap.put("dependencies", this.npmDependencies);
        dataMap.put("devDependencies", this.npmDevDependencies);
        dataMap.put("webAppName", webAppName);
        this.processTemplate(template, dataMap, writer);

        // Export angular.json
        template = FreeMarkerUtil.getInstance().getTemplate("ng-angular.json.ftl");
        writer = new FileWriter(new File(PathUtil.combinePath(webAppPath,
                "angular.json")));
        dataMap = new HashMap<>();
        dataMap.put("styles", this.ngStyles);
        dataMap.put("webAppName", webAppName);
        this.processTemplate(template, dataMap, writer);


    }

    private void processTemplate(Template template, Map<String, Object> dataMap, Writer writer){
        try {
            template.process(dataMap, writer);
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exportTsDependencies(String path, HashMap<String, Object> dataMap) throws IOException {
        Template template = FreeMarkerUtil.getInstance().getTemplate("ng-exports.ts.ftl");
        Writer writer = new FileWriter(new File(path));
        this.processTemplate(template, dataMap, writer);
    }



    public void exportPageComponentTS(String selector) throws IOException, TemplateException {
        Map<Object,Object> dataMap = new HashMap<>();
        String export_name = selector.substring(0,1).toUpperCase()+ selector.substring(1)+"Component";
        dataMap.put("selector",selector);
        dataMap.put("export_name",export_name);
        Template template = FreeMarkerUtil.getInstance().getTemplate("component.ts.ftl");
        Writer stringWriter = new StringWriter();
        template.process(dataMap, stringWriter);
        String tsStr = stringWriter.toString().trim();
        FileUtils.writeStringToFile(new File(PathUtil.combinePath( Configuration.WEBAPP_DIR_PATH,
                selector, selector+".component.ts")), tsStr);
    }

    public void exportPageComponentHTML(String selector, List<String>components) throws IOException, TemplateException {
        Map<Object,Object> dataMap = new HashMap<>();
        dataMap.put("components",components);
        Template template = FreeMarkerUtil.getInstance().getTemplate("page-component.html.ftl");
        Writer stringWriter = new StringWriter();
        template.process(dataMap, stringWriter);
        String tsStr = stringWriter.toString().trim();
        FileUtils.writeStringToFile(new File(PathUtil.combinePath( Configuration.WEBAPP_DIR_PATH,
                selector, selector+".component.html")), tsStr);

    }

    public void exportComponentTS(String page,String selector) throws IOException, TemplateException {
        Map<Object,Object> dataMap = new HashMap<>();
        String export_name = selector.substring(0,1).toUpperCase()+ selector.substring(1)+"Component";
        dataMap.put("selector",selector);
        dataMap.put("export_name",export_name);
        Template template = FreeMarkerUtil.getInstance().getTemplate("component.ts.ftl");
        Writer stringWriter = new StringWriter();
        template.process(dataMap, stringWriter);
        String tsStr = stringWriter.toString().trim();
        FileUtils.writeStringToFile(new File(PathUtil.combinePath( Configuration.WEBAPP_DIR_PATH,
                page, selector, selector+".component.ts")), tsStr);
    }



    public void exportComponentHTML(String page,String selector,String html) throws IOException {
        FileUtils.writeStringToFile(new File(PathUtil.combinePath( Configuration.WEBAPP_DIR_PATH,
                page, selector,selector+".component.html")), html);
    }

    public void exportAppModules(String page_capitalized, String page, Map<Object,Object>components) throws IOException, TemplateException {
        Map<Object,Object> dataMap = new HashMap<>();
        dataMap.put("page_capitalized", page_capitalized);
        dataMap.put("page", page);
        dataMap.put("components", components);
        Template template = FreeMarkerUtil.getInstance().getTemplate("app.module.ts.ftl");
        Writer stringWriter = new StringWriter();
        template.process(dataMap, stringWriter);
        String str = stringWriter.toString().trim();
        FileUtils.writeStringToFile(new File(PathUtil.combinePath( Configuration.WEBAPP_DIR_PATH,
                "app.module.ts")), str);
    }


}
