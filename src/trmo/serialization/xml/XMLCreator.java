package trmo.serialization.xml;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;

import static trmo.serialization.xml.Utils.isWrapper;

class XMLCreator {

    private StringBuilder sb;

    String toXML(Object obj) {
        sb = new StringBuilder();
        int indents = 0;
        addXMLDeclaration();
        printObject(obj, "", indents);

        return sb.toString();
    }

    private void printObject(Object obj, String fieldName, int indents) {
        Class<?> type = obj.getClass();

        if (type.isAssignableFrom(String.class)) {
            printSimple(obj, fieldName, indents);

        } else if (type.isPrimitive()) {
            printSimple(obj, fieldName, indents);

        } else if (isWrapper(type)) {
            printSimple(obj, fieldName, indents);

        } else if (type.isArray()) {
            printArray(obj, fieldName, indents);

        } else if (type.isEnum()) {
            sb.append("enum\n");

        } else if (Collection.class.isAssignableFrom(type)) {
            printCollection((Collection<?>) obj, fieldName, indents);

        } else if (Object.class.isAssignableFrom(type)) {
            printComplex(obj, indents, type, fieldName);
        }
    }



    private void printComplex(Object obj, int indents, Class<?> type, String fieldName) {
        sb.append(indents(indents)).append("<object name=\"" + fieldName + "\" type=\"" + type.getName() + "\">").append("\n");
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Class<?> aClass = field.getType();
            fieldName = field.getName();
            Object o = null;
            try {
                o = field.get(obj);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Error accessing field " + fieldName + " of object " + obj.getClass().getName() + ", message: " + e.getMessage());
            }
            if (aClass.isPrimitive()) {
                printSimple(o, fieldName, indents + 1);
            } else {
                printObject(o, fieldName, indents + 1);
            }
        }
        sb.append(indents(indents)).append("</object>").append("\n");
    }

    private void printCollection(Collection<?> objects, String fieldName, int indents) {
        sb.append(indents(indents)).append("<collection name=\"" + fieldName + "\">").append("\n");
        for (Object o : objects) {
            sb.append(indents(indents + 1)).append("<element>").append("\n");
            printObject(o, "", indents + 2);
            sb.append(indents(indents + 1)).append("</element>").append("\n");
        }
        sb.append(indents(indents)).append("</collection>").append("\n");
    }

    private void printArray(Object obj, String fieldName, int indents) {
        sb.append(indents(indents)).append("<collection name=\"" + fieldName + "\">").append("\n");
        int length = Array.getLength(obj);
        for (int i = 0; i < length; i++) {
            sb.append(indents(indents + 1)).append("<element>").append("\n");
            Object o = Array.get(obj, i);
            if (obj.getClass().getComponentType().isPrimitive()) {
                printSimple(o, "", indents + 2);
            } else {
                printObject(o, "", indents + 2);
            }
            sb.append(indents(indents + 1)).append("</element>").append("\n");
        }
        sb.append(indents(indents)).append("</collection>").append("\n");
    }

    private void printSimple(Object obj, String fieldName, int indents) {
        sb.append(indents(indents)).append("<value name=\"" + fieldName + "\" type=\"" + obj.getClass().getName() + "\">").append("\n");
        sb.append(indents(indents + 1)).append(obj).append("\n");
        sb.append(indents(indents)).append("</value>").append("\n");
    }

    private void addXMLDeclaration() {
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>").append("\n");
    }


    private String indents(int x) {
        return "\t".repeat(x);
    }
}
