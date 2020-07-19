package selab.ui_composite_engine.component;

public abstract class LeafComponent extends RenderingComponent {
    public LeafComponent(String componentName) {
        super(componentName);
    }

    abstract public String getUicdKey();
    abstract public boolean getIsInput();
}
