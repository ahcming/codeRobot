package ahcming.kit.gc;

import ahcming.kit.gc.parse.ConfigParser;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.testng.annotations.Test;

public class ConfigParserTest {

    Log log = new SystemStreamLog();

    @Test
    public void testParse() {
        String configPath = ConfigParserTest.class.getResource("/gcc.xml").getPath();
        ConfigParser parser = new ConfigParser(configPath);
        parser.parse(log);

        System.out.println("--> " + parser.getProject());
        System.out.println("--> " + parser.getStorage());
        System.out.println("--> " + parser.getDomain());
        System.out.println("--> " + parser.getGc());
        log.info("success");
    }

}