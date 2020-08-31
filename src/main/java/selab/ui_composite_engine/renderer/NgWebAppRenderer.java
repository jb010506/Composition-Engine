package selab.ui_composite_engine.renderer;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
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


    private String webAppPath; // File Path to generate webapp NG source code
    private String webAppName; // WebApp Name, which is the lowercase of bpel-wsdl name

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
        NgWebAppRenderer.instance = this;


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
        String htmlStr = stringWriter.toString().trim();
        FileUtils.writeStringToFile(new File(PathUtil.combinePath( Configuration.WEBAPP_DIR_PATH,
                selector, selector+".component.html")), htmlStr);

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

    public void exportAppModules(Map<Object, Object> pageSelectorMap, Map<Object,Object>components, Map<Object,Object>selectorPageMap) throws IOException, TemplateException {
        Map<Object,Object> dataMap = new HashMap<>();
        dataMap.put("pages", pageSelectorMap);
        dataMap.put("components", components);
        dataMap.put("selectorPageMap", selectorPageMap);
        Template template = FreeMarkerUtil.getInstance().getTemplate("app.module.ts.ftl");
        Writer stringWriter = new StringWriter();
        template.process(dataMap, stringWriter);
        String str = stringWriter.toString().trim();
        FileUtils.writeStringToFile(new File(PathUtil.combinePath( Configuration.WEBAPP_DIR_PATH,
                "app.module.ts")), str);
    }

    public void buildNgLayout(String i) throws IOException {
        // default = prime
        if(i.equals("")) i = "Prime";
        File srcDir = new File(Configuration.ANGULAR_SRC_DIR_PATH+i);
        File desDir = new File(this.webAppPath);
        FileUtils.copyDirectory(srcDir, desDir);
    }


    public void exportLayoutHTML(JSONObject layout) throws IOException, TemplateException {
        JSONArray header = layout.getJSONArray("header");
        JSONArray footer = layout.getJSONArray("footer");
        JSONArray asidebar = layout.getJSONArray("asidebar");
        ArrayList<JSONObject>headerItems = new ArrayList<>();
        ArrayList<JSONObject>footerItems = new ArrayList<>();
        ArrayList<JSONObject>asidebarItems = new ArrayList<>();
        this.buildHeaderItems(header,headerItems);
        this.buildFooterItems(footer,footerItems);
        this.buildAsidebarItems(asidebar, asidebarItems);

        Map<Object,Object> dataMap = new HashMap<>();
        dataMap.put("headerItems",headerItems);
        dataMap.put("footerItems", footerItems);
        dataMap.put("asidebarItems", asidebarItems);
        Template template = FreeMarkerUtil.getInstance().getTemplate("default-layout.component.html.ftl");
        Writer stringWriter = new StringWriter();
        template.process(dataMap, stringWriter);
        String str = stringWriter.toString().trim();
        FileUtils.writeStringToFile(new File(PathUtil.combinePath( Configuration.WEBAPP_DEFAULT_LAYOUT,
                "default-layout.component.html")), str);
    }

    public void exportLayoutTS(JSONObject layout) throws IOException, TemplateException {
        JSONArray sidebar = layout.getJSONArray("sidebar");
        String sidebarItems = this.buildSidebarItems(sidebar);

        Map<Object,Object> dataMap = new HashMap<>();
        dataMap.put("items", sidebarItems);
        Template template = FreeMarkerUtil.getInstance().getTemplate("default-layout.component.ts.ftl");
        Writer stringWriter = new StringWriter();
        template.process(dataMap, stringWriter);
        String str = stringWriter.toString().trim();
        FileUtils.writeStringToFile(new File(PathUtil.combinePath( Configuration.WEBAPP_DEFAULT_LAYOUT,
                "default-layout.component.ts")), str);

    }

    public void buildHeaderItems(JSONArray header, ArrayList<JSONObject>headerItems){
        for(int i=0;i<header.length();i++){
            JSONObject item = new JSONObject();
            item.put("text",header.getJSONObject(i).getString("text"));
            item.put("href",header.getJSONObject(i).getString("href"));
            headerItems.add(item);
        }

    }

    public void buildFooterItems(JSONArray footer, ArrayList<JSONObject>footerItems){
        for(int i=0;i<footer.length();i++){
            JSONObject item = new JSONObject();
            item.put("text",footer.getJSONObject(i).getString("text"));
            item.put("href",footer.getJSONObject(i).getString("href"));
            footerItems.add(item);
        }
    }

    public String buildSidebarItems(JSONArray sidebar){
        String items = "";
        for(int i=0;i<sidebar.length();i++){
            items += "{ name:\"" + sidebar.getJSONObject(i).getString("text") + "\", url: \"/"
                    + sidebar.getJSONObject(i).getString("href") + "\"},";
        }
        return items;
    }

    public void buildAsidebarItems(JSONArray asidebar, ArrayList<JSONObject>asidebarItems){
        for(int i=0;i<asidebar.length();i++){
            JSONObject item = new JSONObject();
            item.put("text",asidebar.getJSONObject(i).getString("text"));
            item.put("href",asidebar.getJSONObject(i).getString("href"));
            asidebarItems.add(item);
        }
    }

    public void exportNavigation(String ndl) throws IOException, TemplateException {
        JSONObject navigation = new JSONObject(ndl);
        JSONObject routes = new JSONObject();
        ArrayList<String> components = new ArrayList<>();
        Map<Object,Object> dataMap = new HashMap<>();
        getComponents(navigation,components, routes);
        System.out.println(components);
        System.out.println(ndl);
        dataMap.put("ndl", navigation);
        dataMap.put("components", components);

        Template template = FreeMarkerUtil.getInstance().getTemplate("app.routing.ts.ftl");
        Writer stringWriter = new StringWriter();
        template.process(dataMap, stringWriter);
        String str = stringWriter.toString().trim();
        FileUtils.writeStringToFile(new File(PathUtil.combinePath( Configuration.WEBAPP_DIR_PATH,
                "app.routing.ts")), str);
    }

    public void getComponents(JSONObject navigation, ArrayList<String> components, JSONObject routes){
        if(!navigation.getString("component").toLowerCase().startsWith("default")) {
            components.add(navigation.getString("component"));
        }
        if(navigation.getJSONArray("children").length()>0) {
            JSONArray children = navigation.getJSONArray("children");
            for(int i=0;i<children.length();i++){
                getComponents(children.getJSONObject(i), components, routes);
            }
        }
    }
}
