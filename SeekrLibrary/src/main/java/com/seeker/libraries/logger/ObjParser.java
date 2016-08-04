package com.seeker.libraries.logger;

import android.util.Pair;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @author Kale
 * @date 2016/3/23
 */
public class ObjParser {

    private static final String NULL = "null";

    // 基本数据类型
    private final static String[] TYPES = {"int", "java.lang.String", "boolean", "char",
            "float", "double", "long", "short", "byte"};

    public static String parseObj(Object object) {
        if (object == null) {
            return NULL;
        }

        final String simpleName = object.getClass().getSimpleName();
        if (object.getClass().isArray()) {
            StringBuilder msg = new StringBuilder("Temporarily not support more than two dimensional Array!");
            int dim = ArrayParser.getArrayDimension(object);
            switch (dim) {
                case 1:
                    Pair pair = ArrayParser.arrayToString(object);
                    msg = new StringBuilder(simpleName.replace("[]", "[" + pair.first + "] {\n"));
                    msg.append(pair.second);
                    break;
                case 2:
                    Pair pair1 = ArrayParser.arrayToObject(object);
                    Pair pair2 = (Pair) pair1.first;
                    msg = new StringBuilder(simpleName.replace("[][]", "[" + pair2.first + "][" + pair2.second + "] {\n"));
                    msg.append(pair1.second);
                    break;
            }
            return msg.append("}").toString();
        } else if (object instanceof Collection) {
            Collection collection = (Collection) object;
            StringBuilder msg = new StringBuilder();
            msg .append(String.format(Locale.ENGLISH, "%s size = %d {\n", simpleName, collection.size()));
            if (!collection.isEmpty()) {
                Iterator iterator = collection.iterator();
                int flag = 0;
                while (iterator.hasNext()) {
                    Object item = iterator.next();
                    msg.append(String.format(Locale.ENGLISH, "[%d]:%s%s",
                            flag,
                            objectToString(item),
                            flag++ < collection.size() - 1 ? ",\n" : "\n"));
                }
            }
            return msg.append("}").toString();
        } else if (object instanceof Map) {
            StringBuilder msg = new StringBuilder();
            msg.append(simpleName).append(" {\n");
            Map map = (Map) object;
            Set keys = map.keySet();
            for (Object key : keys) {
                Object value = map.get(key);
                msg.append(String.format("[%s -> %s]\n", objectToString(key), objectToString(value)));
            }
            return msg.append("}").toString();
        } else {
            return objectToString(object);
        }
    }


    /**
     * 将对象转化为String
     */
    protected static <T> String objectToString(T object) {
        if (object == null) {
            return "Object{object is null}";
        }
        if (object.toString().startsWith(object.getClass().getName() + "@")) {
            StringBuilder builder = new StringBuilder(object.getClass().getSimpleName() + "{\n");
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                boolean flag = false;
                for (String type : TYPES) {
                    if (field.getType().getName().equalsIgnoreCase(type)) {
                        flag = true;
                        Object value = null;
                        try {
                            value = field.get(object);
                        } catch (IllegalAccessException e) {
                            value = e;
                        } finally {
                            builder.append(String.format("%s = %s,", field.getName(),
                                    value == null ? "null" : value.toString()))
                                    .append("\n");
                        }
                    }
                }
                if (!flag) {
                    builder.append(String.format("%s = %s, ", field.getName(), "Object"));
                }
            }
            return builder.replace(builder.length() - 2, builder.length() - 1, "\n}").toString();
        } else {
            return object.toString();
        }
    }
}
