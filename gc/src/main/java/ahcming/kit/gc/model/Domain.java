package ahcming.kit.gc.model;

import java.util.List;

public class Domain {

    public List<Entity> entities;

    public static class Entity {
        public String id;
        public String scheme;
        public String remark;
        public String engine;
        public String charset;

        public Mapper mapper;
        public IndexList indexs;
        public Sharding sharding;

        @Override
        public String toString() {
            return "Entity{" +
                   "id='" + id + '\'' +
                   ", scheme='" + scheme + '\'' +
                   ", remark='" + remark + '\'' +
                   ", engine='" + engine + '\'' +
                   ", charset='" + charset + '\'' +
                   ", mapper=" + mapper +
                   ", indexs=" + indexs +
                   ", sharding=" + sharding +
                   '}';
        }
    }

    public static class Mapper {
        public String table;
        public String bean;
        public List<Property> property;

        @Override
        public String toString() {
            return "Mapper{" +
                   "table='" + table + '\'' +
                   ", bean='" + bean + '\'' +
                   ", property=" + property +
                   '}';
        }
    }

    public static class IndexList {
        public List<Index> index;

        @Override
        public String toString() {
            return "IndexList{" +
                   "index=" + index +
                   '}';
        }
    }

    public static class Property {
        public String column;
        public String jdbcType;
        public int length;
        public String name;
        public String javaType;
        public String remark;

        @Override
        public String toString() {
            return "Property{" +
                   "column='" + column + '\'' +
                   ", jdbcType='" + jdbcType + '\'' +
                   ", length=" + length +
                   ", name='" + name + '\'' +
                   ", javaType='" + javaType + '\'' +
                   ", remark='" + remark + '\'' +
                   '}';
        }
    }

    public static class Index {
        public String name;
        public String type;
        public String cloumns;

        @Override
        public String toString() {
            return "Index{" +
                   "name='" + name + '\'' +
                   ", type='" + type + '\'' +
                   ", cloumns='" + cloumns + '\'' +
                   '}';
        }
    }

    public static class Sharding {
        public int number;
        public String type;
        public String hash;

        @Override
        public String toString() {
            return "Sharding{" +
                   "number=" + number +
                   ", type='" + type + '\'' +
                   ", hash='" + hash + '\'' +
                   '}';
        }
    }

    @Override
    public String toString() {
        return "Domain{" +
               "entities=" + entities +
               '}';
    }
}
