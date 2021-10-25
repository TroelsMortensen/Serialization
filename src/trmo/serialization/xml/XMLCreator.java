package trmo.serialization.xml;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;

class XMLCreator {

    String toXML(Object obj) {
        StringBuilder sb = new StringBuilder();
        int indents = 0;
        addXMLDeclaration(sb);
        printObject(obj, sb, indents);

        return sb.toString();
    }

    private void printObject(Object obj, StringBuilder sb, int indents) {
        Class<?> type = obj.getClass();

        // TODO: 25/10/2021 wrapper types?

        if (type.isAssignableFrom(String.class)) {
            printSimple(obj, sb, indents);

        } else if (type.isPrimitive()) {
            printSimple(obj, sb, indents);

        } else if (type.isArray()) {
            printArray(obj, sb, indents);

        } else if (type.isEnum()) {
            sb.append("enum\n");

        } else if (Collection.class.isAssignableFrom(type)) {
            printCollection((Collection<?>) obj, sb, indents);

        } else if (Object.class.isAssignableFrom(type)) {
            printComplex(obj, sb, indents, type);
        }
    }

    private void printComplex(Object obj, StringBuilder sb, int indents, Class<?> type) {
        sb.append(indents(indents)).append("<object type=\"" + type.getName() + "\">").append("\n");
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Class<?> aClass = field.getType();
            Object o = null;
            try {
                o = field.get(obj);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Error accessing field " + field.getName() + " of object " + obj.getClass().getName() + ", message: " + e.getMessage());
            }
            if (aClass.isPrimitive()) {
                printSimple(o, sb, indents + 1);
            } else {
                printObject(o, sb, indents + 1);
            }
        }
        sb.append(indents(indents)).append("</object>").append("\n");
    }

    private void printCollection(Collection<?> objects, StringBuilder sb, int indents) {
        sb.append(indents(indents)).append("<collection>").append("\n");
        for (Object o : objects) {
            sb.append(indents(indents + 1)).append("<element>").append("\n");
            printObject(o, sb, indents + 2);
            sb.append(indents(indents + 1)).append("</element>").append("\n");
        }
        sb.append(indents(indents)).append("</collection>").append("\n");
    }

    private void printArray(Object obj, StringBuilder sb, int indents) {
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
    }

    private void printSimple(Object obj, StringBuilder sb, int indents) {
        sb.append(indents(indents)).append("<value type=\"" + obj.getClass().getName() + "\">").append("\n");
        sb.append(indents(indents + 1)).append(obj).append("\n");
        sb.append(indents(indents)).append("</value>").append("\n");
    }

    private void addXMLDeclaration(StringBuilder sb) {
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>").append("\n");
    }


    private String indents(int x) {
        return "\t".repeat(x);
    }
}
