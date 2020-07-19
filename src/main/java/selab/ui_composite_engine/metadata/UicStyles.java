package selab.ui_composite_engine.metadata;

import com.google.gson.JsonObject;
import selab.ui_composite_engine.util.JsonObjUtil;

import java.util.*;

public class UicStyles {

    private Map<String, String> cssMap;
    private Set<String> scssImports;
    private Set<String> scssImportsFilePaths;

    public UicStyles(){
        this.cssMap = new HashMap<>();
        this.scssImports = new LinkedHashSet<>();
        this.scssImportsFilePaths = new LinkedHashSet<>();
    }

    public UicStyles(JsonObject jsonObject) {
        this.cssMap = JsonObjUtil.processJsonObject2MapString(jsonObject);
    }


    public Map<String, String> getCssMap() {
        return cssMap;
    }

    public String getCssMapAsString() {
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, String> entry: this.cssMap.entrySet()) {
            stringBuffer.append(entry.getKey() + ":" + entry.getValue() + ";\n");
        }
        return stringBuffer.toString();
    }

    public void addScssImport(String scssPath){
        this.scssImports.add(scssPath);
    }

    public void addAllScssImport(Set<String> scssPath){
        this.scssImports.addAll(scssPath);
    }

    public Set<String> getScssImports(){
        return this.scssImports;
    }

    public void addScssImportsFilePath(String scssPath){
        this.scssImportsFilePaths.add(scssPath);
    }

    public void addAllScssImportsFilePaths(Set<String> scssPaths){
        this.scssImportsFilePaths.addAll(scssPaths);
    }

    public Set<String> getScssImportsFilePaths() {
        return scssImportsFilePaths;
    }

}
