package trmo.serialization.xml;

import java.util.Map;

public class TagInfo {
    // I miss C# properties

    String tag;
    String tagType;
    Map<String, String> attributes;

    public TagInfo(String tag, String tagType, Map<String, String> attributes) {
        this.tag = tag;
        this.tagType = tagType;
        this.attributes = attributes;
    }
}
