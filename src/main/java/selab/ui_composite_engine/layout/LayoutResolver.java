package selab.ui_composite_engine.layout;

import com.google.gson.JsonObject;
import selab.ui_composite_engine.Configuration;
import selab.ui_composite_engine.model.BpelWsdlDefinition;
import selab.ui_composite_engine.model.LDLWrapper;
import selab.ui_composite_engine.model.LayoutWrapper;
import selab.ui_composite_engine.outlet.factory.OutletFactory;
import selab.ui_composite_engine.parser.ldl.LDLParser;
import selab.ui_composite_engine.parser.uicdl.UICDLParser;
import selab.ui_composite_engine.util.PathUtil;

import java.util.List;

public class LayoutResolver {
    public static Layout resolveLayout(BpelWsdlDefinition bpelWsdlDefinition){

        JsonObject layoutJSONObject = UICDLParser.parse(Configuration.UICDL_LAYOUT_PATH);
        //sample layout; can be changed
        String layoutName = layoutJSONObject.get("content").getAsJsonObject().get("layout").getAsString();
        //LDLWrapper ldlWrapper = LDLParser.parse(PathUtil.combinePath(Configurati
        // on.LDL_DIR_PATH, layoutName, layoutName + ".ldl.xml"));
        LayoutWrapper layoutWrapper = UICDLParser.parse(layoutJSONObject);
        return new NgLayout(layoutName, layoutWrapper);
    }
}
