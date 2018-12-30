package ahcming.kit.gc.kit;

import org.apache.commons.lang3.math.NumberUtils;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class XmlUtil {

    /**
     * 解析完整个node的属性及子节点
     */
    public static <T> T parseNode(Node node, Class<T> clazz) {
        try {
            T value = clazz.newInstance();

            Field[] fields = clazz.getDeclaredFields();
            Map<String, Field> fieldMap = Arrays.stream(fields)
                    .map(f -> {
                        f.setAccessible(true);
                        return f;
                    })
                    .collect(Collectors.toMap(f -> f.getName(), f -> f));

            NamedNodeMap nodeMap = node.getAttributes();
            for(int idx = 0; idx < nodeMap.getLength(); idx++) {
                Node attrNode = nodeMap.item(idx);
                Field field = findByName(fieldMap, attrNode.getNodeName());
                if(field != null) {
                    setValue(value, field, attrNode.getNodeValue());

                } else {
                    System.err.println("不认识的属性:" + attrNode.getNodeName() + ", " + node.getNodeName());
                }
            }

            NodeList childNodes = node.getChildNodes();
            for (int idx = 0; idx < childNodes.getLength(); idx++) {
                Node childNode = childNodes.item(idx);
                if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                    Field field = findByName(fieldMap, childNode.getNodeName());
                    if(field != null) {
                        if(field.getType() == List.class) {
                            Type[] listFieldTypes = ((ParameterizedType)field.getGenericType()).getActualTypeArguments();
                            Class<?> fieldType = Object.class;
                            if (listFieldTypes != null && listFieldTypes.length > 0) {
                                fieldType = Class.forName(listFieldTypes[0].getTypeName());
                            }
                            Object nodeValue = parseNode(childNode, fieldType);
                            if (field.get(value) == null) {
                                List list = new ArrayList();
                                list.add(nodeValue);
                                field.set(value, list);
                            } else {
                                List list = (List) field.get(value);
                                list.add(nodeValue);
                                field.set(value, list);
                            }

                        } else {
                            Object nodeValue = parseNode(childNode, field.getType());
                            field.set(value, nodeValue);
                        }

                    } else {
                        System.err.println("不认识的子对象:" + childNode.getNodeName() + ", " + node.getNodeName());
                    }

                } else {
//                    System.out.println("无用的节点:" + childNode.getNodeName() + ", " + childNode.getNodeType() + ", " + childNode.getTextContent() + "," + node.getNodeName());
                }
            }
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Field findByName(Map<String, Field> fieldMap, String name) {
        if (fieldMap.containsKey(name)) {
            return fieldMap.get(name);

        }

        // name -> Camel形式
        // mapper.xml.path -> mapperXmlPath
        // driver_jar -> driverJar
        String camelName = TextUtil.toCamel(name);
        if (fieldMap.containsKey(camelName)) {
            return fieldMap.get(camelName);
        }

        // 单数变算数
        return null;
    }


    public static void setValue(Object target, Field field, String value) {
        try {
            Class<?> fieldType = field.getType();
            if (fieldType.isPrimitive()) {
                if (fieldType == int.class) {
                    field.set(target, NumberUtils.toInt(value));
                } else {
                    throw new RuntimeException("不支持的字段类型: " + fieldType);
                }

            } else if (fieldType == String.class) {
                field.set(target, value);

            } else if (fieldType.isEnum()) { // 枚举
                Class<?> enumClazz = Class.forName(fieldType.getTypeName());
                Method getValueMethod = enumClazz.getMethod("valueOf", String.class);
                Object fieldValue = getValueMethod.invoke(null, value);
                field.set(target, fieldValue);

            } else {
                System.out.println("忽略的设值: " + target.getClass() + "#" + field.getName() + " -> " + value);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
