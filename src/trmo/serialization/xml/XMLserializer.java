package trmo.serialization.xml;

public class XMLserializer {

    public static String toXML(Object obj) {
        return new XMLCreator().toXML(obj);
    }

    public static <T> T toObject(String s) {
        return new ObjectCreator<T>().toObject(s);
    }
}
