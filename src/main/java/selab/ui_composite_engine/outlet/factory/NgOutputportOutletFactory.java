package selab.ui_composite_engine.outlet.factory;

import selab.ui_composite_engine.component.BasicComponent;
import selab.ui_composite_engine.component.OperationComponent;
import selab.ui_composite_engine.component.PageComponent;
import selab.ui_composite_engine.component.RenderingComponent;
import selab.ui_composite_engine.configurer.ConfigurerVisitor;
import selab.ui_composite_engine.configurer.porttype.OutputPortConfigurerVisitor;
import selab.ui_composite_engine.model.*;
import selab.ui_composite_engine.operator.NgBasicTemplateOperator;
import selab.ui_composite_engine.operator.NgOperationStrcombOperator;
import selab.ui_composite_engine.operator.NgListViewDynamicLoaderOperator;

import java.util.ArrayList;
import java.util.List;

public class NgOutputportOutletFactory implements OutletFactory {

    private final String portType = "outputPort";

    public List<RenderingComponent> createComponents(BpelWsdlDefinition bpelWsdlDefinition){
        ArrayList<RenderingComponent> compositedList = new ArrayList<>();

        List<BpelDefinition> bpelDefinitionList = bpelWsdlDefinition.getBpelDefinitions();

        for(BpelDefinition bpelDefinition: bpelDefinitionList){
            compositedList.add(createPageComponent(bpelDefinition));
        }

        return compositedList;
    }

    @Override
    public List<ConfigurerVisitor> createConfigurerVisitors() {
        List<ConfigurerVisitor> visitors = new ArrayList<>();
        visitors.add(new OutputPortConfigurerVisitor());
        return visitors;

    }

    private RenderingComponent createPageComponent(BpelDefinition bpelDefinition){
        PageComponent pageComponent = new PageComponent(bpelDefinition);
        pageComponent.setOperator(new NgListViewDynamicLoaderOperator());

        for(OperationWrapper operationWrapper:
                bpelDefinition.getOperationsByPortType(this.portType)){
            pageComponent.addSubComponent(createOperationComponent(operationWrapper));
        }

        return pageComponent;
    }

    private RenderingComponent createOperationComponent(OperationWrapper operationWrapper) {
        MessageWrapper messageWrapper = operationWrapper.getOutputMessage();
        OperationComponent operationComponent = new OperationComponent(operationWrapper, messageWrapper);
        operationComponent.setOperator(new NgOperationStrcombOperator());
        for(MessagePartWrapper messagePartWrapper: messageWrapper.getParts()){
            operationComponent.addSubComponent(createBasicComponent(messagePartWrapper));
        }

        return operationComponent;
    }

    private RenderingComponent createBasicComponent(MessagePartWrapper messagePartWrapper){
        BasicComponent basicComponent = new BasicComponent(messagePartWrapper);
        basicComponent.setOperator(new NgBasicTemplateOperator());
        return basicComponent;
    }
}
