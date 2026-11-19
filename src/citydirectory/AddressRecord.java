package citydirectory;

import java.util.Objects;

public class AddressRecord {

    private final String city;
    private final String street;
    private final String house;
    private final int floor;

    public AddressRecord(String city, String street, String house, int floor) {
        this.city = city;
        this.street = street;
        this.house = house;
        this.floor = floor;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getHouse() {
        return house;
    }

    public int getFloor() {
        return floor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AddressRecord)) {
            return false;
        }
        AddressRecord that = (AddressRecord) o;
        return floor == that.floor
                && city.equalsIgnoreCase(that.city)
                && street.equalsIgnoreCase(that.street)
                && house.equalsIgnoreCase(that.house);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                city.toLowerCase(),
                street.toLowerCase(),
                house.toLowerCase(),
                floor
        );
    }

    @Override
    public String toString() {
        return city + ", " + street + ", дом " + house + ", этажей: " + floor;
    }
}
