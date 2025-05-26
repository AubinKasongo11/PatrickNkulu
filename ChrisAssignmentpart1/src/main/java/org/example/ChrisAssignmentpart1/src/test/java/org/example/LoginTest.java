package org.example;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginTest {

    @Test
    void checkUserNameFormat() {
        assertTrue(Login.checkUserNameFormat("abc_"));
        assertFalse(Login.checkUserNameFormat("abcdef"));
        assertFalse(Login.checkUserNameFormat("abcde"));

    }

    @Test
    void isValidPasswordComplexity() {
        assertTrue(Login.IsValidPasswordComplexity("Password1!"));
        assertFalse(Login.IsValidPasswordComplexity("password")); //
        assertFalse(Login.IsValidPasswordComplexity("Password"));
        assertFalse(Login.IsValidPasswordComplexity("Pass1"));

    }

    @Test
    void isValidcellPhoneNumber() {
        assertTrue(Login.IsValidcellPhoneNumber("+271234567890"));
        assertFalse(Login.IsValidcellPhoneNumber("0123456789")); // missing +27
        assertFalse(Login.IsValidcellPhoneNumber("+27123456")); // too short

    }

    @Test
    void registerUser() {
        Login login = new Login("John", "Doe", "jd_1", "Password1!", "+271234567890");
        String result = login.registerUser("jd_1", "Password1!", "+271234567890");
        assertEquals("User registered successfully", result);
    }

    @Test
    public void testRegisterInvalidUsername() {
        Login login = new Login("John", "Doe", "jd_1", "Password1!", "+271234567890");
        String result = login.registerUser("johnny", "Password1!", "+271234567890");
        assertEquals("Username format is invalid. It must contain an underscore and no more than 5 characters.", result);



    }

    @Test
    void authentication() {
            Login login = new Login("John", "Doe", "jd_1", "Password1!", "+271234567890");
            login.registerUser("jd_1", "Password1!", "+271234567890");
            assertFalse(login.authentication("wrong", "Password1!"));
            assertFalse(login.authentication("jd_1", "WrongPass"));
    }

    @Test
    void getLoginStatus() {
        Login login = new Login("John", "Doe", "jd_1", "Password1!", "+271234567890");
        login.registerUser("jd_1", "Password1!", "+271234567890");
        String message = login.getLoginStatus("jd_1", "WrongPass");
        assertEquals("Incorrect user or password. please try again.", message);
    }
}