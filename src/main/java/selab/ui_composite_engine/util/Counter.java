package selab.ui_composite_engine.util;

public class Counter {
    private static int componentCounter = 0;

    public static int getComponentId(){
        return ++componentCounter;
    }
}
