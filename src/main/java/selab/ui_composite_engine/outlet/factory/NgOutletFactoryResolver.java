package selab.ui_composite_engine.outlet.factory;

import selab.ui_composite_engine.model.BpelWsdlDefinition;

import java.util.ArrayList;
import java.util.List;

public class NgOutletFactoryResolver {

    /**
     * Given bpelWsdlDefinition and resolve outletFactories to compose components
     *
     * TODO: Trace bpelWsdlDefinition and detect OutletFactory (Rule-Base currently)
     *
     */
    public static List<OutletFactory> resolveFactories(BpelWsdlDefinition bpelWsdlDefinition){
        List<OutletFactory> outletFactoryList = new ArrayList<>();
        outletFactoryList.add(new NgInteractionOutletFactory());
        outletFactoryList.add(new NgOutputportOutletFactory());
        return outletFactoryList;
    }
}
