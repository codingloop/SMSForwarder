package in.codingloop.sms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static in.codingloop.sms.Constants.BU_ID;
import static in.codingloop.sms.Constants.BU_NAME;
import static in.codingloop.sms.Constants.BU_RELATION;
import static in.codingloop.sms.Constants.CT_EXTENSION;
import static in.codingloop.sms.Constants.CT_ID;
import static in.codingloop.sms.Constants.CT_NUMBER;
import static in.codingloop.sms.Constants.T_BLOCKED_USERS;
import static in.codingloop.sms.Constants.T_CONTACTS;

public class DatabaseManager {
    private static DBManager dbRef;

    public DatabaseManager(Context c) {
        dbRef = new DBManager(c);
    }

    private long insertInToTable(String table_name, ContentValues cv) {
        SQLiteDatabase writer = dbRef.getWritableDatabase();
        long res = writer.insert(table_name, null, cv);
        writer.close();
        return res;
    }

    private Cursor selectAllEntries(String table_name) {
        SQLiteDatabase reader = dbRef.getReadableDatabase();
        Cursor result = reader.query(table_name, null, null, null,
                null,null, null);
//        reader.close();
        return result;
    }

    private int deleteEntryByPk(String table_name, String column, int pk) {
        SQLiteDatabase writer = dbRef.getWritableDatabase();
        int res = writer.delete(table_name, column + " = ?;", new String[]{"" + pk});
        writer.close();
        return res;
    }

    public long insertContact(String extension, String contact) {
        ContentValues cv = new ContentValues();
        cv.put(CT_EXTENSION, extension);
        cv.put(CT_NUMBER, contact);
        return insertInToTable(T_CONTACTS, cv);
    }

    public Cursor getAllContacts() {
        return selectAllEntries(T_CONTACTS);
    }

    public int deleteContact(int contact_id) {
        return deleteEntryByPk(T_CONTACTS, CT_ID, contact_id);
    }

    public long insertBlockedSender(String sender_name, int block_type) {
        ContentValues cv = new ContentValues();
        cv.put(BU_NAME, sender_name);
        cv.put(BU_RELATION, block_type);
        return insertInToTable(T_BLOCKED_USERS, cv);
    }

    public Cursor getAllBlockedSenders() {
        return selectAllEntries(T_BLOCKED_USERS);
    }

    public int deleteBlockedSender(int sender_id) {
        return deleteEntryByPk(T_BLOCKED_USERS, BU_ID, sender_id);
    }

    private static class DBManager extends SQLiteOpenHelper {
        private static final String DB_NAME = Constants.DATABASE_NAME;
        private static final int DB_VERSION = Configs.DATABASE_VERSION;

        private static final String CREATE_TABLE1 = "CREATE TABLE " + Constants.T_CONTACTS + " (" +
                Constants.CT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CT_EXTENSION + " VARCHAR(5) " +
                "DEFAULT '" + Constants.DEFAULT_EXTENSION + "' NOT NULL," +
                Constants.CT_NUMBER + " VARCHAR(12) NOT NULL," +
                Constants.CT_MAX_LIMIT + " INTEGER" +
                ");";

        public static final String CREATE_TABLE2 = "CREATE TABLE " + Constants.T_BLOCKED_USERS +
                " (" +
                Constants.BU_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Constants.BU_NAME + " VARCHAR(30) NOT NULL," +
                Constants.BU_RELATION + " INTEGER DEFAULT " +
                Constants.BU_R2_CONTAINS+ " NOT NULL" +
                ");";

        public static final String CREATE_TABLE3 = "CREATE TABLE " + Constants.T_ACTION_HISTORY +
                " (" +
                Constants.AH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Constants.AH_ACTION + " VARCHAR(10) NOT NULL," +
                Constants.AH_ENTITY + " VARCHAR(30) NOT NULL," +
                Constants.AH_INFORMATION + " VARCHAR(250) NOT NULL," +
                Constants.AH_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ");";

        public DBManager(@Nullable Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_TABLE1);
            sqLiteDatabase.execSQL(CREATE_TABLE2);
            sqLiteDatabase.execSQL(CREATE_TABLE3);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }
}
