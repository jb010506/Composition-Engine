package selab.ui_composite_engine.operator;

import com.google.gson.JsonObject;
import selab.ui_composite_engine.Configuration;
import selab.ui_composite_engine.component.LeafComponent;
import selab.ui_composite_engine.metadata.UicMetadata;
import selab.ui_composite_engine.parser.uicdl.UICDLParser;

public abstract class AtomicUIComponentMatcher extends LeafOperator{

    protected Boolean isInput;

    @Override
    public void operate(LeafComponent leafComponent) {
        JsonObject uicJsonObject = this.loadUicJsonObject(leafComponent.getUicdKey());
        leafComponent.setMetadata(new UicMetadata(uicJsonObject));
        this.isInput = leafComponent.getIsInput();
    }

    private JsonObject loadUicJsonObject(String uicdKey){
        String matchedUicdlPath =  uicdKey + Configuration.UICDL_FILENAME_EXTENSION;
        return UICDLParser.parse(matchedUicdlPath);
    }
}
