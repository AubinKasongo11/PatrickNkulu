import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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

    // ghuy

    @Test
    void testMessageHashFormat() {
        Message message = new Message("+27718693002", "Hi Keegan, did you receive the payment.");
        String hash = message.createMessageHash();
        assertTrue(hash.matches("^\\d{2};\\d+:[A-Z]{2,}$"), "Hash format is invalid: " + hash);
    }

    @Test
    void testMessageIDlength() {
        Message message = new Message("+27718693002", "Hey");
        assertTrue(message.checkMessageID());
    }

    @Test
    void testSendNessageOptions() {
        Message message = new Message("+27718693002", "testing message");
        assertEquals("Message successfully sent.", message.MessageOptions(1));
        assertEquals("Press 0 to delete message.", message.MessageOptions(2));
        assertEquals("Message successfully stored.", message.MessageOptions(3));
        assertEquals("Invalid option entered.", message.MessageOptions(99));
    }



}