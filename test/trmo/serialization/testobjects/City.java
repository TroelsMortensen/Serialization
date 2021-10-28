package trmo.serialization.testobjects;

public class City {
    private String name;
    private String postalCode;
    private int numOfCitizens;
    public City() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public int getNumOfCitizens() {
        return numOfCitizens;
    }

    public void setNumOfCitizens(int numOfCitizens) {
        this.numOfCitizens = numOfCitizens;
    }
}
