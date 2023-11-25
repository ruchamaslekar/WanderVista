package hotelData;


/** Demonstrating Hotel class */
public class Hotel {
    private final String hotelName;
    private final String hotelId;
    private final String latitude;
    private final String city;
    private final String state;
    private final String address;
    private final String longitude;
    private final String country;

    /** Constructor Hotel
     * @param hotelName name of hotel
     * @param hotelId hotelId
//     * @param latitude latitude
//     * @param city city
//     * @param state state
     */
    public Hotel(String hotelId, String hotelName, String address, String latitude, String longitude,String city, String state,String country) {
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.state = state;
        this.country= country;
    }

    /** Getter for HotelName */
    public String getHotelName() {
        return hotelName;
    }

    /** Getter for HotelId */
    public String getHotelId() {
        return hotelId;
    }

    /** Getter for Latitude */
    public String getLatitude() {
        return latitude;
    }

    public String getCountry() {
        return country;
    }

    /** Getter for City */
    public String getCity() {
        return city;
    }

    /** Getter for State */
    public String getState() {
        return state;
    }

    /** Getter for Address */
    public String getAddress() {
        return address;
    }

    /** Getter for Longitude */
    public String getLongitude() {
        return longitude;
    }

    /** toString
     * @return string representation of this hotel
     */
    @Override
    public String toString() {
        return  this.hotelId + System.lineSeparator()+
                this.hotelName +System.lineSeparator()+
                this.address + System.lineSeparator()+
                this.latitude + System.lineSeparator()+
                this.longitude + System.lineSeparator()+
                this.city + System.lineSeparator()+
                this.state + System.lineSeparator()+
                this.country +System.lineSeparator();
    }

    /** toStringDisplay
     * @return string representation of this hotel in different format
     */
    public String toStringDisplay() {
        return  "HotelName = " + this.hotelName + System.lineSeparator()+
                "HotelId = " + this.hotelId + System.lineSeparator()+
                "Latitude = " +  this.latitude+ System.lineSeparator()+
                "Longitude = " + this.longitude + System.lineSeparator()+
                "Address = " +  this.address +","+ this.city+","+this.state;
    }
}


