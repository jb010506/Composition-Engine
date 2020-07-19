package selab.ui_composite_engine.model;

import javax.wsdl.Definition;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.PortType;

public class OperationWrapper {

    private final Operation wsdlOperation;
    private final PortType wsdlPortType;
    private final String partnerLinkName;
    private final BpelDefinition bpelDefinition;

    public OperationWrapper(Operation wsdlOperation, PortType wsdlPortType, String partnerLinkName, BpelDefinition bpelDefinition) {
        this.wsdlOperation = wsdlOperation;
        this.wsdlPortType = wsdlPortType;
        this.partnerLinkName = partnerLinkName;
        this.bpelDefinition = bpelDefinition;
    }

    public Operation getWsdlOperation() {
        return wsdlOperation;
    }

    public PortType getWsdlPortType() {
        return wsdlPortType;
    }

    public String getPartnerLinkName() {
        return partnerLinkName;
    }

    public MessageWrapper getInputMessage(){
        if(this.wsdlOperation.getInput() == null) {
            return null;
        }
        Message message = this.wsdlOperation.getInput().getMessage();
        return new MessageWrapper(message, true, this);
    }

    public MessageWrapper getOutputMessage(){
        if(this.wsdlOperation.getOutput() == null) {
            return null;
        }
        Message message = this.wsdlOperation.getOutput().getMessage();
        return new MessageWrapper(message, false, this);
    }

    public String getName(){
        return this.wsdlOperation.getName();
    }

    public String getBpelWsdlName() {
        return this.bpelDefinition.getBpelWsdlName();
    }

    public String getPortTypeName() {
        return this.wsdlPortType.getQName().getLocalPart();
    }
}
