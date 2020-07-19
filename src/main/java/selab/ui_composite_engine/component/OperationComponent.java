package selab.ui_composite_engine.component;

import selab.ui_composite_engine.model.MessageWrapper;
import selab.ui_composite_engine.model.OperationWrapper;

public class OperationComponent extends CompositeComponent{

    private final OperationWrapper operationWrapper;
    private final MessageWrapper messageWrapper;

    public OperationComponent(OperationWrapper operationWrapper, MessageWrapper messageWrapper) {
        super(operationWrapper.getName());
        this.operationWrapper = operationWrapper;
        this.messageWrapper = messageWrapper;
    }

    @Override
    public void compose() {
        super.compose();
        this.getOperator().operate(this);

        this.getOperator().render(this);
    }

}
