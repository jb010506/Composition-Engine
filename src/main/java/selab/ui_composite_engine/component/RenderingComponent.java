package selab.ui_composite_engine.component;

import selab.ui_composite_engine.metadata.UicMetadata;
import selab.ui_composite_engine.operator.Operator;
import selab.ui_composite_engine.util.Counter;

public abstract class RenderingComponent {

    private int componentId;
    private Operator operator;
    private UicMetadata uicMetadata;
    private String name;

    public RenderingComponent(String componentName){
        this.componentId = Counter.getComponentId();
        this.name = componentName;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public void setMetadata(UicMetadata uicMetadata) {
        this.uicMetadata = uicMetadata;
    }

    public UicMetadata getUicMetadata() {
        if(this.uicMetadata == null){
            this.uicMetadata = new UicMetadata();
            return this.uicMetadata;
        }
        return uicMetadata;
    }

    public abstract void compose();

    public int getComponentId() {
        return componentId;
    }

    public String getName() {
        return name;
    }

}
