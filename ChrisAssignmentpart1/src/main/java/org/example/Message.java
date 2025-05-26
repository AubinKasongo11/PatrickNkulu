
import java.util.Random;

import com.google.gson.JsonObject;




public class Message {

    //Method that tracks number of messages semt
    private static int messageSentCount = 0;
    private int numMessages;

    // Attributes field
    private String messageID;
    private String recipient;
    private String messageContent;
    private String messageHash;
    private String generateMessageID;

    // Getter for message content
    public String getMessageContent() {
        return messageContent;
    }

    // Constructor
    public Message(String recipient, String messageContent) {

        if (!recipient.matches("\\+27\\d{9}")) {
            throw new IllegalArgumentException("Invalid recipient number. Must starts 10 digits with international code(+27xxxxxx");

        }

        if (messageContent.length() > 250) {
            throw new IllegalArgumentException("Message exceeds 250 characters.");
        }

        this.recipient = recipient.trim();
        this.messageContent = messageContent;
        this.messageID = generateMessgeID();
        this.numMessages = ++messageSentCount;
        this.messageHash = createMessageHash();
    }

    // Generate a 10 digit message ID
    private String generateMessgeID() {
        Random rand = new Random();
        int id = 1000000000 + rand.nextInt(900000000);
        return String.valueOf(id);
    }

    //Method to check Message ID
    public boolean checkMessageID() {
        return messageID.length() == 10;
    }

    // Method to check recipient number format
    public boolean checkRecipientCell(String number) {
        return number.matches("\\+27\\d{9}");
    }

    // Method to create and return MessageHash
    public String createMessageHash() {
        String[] words = messageContent.trim().split("\\s+");
        String firstWord = words.length > 0 ? words[0].toUpperCase() : "NA";
        String lastWord = words.length > 1 ? words[words.length - 1].toUpperCase() : firstWord;
        String idPrefix = messageID.substring(0, 2);

        return idPrefix + ":" + numMessages + ":" + firstWord + lastWord;
    }

    // Method to ask the user one of the options
    public String MessageOptions(int option) {
        return switch (option) {
            case 1 -> "Message successfully sent.";
            case 2 -> "Press 0 to delete message.";
            case 3 -> "Message successfully stored.";
            default -> "Invalid option entered.";

        };
    }

        // Method to store message in a JSON Object
        public JsonObject storeMessage() {
            JsonObject store = new JsonObject();
            store.addProperty("messageID" , messageID);
            store.addProperty("messageHash" , messageHash);
            store.addProperty("recipient" , recipient);
            store.addProperty("message" , messageContent);

            return store;

        }

        // Print message details
        public String printmessages() {
            return "Message ID: " + messageID +
                    "\nMessage Hash: " + messageHash +
                    "\nRecipient: " + recipient +
                    "\nMessage: " + messageContent;
        }



        // Return number of messages sent
        public static int returnTotalMessages() {
            return messageSentCount;
        }


    }


