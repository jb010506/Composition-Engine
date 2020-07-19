import org.junit.Test;
import selab.ui_composite_engine.Main;

public class MainTest {
    @Test
    public void testMainAutoGenPatientMonitoringDB() {
        String[] args = new String[3];
        args[0] = "auto_gen_patientMonitoringDB_underscore.bpel";
        args[1] = "auto_gen_patientMonitoringDB_underscore.wsdl";
        args[2] = "./output/";

        Main.main(args);
    }

    @Test
    public void testMainAutoGenPeerReview() {
        String[] args = new String[3];
        args[0] = "auto_gen_peerReview.bpel";
        args[1] = "auto_gen_peerReview.wsdl";
        args[2] = "./output/";

        Main.main(args);
    }
}
