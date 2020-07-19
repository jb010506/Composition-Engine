package selab.ui_composite_engine.util;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class PathUtil {
    private static final Logger LOG = Logger.getLogger(PathUtil.class.getName());

    public static String combinePath(String... paths){
            File file = new File(paths[0]);
            for (int i = 1; i < paths.length ; i++) {
                file = new File(file, paths[i]);
            }
            return file.getPath();
    }

    public static String getFileName(String path){
        File file = new File(path);
        return file.getName();
    }

    public static String getFileNameWithoutExt(String path){
        File file = new File(path);
        return FilenameUtils.removeExtension(file.getName());
    }

    public static String getAbsolutePath(String path) throws IOException {
        return Paths.get(path).toRealPath().toString();
    }

    public static String getAbsoluteDirOfPath(String path) throws IOException {
        return Paths.get(path).toRealPath().getParent().toString();
    }
}
