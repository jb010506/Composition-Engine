package selab.ui_composite_engine.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class IOUtil {

    public static String readFile(String path){
        String text = "";
        try {
            text = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    public static void writeFile(String path, String str){
        try {
            FileWriter fw = new FileWriter(path);
            fw.write(str);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void traverseDirectory(File dir, List container) {
        /*
         * Traverse given "directory" file object, and store file paths in the context.
         */

        try {
            File[] files = dir.listFiles();
            for (File file : files) {
                if(!file.isHidden() && !Files.isSymbolicLink(file.toPath())) {
                    if (file.isDirectory()) {
                        traverseDirectory(file, container);
                    }
                    else {
                        container.add(file.getCanonicalPath());
                    }
                }
            }
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public static List<String> traversePath(String path){
        /*
         * Traverse given path and get all file paths recursively.
         */

        File currentDir = new File(path);
        List<String> container = new ArrayList<>();
        if (currentDir.isDirectory()) {
            traverseDirectory(currentDir, container);
        } else {
            container.add(path);
        }
        return container;
    }

    public static List<String> traversePath(String path, String suffix) {
        /*
         * Traverse given path and get all file path with given suffix recursively.
         */

        List<String> specificPaths = new ArrayList<>();
        for (String p : traversePath(path)) {
            if (p.endsWith(suffix)){
                specificPaths.add(p);
            }
        }
        return specificPaths;
    }
}
