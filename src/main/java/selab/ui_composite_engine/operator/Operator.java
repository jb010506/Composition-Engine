package selab.ui_composite_engine.operator;

import selab.ui_composite_engine.component.CompositeComponent;
import selab.ui_composite_engine.component.LeafComponent;
import selab.ui_composite_engine.component.RenderingComponent;

import java.util.List;

public interface Operator {
    void operate(LeafComponent leafComponent);
    void operate(CompositeComponent compositeComponent);

    void render(RenderingComponent renderingComponent);
}
