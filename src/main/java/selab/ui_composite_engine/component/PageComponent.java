package selab.ui_composite_engine.component;

import selab.ui_composite_engine.model.BpelDefinition;

public class PageComponent extends CompositeComponent{

    private BpelDefinition bpelDefinition;

    public PageComponent(BpelDefinition bpelDefinition) {
        super(bpelDefinition.getName());
        this.bpelDefinition = bpelDefinition;
    }

    @Override
    public void compose() {
        super.compose();
        this.getOperator().operate(this);
        this.getUicMetadata().setMainComponent(this.bpelDefinition.isMainBpel());
        this.getUicMetadata().setInfo("partnerLink", this.bpelDefinition.getPartnerLink());
        this.getUicMetadata().setInfo("portTypeNamespace", this.bpelDefinition.getQname().getNamespaceURI());
        this.getOperator().render(this);
    }

}
