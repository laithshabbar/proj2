package util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ReservationHistory {
    private int reservationId;
    private String cityName;
    private String stationName;
    private String seatNumber;
    private Timestamp reservationTime;
    private Timestamp departureTime;
    
    // Constructor
    public ReservationHistory(int reservationId, String cityName, String stationName, 
                              String seatNumber, Timestamp reservationTime, Timestamp departureTime) {
        this.reservationId = reservationId;
        this.cityName = cityName;
        this.stationName = stationName;
        this.seatNumber = seatNumber;
        this.reservationTime = reservationTime;
        this.departureTime = departureTime;
    }

    // Getters
    public int getReservationId() { return reservationId; }
    public String getCityName() { return cityName; }
    public String getStationName() { return stationName; }
    public String getSeatNumber() { return seatNumber; }
    public Timestamp getReservationTime() { return reservationTime; }
    public Timestamp getDepartureTime() { return departureTime; }
    
    // Formatted date getters
    public String getFormattedReservationTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a"); // 12-hour format with AM/PM
        return sdf.format(this.reservationTime);
    }
    
    public String getFormattedDepartureTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a"); // 12-hour format with AM/PM
        return sdf.format(this.departureTime);
    }
    
    // Check if the reservation is upcoming
    public boolean isUpcoming() {
        return this.departureTime.after(new java.util.Date());
    }
}
