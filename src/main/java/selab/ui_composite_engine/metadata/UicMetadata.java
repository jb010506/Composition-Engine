package selab.ui_composite_engine.metadata;

import com.google.gson.JsonObject;

import java.util.HashMap;

public class UicMetadata {

    private String portType;

    // Dependencies
    private PackageDependency packageDependency;
    private TsDependency tsDependency;
    private ModuleDependency moduleDependency;
    private UicStyles uicStyles;

    // Module Definition
    private String moduleName;

    // Component Definition
    private String selectorName;
    private String ngType;
    private String hostListener;
    private Boolean startTag;
    private Boolean endTag;
    private String pathName;
    private String className;

    // Property
    private JsonObject ngProperty;
    private JsonObject inputProperty;
    private JsonObject outputProperty;
    private JsonObject content;

    // Extra Info
    private HashMap<String, Object> infoData = new HashMap<>();

    // default: no click button; unless we set it
    private boolean handleClickNeeded = true;
    private boolean mainComponent = false;

    // Rendered Info
    private String htmlStr= "";
    private String tsStr = "";
    private String serviceStr = "";
    private String cssStr = "";

    // Ts Info
    private HashMap<String, String> tsInputInitMap = new HashMap<>();
    private HashMap<String, String> tsNgModelInitMap = new HashMap<>();
    private HashMap<String, String> tsNgModelParamMap = new HashMap<>();

    private HashMap<String, String> tsAttributeStringMap = new HashMap<>();

    public UicMetadata(){
        packageDependency = new PackageDependency();
        tsDependency =  new TsDependency();
        moduleDependency = new ModuleDependency();
        uicStyles = new UicStyles();
    }

    public UicMetadata(JsonObject jsonObject){
        this.portType = jsonObject.get("content").getAsJsonObject().get("portType").getAsString();
        this.packageDependency = new PackageDependency(jsonObject.get("package_dependencies").getAsJsonObject());
        this.tsDependency = new TsDependency(jsonObject.get("ts_dependencies").getAsJsonObject());
        this.moduleDependency = new ModuleDependency(jsonObject.get("module_dependencies").getAsJsonObject());

        this.uicStyles = new UicStyles(jsonObject.get("content").getAsJsonObject().get("style").getAsJsonObject());

        JsonObject moduleDefinition = jsonObject.get("module_definition").getAsJsonObject();
        this.moduleName = moduleDefinition.get("name").getAsString();

        JsonObject componentDefinition = jsonObject.get("component_definition").getAsJsonObject();
        this.selectorName = componentDefinition.get("selector").getAsString();
        this.ngType = componentDefinition.get("type").getAsString();
        this.hostListener = componentDefinition.get("hostListener").getAsString();
        this.startTag = componentDefinition.get("startTag").getAsString().equals("required");
        this.endTag = componentDefinition.get("endTag").getAsString().equals("required");

        this.ngProperty = jsonObject.get("ng_property").getAsJsonObject();
        this.inputProperty = jsonObject.get("input_property").getAsJsonObject();
        this.outputProperty = jsonObject.get("output_property").getAsJsonObject();
        this.content = jsonObject.get("content").getAsJsonObject();


    }

    public String getNgType() {
        return ngType;
    }

    public String getSelectorName() {
        return selectorName;
    }

    public String getHostListener() {
        return hostListener;
    }

    public JsonObject getContent() {
        return content;
    }

    public void setInfo(String key, Object val){
        this.infoData.put(key, val);
    }

    public String getInfoAsString(String key){
        return (String)this.infoData.get(key);
    }

    public boolean ifInfoContainsKey(String key){
        return this.infoData.containsKey(key);
    }

    public void setHtmlStr(String htmlStr) {
        this.htmlStr = htmlStr;
    }

    public String getHtmlStr() {
        return htmlStr;
    }

    public void setSelectorName(String selectorName) {
        this.selectorName = selectorName;
    }

    public PackageDependency getPackageDependency() {
        return packageDependency;
    }

    public TsDependency getTsDependency() {
        return tsDependency;
    }

    public ModuleDependency getModuleDependency() {
        return moduleDependency;
    }

    public void addSubMetadata(UicMetadata uicMetadata){
        this.moduleDependency.addDependencies(uicMetadata.getModuleDependency());
        this.packageDependency.addDependencies(uicMetadata.getPackageDependency());
        this.tsDependency.addDependencies(uicMetadata.getTsDependency());
        this.handleClickNeeded &= uicMetadata.isHandleClickNeeded();
    }

    public void setNgType(String type) {
        this.ngType = type;
    }

    public boolean isHandleClickNeeded() {
        return handleClickNeeded;
    }

    public void setHandleClickNeeded(boolean handleClickNeeded) {
        this.handleClickNeeded = handleClickNeeded;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public HashMap<String, String> getTsInputInitMap() {
        return tsInputInitMap;
    }

    public HashMap<String, String> getTsNgModelInitMap() {
        return tsNgModelInitMap;
    }

    public HashMap<String, String> getTsNgModelParamMap() {
        return tsNgModelParamMap;
    }

    public void addTsInputInitMap(String key, String val){
        this.tsInputInitMap.put(key, val);

    }
    public void addTsNgModelInitMap(String key, String val){
        this.tsNgModelInitMap.put(key, val);

    }
    public void addTsNgModelParamMap(String key, String val) {
        this.tsNgModelParamMap.put(key, val);

    }

    public void setTsStr(String tsStr) {
        this.tsStr = tsStr;
    }

    public String getTsStr() {
        return tsStr;
    }

    public boolean isMainComponent() {
        return mainComponent;
    }

    public void setMainComponent(boolean mainComponent) {
        this.mainComponent = mainComponent;
    }

    public String getServiceStr() {
        return serviceStr;
    }

    public void setServiceStr(String serviceStr) {
        this.serviceStr = serviceStr;
    }

    public String getCssStr() {
        return cssStr;
    }

    public void setCssStr(String cssStr) {
        this.cssStr = cssStr;
    }

    public void addTsAttributeString(String attributeName, String attributeVal) {
        this.tsAttributeStringMap.put(attributeName, attributeVal);
    }

    public HashMap<String, String> getTsAttributeStringMap() {
        return tsAttributeStringMap;
    }

    public UicStyles getUicStyles() {
        return uicStyles;
    }

    public String getPortType() {
        return portType;
    }
}
