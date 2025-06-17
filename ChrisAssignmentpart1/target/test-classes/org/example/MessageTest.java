import org.example.Message;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class MessageTest {

    @Test
    void testValidRecipientNumber() {
        Message message = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight");
        assertTrue(message.checkRecipientCell("+27718693002"));
    }

    @Test
    void testInvalidRecipientNumber() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Message("08575975889", "Test");
        });
    }

    @Test
    void testMessageBelow250char() {
        String ValidNumber = "+27718693002";
        String shortMessage = "Hey, this is below 250 characters!";
        Message message = new Message(ValidNumber, shortMessage);
        assertEquals(shortMessage, message.getMessageContent());
    }

    @Test
    void testMessageOver250char() {
        String longMessage = "A".repeat(251);
        String ValidNumber = "+27625995064";
        assertThrows(IllegalArgumentException.class, () -> {
            new Message(ValidNumber,longMessage);
        });
    }

    @Test
    void testMessageHashFormat() {
        Message message = new Message("+27718693002", "Hi Keegan, did you receive the payment.");
        String hash = message.createMessageHash();
        assertNotNull(hash);
        assertTrue(hash.length() > 0);
    }

    @Test
    void testMessageIDlength() {
        Message message = new Message("+27718693002", "Hey");
        assertTrue( message.checkMessageID());
    }

    @Test
    void testSendMessageOptions() {
        Message message = new Message("+27718693002", "testing message");
        assertEquals("Message successfully sent.", message.getMessageOptions(1));
        assertEquals("Press 0 to delete message.", message.getMessageOptions(2));
        assertEquals("Message successfully stored.", message.getMessageOptions(3));
        assertEquals("Invalid option entered.", message.getMessageOptions(99));
    }


    @Test
    void testSentMessagesArrayCorrectlyPopulated() {
        Message.sentMessages.clear();
        new Message("+27834557896", "Did you get the cake?");
        new Message("0838884567", "It is dinner time !");

        assertEquals(2, Message.sentMessages.size());
        assertEquals("Did you get the cake?", Message.sentMessages.get(0).getMessageContent());
        assertEquals("It is dinner time !", Message.sentMessages.get(1).getMessageContent());
    }

    @Test
    void testLongestMessage() {
        Message.sentMessages.clear();
        new Message("+27834557896", "Did you get the cake?");
        new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.");
        new Message("+27834484567", "Yohoooo, I am at your gate.");
        new Message("0838884567", "It is dinner time !");

        Message.displayLongestMessage();
        Message longest = Message.sentMessages.stream()
                .max((m1, m2) -> Integer.compare(m1.getMessageContent().length(), m2.getMessageContent().length()))
                .orElse(null);

        assertNotNull(longest);
        assertEquals("Where are you? You are late! I have asked you to be on time.", longest.getMessageContent());
    }

    @Test
    void testSearchByMessageID() {
        Message.sentMessages.clear();
        Message msg = new Message("0838884567", "It is dinner time !");
        String id = msg.getMessageID();

        Message.searchByMessageID(id);
        assertEquals("It is dinner time !", msg.getMessageContent());
    }

    @Test
    void testSearchMessagesByRecipient() {
        Message.sentMessages.clear();
        new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.");
        new Message("+27838884567", "Ok, I am leaving without you.");

        List<Message> matches = Message.sentMessages.stream()
                .filter(m -> m.getRecipient().equals("+27838884567"))
                .toList();

        assertEquals(2, matches.size());
        assertEquals("Where are you? You are late! I have asked you to be on time.", matches.get(0).getMessageContent());
        assertEquals("Ok, I am leaving without you.", matches.get(1).getMessageContent());
    }

    @Test
    void testDeleteMessageByHash() {
        Message.sentMessages.clear();
        Message m = new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.");
        String hash = m.getMessageHash();

        Message.deleteMessageByHash(hash);

        boolean stillExists = Message.sentMessages.stream()
                .anyMatch(msg -> msg.getMessageHash().equals(hash));

        assertFalse(stillExists);
    }

    @Test
    void testDisplayFullReport() {
        Message.sentMessages.clear();
        new Message("+27834557896", "Did you get the cake?");
        new Message("0838884567", "It is dinner time !");

        assertEquals(2, Message.sentMessages.size());
        for (Message msg : Message.sentMessages) {
            assertNotNull(msg.getMessageHash());
            assertNotNull(msg.getRecipient());
            assertNotNull(msg.getMessageContent());
        }
    }
}
