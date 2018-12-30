package ahcming.kit.gc.model;

import java.util.List;

public class GC {

    public List<Generator> generator;

    public static class Generator {
        public String id;
        public String entity;
        public String task;
        public String status;

        @Override
        public String toString() {
            return "Generator{" +
                   "id='" + id + '\'' +
                   ", entity='" + entity + '\'' +
                   ", task='" + task + '\'' +
                   ", status='" + status + '\'' +
                   '}';
        }
    }

    @Override
    public String toString() {
        return "GC{" +
               "generator=" + generator +
               '}';
    }
}
