
/**
 * The Flight class represents an individual flight with associated details
 * such as flight number, airplane make, flight type, and additional information.
 * This class is designed to be used in a JavaFX application, utilizing
 * observable properties for binding with UI components like TableView.
 */

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Flight {
    private final StringProperty flightNumber = new SimpleStringProperty();
    private final StringProperty airplaneMake = new SimpleStringProperty();
    private final StringProperty type = new SimpleStringProperty();
    private final StringProperty additionalInfo = new SimpleStringProperty();
    private final StringProperty departureTime = new SimpleStringProperty();
    private final StringProperty landingTime = new SimpleStringProperty();
    private final StringProperty flightDuration = new SimpleStringProperty();


    public Flight(String flightNumber, String airplaneMake, String type, String additionalInfo, String departureTime, String landingTime, String flightDuration) {
        this.flightNumber.set(flightNumber);
        this.airplaneMake.set(airplaneMake);
        this.type.set(type);
        this.additionalInfo.set(additionalInfo);
        this.departureTime.set(departureTime);
        this.landingTime.set(landingTime);
        this.flightDuration.set(flightDuration);
    }

    public StringProperty flightNumberProperty() {
        return flightNumber;
    }

    public StringProperty departureTimeProperty() {
        return departureTime;
    }

    public StringProperty landingTimeProperty() {
        return landingTime;
    }

    public StringProperty flightDurationProperty() {
        return flightDuration;
    }


    public String getFlightNumber() {
        return flightNumber.get();
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber.set(flightNumber);
    }

    public StringProperty airplaneMakeProperty() {
        return airplaneMake;
    }

    public String getAirplaneMake() {
        return airplaneMake.get();
    }

    public void setAirplaneMake(String airplaneMake) {
        this.airplaneMake.set(airplaneMake);
    }

    public StringProperty typeProperty() {
        return type;
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public StringProperty additionalInfoProperty() {
        return additionalInfo;
    }

    public String getAdditionalInfo() {
        return additionalInfo.get();
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo.set(additionalInfo);
    }
}