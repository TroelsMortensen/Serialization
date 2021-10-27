package trmo.serialization.xml;

class ObjectCreator<T> {

    public T toObject(String xml, Class<T> type) {
        xml = xml.replace("\n", "").replace("\t", "");

        return null;
    }
}
