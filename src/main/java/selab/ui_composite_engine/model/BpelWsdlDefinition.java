package selab.ui_composite_engine.model;


import selab.ui_composite_engine.parser.wsdlbpel.ServiceExtractor;
import selab.ui_composite_engine.util.PathUtil;

import javax.wsdl.Definition;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BpelWsdlDefinition {

    private static ServiceExtractor serviceExtractor = new ServiceExtractor();

    private Definition wsdlDefinition; // parsed by wsdlReader
    private Map<QName, String> qNameBpelMap; // Mapping between ServiceQName and BpelPath

    public BpelWsdlDefinition(Definition wsdlDefinition){
        this.wsdlDefinition = wsdlDefinition;
        this.qNameBpelMap = serviceExtractor.extractAllServiceBPELPaths(wsdlDefinition);
    }

    public List<BpelDefinition> getBpelDefinitions(){
        List<BpelDefinition> bpelDefinitionList = new ArrayList<>();
        for(Map.Entry<QName, String> entry: this.qNameBpelMap.entrySet()){
            bpelDefinitionList.add(new BpelDefinition(entry.getKey(), entry.getValue(), this.wsdlDefinition, this));
        }
        return bpelDefinitionList;
    }

    public Definition getWsdlDefinition(){
        return this.wsdlDefinition;
    }

    public String getName() {
        String urlName = this.wsdlDefinition.getDocumentBaseURI();
        return PathUtil.getFileNameWithoutExt(urlName);
    }
}
