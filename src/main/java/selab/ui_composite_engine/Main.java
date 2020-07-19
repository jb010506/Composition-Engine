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
import java.util.LinkedList;
import java.util.List;
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
        List<String> components = new LinkedList<>();
        while(rs.next()){
           pdl = new JSONObject(rs.getString("pdl"));
           pageSelector = pdl.getString("selector");
           webAppRenderer.exportPageComponentTS(pageSelector);
        }

        ResultSet rs2 = stmt.executeQuery("select * from templates");
        while(rs2.next()){
            String id = rs2.getString("selector");
            String html = rs2.getString("html");
            components.add(html);
        }

        webAppRenderer.exportPageComponentHTML(pageSelector,components);
    }
}
