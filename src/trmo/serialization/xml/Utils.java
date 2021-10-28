package trmo.serialization.xml;

public class Utils {

    static boolean isWrapper(Class<?> type) {
        if(type == Integer.class) return true;
        if(type == Double.class) return true;
        if(type == Character.class) return true;
        if(type == Short.class) return true;
        if(type == Long.class) return true;
        if(type == Byte.class) return true;
        if(type == Float.class) return true;
        if(type == Boolean.class) return true;
        return false;
    }
}
