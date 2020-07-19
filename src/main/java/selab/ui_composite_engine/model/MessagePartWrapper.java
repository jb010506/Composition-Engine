package selab.ui_composite_engine.model;

import javax.wsdl.Definition;
import javax.wsdl.Part;
import javax.xml.namespace.QName;

public class MessagePartWrapper {

    private final Part part;
    private final MessageWrapper messageWrapper;

    public MessagePartWrapper(Part part, MessageWrapper messageWrapper) {
        this.part = part;
        this.messageWrapper = messageWrapper;
    }

    public QName getTypeName() {
        return this.part.getTypeName();
    }

    public String getName() {
        return this.part.getName();
    }

    public String getMessageName(){
        return this.messageWrapper.getName();
    }

    public String getOperationName(){
        return this.messageWrapper.getOperationName();
    }

    public String getBpelWsdlName(){
        return this.messageWrapper.getBpelWsdlName();
    }

    @Override
    public String toString(){
        return this.part.toString();
    }

    public String getPortTypeName() {
        return this.messageWrapper.getPortTypeName();
    }

    public boolean getIsInput(){
        return this.messageWrapper.getIsInput();
    }
}
