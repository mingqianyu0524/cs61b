package testing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) {
        String regex = "[-_.A-Za-z0-9]+";
        Pattern p = Pattern.compile(".*");
        Matcher m = p.matcher("test.txt");
        boolean b = m.matches();
        System.out.println(b);
    }
}
