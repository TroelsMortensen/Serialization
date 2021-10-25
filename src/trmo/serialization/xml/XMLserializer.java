package trmo.serialization.xml;

import java.lang.reflect.Field;
import java.util.Collection;

public class XMLserializer {

    public static String toXML(Object obj) throws ClassNotFoundException, IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        int indents = 0;
        addXMLDeclaration(sb);
        printObject(obj, sb, indents);

        return sb.toString();
    }

    private static void printObject(Object obj, StringBuilder sb, int indents) throws ClassNotFoundException, IllegalAccessException {
        startObject(sb, obj);

        printFields(sb, obj, indents+1);

        endObject(sb);
    }

    private static void printFields(StringBuilder sb, Object obj, int indents) throws ClassNotFoundException, IllegalAccessException {
        Class<?> aClass = Class.forName(obj.getClass().getName());
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            printField(sb, field, obj, indents);
        }
    }

    private static void printField(StringBuilder sb, Field field, Object obj, int indents) throws IllegalAccessException, ClassNotFoundException {
        // TODO handle super class fields
        // TODO: 25/10/2021 handle enums
        // TODO: 25/10/2021 handle collections
        // TODO: 25/10/2021 handle object fields
        // TODO: 25/10/2021 handle simple fields
        field.setAccessible(true);
        Class<?> type = field.getType();
        Object value = field.get(obj);
        String name = field.getName();
        sb.append(indents(indents)).append("<field name=\""+ name +"\" type=\""+type.getName()+"\">").append("\n");

        if (type.isAssignableFrom(String.class)) {
            sb.append(indents(indents+1)).append(value).append("\n");
        } else if (type.isPrimitive()) {
            sb.append(indents(indents+1)).append(value).append("\n");
        }else if(type.isAssignableFrom(Object.class)) {
            printObject(value, sb, indents+1);
        } else if(Collection.class.isAssignableFrom(field.getType())) {
            sb.append(indents(indents+1)).append("<collection>").append("\n");
            Collection<?> list = (Collection<?>) value;
            printCollection(sb, indents+1, list);
            sb.append(indents(indents+1)).append("<collection>").append("\n");
        } else if(type.isArray()) {
            // TODO
        } else if(type.isEnum()) {

        }
        sb.append(indents(indents)).append("</field>").append("\n");
    }

    private static void printCollection(StringBuilder sb, int indents, Collection<?> list) throws ClassNotFoundException, IllegalAccessException {
        for (Object o : list) {
            sb.append(indents(indents +1)).append("<element type=\""+o.getClass().getName()+"\">").append("\n");
            if(o.getClass().isPrimitive()) {
                sb.append(indents(indents +2)).append(o).append("\n");
            } else if(o.getClass().isAssignableFrom(String.class)) {
                sb.append(indents(indents +2)).append(o).append("\n");
            } else {
                printObject(o, sb, indents +2);
            }
            sb.append(indents(indents +1)).append("</element>").append("\n");
        }
    }

    private static void addXMLDeclaration(StringBuilder sb) {
        appendLine(sb, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>");
    }

    private static void endObject(StringBuilder sb) {
        appendLine(sb, "</object>");
    }

    private static void startObject(StringBuilder sb, Object obj) {
        appendLine(sb, "<object type=\"" + obj.getClass().getCanonicalName() + "\">");
    }

    public static <T> T toObject(String s) {

        return null;
    }

    private static StringBuilder appendLine(StringBuilder sb, Object val) {
        sb.append(val).append("\n");
        return sb;
    }

    private static String indents(int x) {
        String s = "";
        for (int i = 0; i < x; i++) {
            s += "\t";
        }
        return s;
    }
}
