package selab.ui_composite_engine.metadata;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import selab.ui_composite_engine.util.JsonObjUtil;

import java.util.*;

public class TsDependency {
    private Set<String> nodeModulesStyles;
    private Set<String> nodeModulesModules;
    private Set<String> nodeModulesComponents;
    private Map<String, String> tsImportPaths;
    private List<String> tsImports;

    public TsDependency(Set<String> nodeModulesStyles, Map<String, String> tsImportPaths, Set<String> nodeModulesComponents) {
        this.nodeModulesStyles = nodeModulesStyles;
        this.nodeModulesModules = tsImportPaths.keySet();
        this.nodeModulesComponents = nodeModulesComponents;
    }

    public TsDependency(Set<String> nodeModulesStyles, Set<String> nodeModulesModules, Set<String> nodeModulesComponents) {
        this.nodeModulesStyles = nodeModulesStyles;
        this.nodeModulesModules = nodeModulesModules;
        this.nodeModulesComponents = nodeModulesComponents;
    }

    public TsDependency() {
        this.nodeModulesStyles = new HashSet<>();
        this.nodeModulesModules = new HashSet<>();
        this.nodeModulesComponents = new HashSet<>();
    }

    public TsDependency(JsonObject tsDependencies) {

        JsonArray styleArray = tsDependencies.get("node_modules").getAsJsonObject().get("style").getAsJsonArray();
        this.nodeModulesStyles = JsonObjUtil.processJsonArray2SetString(styleArray);

        JsonArray moduleArray = tsDependencies.get("node_modules").getAsJsonObject().get("module").getAsJsonArray();
        this.nodeModulesModules = JsonObjUtil.processJsonArray2SetString(moduleArray);

        if(tsDependencies.get("node_modules").getAsJsonObject().get("imports")!=null) {
            JsonObject nodes = tsDependencies.get("node_modules").getAsJsonObject().get("imports").getAsJsonObject();
            this.tsImportPaths = JsonObjUtil.processJsonStringListMapReverseMapString(nodes);
        }

        JsonArray componentArray = tsDependencies.get("node_modules").getAsJsonObject().get("component").getAsJsonArray();
        this.nodeModulesComponents = JsonObjUtil.processJsonArray2SetString(componentArray);
    }

    public Set<String> getNodeModulesStyles() {
        return nodeModulesStyles;
    }

    public Set<String> getNodeModulesModules() {
        return nodeModulesModules;
    }

    public Set<String> getNodeModulesComponents() {
        return nodeModulesComponents;
    }

    public void addDependencies(TsDependency tsDependency){
        this.nodeModulesStyles.addAll(tsDependency.getNodeModulesStyles());
        this.nodeModulesModules.addAll(tsDependency.getNodeModulesModules());
        this.nodeModulesComponents.addAll(tsDependency.getNodeModulesComponents());
    }

    public Map<String, String> getTsImportPaths() {
        return tsImportPaths;
    }

    public List<String> getTsImports(){
        return tsImports;
    }

    private List<String> getAsList(JsonObject contentObject, String item){
        JsonArray actionsJA = contentObject.get(item).getAsJsonArray();
        List<String> actions = new ArrayList<>();
        if(actionsJA!=null){
            for(int i=0;i<actionsJA.size();i++){
                actions.add(actionsJA.get(i).getAsString());
            }
        }
        return actions;
    }

}
