package org.example;

import com.google.gson.JsonObject;
import org.example.Message;

import javax.swing.JOptionPane;

public class Main {

    public static void main(String[] args) {

        // === Load messages from file on app start ===
        Message.loadMessagesFromFile();

        // Welcome message
        JOptionPane.showMessageDialog(null, "Welcome to QuickChatApp");

        // === Registration ===//

        String firstName = JOptionPane.showInputDialog("Enter your first name:");
        String lastName = JOptionPane.showInputDialog("Enter your last name:");
        String username = JOptionPane.showInputDialog("Enter a username (max 5 characters, must contain '_'):");
        String password = JOptionPane.showInputDialog("Enter a password (min 8 chars, with uppercase, number, and special character):");
        String cellNumber = JOptionPane.showInputDialog("Enter cell number (e.g., +27831234567):");




        Login login = new Login(firstName, lastName, username, password, cellNumber);
        String registrationResult = login.registerUser(username, password, cellNumber);

        JOptionPane.showMessageDialog(null,registrationResult);


        // Check if registration was successful
        if (!registrationResult.equals("User registered successfully.")) {
            JOptionPane.showMessageDialog(null,"Registration failed. Exiting");
            return;
        }

        // === Login ===
        JOptionPane.showMessageDialog(null, "=== User Login ===");

        String LoginUsername = JOptionPane.showInputDialog("Enter your Username");
        String LoginPassword = JOptionPane.showInputDialog("Enter your Password");

        if (!login.authentication(LoginUsername, LoginPassword)) {

            JOptionPane.showMessageDialog(null, "incorrect username or password. Exiting...");
            return;
        }

        JOptionPane.showMessageDialog(null,login.getLoginStatus(LoginUsername, LoginPassword));

        //=== Sending messages ===
        while (true) {

            int choice = JOptionPane.showConfirmDialog(null, "Do you want to send a message?" , "Send message", JOptionPane.YES_NO_OPTION);
            if (choice != JOptionPane.YES_OPTION) {
                break;
            }

            String recipient = JOptionPane.showInputDialog("Enter recipient number (+27xxxxxxxxx)");
            String messageContent = JOptionPane.showInputDialog("Enter your message (max 250 characters):");


            try {
                Message message = new Message(recipient, messageContent);
                JsonObject stored = message.storeMessage();

                // Display message details in a dialog
                JOptionPane.showMessageDialog(null,
                        message.getMessageDetails() +
                                "\n\nJSON: " + stored.toString() +
                                "\n\nTotal Messages Sent: " + Message.getTotalMessages());

            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        }

        // === Save messages to file on exit ===
        Message.saveMessageToFile();

        JOptionPane.showMessageDialog(null, "Thank you for using QuickChatApp. Goodbye!");





        }




}