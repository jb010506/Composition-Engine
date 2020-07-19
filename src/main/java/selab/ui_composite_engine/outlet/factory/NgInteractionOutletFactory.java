package selab.ui_composite_engine.outlet.factory;

import selab.ui_composite_engine.component.*;
import selab.ui_composite_engine.configurer.ConfigurerVisitor;
import selab.ui_composite_engine.configurer.porttype.NavigationPortConfigurerVisitor;
import selab.ui_composite_engine.configurer.porttype.PageInteractionPortConfigurerVisitor;
import selab.ui_composite_engine.configurer.porttype.StartInteractionPortConfigurerVisitor;
import selab.ui_composite_engine.operator.NgBasicTemplateOperator;
import selab.ui_composite_engine.operator.NgOperationStrcombOperator;
import selab.ui_composite_engine.operator.NgFormTemplateOperator;
import selab.ui_composite_engine.model.*;

import java.util.ArrayList;
import java.util.List;

public class NgInteractionOutletFactory implements OutletFactory{

    private final String portType = "pageInteractionPort";

    @Override
    public List<RenderingComponent> createComponents(BpelWsdlDefinition bpelWsdlDefinition){
        ArrayList<RenderingComponent> compositedList = new ArrayList<>();

        List<BpelDefinition> bpelDefinitionList = bpelWsdlDefinition.getBpelDefinitions();

        // only one bpel file here
        for(BpelDefinition bpelDefinition: bpelDefinitionList){
            compositedList.add(createPageComponent(bpelDefinition));
        }

        return compositedList;
    }

    @Override
    public List<ConfigurerVisitor> createConfigurerVisitors() {
        List<ConfigurerVisitor> visitors = new ArrayList<>();
        visitors.add(new NavigationPortConfigurerVisitor());
        visitors.add(new StartInteractionPortConfigurerVisitor());
        visitors.add(new PageInteractionPortConfigurerVisitor());
        return visitors;
    }

    private RenderingComponent createPageComponent(BpelDefinition bpelDefinition){
        PageComponent pageComponent = new PageComponent(bpelDefinition);
        pageComponent.setOperator(new NgFormTemplateOperator());

        for(OperationWrapper operationWrapper:
                bpelDefinition.getOperationsByPortType(this.portType)){

//            System.out.println(operationWrapper.getInputMessage());
//            System.out.println("--------");
             pageComponent.addSubComponent(createOperationComponent(operationWrapper));
        }

        return pageComponent;
    }

    private RenderingComponent createOperationComponent(OperationWrapper operationWrapper) {
        MessageWrapper messageWrapper = operationWrapper.getInputMessage();
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
