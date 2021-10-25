package trmo.serialization.xml;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;

public class XMLserializer {

    public static String toXML(Object obj) throws IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        int indents = 0;
        addXMLDeclaration(sb);
        printObject(obj, sb, indents);

        return sb.toString();
    }

    private static void printObject(Object obj, StringBuilder sb, int indents) throws IllegalAccessException {
        Class<?> type = obj.getClass();
        if (type.isAssignableFrom(String.class)) {
            printSimple(obj, sb, indents);

        } else if (type.isPrimitive()) {
            printSimple(obj, sb, indents);

        } else if (type.isArray()) {
            sb.append(indents(indents)).append("<collection>").append("\n");
            int length = Array.getLength(obj);
            for (int i = 0; i < length; i++) {
                sb.append(indents(indents + 1)).append("<element>").append("\n");
                Object o = Array.get(obj, i);
                if (obj.getClass().getComponentType().isPrimitive()) {
                    printSimple(o, sb, indents + 2);
                } else {
                    printObject(o, sb, indents + 2);
                }
                sb.append(indents(indents + 1)).append("</element>").append("\n");
            }
            sb.append(indents(indents)).append("</collection>").append("\n");

        } else if (type.isEnum()) {
            sb.append("enum\n");

        } else if (Collection.class.isAssignableFrom(type)) {
            sb.append(indents(indents)).append("<collection>").append("\n");
            Collection<?> list = (Collection<?>) obj;
            for (Object o : list) {
                sb.append(indents(indents + 1)).append("<element>").append("\n");
                printObject(o, sb, indents + 2);
                sb.append(indents(indents + 1)).append("</element>").append("\n");
            }
            sb.append(indents(indents)).append("</collection>").append("\n");

        } else if (Object.class.isAssignableFrom(type)) {
            sb.append(indents(indents)).append("<object type=\"" + type.getName() + "\">").append("\n");
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Class<?> aClass = field.getType();
                Object o = field.get(obj);
                if (aClass.isPrimitive()) {
                    printSimple(o, sb, indents + 1);
                } else {
                    printObject(o, sb, indents + 1);
                }
            }
            sb.append(indents(indents)).append("</object>").append("\n");
        }
    }

    private static void printSimple(Object obj, StringBuilder sb, int indents) {
        sb.append(indents(indents)).append("<value type=\"" + obj.getClass().getName() + "\">").append("\n");
        sb.append(indents(indents + 1)).append(obj).append("\n");
        sb.append(indents(indents)).append("</value>").append("\n");
    }

    private static void addXMLDeclaration(StringBuilder sb) {
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>").append("\n");
    }

    public static <T> T toObject(String s) {

        return null;
    }


    private static String indents(int x) {
        return "\t".repeat(x);
    }
}
