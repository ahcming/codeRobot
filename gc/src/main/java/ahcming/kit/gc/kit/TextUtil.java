package ahcming.kit.gc.kit;

import java.util.*;

public class TextUtil {

    public static String firstToUpper(String text) {
        char[] chs = text.toCharArray();
        if(chs[0] >= 'a' && chs[0] < 'z') {
            chs[0] = (char)(chs[0] - 32);
        }

        return new String(chs);
    }

    public static String firstToLower(String text) {
        char[] chs = text.toCharArray();
        if(chs[0] >= 'A' && chs[0] < 'Z') {
            chs[0] = (char)(chs[0] + 32);
        }

        return new String(chs);
    }

    public static String toCamel(String name) {
        String camelName = Arrays.stream(name.split("\\.|_"))
                .map(TextUtil::firstToUpper)
                .reduce("", (a, b) -> a + b);

        return firstToLower(camelName);
    }
}
