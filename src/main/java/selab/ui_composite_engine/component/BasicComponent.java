package selab.ui_composite_engine.component;

import selab.ui_composite_engine.Configuration;
import selab.ui_composite_engine.model.MessagePartWrapper;
import selab.ui_composite_engine.util.PathUtil;

public class BasicComponent extends LeafComponent {
    private MessagePartWrapper messagePartWrapper;

    public BasicComponent(MessagePartWrapper messagePartWrapper) {
        super(messagePartWrapper.getName());
        this.messagePartWrapper = messagePartWrapper;
        // message contains single parameter and data type
        // According to the data type, we can decide the UI Component

    }

    @Override
    public boolean getIsInput() {
        return messagePartWrapper.getIsInput();
    }

    @Override
    public void compose() {
        this.getOperator().operate(this);
        this.getOperator().render(this);
    }

    @Override
    public String getUicdKey() {
        return PathUtil.combinePath(Configuration.MATCHED_UICDL_DIR_PATH,
        this.messagePartWrapper.getBpelWsdlName(),
                this.messagePartWrapper.getPortTypeName(),
                this.messagePartWrapper.getOperationName(),
                this.messagePartWrapper.getMessageName(),
                this.messagePartWrapper.getName());
    }

}
