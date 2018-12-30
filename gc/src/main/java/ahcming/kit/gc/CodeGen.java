package ahcming.kit.gc;

import ahcming.kit.gc.model.Domain;
import ahcming.kit.gc.model.GC;
import ahcming.kit.gc.model.Project;
import ahcming.kit.gc.model.Storage;
import org.apache.commons.lang3.StringUtils;

public interface CodeGen {

    // 模板
    void gen(Project project, Storage storage, Domain domain, GC.Generator generator);

    default Domain.Entity getEntity(Domain domain, String entityId) {
        for (Domain.Entity entity : domain.entities) {
            if (StringUtils.equals(entity.id, entityId)) {
                return entity;
            }
        }

        return null;
    }

    default GC.Generator getGenerator(GC gc, String id) {
        for (GC.Generator generator : gc.generator) {
            if (StringUtils.equals(generator.id, id)) {
                return generator;
            }
        }

        return null;
    }
}
