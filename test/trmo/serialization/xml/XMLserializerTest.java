package trmo.serialization.xml;

import org.junit.jupiter.api.Test;
import trmo.serialization.testobjects.Address;
import trmo.serialization.testobjects.City;
import trmo.serialization.testobjects.ObjectOfPrimitives;
import trmo.serialization.testobjects.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class XMLserializerTest {

    @Test
    public void printFields() {
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>\n" +
                "<object name=\"\" type=\"trmo.serialization.testobjects.Person\">\n" +
                "\t<value name=\"firstName\" type=\"java.lang.String\">\n" +
                "\t\tTroels\n" +
                "\t</value>\n" +
                "\t<value name=\"lastName\" type=\"java.lang.String\">\n" +
                "\t\tMortensen\n" +
                "\t</value>\n" +
                "\t<value name=\"age\" type=\"java.lang.Integer\">\n" +
                "\t\t35\n" +
                "\t</value>\n" +
                "\t<value name=\"isSingle\" type=\"java.lang.Boolean\">\n" +
                "\t\tfalse\n" +
                "\t</value>\n" +
                "\t<value name=\"gender\" type=\"java.lang.Character\">\n" +
                "\t\tm\n" +
                "\t</value>\n" +
                "\t<value name=\"height\" type=\"java.lang.Double\">\n" +
                "\t\t183.2\n" +
                "\t</value>\n" +
                "\t<collection name=\"middleNames\">\n" +
                "\t\t<element>\n" +
                "\t\t\t<value name=\"\" type=\"java.lang.String\">\n" +
                "\t\t\t\thello\n" +
                "\t\t\t</value>\n" +
                "\t\t</element>\n" +
                "\t\t<element>\n" +
                "\t\t\t<value name=\"\" type=\"java.lang.String\">\n" +
                "\t\t\t\tworld\n" +
                "\t\t\t</value>\n" +
                "\t\t</element>\n" +
                "\t</collection>\n" +
                "\t<collection name=\"numbers\">\n" +
                "\t\t<element>\n" +
                "\t\t\t<value name=\"\" type=\"java.lang.Integer\">\n" +
                "\t\t\t\t3\n" +
                "\t\t\t</value>\n" +
                "\t\t</element>\n" +
                "\t\t<element>\n" +
                "\t\t\t<value name=\"\" type=\"java.lang.Integer\">\n" +
                "\t\t\t\t8\n" +
                "\t\t\t</value>\n" +
                "\t\t</element>\n" +
                "\t\t<element>\n" +
                "\t\t\t<value name=\"\" type=\"java.lang.Integer\">\n" +
                "\t\t\t\t123\n" +
                "\t\t\t</value>\n" +
                "\t\t</element>\n" +
                "\t</collection>\n" +
                "\t<object name=\"address\" type=\"trmo.serialization.testobjects.Address\">\n" +
                "\t\t<value name=\"street\" type=\"java.lang.String\">\n" +
                "\t\t\tsomehwere\n" +
                "\t\t</value>\n" +
                "\t\t<value name=\"houseNumber\" type=\"java.lang.Integer\">\n" +
                "\t\t\t11\n" +
                "\t\t</value>\n" +
                "\t\t<object name=\"city\" type=\"trmo.serialization.testobjects.City\">\n" +
                "\t\t\t<value name=\"name\" type=\"java.lang.String\">\n" +
                "\t\t\t\tMyCity\n" +
                "\t\t\t</value>\n" +
                "\t\t\t<value name=\"postalCode\" type=\"java.lang.String\">\n" +
                "\t\t\t\t005-1234\n" +
                "\t\t\t</value>\n" +
                "\t\t</object>\n" +
                "\t</object>\n" +
                "</object>";
        Person p = new Person("Troels",
                "Mortensen",
                35,
                false,
                'm',
                183.2,
                "red");
        p.setMiddleNames(Arrays.stream(new String[]{"hello", "world"}).toList());
        p.setNumbers(new int[]{3, 8, 123});
        City city = new City();
        city.setName("Horsens");
        city.setPostalCode("8700");
        city.setNumOfCitizens(57004);

        p.setAddress(new Address("somehwere", 11, city));

        String actual = XMLserializer.toXML(p);
        System.out.println(actual);

        actual = strip(actual);
        expected = strip(expected);
        assertEquals(expected, actual);
    }

    private String strip(String s) {
        return s.replace("\t", "").replace("\n", "");
    }

    @Test
    public void singleStringToAndFrom() {
        String input = "Hello World";
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>" +
                "<value name=\"\" type=\"java.lang.String\">" +
                "Hello World" +
                "</value>";
        String serialized = XMLserializer.toXML(input);
        serialized = serialized.replace("\n", "").replace("\t", "");
        assertEquals(expected, serialized);
        String deserialized = XMLserializer.toObject(input, String.class);
        assertEquals(deserialized, input);
    }

    @Test
    public void listOfStringsToXML() {
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>\n" +
                "<collection name=\"\">\n" +
                "\t<element>\n" +
                "\t\t<value name=\"\" type=\"java.lang.String\">\n" +
                "\t\t\tHello\n" +
                "\t\t</value>\n" +
                "\t</element>\n" +
                "\t<element>\n" +
                "\t\t<value name=\"\" type=\"java.lang.String\">\n" +
                "\t\t\tWorld\n" +
                "\t\t</value>\n" +
                "\t</element>\n" +
                "</collection>\n";
        List<String> list = new ArrayList<>();
        list.add("Hello");
        list.add("World");
        String actual = XMLserializer.toXML(list);
        assertEquals(expected, actual);
    }

    @Test
    public void deserializeSimpleString() {
        String s = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>\n" +
                "<value name=\"\" type=\"java.lang.String\">\n" +
                "\tTroels\n" +
                "</value>";
        s = strip(s);
        String result = XMLserializer.toObject(s, String.class);
        int stopher = 0;
    }

    @Test
    public void testString(){
        String expected = "Hello world";
        String xml = XMLserializer.toXML(expected);
        String actual = XMLserializer.toObject(xml, String.class);
        assertEquals(expected, actual);
    }

    @Test
    public void deserializeInt() {
        XMLserializer.toObject("", int.class);
    }

    @Test
    public void xmlToSimpleObj() {
        String horsensCity = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>\n" +
                "<object name=\"\" type=\"trmo.serialization.testobjects.City\">\n" +
                "\t<value name=\"name\" type=\"java.lang.String\">\n" +
                "\t\tHorsens\n" +
                "\t</value>\n" +
                "\t<value name=\"postalCode\" type=\"java.lang.String\">\n" +
                "\t\t8700\n" +
                "\t</value>\n" +
                "\t<value name=\"numOfCitizens\" type=\"java.lang.Integer\">\n" +
                "\t\t57004\n" +
                "\t</value>\n" +
                "</object>";

        City city = XMLserializer.toObject(horsensCity, City.class);
        int stopher = 0;
    }

    @Test
    public void serializeCity() {
        City city = new City();
        city.setName("Horsens");
        city.setPostalCode("8700");
        city.setNumOfCitizens(57004);
        String xml = XMLserializer.toXML(city);
        System.out.println(xml);
    }

    @Test
    void objectOfPrimitivesBackAndForth() {
        byte by = 8;
        short aShort = 1235;
        ObjectOfPrimitives oop = new ObjectOfPrimitives().
                setaBoolean(true).
                setaByte(by).
                setaChar('M').
                setaFloat(12.35f).
                setaDouble(123.6543).
                setaShort(aShort).
                setAnInt(8484).
                setaString("Hello world").
                setaLong(783947593L);
        String s = XMLserializer.toXML(oop);
        System.out.println(s);
        ObjectOfPrimitives result = XMLserializer.toObject(s, ObjectOfPrimitives.class);
        assertEquals(true, result.getBoolean());
        assertEquals(by, result.getByte());
        assertEquals('M', result.getChar());
        assertEquals(12.35f, result.getFloat());
        assertEquals(123.6543, result.getDouble());
        assertEquals(aShort, result.getShort());
        assertEquals(8484, result.getInt());
        assertEquals("Hello world", result.getString());
        assertEquals(783947593L, result.getLong());
    }

    @Test
    public void testNestedObjects(){
        City city = new City();
        city.setName("Horsens");
        city.setPostalCode("8700");
        city.setNumOfCitizens(57004);
        Address address = new Address("somewhere", 11, city);
        String xml = XMLserializer.toXML(address);
        System.out.println(xml);
        Address result = XMLserializer.toObject(xml, Address.class);
        assertEquals("somewhere", address.getStreet());
        assertEquals(11, address.getHouseNumber());
        assertEquals("Horsens", address.getCity().getName());
        assertEquals("8700", address.getCity().getPostalCode());
        assertEquals(57004, address.getCity().getNumOfCitizens());
    }

    @Test
    public void testOnlyString(){
        String expected = "Hello world";
        String s1 = XMLserializer.toXML(expected);
        String actual = XMLserializer.toObject(s1, String.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testOnlyInteger(){
        Integer expected = 7;
        String xml = XMLserializer.toXML(expected);
        System.out.println(xml);
        Integer actual = XMLserializer.toObject(xml, Integer.class);
        assertEquals(expected.intValue(), actual.intValue());
    }

    @Test
    public void testBoolean(){
        Boolean b = false;
        String xml1 = XMLserializer.toXML(b);
        Boolean actual1 = XMLserializer.toObject(xml1, Boolean.class);
        assertEquals(b, actual1);

        Boolean b1 = true;
        String xml2 = XMLserializer.toXML(b1);
        Boolean actual2 = XMLserializer.toObject(xml2, Boolean.class);
        assertEquals(b1, actual2);
    }

    @Test
    public void testDouble(){
        Double expected = 123.145;
        String xml = XMLserializer.toXML(expected);
        Double actual = XMLserializer.toObject(xml, Double.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testOnlyInt(){
        Integer expected = 7;
        String xml = XMLserializer.toXML(expected);
        System.out.println(xml);
        Integer actual = XMLserializer.toObject(xml, int.class);
        assertEquals(expected.intValue(), actual.intValue());
    }

    @Test
    public void testboolean(){
        boolean b = false;
        String xml1 = XMLserializer.toXML(b);
        boolean actual1 = XMLserializer.toObject(xml1, boolean.class);
        assertEquals(b, actual1);

        boolean b1 = true;
        String xml2 = XMLserializer.toXML(b1);
        boolean actual2 = XMLserializer.toObject(xml2, boolean.class);
        assertEquals(b1, actual2);
    }

    @Test
    public void testArrayListOfStrings(){
        List<String> strings = Arrays.asList("hello", "world", "how", "are", "you");
        String xml = XMLserializer.toXML(strings);
        System.out.println(xml);
        List<String> result = XMLserializer.toList(xml, String.class);
        strings.getClass().getName();

    }

}