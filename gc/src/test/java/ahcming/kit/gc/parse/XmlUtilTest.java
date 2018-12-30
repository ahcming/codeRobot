package ahcming.kit.gc.parse;

import ahcming.kit.gc.kit.XmlUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.HashMap;

public class XmlUtilTest {

    @Test
    public void testFindByName() {
        Field field = XmlUtil.findByName(new HashMap<>(), "mapper.xml.path");
        Assert.assertNull(field);
    }

}