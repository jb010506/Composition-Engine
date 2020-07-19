package selab.ui_composite_engine.metadata;

import com.google.gson.JsonObject;
import selab.ui_composite_engine.util.JsonObjUtil;

import java.util.HashMap;
import java.util.Map;

public class PackageDependency {

    private Map<String, String> npmDependencies;

    private Map<String, String> npmDevDependencies;

    public PackageDependency(){
        this.npmDependencies = new HashMap<>();
        this.npmDevDependencies = new HashMap<>();
    }

    public PackageDependency(Map<String, String> npmDependencies, Map<String, String> npmDevDependencies) {
        this.npmDependencies = npmDependencies;
        this.npmDevDependencies = npmDevDependencies;
    }

    public PackageDependency(JsonObject packageDependencies) {
        JsonObject npmDependenciesObj = packageDependencies.get("npm_dependencies").getAsJsonObject();
        this.npmDependencies = JsonObjUtil.processJsonObject2MapString(npmDependenciesObj);

        JsonObject npmDevDependenciesObj = packageDependencies.get("npm_dev_dependencies").getAsJsonObject();
        this.npmDevDependencies = JsonObjUtil.processJsonObject2MapString(npmDevDependenciesObj);
    }

    public Map<String, String> getNpmDependencies() {
        return npmDependencies;
    }

    public Map<String, String> getNpmDevDependencies() {
        return npmDevDependencies;
    }

    public void addDependencies(PackageDependency packageDependency){
        this.npmDependencies.putAll(packageDependency.getNpmDependencies());
        this.npmDevDependencies.putAll(packageDependency.getNpmDevDependencies());
    }

}
