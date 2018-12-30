package ahcming.kit.gc.impl;

import ahcming.kit.gc.ConfigParserTest;
import ahcming.kit.gc.model.Domain;
import ahcming.kit.gc.model.GC;
import ahcming.kit.gc.parse.ConfigParser;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class SqlCodeGenTest {

    @Test
    public void testGen() {
        Log log = new SystemStreamLog();

        String configPath = ConfigParserTest.class.getResource("/gcc.xml").getPath();
        ConfigParser parser = new ConfigParser(configPath);
        parser.parse(log);

        SqlCodeGen codeGen = new SqlCodeGen();
        GC.Generator generator = codeGen.getGenerator(parser.getGc(), "dsp_plan-sql");
        codeGen.gen(parser.getProject(), parser.getStorage(), parser.getDomain(), generator);
    }
}