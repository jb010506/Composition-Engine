package selab.ui_composite_engine;

import freemarker.template.TemplateException;
import selab.ui_composite_engine.util.logger.LoggerUtils;
import java.io.IOException;
import java.sql.*;

import java.util.logging.Logger;

public class Main {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    static {
        LoggerUtils.config();
    }
    private static WebAppGenerator generator;

    static {
        try {
            generator = new WebAppGenerator();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, SQLException, TemplateException {
        generator.generate();
    }
}
