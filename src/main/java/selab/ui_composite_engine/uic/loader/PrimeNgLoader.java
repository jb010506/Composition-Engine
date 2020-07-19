package selab.ui_composite_engine.uic.loader;

import com.google.gson.JsonObject;
import selab.ui_composite_engine.Configuration;
import selab.ui_composite_engine.metadata.UicMetadata;
import selab.ui_composite_engine.parser.uicdl.UICDLParser;
import selab.ui_composite_engine.util.PathUtil;

public class PrimeNgLoader extends UICLoader{

    private final static String buttonUicPath = PathUtil.combinePath(Configuration.UICDL_DIR_PATH, "primeng/components/button/button/Button");
    public static UicMetadata getButtonUic(){
        JsonObject uicJsonObject = loadUicJsonObject(buttonUicPath);
        return new UicMetadata(uicJsonObject);
    }

    private final static String scrollPanelPath = PathUtil.combinePath(Configuration.UICDL_DIR_PATH, "primeng/components/scrollpanel/scrollpanel/ScrollPanel");
    public static UicMetadata getScrollPanelUic(){
        JsonObject uicJsonObject = loadUicJsonObject(scrollPanelPath);
        return new UicMetadata(uicJsonObject);
    }

    private static JsonObject loadUicJsonObject(String uicdKey){
        String matchedUicdlPath =  uicdKey + Configuration.UICDL_FILENAME_EXTENSION;
        return UICDLParser.parse(matchedUicdlPath);
    }
}
