package component.metadata;

import com.google.gson.JsonObject;
import org.junit.Test;
import selab.ui_composite_engine.metadata.UicMetadata;
import selab.ui_composite_engine.parser.uicdl.UICDLParser;
import selab.ui_composite_engine.util.PathUtil;

import java.io.IOException;

public class MetadataTest {

    @Test
    public void testInitMetadata() throws IOException {
        UICDLParser parser = new UICDLParser();
        JsonObject jsonObject = parser.parse(PathUtil.getAbsolutePath("test_data/UICDL/primeng/components/button/button/Button.uicdl.json"));
        UicMetadata uicMetadata = new UicMetadata(jsonObject);
    }
}
