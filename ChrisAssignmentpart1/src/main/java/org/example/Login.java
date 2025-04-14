package org.example;

import javax.print.attribute.standard.JobOriginatingUserName;

public class Login {

    // Attributes (fields)
    private String firstName;
    private String lastName;
    private String username;
    private String savedUsername;
    private String savedPassword;
    private String savedPhone;

    // Constructor
    public Login(String firstName, String lastName, String username, String password, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.savedUsername = username;
        this.savedPassword = password;
        this.savedPhone = phone;
    }

    // Check if the username is valid (contains ("_") and is no more than 5characters)
    public static boolean checkUserNameFormat(String userName) {
        return userName.contains("_")&& userName.length()<= 5;
    }

    // Check if password
    public static boolean IsValidPasswordComplexity(String password) {
        return password.length() >=8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[!@#_$%^&*()].*");

    }

    // check if the cell phone has the correct length and contains the country code
    public static boolean IsValidcellPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^\\+27\\d{10}$");

    }

    //Registration
    public String registerUser(String username, String password, String cellPhoneNumber) {

        if (!checkUserNameFormat(username)) {
            return "Username format is invalid. It must contain an underscore and no more than 5 characters.";

        }

        if (!IsValidPasswordComplexity(password)) {
            return "password format is invalid. It must at least 8 characters long and include capital letter, a number, and a special character.";

        }

        this.savedUsername = username;
        this.savedPassword = password;
        this.savedPhone = cellPhoneNumber;
        return "user registered successfully";
    }

    //Verifying login credentials
    public  boolean authentication(String enteredUsername, String enteredPassword) {
        return enteredUsername.equals(this.username) && enteredPassword.equals(this.savedPassword);
    }

    //Return loginStatus Message
    public String getLoginStatus(String enteredUsername, String enteredPassword) {

        if (enteredUsername.equals(this.username) && enteredPassword.equals(this.savedPassword)) {
            return "Welcome" + firstName + " " + lastName + ", it's great to see you again!";


        }else {
            return "Incorrect user or password. please try again";

        }

    }

}



