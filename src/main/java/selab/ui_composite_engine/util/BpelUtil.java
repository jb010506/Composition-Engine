package selab.ui_composite_engine.util;

import selab.ui_composite_engine.Configuration;

import java.io.IOException;

public class BpelUtil {

    public static String getAbsolutePath(String path) throws IOException {
        if (path.matches("^(http|https|ftp)://.*$")) return path;
        try {
            String bpelDirPath = PathUtil.combinePath(Configuration.BPEL_DIR_PATH, path);
            return PathUtil.getAbsolutePath(bpelDirPath);
        } catch (Exception e){
            return PathUtil.getAbsolutePath(path);
        }
    }
}
