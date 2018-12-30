package ahcming.kit.gc;

import ahcming.kit.gc.parse.ConfigParser;
import org.testng.annotations.Test;

public class ConfigParserTest {

    @Test
    public void testParse() {
        String configPath = "/Users/ahcming/workspace/github/codeRobot/gc/src/test/resources/gcc.xml";

        ConfigParser parser = new ConfigParser(configPath);
        parser.parse();

        System.out.println("--> " + parser.getProject());
        System.out.println("--> " + parser.getStorage());
        System.out.println("--> " + parser.getDomain());
        System.out.println("--> " + parser.getGc());
    }

}