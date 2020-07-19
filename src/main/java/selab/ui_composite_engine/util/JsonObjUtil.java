package selab.ui_composite_engine.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.*;

public class JsonObjUtil {
    private static Gson gson = new Gson();

    public static Map<String, String> processJsonObject2MapString(JsonObject jsonObject){
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        return gson.fromJson(jsonObject, type);
    }

    public static Set<String> processJsonArray2SetString(JsonArray jsonArray){
        Type type = new TypeToken<Set<String>>(){}.getType();
        return gson.fromJson(jsonArray, type);
    }

    public static Map<String, String> processJsonStringListMapReverseMapString(JsonObject jsonObject){
        Map<String, String> reversedMap = new HashMap<>();
        for(String key: jsonObject.keySet()){
            for(JsonElement el: jsonObject.get(key).getAsJsonArray()){
                reversedMap.put(el.getAsString(), key);
            }
        }
        return reversedMap;
    }
}
