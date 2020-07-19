package selab.ui_composite_engine.metadata;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import selab.ui_composite_engine.util.JsonObjUtil;

import java.util.HashSet;
import java.util.Set;

public class ModuleDependency {
    private Set<String> moduleModules;
    private Set<String> moduleComponents;


    public ModuleDependency(){
        this.moduleModules = new HashSet<>();
        this.moduleComponents = new HashSet<>();
    }

    public ModuleDependency(Set<String> moduleModules, Set<String> moduleComponents) {
        this.moduleModules = moduleModules;
        this.moduleComponents = moduleComponents;
    }

    public ModuleDependency(JsonObject moduleDependencies) {
        JsonArray styleArray = moduleDependencies.get("module").getAsJsonArray();
        this.moduleModules = JsonObjUtil.processJsonArray2SetString(styleArray);

        JsonArray moduleArray = moduleDependencies.get("component").getAsJsonArray();
        this.moduleComponents = JsonObjUtil.processJsonArray2SetString(moduleArray);
    }

    public Set<String> getModuleModules() {
        return moduleModules;
    }

    public Set<String> getModuleComponents() {
        return moduleComponents;
    }

    public void addDependencies(ModuleDependency moduleDependency){
        moduleModules.addAll(moduleDependency.getModuleModules());
        moduleComponents.addAll(moduleDependency.getModuleComponents());
    }
}
