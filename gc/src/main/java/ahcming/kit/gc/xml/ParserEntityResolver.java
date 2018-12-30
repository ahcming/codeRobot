package ahcming.kit.gc.xml;

import org.apache.commons.io.IOUtils;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

public class ParserEntityResolver implements EntityResolver {

    public ParserEntityResolver() {
        super();
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        System.out.println("publicId: " + publicId);
        System.out.println("systemId: " + systemId);

        if("-//ahcming.kit//DTD Code Generator Configuration 1.0//ZH".equalsIgnoreCase(publicId)) {
            InputStream is = getClass().getClassLoader().getResourceAsStream("generator-config_1_0.dtd");
//            System.out.println(IOUtils.readLines(is, "UTF-8"));
            return new InputSource(is);

        } else {
            return null;
        }
    }
}
