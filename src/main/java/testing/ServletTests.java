package testing;

import static org.junit.Assert.*;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Timestamp;
import util.DBConnection;

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

    /* Basic Validation Tests */
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
}