package selab.ui_composite_engine.parser.uicdl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import selab.ui_composite_engine.Configuration;
import selab.ui_composite_engine.metadata.ModuleDependency;
import selab.ui_composite_engine.metadata.PackageDependency;
import selab.ui_composite_engine.metadata.TsDependency;
import selab.ui_composite_engine.model.LayoutWrapper;
import selab.ui_composite_engine.parser.ldl.LDLParser;
import selab.ui_composite_engine.util.IOUtil;
import selab.ui_composite_engine.util.PathUtil;

import java.io.IOException;
import java.util.logging.Logger;


public class UICDLParser {

    private static final Logger LOG = Logger.getLogger(LDLParser.class.getName());

    public static JsonObject parse(String filePath) {
        String content = IOUtil.readFile(filePath);
        JsonParser jsonParser = new JsonParser();
        return jsonParser.parse(content).getAsJsonObject();
    }

    public static LayoutWrapper parse(JsonObject jsonObject){

        PackageDependency packageDependency = new PackageDependency(jsonObject.get("package_dependencies").getAsJsonObject());
        ModuleDependency moduleDependency = new ModuleDependency(jsonObject.get("module_dependencies").getAsJsonObject());


        TsDependency tsDependency = new TsDependency(jsonObject.get("ts_dependencies").getAsJsonObject());
        LayoutWrapper layoutWrapper = new LayoutWrapper(packageDependency,moduleDependency,tsDependency);

        String layout = jsonObject.get("content").getAsJsonObject().get("layout").getAsString();
        layoutWrapper.setLayoutPath(PathUtil.combinePath(Configuration.LDL_DIR_PATH,layout,"layout"));
        layoutWrapper.setAssetsPath(PathUtil.combinePath(Configuration.LDL_DIR_PATH,layout,"assets"));

        JsonArray components = jsonObject.get("content").getAsJsonObject().get("component").getAsJsonArray();
        for(int i=0;i<components.size();i++){
            JsonObject component = components.get(i).getAsJsonObject();
            String name = component.get("name").getAsString();
            String type = component.get("type").getAsString();
            String routing = component.get("routing").getAsString();
            layoutWrapper.addComponentRouting(name,routing,type);
        }
        return layoutWrapper;
    }
}
