package selab.ui_composite_engine.outlet.factory;

import selab.ui_composite_engine.component.RenderingComponent;
import selab.ui_composite_engine.configurer.ConfigurerVisitor;
import selab.ui_composite_engine.model.BpelWsdlDefinition;

import java.util.List;

public interface OutletFactory {
    List<RenderingComponent> createComponents(BpelWsdlDefinition bpelWsdlDefinition);
    List<ConfigurerVisitor> createConfigurerVisitors();
}
