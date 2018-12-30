package ahcming.kit.gc.impl;

import ahcming.kit.gc.CodeGen;
import ahcming.kit.gc.model.Domain;
import ahcming.kit.gc.model.GC;
import ahcming.kit.gc.model.Project;
import ahcming.kit.gc.model.Storage;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CREATE TABLE `dy_content_hate` (
 *   `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
 *   `device_id` varchar(64) NOT NULL DEFAULT '' COMMENT '设备id',
 *   `dy_id` varchar(7) NOT NULL DEFAULT '' COMMENT 'dyId',
 *   `ip` varchar(64) DEFAULT '' COMMENT '请求ip',
 *   `type` smallint(4) NOT NULL DEFAULT '0' COMMENT '不感兴趣类型, 1:内容不感兴趣，2：话题不感兴趣，3：作者不感兴趣，4：重复，5:低俗',
 *   `reason` varchar(32) NOT NULL DEFAULT '' COMMENT '不感兴趣具体内容',
 *   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
 *   PRIMARY KEY (`id`),
 *   KEY `idx_device_id` (`device_id`) USING BTREE
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户帖子不感兴趣记录';
 */
public class SqlCodeGen implements CodeGen {

    @Override
    public void gen(Project project, Storage storage, Domain domain, GC.Generator generator) {
        Domain.Entity entity = getEntity(domain, generator.entity);

        Domain.Sharding sharding = entity.sharding;
        for (int idx = 0; idx < (sharding == null ? 1 : sharding.number); idx++) {
            String tableName;

            if (sharding == null) {
                tableName = String.format("%s.%s", entity.scheme, entity.mapper.table);
            } else {
                tableName = String.format("%s.%s_%03d", entity.scheme, entity.mapper.table, idx);
            }

            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE `").append(tableName).append("` (\n");

            buildColumn(sb, entity);

            if (entity.indexs != null && entity.indexs.index != null) {
                buildIndex(sb, entity);
            }

            // 去掉结尾的逗号
            sb.deleteCharAt(sb.length()-3);

            sb.append(") ENGINE=").append(entity.engine).append(" DEFAULT CHARSET=").append(entity.charset).append(" COMMENT='").append(entity.remark).append("';\n\n");

            System.out.println(sb);
        }
    }

    void buildColumn(StringBuilder sb, Domain.Entity entity) {
        // 生成每一行数据
        for (Domain.Property property : entity.mapper.property) {
            sb.append("\t`").append(property.column).append("` ").append(property.jdbcType);
            if (property.length > 0) {
                sb.append("(").append(property.length).append(")");
            }

            sb.append(" NOT NULL");

            if (isPrimary(property, entity.indexs)) {
                sb.append(" AUTO_INCREMENT");
            } else {
                if (StringUtils.equals(property.jdbcType, "smallint") ||
                    StringUtils.equals(property.jdbcType, "int") ||
                    StringUtils.equals(property.jdbcType, "bigint")) {
                    sb.append(" DEFAULT 0");
                } else if (StringUtils.equals(property.jdbcType, "varchar")) {
                    sb.append(" DEFAULT ''");
                }
            }

            if (StringUtils.isNotBlank(property.remark)) {
                sb.append(" COMMENT '").append(property.remark).append("'");
            }

            sb.append(", \n");
        }
    }

    boolean isPrimary(Domain.Property property, Domain.IndexList indexList) {
        if (null == indexList || indexList.index == null) {
            return false;
        }

        for (Domain.Index index : indexList.index) {
            if (StringUtils.equals(index.type, "PRIMARY") && StringUtils.equals(index.cloumns, property.column)) {
                return true;
            }
        }

        return false;
    }

    void buildIndex(StringBuilder sb, Domain.Entity entity) {
        for (Domain.Index index : entity.indexs.index) {
            List<String> columnWrapper = Arrays.stream(index.cloumns.split(","))
                    .map(s -> StringUtils.join("`", s, "`"))
                    .collect(Collectors.toList());
            String columnSql = StringUtils.join(columnWrapper, ",");

            if (StringUtils.equals(index.type, "PRIMARY")) {
                sb.append("\tPRIMARY KEY (").append(columnSql).append("), \n");
            } else {

                String keyName = index.name;
                if (StringUtils.equals(index.type, "UNIQUE")) {
                    if (StringUtils.isBlank(keyName)) {
                        keyName = "UDX_" + StringUtils.join(StringUtils.split(index.cloumns, ","), "_");
                    }
                    sb.append("\tUNIQUE INDEX ").append(keyName).append(" (").append(columnSql).append("), \n");
                } else {
                    if (StringUtils.isBlank(keyName)) {
                        keyName = "IDX_" + StringUtils.join(StringUtils.split(index.cloumns, ","), "_");
                    }

                    sb.append("\tKEY ").append(keyName).append(" (").append(columnSql).append("), \n");
                }
            }
        }

    }


}
