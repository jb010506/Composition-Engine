package selab.ui_composite_engine.parser.wsdlbpel;

import com.ibm.wsdl.ServiceImpl;
import selab.ui_composite_engine.util.BpelUtil;

import javax.wsdl.Definition;
import javax.wsdl.Import;
import javax.wsdl.extensions.UnknownExtensibilityElement;
import javax.xml.namespace.QName;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;


/**
 * This class recursively parse WSDL Definition and trace all its imported sub-wsdl recursively.
 * A service in a wsdl file refers to a bpel path.
 * Return the mapping with all the bpel paths and their WSDL service-QName
 *
 * Note: We cannot use (javax.wsdl.Definition) def.getAllServices() to extract services,
 *       It would cause endless loop
 * */
public class ServiceExtractor {
    private static final Logger LOG = Logger.getLogger(ServiceExtractor.class.getName());

    private Set<Definition> defSet;

    public Map<QName, String> extractAllServiceBPELPaths(Definition def){

        Map<QName, String> BpelPathMap = new HashMap<>();

        @SuppressWarnings("unchecked") // Extract Services recursively in WSDL files
        Map<QName, ServiceImpl> servicesMap = (HashMap<QName, ServiceImpl>) extractAllServices(def);

        // Filter and extract BPEL Paths in services
        for(Map.Entry<QName, ServiceImpl> serviceEntry: servicesMap.entrySet()){
            ServiceImpl serviceImpl = serviceEntry.getValue();
            if(serviceImpl.getExtensibilityElements().size() > 0 &&
                    serviceImpl.getExtensibilityElements().get(0)
                            instanceof UnknownExtensibilityElement){ // check ext element exists
                UnknownExtensibilityElement unknownEl =
                        (UnknownExtensibilityElement)serviceImpl.getExtensibilityElements().get(0);
                if(unknownEl.getElement().getAttributes().getNamedItem("bpel") != null &&
                        unknownEl.getElement().getAttributes().getNamedItem("wsdl") != null){
                    String bpelPath = unknownEl.getElement().getAttributes().getNamedItem("bpel").getNodeValue();
                    try {
                        System.out.println(bpelPath);
                        bpelPath = BpelUtil.getAbsolutePath(bpelPath);
                        BpelPathMap.put(serviceImpl.getQName(), bpelPath);
                    } catch (IOException e) {
                        LOG.warning("BPEL file Path doesn't exists: " + bpelPath);
                        e.printStackTrace();
                    }
                }
            }
        }
        return BpelPathMap;
    }

    private Map extractAllServices(Definition def){
        this.defSet = new HashSet<>();
        return this.extractAllServicesHelper(def);
    }

    private Map extractAllServicesHelper(Definition headDef) // BFS
    {
        if(this.defSet.contains(headDef)) return null; // Terminal Condition

        this.defSet.add(headDef);

        Map allServices = new HashMap(headDef.getServices());
        Map importMap = headDef.getImports();
        Iterator mapItr = importMap.values().iterator();
        while(mapItr.hasNext())
        {
            Vector importDefs = (Vector) mapItr.next();
            Iterator vecItr = importDefs.iterator();
            while(vecItr.hasNext())
            {
                Import importDef = (Import) vecItr.next();
                Definition importedDef = importDef.getDefinition();
                //importedDef may be null (e.g. if the javax.wsdl.importDocuments feature is disabled).
                if(importedDef != null)
                {
                    Map serviceMap = this.extractAllServicesHelper(importedDef); // recursively parse sub services
                    if(serviceMap!=null){
                        allServices.putAll(serviceMap);
                    }

                }
            }
        }
        return allServices;
    }
}
