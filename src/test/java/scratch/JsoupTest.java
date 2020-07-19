package scratch;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Test;
import selab.ui_composite_engine.util.IOUtil;

public class JsoupTest {

    @Test
    public void test01(){
        String str = IOUtil.readFile("test_data/BPEL/auto_gen_patientMonitoringDB.bpel");
        Document doc = Jsoup.parse(str, "", Parser.xmlParser());

        Elements els = doc.select("[portType=tns:pageInteractionPort]");
        for(Element el: els){
            System.out.println(el);
            System.out.println("----");
        }



    }
}


