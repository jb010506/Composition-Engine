package selab.ui_composite_engine.parser.wsdlbpel;

import selab.ui_composite_engine.model.BpelWsdlDefinition;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;

public class WsdlBpelParser {

    private WSDLReader wsdlReader;

    public WsdlBpelParser(){
        WSDLFactory factory = null;
        try {
            factory = WSDLFactory.newInstance();
        } catch (WSDLException e) {
            System.err.println("Unable to initialize WSDL Factory.");
            e.printStackTrace();
            System.exit(1);
        }
        this.wsdlReader = factory.newWSDLReader();
        this.wsdlReader.setFeature("javax.wsdl.verbose", false);
        this.wsdlReader.setFeature("javax.wsdl.importDocuments", true);
    }

    public BpelWsdlDefinition parse(String wsdlPath) {
        // Parse WSDL and return Definition, which is a information wrapper that aggregates parsed wsdlDefinition
        Definition wsdlDefinition = null;
        try {
            // Use wsdlReader to parse wsdlPath
            wsdlDefinition = this.wsdlReader.readWSDL(wsdlPath);
        } catch (WSDLException e) {
            System.err.println("Unable to parse WSDL.");
            e.printStackTrace();
            System.exit(1);
        }

        return new BpelWsdlDefinition(wsdlDefinition);
    }
}
