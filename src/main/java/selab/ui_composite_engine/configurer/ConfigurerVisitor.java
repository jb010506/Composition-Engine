package selab.ui_composite_engine.configurer;

import selab.ui_composite_engine.component.CompositeComponent;
import selab.ui_composite_engine.component.RenderingComponent;

import java.lang.reflect.Method;

public abstract class ConfigurerVisitor {
    public void doubleDispatch(RenderingComponent component){
        try{
            Method method = this.getClass().getMethod("visit", component.getClass());
            method.invoke(this, component);
        } catch (Exception e) {
            this.visit(component);
        }
    }

    public void visit(RenderingComponent component){
        this.visitNext(component);
    }

    public void visitNext(RenderingComponent component){
        if(component instanceof CompositeComponent){
            for(RenderingComponent subComponent: ((CompositeComponent) component).getAllSubComponents()){
                this.doubleDispatch(subComponent);
            }
        }
    }

    public abstract void process();
}
