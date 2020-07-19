package selab.ui_composite_engine.model;

import selab.ui_composite_engine.metadata.ModuleDependency;
import selab.ui_composite_engine.metadata.PackageDependency;
import selab.ui_composite_engine.metadata.TsDependency;
import selab.ui_composite_engine.metadata.UicStyles;

import java.util.*;

public class LayoutWrapper {

    private PackageDependency packageDependency;
    private TsDependency tsDependency;
    private ModuleDependency moduleDependency;
    private UicStyles uicStyles;
    private Set<String> declarationComponentNames;
    private Set<String> entryComponentNames;
    private Set<String> stylesImport;
    private List<Map<String, String>> componentRoutings;

    private String layoutPath;
    private String assetsPath;

    public LayoutWrapper(PackageDependency packageDependency, ModuleDependency moduleDependency, TsDependency tsDependency) {
        this.packageDependency = packageDependency;
        this.tsDependency = tsDependency;
        this.moduleDependency = moduleDependency;

        this.declarationComponentNames = new HashSet<>();
        this.entryComponentNames = new HashSet<>();
        this.componentRoutings = new LinkedList<>();
    }


    public void addComponentRouting(String componentName, String routingPath, String componentType) {
        this.declarationComponentNames.add(componentName);

        if(routingPath != null){
            Map<String, String> pageRouting = new HashMap<>();
            pageRouting.put("path", routingPath);
            pageRouting.put("component", componentName);
            this.componentRoutings.add(pageRouting);
        }

        // not understandable
        if(componentType.equals("entry")){
            this.entryComponentNames.add(componentName);
        }
    }


    public String getLayoutPath() {
        return layoutPath;
    }

    public void setLayoutPath(String layoutPath) {
        this.layoutPath = layoutPath;
    }

    public String getAssetsPath() {
        return assetsPath;
    }

    public void setAssetsPath(String assetsPath) {
        this.assetsPath = assetsPath;
    }

    public TsDependency getTsDependency() {
        return tsDependency;
    }

    public ModuleDependency getModuleDependency() {
        return moduleDependency;
    }

    public PackageDependency getPackageDependency() {
        return packageDependency;
    }

    public Set<String> getDeclarationComponentNames() {
        return declarationComponentNames;
    }

    public Set<String> getEntryComponentNames() {
        return entryComponentNames;
    }

    public List<Map<String, String>> getComponentRoutings() {
        return componentRoutings;
    }

    public Set<String> getStylesImport() {
        return stylesImport;
    }

    public void setStylesImport(Set<String> stylesImport) {
        this.stylesImport = stylesImport;
    }

    public UicStyles getUicStyles() {
        return uicStyles;
    }

    public void setUicStyles(UicStyles uicStyles) {
        this.uicStyles = uicStyles;
    }



}
