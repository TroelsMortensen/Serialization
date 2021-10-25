package trmo.serialization.testobjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Person extends Human{
    private String firstName;
    private String lastName;
    private int age;
    private boolean isSingle;
    private char gender;
    private double height;
    private List<String> middleNames;// = Arrays.stream(new String[]{"hello", "world"}).toList();
    private int[] numbers;// = {3,4,11};
    private Address address;

    public Person(String firstName, String lastName, int age, boolean isSingle, char gender, double height, String skinColor) {
        super(skinColor);
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.isSingle = isSingle;
        this.gender = gender;
        this.height = height;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isSingle() {
        return isSingle;
    }

    public void setSingle(boolean single) {
        isSingle = single;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public List<String> getMiddleNames() {
        return middleNames;
    }

    public void setMiddleNames(List<String> middleNames) {
        this.middleNames = middleNames;
    }

    public int[] getNumbers() {
        return numbers;
    }

    public void setNumbers(int[] numbers) {
        this.numbers = numbers;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
