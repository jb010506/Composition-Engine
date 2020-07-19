package selab.ui_composite_engine.operator;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import freemarker.core.XHTMLOutputFormat;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import selab.ui_composite_engine.component.RenderingComponent;
import selab.ui_composite_engine.metadata.UicMetadata;
import selab.ui_composite_engine.util.FreeMarkerUtil;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;
import java.util.logging.Logger;

public class NgBasicTemplateOperator extends AtomicUIComponentMatcher{

    private static final Logger LOG = Logger.getLogger(NgBasicTemplateOperator.class.getName());

    private static final String templateName = "ng-basic-template-operator.html.ftl";

    Map<String, Object> dataMap = new HashMap<>();
    Map<String, Object> attributeMap = new HashMap<>();
    Map<String, Object> propertyBindingMap = new HashMap<>();
    Map<String, Object> eventBindingMap = new HashMap<>();
    List<String> interpolationList = new ArrayList<>();

    @Override
    public void render(RenderingComponent renderingComponent) {
        UicMetadata uicMetadata = renderingComponent.getUicMetadata();
        String componentType = uicMetadata.getNgType();
        String componentId = String.valueOf(renderingComponent.getComponentId());
        String portType = uicMetadata.getPortType();

        this.dataMap.put("selector",uicMetadata.getSelectorName());
        this.dataMap.put("type", componentType);
        if(componentType.equals("Directive")) {
            this.dataMap.put("directive", uicMetadata.getSelectorName());
            this.dataMap.put("hostListener", uicMetadata.getHostListener());
            this.dataMap.put("selector",uicMetadata.getSelectorName());
        }
        else{ // componentType.equals("Component")
            this.dataMap.put("component", uicMetadata.getSelectorName());
        }

        if(this.isInput){
            JsonObject contentObject = uicMetadata.getContent();
            String partType = contentObject.get("part").getAsJsonObject().get("type").getAsString();
            if(!partType.equals("boolean")){
                // Input Component
                JsonObject textObj = contentObject.get("text").getAsJsonObject();
                attributeMap.put("placeholder", textObj.get("placeholder").getAsString());
                if(textObj.has("label")){
                    dataMap.put("label",textObj.get("label").getAsString());
                }
                dataMap.put("ngModel", "ngModel" + componentId);
                uicMetadata.addTsNgModelInitMap("ngModel" + componentId, "string");
                uicMetadata.addTsNgModelParamMap(renderingComponent.getName(), "ngModel" + componentId);
                dataMap.put("endTag", false);
            }
            else{
                // Button Component
                JsonObject textObj = contentObject.get("text").getAsJsonObject();
                this.attributeMap.put("label", textObj.get("placeholder").getAsString());
                this.propertyBindingMap.put("disabled", "unready");
                this.eventBindingMap.put("onClick", "handleClick");
                dataMap.put("endTag", true);

                // Button's <var>true</var>
                uicMetadata.addTsAttributeString("button" + componentId, "true");
                uicMetadata.addTsNgModelParamMap(renderingComponent.getName(), "button" + componentId); //

                renderingComponent.getUicMetadata().setHandleClickNeeded(false);
            }
        }
        // Card Component
        else{
            this.interpolationList.add("content");
            uicMetadata.addTsInputInitMap("content", "string");
            dataMap.put("endTag", true);

            renderingComponent.getUicMetadata().setHandleClickNeeded(false);
        }

        // HTML element id
        dataMap.put("id", "uic" + componentId);
        dataMap.put("count", componentId);
        Template template = FreeMarkerUtil.getInstance().getTemplate(templateName);


        if(portType.startsWith("html")){
            template = FreeMarkerUtil.getInstance().getTemplate("ng-basic-"+portType+".html.ftl");
            JsonObject contentObject = uicMetadata.getContent();
            if(portType.equals("htmlInput")){
                try{
                    String type = contentObject.get("type").getAsString();
                    dataMap.put("type",type);
                }catch(Exception e){
                    System.out.println("Exception in htmlInput parsing");
                }

            }
            if(portType.equals("htmlProgress")){
                String value=contentObject.get("value").getAsString();
                String max=contentObject.get("max").getAsString();
                dataMap.put("value",value);
                dataMap.put("max",max);
            }
            else if(portType.equals("htmlAudio")){
                String src = contentObject.get("src").getAsString();
                dataMap.put("src",src);
            }
            else if(portType.equals("htmlVideo")){
                String src = contentObject.get("src").getAsString();
                String width = contentObject.get("width").getAsString();
                String height = contentObject.get("height").getAsString();
                dataMap.put("src",src);
                dataMap.put("width",width);
                dataMap.put("height",height);
            }
            else if(portType.equals("htmlTextarea")){
                String rows = contentObject.get("rows").getAsString();
                String cols = contentObject.get("cols").getAsString();
                dataMap.put("rows",rows);
                dataMap.put("cols",cols);
            }
            else if(portType.equals("htmlSelect")){
                JsonArray optionsJA= contentObject.get("options").getAsJsonArray();
                List<String>options = new ArrayList<>();
                if(optionsJA!=null){
                    for(int i=0;i<optionsJA.size();i++){
                        System.out.println(optionsJA.get(i).toString());
                        options.add(optionsJA.get(i).getAsString());
                    }
                }
                dataMap.put("options",options);
            }
            else if(portType.equals("htmlFieldset")){
                String legend = contentObject.get("legend").getAsString();
                dataMap.put("legend",legend);
            }
            else if(portType.equals("htmlIframe")){
                String src = contentObject.get("src").getAsString();
                dataMap.put("src",src);
            }
            else if(portType.equals("htmlImage")){
                String src = contentObject.get("src").getAsString();
                dataMap.put("src",src);
            }
            else if(portType.equals("htmlHypertext")){
                String href = contentObject.get("href").getAsString();
                dataMap.put("href",href);
            }
            else if(portType.equals("htmlUnorderedlist")){
                JsonArray listJA= contentObject.get("list").getAsJsonArray();
                List<String>list = new ArrayList<>();
                if(listJA!=null){
                    for(int i=0;i<listJA.size();i++){
                        //System.out.println(listJA.get(i).toString());
                        list.add(listJA.get(i).getAsString());
                    }
                }
                dataMap.put("list",list);
            }
            else if(portType.equals("htmlOrderedlist")){
                JsonArray listJA= contentObject.get("list").getAsJsonArray();
                List<String>list = new ArrayList<>();
                if(listJA!=null){
                    for(int i=0;i<listJA.size();i++){
                        //System.out.println(listJA.get(i).toString());
                        list.add(listJA.get(i).getAsString());
                    }
                }
                String start = contentObject.get("start").getAsString();
                dataMap.put("list",list);
                dataMap.put("start",start);
            }
            else if(portType.equals("htmlTable")){
                JsonArray headersJA= contentObject.get("headers").getAsJsonArray();
                JsonArray rowsJA= contentObject.get("rows").getAsJsonArray();
                List<String>headers = new ArrayList<>();
                List<List<String>> rows = new ArrayList<>();
                if(headersJA!=null){
                    for(int i=0;i<headersJA.size();i++){
                        String header = headersJA.get(i).getAsString();
                        headers.add(header);
                    }
                }
                if(rowsJA!=null){
                    for(int i=0;i<rowsJA.size();i++){
                        JsonObject r = rowsJA.get(i).getAsJsonObject();
                        List<String> row =new ArrayList<>();
                        for(int j=0;j<headers.size();j++){
                            //System.out.println(r.get(headers.get(j)).getAsString());
                            row.add(r.get(headers.get(j)).getAsString());
                        }
                        rows.add(row);
                    }

                }
                dataMap.put("headers",headers);
                dataMap.put("rows",rows);
            }

        }

        else if(portType.startsWith("mat")){
            template = FreeMarkerUtil.getInstance().getTemplate("ng-basic-"+portType+".html.ftl");
            JsonObject contentObject = uicMetadata.getContent();
            if(portType.equals("matBadge")){
                dataMap.put("badge",contentObject.get("badge").getAsString());
            }
            else if(portType.equals("matProgressSpinner")){
                String value = null;
                if(contentObject.has("value")){
                    value = contentObject.get("value").getAsString();
                }
                if(value!=null) {
                    dataMap.put("value", value);
                }
            }
            else if(portType.equals("matProgressBar")){
                String mode = null;
                String value = null;
                if(contentObject.has("mode")){
                    mode = contentObject.get("mode").getAsString();
                }
                if(mode!=null) {
                    dataMap.put("mode", mode);
                }
                if(contentObject.has("value")){
                    value = contentObject.get("value").getAsString();
                }
                if(value!=null) {
                    dataMap.put("value", value);
                }
            }
            else if(portType.equals("matMenu")){
                JsonArray optionsJA= contentObject.get("items").getAsJsonArray();
                List<String>items = new ArrayList<>();
                String icon = contentObject.get("icon").getAsString();
                if(optionsJA!=null){
                    for(int i=0;i<optionsJA.size();i++){
                        System.out.println(optionsJA.get(i).toString());
                        items.add(optionsJA.get(i).getAsString());
                    }
                }
                dataMap.put("items",items);
                dataMap.put("icon",icon);
            }
            else if(portType.equals("matList")){
                JsonArray itemsJA = contentObject.get("items").getAsJsonArray();
                List<String> items = new ArrayList<>();
                if(itemsJA!=null){
                    for(int i=0;i<itemsJA.size();i++){
                        System.out.println(itemsJA.get(i).toString());
                        items.add(itemsJA.get(i).getAsString());
                    }
                }
                dataMap.put("items",items);
            }
            else if(portType.equals("matCard")){
                JsonArray actionsJA = contentObject.get("actions").getAsJsonArray();
                String src = contentObject.get("src").getAsString();
                List<String> actions = new ArrayList<>();
                if(actionsJA!=null){
                    for(int i=0;i<actionsJA.size();i++){
                        System.out.println(actionsJA.get(i).toString());
                        actions.add(actionsJA.get(i).getAsString());
                    }
                }
                dataMap.put("actions",actions);
                dataMap.put("src",src);
            }
            else if(portType.equals("matExpansionPanel")){
                String title = contentObject.get("title").getAsString();
                String description = contentObject.get("description").getAsString();
                dataMap.put("title",title);
                dataMap.put("description",description);
            }
            else if(portType.equals("matGridList")){
                String cols = contentObject.get("cols").getAsString();
                String rowsHeight = contentObject.get("rowsHeight").getAsString();
                List<String>tiles=this.getAsList(contentObject,"tiles");
                dataMap.put("cols",cols);
                dataMap.put("rowsHeight",rowsHeight);
                dataMap.put("tiles",tiles);
            }
            else if(portType.equals("matStepper")){
                List<String> steps = getAsList(contentObject,"steps");
                dataMap.put("steps",steps);
            }
            else if(portType.equals("matTab")){
                Map<String, String> tabs = getAsKVList(contentObject,"tabs");
                dataMap.put("tabs",tabs);

            }
            else if(portType.equals("matFormField")){
                String appearance = contentObject.get("appearance").getAsString();
                String placeholder = contentObject.get("placeholder").getAsString();
                String icon = contentObject.get("icon").getAsString();
                String hint = contentObject.get("hint").getAsString();
                dataMap.put("appearance", appearance);
                dataMap.put("placeholder", placeholder);
                dataMap.put("icon", icon);
                dataMap.put("hint", hint);
            }
            else if(portType.equals("matRadio")){
                Map<String, String> radios = getAsKVList(contentObject,"radios");
                dataMap.put("radios",radios);
            }
            else if (portType.equals("matInput")){
                String type = contentObject.get("type").getAsString();
                dataMap.put("type",type);
            }
            else if(portType.equals("matSlider")){

            }
            else if(portType.equals("matSelect")){
                Map<String, String> options = getAsKVList(contentObject,"options");
                String label=contentObject.get("label").getAsString();
                dataMap.put("options", options);
                dataMap.put("label",label);
            }
            else if(portType.equals("matSlideToggle")){
                String label=contentObject.get("label").getAsString();
                dataMap.put("label",label);
            }
            else if(portType.equals("matCheckbox")){
                String label=contentObject.get("label").getAsString();
                dataMap.put("label",label);
            }
            else if(portType.equals("matDatepicker")){
                String label=contentObject.get("label").getAsString();
                String icon=contentObject.get("icon").getAsString();
                dataMap.put("label",label);
                dataMap.put("icon",icon);
            }
            else if(portType.equals("matAutocomplete")){
                List<String> options = getAsList(contentObject,"options");
                dataMap.put("options",options);
            }
            else if(portType.equals("matPaginator")){
                String length = contentObject.get("length").getAsString();
                String pageSize = contentObject.get("pageSize").getAsString();
                dataMap.put("length",length);
                dataMap.put("pageSize",pageSize);
            }
            else if(portType.equals("matSort")){
                List<String> headers = getAsList(contentObject,"headers");
                List<String> rows = getAsList(contentObject,"rows");
                dataMap.put("headers",headers);
                dataMap.put("rows",rows);

            }
            else if(portType.equals("matChips")){
                List<String> chips = getAsList(contentObject,"chips");
                dataMap.put("chips",chips);

            }
            else if(portType.equals("matRipple")){
                String label = contentObject.get("label").getAsString();
                dataMap.put("label",label);
            }

        }


        this.buildDataMap();

        Writer stringWriter = new StringWriter();
        try {
            template.process(dataMap, stringWriter);
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String htmlStr = stringWriter.toString().trim()
                .replaceAll(" +", " ")
                .replace("\n","");

        uicMetadata.setHtmlStr(htmlStr);
        System.out.println(htmlStr);
        // CSS
        uicMetadata.setCssStr(
                "#uic" + componentId + " {" + uicMetadata.getUicStyles().getCssMapAsString() + "}"
        );
    }

    private void buildDataMap(){
        this.dataMap.put("attributes", attributeMap);
        this.dataMap.put("propertyBindings", propertyBindingMap);
        this.dataMap.put("eventBindings", eventBindingMap);
        this.dataMap.put("interpolations", interpolationList);
    }

    private List<String> getAsList(JsonObject contentObject, String item){
        JsonArray actionsJA = contentObject.get(item).getAsJsonArray();
        List<String> actions = new ArrayList<>();
        if(actionsJA!=null){
            for(int i=0;i<actionsJA.size();i++){
                System.out.println(actionsJA.get(i).toString());
                actions.add(actionsJA.get(i).getAsString());
            }
        }
        return actions;
    }

    private Map<String, String> getAsKVList(JsonObject contentObject, String item){
        JsonObject actionsJA = contentObject.get(item).getAsJsonObject();
        System.out.println(actionsJA.toString());
        Map<String, String> actions = new LinkedHashMap<>();
        if(actionsJA!=null){
            for(Map.Entry<String, JsonElement> entry: actionsJA.entrySet()){
                actions.put(entry.getKey(),actionsJA.get(entry.getKey()).getAsString());
            }
        }
        System.out.println(actions);
        return actions;
    }
}
