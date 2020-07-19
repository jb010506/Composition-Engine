package selab.ui_composite_engine.parser.ldl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import selab.ui_composite_engine.metadata.ModuleDependency;
import selab.ui_composite_engine.metadata.PackageDependency;
import selab.ui_composite_engine.metadata.TsDependency;
import selab.ui_composite_engine.metadata.UicStyles;
import selab.ui_composite_engine.model.LDLWrapper;
import selab.ui_composite_engine.util.JsonObjUtil;
import selab.ui_composite_engine.util.PathUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class LDLParser {
    private static final Logger LOG = Logger.getLogger(LDLParser.class.getName());
    private static JsonParser jsonParser = new JsonParser();

    public LDLParser() {
    }

    public static LDLWrapper parse(String filePath) {
        try {
            String ldlStr = FileUtils.readFileToString(new File(filePath));

            Document ldlDocument = Jsoup.parse(ldlStr, "", Parser.xmlParser());

            // layout sample directory (without file (.ldl.xml))
            String ldlDirPath = PathUtil.getAbsoluteDirOfPath(filePath);


            // Parse Package Dependency
            String packageDependencyFilePath = PathUtil.combinePath(
                    ldlDirPath,
                    ldlDocument.select("package_dependency").attr("import")
                    );
            String packageDependencyContent = FileUtils.readFileToString(new File(packageDependencyFilePath));
            JsonObject packageDependencyObj = jsonParser.parse(packageDependencyContent).getAsJsonObject();
            JsonObject npmDependenciesObj = packageDependencyObj.get("dependencies").getAsJsonObject();
            JsonObject npmDevDependenciesObj = packageDependencyObj.get("devDependencies").getAsJsonObject();
            PackageDependency packageDependency = new PackageDependency(
                    JsonObjUtil.processJsonObject2MapString(npmDependenciesObj),
                    JsonObjUtil.processJsonObject2MapString(npmDevDependenciesObj)
            );



            // Parse Module Dependency
            String moduleDependencyFilePath = PathUtil.combinePath(
                    ldlDirPath,
                    ldlDocument.select("module_dependency").attr("import")
            );
            String moduleDependencyContent = FileUtils.readFileToString(new File(moduleDependencyFilePath));
            JsonArray moduleDependencyObj = jsonParser.parse(moduleDependencyContent).getAsJsonArray();
            ModuleDependency moduleDependency = new ModuleDependency(
                    JsonObjUtil.processJsonArray2SetString(moduleDependencyObj), null);

            // Parse Ts Dependency
            String tsDependencyFilePath = PathUtil.combinePath(
                    ldlDirPath,
                    ldlDocument.select("ts_dependency").attr("import")
            );
            String tsDependencyContent = FileUtils.readFileToString(new File(tsDependencyFilePath));
            JsonObject tsDependencyObj = jsonParser.parse(tsDependencyContent).getAsJsonObject();

            TsDependency tsDependency = new TsDependency(
                    new HashSet<>(getAllJsoupElmentsAttr(ldlDocument.select("style").select("dependency"), "import")),
                    JsonObjUtil.processJsonStringListMapReverseMapString(tsDependencyObj),
                    new HashSet<>(getAllJsoupElmentsAttr(ldlDocument.select("src").select("component"), "import")));
            // Initialize LDL Wrapper
            LDLWrapper ldlWrapper = new LDLWrapper(packageDependency, moduleDependency, tsDependency);

            // Parse Component
            Elements componentEls = ldlDocument.select("src").select("component");
            for(Element el: componentEls){
                String componentName = el.attr("name");
                String componentRouting = el.attr("routing");
                String componentType = el.attr("type");
                ldlWrapper.addComponentRouting(componentName, componentRouting, componentType);
            }

            // Parse Layout Path
            String layoutPath = PathUtil.combinePath(
                    ldlDirPath,
                    ldlDocument.select("src").select("uri").attr("import"));
            ldlWrapper.setLayoutPath(layoutPath);

            // Parse Assets Path
            String assetsPath = PathUtil.combinePath(
                    ldlDirPath,
                    ldlDocument.select("src").select("assets").attr("import"));
            ldlWrapper.setAssetsPath(assetsPath);


            return ldlWrapper;

        } catch (IOException e) {
            LOG.warning("Failed to parse LDL file");
            e.printStackTrace();
        }
        return null;

    }

    private static List<String> getAllJsoupElmentsAttr(Elements elements, String attr){
        List<String> matchedAttrStr = new ArrayList<>();
        for(Element e : elements){      // all elements in html
            if(e.hasAttr(attr)){
                matchedAttrStr.add(e.attr(attr));
            }
        }
        return matchedAttrStr;
    }
}

