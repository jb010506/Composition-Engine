package selab.ui_composite_engine.util;

import selab.ui_composite_engine.Configuration;

import javax.wsdl.Definition;
import javax.wsdl.PortType;
import javax.xml.namespace.QName;
import java.io.IOException;
import java.util.Map;

public class WsdlUtil {
    public static String getAbsolutePath(String path) throws IOException {
        if (path.matches("^(http|https|ftp)://.*$")) return path;
        path = PathUtil.combinePath(Configuration.WSDL_DIR_PATH, path);
        return PathUtil.getAbsolutePath(path);
    }

    public static PortType getPortTypeByLocalPartName(Definition wsdlDefinition, String portTypeName){
        for(Object object : wsdlDefinition.getPortTypes().entrySet()) {
            Map.Entry<QName, javax.wsdl.PortType> entry = (Map.Entry<QName, javax.wsdl.PortType>) object;
            if(entry.getKey().getLocalPart().equals(portTypeName)){
                return entry.getValue();
            }
        }
        return null;
    }
}
