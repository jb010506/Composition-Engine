package selab.ui_composite_engine.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import selab.ui_composite_engine.util.IOUtil;
import selab.ui_composite_engine.util.WsdlUtil;

import javax.wsdl.Definition;
import javax.wsdl.Operation;
import javax.wsdl.PortType;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class BpelDefinition {

    private Document bpelDocument;
    private Definition wsdlDefinition;
    private QName serviceQname;
    private BpelWsdlDefinition bpelWsdlDefinition;
    private Boolean isMainBpel = false;
    private String partnerLink = null;

    public BpelDefinition(QName serviceQname, String bpelPath, Definition wsdlDefinition, BpelWsdlDefinition bpelWsdlDefinition){
        String str = IOUtil.readFile(bpelPath);
        this.serviceQname = serviceQname;
        this.bpelDocument = Jsoup.parse(str, "", Parser.xmlParser());
        this.wsdlDefinition = wsdlDefinition;
        this.bpelWsdlDefinition = bpelWsdlDefinition;
        if(this.serviceQname.getNamespaceURI().equals(this.wsdlDefinition.getTargetNamespace())){
            this.isMainBpel = true;
        }
    }

    public List<OperationWrapper> getOperationsByPortType(String portTypeName){

        Elements els = this.bpelDocument.select("[portType=tns:" + portTypeName + "]");
        List<OperationWrapper> operationWrapperList = new ArrayList<>();

        HashSet<String> operationNameSet = new HashSet<>();

        // Find PortTypes in WSDL ComponentDefinition
        PortType wsdlPortType = WsdlUtil.getPortTypeByLocalPartName(this.wsdlDefinition, portTypeName);
        if(wsdlPortType == null)
            return null;

        for(Element el: els){
            String operationName = el.attr("operation");
            String partnerLinkName = el.attr("partnerLink");

            if(operationNameSet.contains(operationName+partnerLinkName))
                continue;
            operationNameSet.add(operationName+partnerLinkName);

            Operation wsdlOperation = wsdlPortType.getOperation(operationName, null, null);
            OperationWrapper operationWrapper = new OperationWrapper(wsdlOperation, wsdlPortType, partnerLinkName, this);
            operationWrapperList.add(operationWrapper);

            if(this.partnerLink == null)
                this.partnerLink = partnerLinkName;
        }
        return operationWrapperList;
    }

    public String getName() {
        return this.serviceQname.getLocalPart();
    }

    public String getBpelWsdlName() {
        return this.bpelWsdlDefinition.getName();
    }

    public Boolean isMainBpel() {
        return isMainBpel;
    }

    public Object getPartnerLink() {
        return this.partnerLink;
    }

    public QName getQname() {
        return this.serviceQname;
    }
}
