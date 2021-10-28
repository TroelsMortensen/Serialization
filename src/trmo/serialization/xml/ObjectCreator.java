package trmo.serialization.xml;

import jdk.jshell.execution.Util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class ObjectCreator {

    private String xml;
    private int charIdx;

    public <T> T toObject(String xml, Class<T> type) {
        this.xml = xml.replace("\n", "").replace("\t", "");
        charIdx = 0;
        TagInfo firstTag = nextStartTag();
        if (!firstTag.tag.equals("?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?")) {
            throw new RuntimeException("Correct XML header not found");
        }

        if (type.isAssignableFrom(String.class)) {
            return handlePrimitive(type);
        } else if (type.isPrimitive()) {
            return handlePrimitive(type);
        } else if (Utils.isWrapper(type)) {
            return handlePrimitive(type);
        } else if (type.isArray()) {
            throw new RuntimeException("Cannot deserialize a collection. Use method XMLSerializer.toList() instead");
        } else if (type.isEnum()) {

        } else if (List.class.isAssignableFrom(type)) {
            throw new RuntimeException("Cannot deserialize a collection. Use method XMLSerializer.toList() instead");
        } else if (Object.class.isAssignableFrom(type)) {
            T t = startOnObject(type);
            return t;
        }

        throw new RuntimeException("Class type " + type.getTypeName() + " not supported");
    }

    public <T> List<T> toList(String xml, Class<T> elementType) {
        this.xml = xml.replace("\n", "").replace("\t", "");
        charIdx = 0;
        TagInfo firstTag = nextStartTag();
        if (!firstTag.tag.equals("?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?")) {
            throw new RuntimeException("Correct XML header not found");
        }
        return null;
    }

    private <T> T handlePrimitive(Class<?> type) {
        String wrapperTypeName = type.getName();
        if (type.isPrimitive()) {
            wrapperTypeName = convertFromPrimitiveTypeToWrapper(type.getName());
        }
        TagInfo valueTag = nextStartTag();
        String value = parseValue("value");

        try {
            Class<?> aClass = Class.forName(wrapperTypeName);
            Constructor<?> constructor = aClass.getConstructor(String.class);
            T o = (T) constructor.newInstance(value);
            return o;
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private String convertFromPrimitiveTypeToWrapper(String name) {
        switch (name) {
            case "boolean":
                return "java.lang.Boolean";
            case "int":
                return "java.lang.Integer";
            case "double":
                return "java.lang.Double";
            case "float":
                return "java.lang.Float";
            case "char":
                return "java.lang.Character";
            case "long":
                return "java.lang.Long";
            case "short":
                return "java.lang.Short";
            case "byte":
                return "java.lang.Byte";
        }
        throw new RuntimeException("Could not convert primitive to wrapper type, from " + name);
    }

    private <T> T startOnObject(Class<T> typeToReturn) {
        TagInfo tagInfo = nextStartTag();
//        Map<String, String> rootAttributes = getAttributes(rootTypeTag);
//        String tagType = rootTypeTag.split(" ")[0];
        if ("object".equals(tagInfo.tagType)) {
            return handleObject(typeToReturn);
        }
        throw new RuntimeException("Error here. Asked for object, but that's not the root object");
    }

    private <T> T handleObject(Class<T> typeToReturn) {
        T objResult = createObjectOfType(typeToReturn);
        while (true) {
            TagInfo objTag = nextStartTag();
//            Map<String, String> attributes = getAttributes(fieldVariableTag);
//            String fieldTagType = fieldVariableTag.split(" ")[0];

            if ("value".equals(objTag.tagType)) {
                // TODO: 28/10/2021 use the handlePrimitive method here instead?
                String fieldValue = parseValue(objTag.tagType);
                Field match = findField(objResult, objTag.attributes);
                match.setAccessible(true);
                setPrimitiveOnField(match, objResult, fieldValue);

            } else if ("object".equals(objTag.tagType)) {
                Field field = findField(objResult, objTag.attributes);
                field.setAccessible(true);
                Object o = handleObject(field.getType());
                try {
                    field.set(objResult, o);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error setting field " + field.getName() + " on object " + objResult.getClass().getName());
                }

            } else if ("collection".equals(objTag.tagType)) {
                // TODO: 28/10/2021 fix this
            }

            if (xml.charAt(charIdx) == '<' && xml.charAt(charIdx + 1) == '/') {
                break;
            }
        }
        // create object of typeToReturn
        // get all fields
        // while true, nexttag

        return objResult;
    }

    private <T> void setPrimitiveOnField(Field fieldVariable, T objResult, String fieldValue) {
        try {
            Class<?> type = fieldVariable.getType();
            if (type.isAssignableFrom(String.class)) {
                Class<?> aClass = Class.forName(type.getName());
                Constructor<?> constructor = aClass.getConstructor(type);
                T o = (T) constructor.newInstance(fieldValue);
                fieldVariable.set(objResult, o);
            } else if (type.isAssignableFrom(int.class)) {
                int i = Integer.parseInt(fieldValue);
                fieldVariable.set(objResult, i);
            } else if (type.isAssignableFrom(char.class)) {
                char c = fieldValue.charAt(0);
                fieldVariable.set(objResult, c);
            } else if (type.isAssignableFrom(boolean.class)) {
                boolean b = Boolean.parseBoolean(fieldValue);
                fieldVariable.set(objResult, b);
            } else if (type.isAssignableFrom(short.class)) {
                short s = Short.parseShort(fieldValue);
                fieldVariable.set(objResult, s);
            } else if (type.isAssignableFrom(double.class)) {
                double d = Double.parseDouble(fieldValue);
                fieldVariable.set(objResult, d);
            } else if (type.isAssignableFrom(long.class)) {
                long l = Long.parseLong(fieldValue);
                fieldVariable.set(objResult, l);
            } else if (type.isAssignableFrom(float.class)) {
                float f = Float.parseFloat(fieldValue);
                fieldVariable.set(objResult, f);
            } else if (type.isAssignableFrom(byte.class)) {
                byte b = Byte.parseByte(fieldValue);
                fieldVariable.set(objResult, b);
            } else {
                throw new UnsupportedOperationException("Type " + type.getTypeName() + " not yet supported");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /*private <T> T createPrimitive(Class<?> type, String arg, Field match) {
        try {
            if (type.isAssignableFrom(String.class)) {
                Class<?> aClass = Class.forName(type.getName());
                Constructor<?> constructor = aClass.getConstructor(type);
                T o = (T) constructor.newInstance(arg);
                return o;
            } else if (type.isAssignableFrom(int.class)) {
                int i = Integer.parseInt(arg);
                Class<? extends Class> aClass = type.getClass();
                T i1 = (T) i;

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Error parsing primitive or string");
    }*/

    private <T> Field findField(T objResult, Map<String, String> attributes) {
        Field[] fields = objResult.getClass().getDeclaredFields();
        Field match = null;
        for (Field field : fields) {
            if (field.getName().equals(attributes.get("name"))) {
                match = field;
                return match;
            }
        }
        throw new RuntimeException("Could not find field variable " + attributes.get("name") + " on object " + objResult.getClass().getName());
    }

    private Map<String, String> getAttributes(String rootTypeTag) {
        Map<String, String> attributes = new HashMap<>();
        String[] s = rootTypeTag.split(" ");
        for (String s1 : s) {
            if (!s1.contains("=")) continue;
            String[] split = s1.split("=");
            attributes.put(split[0].replace("\"", ""), split[1].replace("\"", ""));
        }
        return attributes;
    }

    private String parseValue(String tagType) {
        String actualValue = "";
        boolean isInEndTag = false;
        String endTag = "";
        while (true) {
            char c = xml.charAt(charIdx);
            if (c == '<') {
                endTag += c;
                charIdx++;
                isInEndTag = true;
            } else if (isInEndTag && c == '>') {
                endTag += c;
                charIdx++;
                break;
            } else if (isInEndTag) {
                endTag += c;
                charIdx++;
            } else {
                actualValue += c;
                charIdx++;
            }
        }
        if (endTag.equals("</" + tagType + ">")) {
            //String tagValue = actualValue.replace("</" + tagType + ">", "");
            //T fieldType = createObject(typeToReturn, actualValue);
            return actualValue;
        } else {
            throw new RuntimeException("Incorrect end-tag at char index " + charIdx);
        }
    }

    private <T> T createObjectOfType(Class<T> type) {
        try {
            if (type.isArray()) {

            } else if (type.isEnum()) {

            } else if (Collection.class.isAssignableFrom(type)) {

            } else if (Object.class.isAssignableFrom(type)) {
                Class<?> aClass = Class.forName(type.getName());
                Constructor<?> constructor = aClass.getConstructor();
                T t = (T) constructor.newInstance();
                return t;
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Class type not found: " + e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No matching constructor found, you must have a no-argument constructor: " + e.getMessage(), e);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private TagInfo nextStartTag() {

        boolean isInTag = false;
        boolean isTagContent = false;
        String tag = "";
        while (true) {
            if (xml.charAt(charIdx) == '<') {
                isInTag = true;
            } else if (xml.charAt(charIdx) == '>') {
                isInTag = false;
                charIdx++;
                break;
            } else {
                tag += xml.charAt(charIdx);
            }
            charIdx++;
        }

        Map<String, String> attributes = getAttributes(tag);
        String tagType = tag.split(" ")[0];
        return new TagInfo(tag, tagType, attributes);
    }


}
