package ahcming.kit.gc.parse;

import ahcming.kit.gc.kit.XmlUtil;
import ahcming.kit.gc.model.Domain;
import ahcming.kit.gc.model.GC;
import ahcming.kit.gc.model.Project;
import ahcming.kit.gc.model.Storage;
import ahcming.kit.gc.xml.ParserEntityResolver;
import ahcming.kit.gc.xml.ParserErrorHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ConfigParser {

    private String configPath;

    private List<String> warnings = new ArrayList<String>();
    private List<String> parseErrors = new ArrayList<String>();

    private Project project;

    private Storage storage;

    private Domain domain;

    private GC gc;

    public ConfigParser(String configPath) {
        this.configPath = configPath;
    }

    public void parse() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(new ParserEntityResolver());
            builder.setErrorHandler(new ParserErrorHandler(warnings, parseErrors));

            InputStream is = new FileInputStream(configPath);
            InputSource inputSource = new InputSource(is);
            Document document = builder.parse(inputSource);
            Element rootNode = document.getDocumentElement();

            _parseProject(rootNode);
            _parseStorage(rootNode);
            _parseDomain(rootNode);
            _parseGc(rootNode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void _parseProject(Element root) {
        NodeList projectHead = root.getElementsByTagName("project");
        if(projectHead.getLength() > 0) {
            Project config = new Project();
            NodeList childNodes = projectHead.item(0).getChildNodes();
            for (int index = 0; index < childNodes.getLength(); index++) {
                Node node = childNodes.item(index);
                if (node.getNodeName().equalsIgnoreCase("mapper.interface.path")) {
                    config.setMapperInterfacePath(node.getTextContent());
                } else if (node.getNodeName().equalsIgnoreCase("mapper.xml.path")) {
                    config.setMapperXmlPath(node.getTextContent());
                }
            }
            this.setProject(config);
        }
    }

    void _parseStorage(Element root) {
        NodeList storageRoot = root.getElementsByTagName("storage");
        if (storageRoot.getLength() > 0) {
            this.storage = XmlUtil.parseNode(storageRoot.item(0), Storage.class);
        }
    }

    void _parseDomain(Element root) {
        NodeList domainRoot = root.getElementsByTagName("domain");
        if (domainRoot.getLength() > 0) {
            this.domain = new Domain();

            List<Domain.Entity> entities = new ArrayList<Domain.Entity>();
            NodeList entityNodes = domainRoot.item(0).getChildNodes();
            for (int idx = 0; idx < entityNodes.getLength(); idx++) {
                Node entityNode = entityNodes.item(idx);
                if(entityNode.getNodeType() == Node.ELEMENT_NODE) {
                    Domain.Entity entity = XmlUtil.parseNode(entityNode, Domain.Entity.class);
                    entities.add(entity);
                }
            }

            this.domain.entities = entities;
        }
    }

    void _parseGc(Element root) {
        NodeList gcRoot = root.getElementsByTagName("gc");
        if(gcRoot.getLength() > 0) {
            this.gc = XmlUtil.parseNode(gcRoot.item(0), GC.class);
        }
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public GC getGc() {
        return gc;
    }

    public void setGc(GC gc) {
        this.gc = gc;
    }
}
