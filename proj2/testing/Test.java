package testing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("master", "7afb");
        map.put("dev", "null");
        map.put("test", "4adc");
        System.out.println(new HashSet<>(map.values()).size());
        System.out.println(map.containsValue(null));
    }
}
