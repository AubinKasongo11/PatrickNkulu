package org.example;

import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Message {

    // Static collections
    public static List<Message> sentMessages = new ArrayList<>();
    public static List<String> disregardedMessages = new ArrayList<>();
    public static List<Message> storedMessages = new ArrayList<>();
    public static List<String> messageHashes = new ArrayList<>();
    public static List<String> messageIDs = new ArrayList<>();

    // File path and Gson
    private static final String MESSAGE_FILE = "messages.json";
    private static final Gson gson = new Gson();

    // Instance variables
    private static int messageSentCount = 0;
    private int numMessages;
    public static int getTotalMessages() {
        return messageSentCount;
    }

    // Attributes fields
    private String messageID;
    private String recipient;
    private String messageContent;
    private String messageHash;
    private String sender = "Christian"; // Optional field

    // Status tracking
    private boolean sent = true;
    private boolean received = true;
    private boolean read = true;

    // Constructor
    public Message(String recipient, String messageContent) {
        try {
            if (!recipient.matches("\\+27\\d{9}")) {
                throw new IllegalArgumentException("Invalid recipient number. Must start with +27 and contain 9 digits.");
            }

            if (messageContent.length() > 250) {
                throw new IllegalArgumentException("Message exceeds 250 characters.");
            }

            this.recipient = recipient.trim();
            this.messageContent = messageContent.trim();
            this.messageID = generateMessageID();
            this.numMessages = ++messageSentCount;
            this.messageHash = createMessageHash();

            // Store details globally
            sentMessages.add(this);
            messageHashes.add(this.messageHash);
            messageIDs.add(this.messageID);
        } catch (IllegalArgumentException e) {
            disregardedMessages.add("[" + recipient + "] " + messageContent);
            throw e;
        }
    }

    public static boolean deleteMessage(Message message) {
        return false;
    }

    // Set sender (optional)
    public void setSender(String sender) {
        this.sender = sender;
    }

    // Generate message ID
    private String generateMessageID() {
        long id = ThreadLocalRandom.current().nextLong(1_000_000_000L, 10_000_000_000L);
        return String.valueOf(id);
    }

    public boolean checkMessageID() {
        return messageID.length() == 10;
    }

    public static boolean checkRecipientCell(String number) {
        return number.matches("\\+27\\d{9}");
    }

    public String createMessageHash() {
        String[] words = messageContent.trim().split("\\s+");
        String firstWord = words.length > 0 ? words[0].toUpperCase() : "NA";
        String lastWord = words.length > 1 ? words[words.length - 1].toUpperCase() : firstWord;
        String idPrefix = messageID.substring(0, 2);

        return idPrefix + ":" + numMessages + ":" + firstWord + lastWord;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public String getMessageID() {
        return messageID;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getSender() {
        return sender;
    }

    public String getMessageDetails() {
        return "Sender: " + sender +
                "\nMessage ID: " + messageID +
                "\nMessage Hash: " + messageHash +
                "\nRecipient: " + recipient +
                "\nMessage: " + messageContent;
    }

    public JsonObject storeMessage() {
        JsonObject store = new JsonObject();
        store.addProperty("messageID", messageID);
        store.addProperty("messageHash", messageHash);
        store.addProperty("recipient", recipient);
        store.addProperty("messageContent", messageContent);
        storedMessages.add(this); // Simulate storing to file
        saveMessageToFile(); // Save every time a message is stored
        return store;
    }

    // Save messages to file
    public static void saveMessageToFile() {
        try (FileWriter writer = new FileWriter(MESSAGE_FILE)) {
            gson.toJson(sentMessages, writer);
            JOptionPane.showMessageDialog(null,"Messages saved to file.");
        }catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to save messages: " + e.getMessage());
        }
    }

    // Load messages from file
    public static void loadMessagesFromFile() {
        try (FileReader reader = new FileReader(MESSAGE_FILE)) {
            Type messageListType = new TypeToken <List<Message>>(){}.getType();
            List<Message> loadedMessages = gson.fromJson(reader, messageListType);

            if (loadedMessages != null) {
                sentMessages.clear();
                sentMessages.addAll(loadedMessages);

                for (Message m : loadedMessages) {
                    messageIDs.add(m.getMessageID());
                    messageHashes.add(m.getMessageHash());
                    messageSentCount++;
                }

                JOptionPane.showMessageDialog(null, "Messages loaded from file.");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "no saved messages found. Starting afresh");


        }
    }

    public String getMessageOptions(int option) {
        return switch (option) {
            case 1 -> "Message successfully sent.";
            case 2 -> "Press 0 to delete message.";
            case 3 -> "Message successfully stored.";
            default -> "Invalid option entered.";
        };
    }

    // Status methods
    public void setMessageStatus(String status) {
        switch (status.toUpperCase()) {
            case "SENT" -> {
                this.sent = true;
                this.received = false;
                this.read = false;
            }
            case "RECEIVED" -> {
                if (this.sent) {
                    this.received = true;
                }
            }
            case "READ" -> {
                if (this.received) {
                    this.read = true;
                }
            }
            default -> throw new IllegalArgumentException("Invalid status: " + status);
        }
    }

    public boolean isSent() {
        return sent;
    }

    public boolean isReceived() {
        return received;
    }

    public boolean isRead() {
        return read;
    }


    // a. Display sender and recipient of all sent messages
    public static void displaySendersAndRecipients() {
        for (Message msg : sentMessages) {
            JOptionPane.showMessageDialog(null,"Sender: " + msg.sender + " | Recipient: " + msg.recipient);
        }
    }

    // b. Display longest message
    public static void displayLongestMessage() {
        Message longest = sentMessages.stream()
                .max(Comparator.comparingInt(m -> m.messageContent.length()))
                .orElse(null);

        if (longest != null) {
            System.out.println("Longest Message:\n" + longest.getMessageDetails());
        } else {
            JOptionPane.showMessageDialog(null,"No messages found.");
        }
    }

    // c. Search for message by ID
    public static void searchByMessageID(String id) {
        for (Message msg : sentMessages) {
            if (msg.messageID.equals(id)) {
                JOptionPane.showMessageDialog(null,"Message Found:\nRecipient: " + msg.recipient + "\nMessage: " + msg.messageContent);
                return;
            }
        }
        System.out.println("No message found with ID: " + id);
    }

    // d. Search messages by recipient
    public static void searchMessagesByRecipient(String recipient) {
        boolean found = false;
        for (Message msg : sentMessages) {
            if (msg.recipient.equals(recipient)) {
                JOptionPane.showMessageDialog(null,msg.getMessageDetails());
                found = true;
            }
        }
        if (!found) {
            JOptionPane.showMessageDialog(null,"No messages found for recipient: " + recipient);
        }
    }

    // e. Delete message by hash
    public static void deleteMessageByHash(String hash) {
        Message toRemove = null;
        for (Message msg : sentMessages) {
            if (msg.messageHash.equals(hash)) {
                toRemove = msg;
                break;
            }
        }

        if (toRemove != null) {
            sentMessages.remove(toRemove);
            messageHashes.remove(toRemove.messageHash);
            messageIDs.remove(toRemove.messageID);
            JOptionPane.showMessageDialog(null,"Message with hash " + hash + " has been deleted.");
        } else {
            JOptionPane.showMessageDialog(null,"No message found with hash: " + hash);
        }
    }

    // f. Display full report of all sent messages
    public static void displayFullReport() {
        if (sentMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null,"No messages sent yet.");
            return;
        }

        System.out.println("Full Report of Sent Messages:");
        for (Message msg : sentMessages) {
            System.out.println("-------------------------------------------------");
            JOptionPane.showMessageDialog(null,msg.getMessageDetails());
        }
    }
}
