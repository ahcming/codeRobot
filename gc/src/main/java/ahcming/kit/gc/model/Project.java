package ahcming.kit.gc.model;

public class Project {

    // mapper.interface.path
    private String mapperInterfacePath;
    // mapper.xml.path
    private String mapperXmlPath;

    public String getMapperInterfacePath() {
        return mapperInterfacePath;
    }

    public void setMapperInterfacePath(String mapperInterfacePath) {
        this.mapperInterfacePath = mapperInterfacePath;
    }

    public String getMapperXmlPath() {
        return mapperXmlPath;
    }

    public void setMapperXmlPath(String mapperXmlPath) {
        this.mapperXmlPath = mapperXmlPath;
    }

    @Override
    public String toString() {
        return "Project{" +
               "mapperInterfacePath='" + mapperInterfacePath + '\'' +
               ", mapperXmlPath='" + mapperXmlPath + '\'' +
               '}';
    }
}
