package in.codingloop.sms;

public interface ActionInterface {
    void createNewContact(String extension, String contact);
    void createNewBlockedSender(String sender, int block_type);
    void deleteBlockedSender(int id);
    void deleteContact(int id);
}
