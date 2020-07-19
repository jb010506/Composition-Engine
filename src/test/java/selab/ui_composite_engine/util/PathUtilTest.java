package selab.ui_composite_engine.util;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class PathUtilTest {

    @Test
    public void getAbsolutePath() throws IOException {
        String ret;
        ret = PathUtil.getAbsolutePath("./");
        System.out.println(ret);
    }
}