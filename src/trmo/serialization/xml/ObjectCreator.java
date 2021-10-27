package trmo.serialization.xml;

import org.xml.sax.*;


class ObjectCreator<T> {

    private String xml;
    private Class<T> type;

    public T toObject(String xml, Class<T> type) {
        this.xml = xml;
        this.type = type;
        xml = xml.replace("\n", "").replace("\t", "");
        int charIdx = 0;
        //parse(0);
        return null;
    }
}
