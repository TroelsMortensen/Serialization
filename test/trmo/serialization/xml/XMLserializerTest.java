package trmo.serialization.xml;

import org.junit.jupiter.api.Test;
import trmo.serialization.testobjects.Address;
import trmo.serialization.testobjects.City;
import trmo.serialization.testobjects.Person;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class XMLserializerTest {

    @Test
    public void printFields() throws ClassNotFoundException, IllegalAccessException {
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

}