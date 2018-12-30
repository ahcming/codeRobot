package ahcming.kit.gc.model;

import java.util.List;

public class Storage {

    public List<Scheme> scheme;

    public static class Scheme {
        public String id;
        public String remark;

        public String driver;
        public String driverJar;
        public DBType type;
        public String url;
        public String user;
        public String password;

        @Override
        public String toString() {
            return "Scheme{" +
                   "id='" + id + '\'' +
                   ", remark='" + remark + '\'' +
                   ", driver='" + driver + '\'' +
                   ", driverJar='" + driverJar + '\'' +
                   ", type=" + type +
                   ", url='" + url + '\'' +
                   ", user='" + user + '\'' +
                   ", password='" + password + '\'' +
                   '}';
        }
    }

    public enum DBType {
        mysql,
        oracle,
        pg,
    }

    @Override
    public String toString() {
        return "Storage{" +
               "scheme=" + scheme +
               '}';
    }
}
