package selab.ui_composite_engine;

import freemarker.template.TemplateException;
import org.json.JSONObject;
import selab.ui_composite_engine.renderer.NgWebAppRenderer;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class WebAppGenerator {

    private static final Logger LOG = Logger.getLogger(WebAppGenerator.class.getName());

    JSONObject pdl = new JSONObject();
    NgWebAppRenderer webAppRenderer = new NgWebAppRenderer(Configuration.BASE_DIR_PATH, "Demo");
    Connection connection;
    Map<Object,Object> pageSelectorMap = new HashMap<>();
    Map<Object,Object> components = new HashMap<>();
    Map<Object,Object> selectorPageMap = new HashMap<>();

    public WebAppGenerator() throws IOException {
    }

    public void generate() throws IOException, SQLException, TemplateException {

        boolean isLayoutBuild = false;
        connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306","root","");
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("use demo");
        ResultSet rs = stmt.executeQuery("select * from pages");

        String pageSelector = "";
        String layoutName;
        JSONObject layoutComponent;
        while(rs.next()){

            pdl = new JSONObject(rs.getString("pdl"));
            if(isLayoutBuild==false) {
                layoutName = pdl.getJSONObject("componentList").getString("selector");
                layoutComponent = pdl.getJSONObject("componentList");
                webAppRenderer.buildNgLayout(layoutName);
                webAppRenderer.exportLayoutTS(layoutComponent);
                webAppRenderer.exportLayoutHTML(layoutComponent);
                isLayoutBuild = true;
            }
            pageSelector = pdl.getString("selector");


            List<String> componentSelector = new LinkedList<>();

            ResultSet rs2 = stmt.executeQuery("select * from templates");
            while(rs2.next()) {
                if (rs2.getString("page").equals(pageSelector)) {
                    String selector = rs2.getString("selector");
                    String selector_capitalized = selector.substring(0, 1).toUpperCase() + selector.substring(1);
                    String html = rs2.getString("html");
                    componentSelector.add(selector);
                    webAppRenderer.exportComponentTS(pageSelector, selector);
                    webAppRenderer.exportComponentHTML(pageSelector, selector, html);
                    components.put(selector_capitalized, selector);

                    String page_capitalized = pageSelector.substring(0, 1).toUpperCase() + pageSelector.substring(1);
                    pageSelectorMap.put(page_capitalized,pageSelector);
                    selectorPageMap.put(selector, pageSelector);
                }
            }
            webAppRenderer.exportPageComponentTS(pageSelector);
            webAppRenderer.exportPageComponentHTML(pageSelector, componentSelector);
        }

        ResultSet rs3 = stmt.executeQuery("select * from navigation");
        while(rs3.next()){
            String ndl = rs3.getString("ndl");
            webAppRenderer.exportNavigation(ndl);
        }
        connection.close();
        // the last page overrides the same component within all pages
        System.out.println(selectorPageMap);
        webAppRenderer.exportAppModules(pageSelectorMap, components, selectorPageMap);


    }
}
