package trmo.serialization.xml;

public class SerializerOptions {
    private Boolean writeIndented;

    public SerializerOptions setWriteIndented(boolean b){
        writeIndented = b;
        return this;
    }
}
