package org.example;

import com.google.gson.JsonObject;
import javax.swing.JOptionPane;

public class Main {

    public static void main(String[] args) {



        // === Registration ===//

        String firstName = JOptionPane.showInputDialog("Enter your first name:");
        String lastName = JOptionPane.showInputDialog("Enter your last name:");
        String username = JOptionPane.showInputDialog("Enter a username (max 5 characters, must contain '_'):");
        String password = JOptionPane.showInputDialog("Enter a password (min 8 chars, with uppercase, number, and special character):");
        String cellNumber = JOptionPane.showInputDialog("Enter cell number (e.g., +27831234567):");




        Login login = new Login(firstName, lastName, username, password, cellNumber);
        String registrationResult = login.registerUser(username, password, cellNumber);
        JOptionPane.showMessageDialog(null,registrationResult);


        if (!registrationResult.equals("User has been registered successfully.")) {
            JOptionPane.showMessageDialog(null,"Registration failed. Exiting");
            return;
        }

        JOptionPane.showMessageDialog(null, "=== User Login ===");

        String LoginUsername = JOptionPane.showInputDialog("Enter your Username to login:");
        String LoginPassword = JOptionPane.showInputDialog("Enter your Password to login:");









    }
}