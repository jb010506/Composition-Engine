package selab.ui_composite_engine.model;

import javax.wsdl.Definition;
import javax.wsdl.Message;
import javax.wsdl.Part;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MessageWrapper {

    private final Message message;
    private final OperationWrapper operationWrapper;
    private final Boolean isInput;

    public MessageWrapper(Message message, Boolean isInput, OperationWrapper operationWrapper) {
        this.message = message;
        this.operationWrapper = operationWrapper;
        this.isInput = isInput;
    }

    public QName getQName() {
        return this.message.getQName();
    }
    public String getName(){
        return this.getQName().getLocalPart();
    }

    public List<MessagePartWrapper> getParts() {
        List<MessagePartWrapper> messagePartList = new ArrayList<>();

        for(Object obj: this.message.getOrderedParts(null)) {
            Part part = (Part) obj;
            messagePartList.add(new MessagePartWrapper(part, this));
        }

        return messagePartList;
    }

    @Override
    public String toString(){
        return this.message.toString();
    }

    public String getOperationName() {
        return this.operationWrapper.getName();
    }


    public String getBpelWsdlName() {
        return this.operationWrapper.getBpelWsdlName();
    }

    public String getPortTypeName() {
        return this.operationWrapper.getPortTypeName();
    }

    public Boolean getIsInput() {
        return isInput;
    }
}
