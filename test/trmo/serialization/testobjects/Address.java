package trmo.serialization.testobjects;

public class Address {

    private String street;
    private int houseNumber;
    private City city;

    public Address(String street, int houseNumber, City city) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
