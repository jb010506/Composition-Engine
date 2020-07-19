package selab.ui_composite_engine.component;

import java.util.ArrayList;
import java.util.List;

public abstract class CompositeComponent extends RenderingComponent {

    private List<RenderingComponent> subComponents = new ArrayList<>();

    public CompositeComponent(String name) {
        super(name);
    }


    public void addSubComponent(RenderingComponent component){
        subComponents.add(component);
    }

    public List<RenderingComponent> getAllSubComponents(){
        return subComponents;
    }

    @Override
    public void compose() {
        for(RenderingComponent component: this.getAllSubComponents()){
            component.compose();
        }
    }
}
