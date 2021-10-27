package trmo.serialization.xml;

import org.junit.jupiter.api.Test;
import trmo.serialization.testobjects.Address;
import trmo.serialization.testobjects.City;
import trmo.serialization.testobjects.Person;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class XMLserializerTest {

    @Test
    public void printFields() {
        Person p = new Person("Troels",
                "Mortensen",
                35,
                false,
                'm',
                183.2,
                "red");
        p.setMiddleNames( Arrays.stream(new String[]{"hello", "world"}).toList());
        p.setNumbers(new int[]{3,8,123});
        p.setAddress(new Address("somehwere", 11, new City("MyCity", "005-1234")));

        String s = XMLserializer.toXML(p);
        System.out.println(s);
    }

    @Test
    public void singleStringToAndFrom(){
        String input = "Hello World";
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>" +
                "<value type=\"java.lang.String\">" +
                "Hello World" +
                "</value>";
        String serialized = XMLserializer.toXML(input);
        serialized = serialized.replace("\n", "").replace("\t", "");
        assertEquals(expected, serialized);
        String deserialized = XMLserializer.toObject(input, String.class);
        assertEquals(deserialized, input);
    }

    @Test
    public void listOfStringsToXML(){
        List<String> list = new ArrayList<>();
        list.add("Hello");
        list.add("World");
        String serialized = XMLserializer.toXML(list);
        System.out.println(serialized);
    }

}