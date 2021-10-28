package trmo.serialization.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class XMLserializer {

    public static String toXML(Object obj) {
        return new XMLCreator().toXML(obj);
    }

    public static <T> T toObject(String s, Class<T> type) {
        return new ObjectCreator().toObject(s, type);
    }

    public static <T> List<T> toList(String xml, Class<T> elementType) {
        return new ObjectCreator().toList(xml, elementType);
    }
}
