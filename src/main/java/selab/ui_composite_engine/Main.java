package selab.ui_composite_engine;

import freemarker.template.TemplateException;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONObject;
import selab.ui_composite_engine.renderer.NgWebAppRenderer;
import selab.ui_composite_engine.util.BpelUtil;
import selab.ui_composite_engine.util.WsdlUtil;
import selab.ui_composite_engine.util.logger.LoggerUtils;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    static {
        LoggerUtils.config();
    }
    private static WebAppGenerator generator = new WebAppGenerator();

    public static void main(String[] args) throws IOException, SQLException, TemplateException {
        NgWebAppRenderer webAppRenderer = new NgWebAppRenderer(Configuration.BASE_DIR_PATH, "Demo");

        Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306","root","");
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("use demo");
        ResultSet rs = stmt.executeQuery("select * from pages");

        JSONObject pdl = new JSONObject();
        String pageSelector = "";
        Map<Object,Object> components = new HashMap<>();
        List<String> componentSelector = new LinkedList<>();
        while(rs.next()){
           pdl = new JSONObject(rs.getString("pdl"));
           pageSelector = pdl.getString("selector");
           webAppRenderer.exportPageComponentTS(pageSelector);
        }

        ResultSet rs2 = stmt.executeQuery("select * from templates");
        while(rs2.next()){
            String selector = rs2.getString("selector");
            String selector_capitalized = selector.substring(0,1).toUpperCase()+selector.substring(1);
            String html = rs2.getString("html");
            componentSelector.add(selector);
            webAppRenderer.exportComponentTS(pageSelector,selector);
            webAppRenderer.exportComponentHTML(pageSelector,selector,html);
            components.put(selector_capitalized,selector);
        }

        String page_capitalized = pageSelector.substring(0,1).toUpperCase()+pageSelector.substring(1);
        webAppRenderer.exportPageComponentHTML(pageSelector,componentSelector);
        webAppRenderer.exportAppModules(page_capitalized,pageSelector,components);
    }
}
