package selab.ui_composite_engine;

import selab.ui_composite_engine.component.RenderingComponent;
import selab.ui_composite_engine.configurer.ConfigurerVisitor;
import selab.ui_composite_engine.layout.Layout;
import selab.ui_composite_engine.layout.LayoutResolver;
import selab.ui_composite_engine.model.BpelWsdlDefinition;
import selab.ui_composite_engine.outlet.factory.OutletFactory;
import selab.ui_composite_engine.outlet.factory.NgOutletFactoryResolver;
import selab.ui_composite_engine.parser.wsdlbpel.WsdlBpelParser;
import selab.ui_composite_engine.renderer.NgWebAppRenderer;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class WebAppGenerator {

    private static final Logger LOG = Logger.getLogger(WebAppGenerator.class.getName());

    private WsdlBpelParser wsdlBpelParser;
    private NgWebAppRenderer webAppRenderer;

    public WebAppGenerator(){
        this.wsdlBpelParser = new WsdlBpelParser();
    }

    public void generate(String bpelPath, String wsdlPath, String webAppPath) throws IOException{

        // Parse Bpel's Wsdl
        String bpelPath1 = "/Users/william/Desktop/composite_UI_engine/test_data/BPEL/testUI_beautify.bpel";
        String wsdlPath1 = "/Users/william/Desktop/composite_UI_engine/test_data/WSDL/testUI_beautify.wsdl";
        BpelWsdlDefinition bpelWsdlDefinition = wsdlBpelParser.parse(wsdlPath);
        // Generate WebApp Writer
        this.webAppRenderer = new NgWebAppRenderer( webAppPath, bpelWsdlDefinition.getName());

        // Generate Composite Component
        List<OutletFactory> outletFactoryList = NgOutletFactoryResolver.resolveFactories(bpelWsdlDefinition);
        for(OutletFactory outletFactory: outletFactoryList){ // An outletFactory would generate one or more composite components
            try {
                // Create RenderingComponents and insert an CompositionOperator for each
                List<RenderingComponent> compositeComponents = outletFactory.createComponents(bpelWsdlDefinition);

                // Create ConfigurerVisitor, which would be used to collect components info and generate typescript config files
                List<ConfigurerVisitor> configurerVisitors = outletFactory.createConfigurerVisitors();

                //Page component
                for(RenderingComponent renderingComponent: compositeComponents){
                    renderingComponent.compose(); // Propagate info from basic components to page components
                    LOG.info("Composed component successfully: " + renderingComponent.getName() + " within " + renderingComponent.getClass().getName());

                    // Use visitors to collect info
                    for(ConfigurerVisitor visitor: configurerVisitors){
                        visitor.doubleDispatch(renderingComponent);
                    }
                }

                // Call visitors to export config files
                for(ConfigurerVisitor visitor: configurerVisitors){
                    visitor.process();
                }

            }catch (NullPointerException e){
                e.printStackTrace();
                LOG.info("Composed component failed.");
            }
        }

        // Generate Layout from Layout Template
        try {
            String path= "/Users/william/Desktop/composite_UI_engine/test_data/LDL/sample";
            String path1= "/Users/william/Desktop/composite_UI_engine/test_data/AngularFrameworkSrc/src/assets";
            for(int i=1;i<4;i++) {
                webAppRenderer.buildLayout(path+String.valueOf(i)+"/layout", String.valueOf(i));
                webAppRenderer.buildLayoutAssets(path1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Layout layout = LayoutResolver.resolveLayout(bpelWsdlDefinition);
        layout.render();


        this.webAppRenderer.flush();

    }
}
