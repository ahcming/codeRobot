package ahcming.kit.gc.parse;

import ahcming.kit.gc.kit.TextUtil;
import org.testng.annotations.Test;

public class TextUtilTest {

    @Test
    public void testFirstToUpper() {
        System.out.println(TextUtil.firstToUpper("mapper.xml.path"));
        System.out.println(TextUtil.firstToUpper("MAPPER.xml.path"));
    }

    @Test
    public void testFirstToLower() {
        System.out.println(TextUtil.firstToLower("mapper.xml.path"));
        System.out.println(TextUtil.firstToLower("MAPPER.xml.path"));
    }

    @Test
    public void testToCamel() {
        System.out.println(TextUtil.toCamel("driver"));
        System.out.println(TextUtil.toCamel("mapper.xml.path"));
        System.out.println(TextUtil.toCamel("driver_jar"));

    }
}