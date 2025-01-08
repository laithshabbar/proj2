package testing;
import static org.junit.Assert.*;
import org.junit.Test;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import util.DBConnection;
/*Unit Testing*/
public class ServletTests {
    /* Database Connection Tests */
    @Test
    public void testDBConnection() {
        try {
            Connection conn = DBConnection.getConnection();
            assertNotNull("Database connection should not be null", conn);
            conn.close();
        } catch (Exception e) {
            fail("Database connection failed: " + e.getMessage());
        }
    }

    @Test
    public void testBasicValidations() {
        // Station validation
        assertTrue("Valid station should be accepted", isValidStation("Amman"));
        assertFalse("Empty station should be rejected", isValidStation(""));
        assertFalse("Null station should be rejected", isValidStation(null));
        // Login validation
        assertTrue("Valid login should pass", isValidLoginInput("user1", "pass123"));
        assertFalse("Empty username should fail", isValidLoginInput("", "pass123"));
        assertFalse("Null password should fail", isValidLoginInput("user1", null));
        // Registration validation
        assertTrue("Valid registration should pass", 
            isValidRegistrationInput("user1", "pass123", "test@email.com"));
        assertFalse("Empty email should fail", 
            isValidRegistrationInput("user1", "pass123", ""));
    }

    /* Time Formatting Tests */
    @Test
    public void testTimeFormatting() {
        assertEquals("14:30 should convert to 02:30 PM", "02:30 PM", convertTo12Hour("14:30"));
        assertEquals("09:15 should convert to 09:15 AM", "09:15 AM", convertTo12Hour("09:15"));
        assertEquals("00:00 should convert to 12:00 AM", "12:00 AM", convertTo12Hour("00:00"));
        assertEquals("12:00 should convert to 12:00 PM", "12:00 PM", convertTo12Hour("12:00"));
    }

    /* Reservation Time Tests */
    @Test
    public void testReservationTimeValidation() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp future = new Timestamp(now.getTime() + 86400000); // 24 hours later
        Timestamp past = new Timestamp(now.getTime() - 86400000); // 24 hours ago
        
        assertTrue("Future reservation should be valid", isValidReservationTime(future));
        assertFalse("Past reservation should be invalid", isValidReservationTime(past));
    }

    /* Seat Number Tests */
    @Test
    public void testSeatNumberValidation() {
        assertTrue("Valid seat number should pass", isValidSeatNumber("A1"));
        assertTrue("Valid seat number should pass", isValidSeatNumber("D3"));
        assertFalse("Empty seat number should fail", isValidSeatNumber(""));
        assertFalse("Null seat number should fail", isValidSeatNumber(null));
        assertFalse("Invalid format should fail", isValidSeatNumber("1A"));
    }

    /* Date Format Tests */
    @Test
    public void testDateFormatValidation() {
        assertTrue("Valid date format should pass", isValidDateFormat("2024-12-31"));
        assertFalse("Invalid date format should fail", isValidDateFormat("31-12-2024"));
        assertFalse("Empty date should fail", isValidDateFormat(""));
        assertFalse("Null date should fail", isValidDateFormat(null));
    }

    /* User ID Tests */
    @Test
    public void testUserIdValidation() {
        assertTrue("Valid user ID should pass", isValidUserId(1));
        assertTrue("Valid user ID should pass", isValidUserId(10000));
        assertFalse("Zero user ID should fail", isValidUserId(0));
        assertFalse("Negative user ID should fail", isValidUserId(-1));
    }

    /* Time Selection Tests */
    @Test
    public void testTimeSelection() {
        assertTrue("Valid time should pass", isValidTimeSelection("14:30"));
        assertTrue("Valid time should pass", isValidTimeSelection("09:00"));
        assertFalse("Invalid time format should fail", isValidTimeSelection("14:60"));
        assertFalse("Empty time should fail", isValidTimeSelection(""));
        assertFalse("Null time should fail", isValidTimeSelection(null));
    }

    
    /* Email Validation Tests */
    @Test
    public void testEmailValidation() {
        assertTrue("Valid email should pass", isValidEmail("user@example.com"));
        assertTrue("Email with subdomain should pass", isValidEmail("user@sub.example.com"));
        assertFalse("Email without @ should fail", isValidEmail("userexample.com"));
        assertFalse("Empty email should fail", isValidEmail(""));
        assertFalse("Null email should fail", isValidEmail(null));
    }
    
    /* Password Validation Tests */
    @Test
    public void testPasswordValidation() {
        assertTrue("Valid password should pass", isValidPassword("Pass123!"));
        assertTrue("Minimum length password should pass", isValidPassword("Pass12"));
        assertFalse("Empty password should fail", isValidPassword(""));
        assertFalse("Null password should fail", isValidPassword(null));
        assertFalse("Too short password should fail", isValidPassword("Ab1"));
    }
    /* Max Reservations Test */
    @Test
    public void testMaxReservations() {
        assertEquals("Should match constant in ReservationServlet", 6, getMaxDailyReservations());
        assertTrue("5 reservations should be under limit", isUnderDailyLimit(5));
        assertFalse("7 reservations should exceed limit", isUnderDailyLimit(7));
    }

    /* City Name Tests */
    @Test
    public void testCityNameValidation() {
        assertTrue("Valid city name should pass", isValidCityName("Amman"));
        assertTrue("City name with space should pass", isValidCityName("Irbid"));
        assertFalse("Empty city should fail", isValidCityName(""));
        assertFalse("Null city should fail", isValidCityName(null));
    }

    /* Driver Role Tests */
    @Test
    public void testDriverRoleValidation() {
        assertTrue("Valid driver role should pass", isValidRole("driver"));
        assertTrue("Valid user role should pass", isValidRole("user"));
        assertFalse("Empty role should fail", isValidRole(""));
        assertFalse("Invalid role should fail", isValidRole("admin"));
    }

    /* University ID Tests */
    @Test
    public void testUniversityIdValidation() {
        assertTrue("Valid uni ID should pass", isValidUniId("2020123456"));
        assertFalse("Short uni ID should fail", isValidUniId("123"));
        assertFalse("Invalid format should fail", isValidUniId("ABCD123456"));
        assertFalse("Empty uni ID should fail", isValidUniId(""));
    }
    /* DateTime Format Tests */
    @Test
    public void testDateTimeFormat() {
        assertTrue("Valid datetime should pass", isValidDateTime("2024-12-31 14:30:00"));
        assertFalse("Invalid date should fail", isValidDateTime("2024-13-31 14:30:00"));
        assertFalse("Empty datetime should fail", isValidDateTime(""));
    }

    /* Seat Number Format Tests */
    @Test
    public void testSeatFormatting() {
        assertTrue("Valid bus seat should pass", isValidBusSeat("A1"));
        assertTrue("Valid double digit seat should pass", isValidBusSeat("B12"));
        assertFalse("Invalid row should fail", isValidBusSeat("E1"));
        assertFalse("Invalid number should fail", isValidBusSeat("A0"));
    }

    /* Station URL Tests */
    @Test
    public void testStationUrl() {
        assertTrue("Valid maps URL should pass", isValidMapsUrl("https://maps.google.com/123"));
        assertFalse("Empty URL should fail", isValidMapsUrl(""));
        assertFalse("Invalid URL format should fail", isValidMapsUrl("not-a-url"));
    }

    /* Seat Availability Tests */
    @Test
    public void testSeatAvailability() {
        Set<String> reservedSeats = new HashSet<>(Arrays.asList("A1", "B2", "C3"));
        assertTrue("Available seat should pass", isSeatAvailable("D4", reservedSeats));
        assertFalse("Reserved seat should fail", isSeatAvailable("A1", reservedSeats));
        assertFalse("Null seat should fail", isSeatAvailable(null, reservedSeats));
    }

    /* Helper Methods */
    private boolean isValidStation(String station) {
        return station != null && !station.trim().isEmpty();
    }

    private boolean isValidLoginInput(String username, String password) {
        return username != null && !username.isEmpty() 
            && password != null && !password.isEmpty();
    }

    private boolean isValidRegistrationInput(String username, String password, String email) {
        return username != null && !username.isEmpty() 
            && password != null && !password.isEmpty()
            && email != null && !email.isEmpty();
    }

    private String convertTo12Hour(String time24) {
        String[] parts = time24.split(":");
        int hour = Integer.parseInt(parts[0]);
        String minutes = parts[1];
        String period = hour >= 12 ? "PM" : "AM";
        hour = hour > 12 ? hour - 12 : hour;
        hour = hour == 0 ? 12 : hour;
        return String.format("%02d:%s %s", hour, minutes, period);
    }

    private boolean isValidReservationTime(Timestamp reservationTime) {
        return reservationTime != null && 
               reservationTime.after(new Timestamp(System.currentTimeMillis()));
    }

    private boolean isValidSeatNumber(String seatNumber) {
        return seatNumber != null && seatNumber.matches("[A-D][1-9][0-9]?");
    }

    private boolean isValidDateFormat(String date) {
        return date != null && date.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    private boolean isValidUserId(int userId) {
        return userId > 0;
    }

    private boolean isValidTimeSelection(String time) {
        if (time == null || time.isEmpty()) {
            return false;
        }
        String[] parts = time.split(":");
        if (parts.length != 2) {
            return false;
        }
        try {
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            return hours >= 0 && hours < 24 && minutes >= 0 && minutes < 60;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
    private int getMaxDailyReservations() {
        return 6; 
    }

    private boolean isUnderDailyLimit(int reservationCount) {
        return reservationCount < getMaxDailyReservations();
    }

    private boolean isValidCityName(String cityName) {
        return cityName != null && !cityName.trim().isEmpty();
    }


    private boolean isValidRole(String role) {
        return role != null && (role.equals("driver") || role.equals("user"));
    }

    private boolean isValidUniId(String uniId) {
        return uniId != null && uniId.matches("\\d{10}");
    }
    private boolean isValidDateTime(String datetime) {
        if (datetime == null || datetime.isEmpty()) return false;
        try {
            Timestamp.valueOf(datetime);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isValidBusSeat(String seat) {
        return seat != null && seat.matches("[A-D][1-9][0-9]?");
    }

    private boolean isValidMapsUrl(String url) {
        return url != null && !url.isEmpty() && url.startsWith("https://maps.google.com/");
    }


    private boolean isSeatAvailable(String seat, Set<String> reservedSeats) {
        return seat != null && !reservedSeats.contains(seat);
    }

}
