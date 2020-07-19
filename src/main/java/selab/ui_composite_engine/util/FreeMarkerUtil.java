package selab.ui_composite_engine.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import selab.ui_composite_engine.TemplateConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class FreeMarkerUtil {

    private static final Logger LOG = Logger.getLogger(FreeMarkerUtil.class.getName());

    private static FreeMarkerUtil instance = new FreeMarkerUtil();
    public static FreeMarkerUtil getInstance(){
        return FreeMarkerUtil.instance;
    }

    Configuration configuration = null;

    private FreeMarkerUtil(){
        this.configuration = new Configuration(Configuration.VERSION_2_3_28);
        try {
            configuration.setDirectoryForTemplateLoading(new File(TemplateConfiguration.TEMPLATE_DIR_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Template getTemplate(String templatePath){
        try {
            Template template = configuration.getTemplate(templatePath);
            return template;
        } catch (IOException e) {
            LOG.warning("Cannot find template");
            e.printStackTrace();
        }
        return null;
    }

}
