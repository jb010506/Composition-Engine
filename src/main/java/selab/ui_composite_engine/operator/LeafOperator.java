package selab.ui_composite_engine.operator;

import selab.ui_composite_engine.component.CompositeComponent;
import selab.ui_composite_engine.component.RenderingComponent;

import java.util.List;

public abstract class LeafOperator implements Operator {

    /**
    * LeafOperator doesn't support operating multi composite components
    * */
    public void operate(CompositeComponent compositeComponent) {
    }

}
