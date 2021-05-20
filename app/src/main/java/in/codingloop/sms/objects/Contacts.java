package in.codingloop.sms.objects;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class Contacts {

    private String extension;
    private String contact;
    private int id;

    public Contacts(int id, String extension, String contact) {
        this.extension = extension;
        this.contact = contact;
        this.id = id;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullContact() {
        return this.getExtension() + " " + this.getContact();
    }

    public String getContactForSend() {
        return extension + contact;
    }

    public static List<Contacts> getContactsFromCursor(Cursor cursor) {
        List<Contacts> contactsList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Contacts cnt = new Contacts(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2)
            );
            contactsList.add(cnt);
        }

        return contactsList;
    }
}
