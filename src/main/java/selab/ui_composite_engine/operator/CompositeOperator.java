package selab.ui_composite_engine.operator;

import selab.ui_composite_engine.component.LeafComponent;

public abstract class CompositeOperator implements Operator {

    /**
     * CompositeOperator doesn't support operating leaf composite components */
    @Override
    public void operate(LeafComponent Component) {
    }

}
