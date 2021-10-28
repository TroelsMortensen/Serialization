package trmo.serialization.xml;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


class ObjectCreator {

    private String xml;
    private int charIdx;

    public <T> T toObject(String xml, Class<T> type) {
        this.xml = xml.replace("\n", "").replace("\t", "");
        charIdx = 0;
        String firstTag = nextStartTag();
        if (!firstTag.equals("?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?")) {
            throw new RuntimeException("Correct XML header not found");
        }

        if (type.isAssignableFrom(String.class)) {

        } else if (type.isPrimitive()) {
            throw new RuntimeException("Cannot deserialize to primitive types. Instead use their wrapper classes, e.g. Integer or Boolean");
        } else if (type.isArray()) {

        } else if (type.isEnum()) {

        } else if (Collection.class.isAssignableFrom(type)) {

        } else if (Object.class.isAssignableFrom(type)) {
            T t = start(type);
            return t;
        }
        //readNextTag(type);
        throw new RuntimeException("Class type " + type.getTypeName() + " not supported");
    }

    private <T> T start(Class<T> typeToReturn) {
        String rootTypeTag = nextStartTag();
        Map<String, String> rootAttributes = getAttributes(rootTypeTag);
        String tagType = rootTypeTag.split(" ")[0];
        if ("object".equals(tagType)) {
            return deserializeObject(typeToReturn, rootAttributes);
        } else if ("value".equals(rootTypeTag)) {
            // return type.
        }
        return null;
    }

    private <T> T deserializeObject(Class<T> typeToReturn, Map<String, String> rootAttributes) {
        T objResult = createObjectOfType(typeToReturn);
        while (true) {
            String fieldVariableTag = nextStartTag();
            Map<String, String> attributes = getAttributes(fieldVariableTag);
            String fieldTagType = fieldVariableTag.split(" ")[0];

            if ("value".equals(fieldTagType)) {
                String fieldValue = parseValue(fieldTagType);
                Field match = findField(objResult, attributes);
                if (match == null)
                    throw new RuntimeException("Could not find field variable " + attributes.get("name") + " on object " + rootAttributes.get("type"));
                match.setAccessible(true);
                //  try {
                //Object objectOfType = createPrimitive(match.getType(), fieldValue, match);
                setPrimitiveOnField(match, objResult, fieldValue);
                //match.set(objResult, objectOfType);
                /*} catch (IllegalAccessException e) {
                    e.printStackTrace();
                }*/
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
                break;
            }
        }
        return match;
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

    private String nextStartTag() {

        boolean isInTag = false;
        boolean isTagContent = false;
        String s = "";
        while (true) {
            if (xml.charAt(charIdx) == '<') {
                isInTag = true;
            } else if (xml.charAt(charIdx) == '>') {
                isInTag = false;
                charIdx++;
                break;
            } else {
                s += xml.charAt(charIdx);
            }
            charIdx++;
        }
        return s;
    }
}
