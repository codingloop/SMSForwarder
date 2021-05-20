package in.codingloop.sms;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import in.codingloop.sms.adapters.BlockedSenderAdapter;
import in.codingloop.sms.adapters.ContactsAdapter;
import in.codingloop.sms.dialogs.BlockSender;
import in.codingloop.sms.dialogs.CreateContact;
import in.codingloop.sms.objects.BlockedSenders;
import in.codingloop.sms.objects.Contacts;

public class MainActivity extends AppCompatActivity implements ActionInterface{

    // Object references
    SharedPrefs prefs;
    DatabaseManager dbManager;

    // Lists and adapters
    ContactsAdapter contactsAdapter;
    BlockedSenderAdapter blockedSenderAdapter;
    List<Contacts> contactsList = new ArrayList<>();
    List<BlockedSenders> blockedSendersList = new ArrayList<>();

    // Views
    LinearLayout ll1;
    RadioButton rb1, rb2, rb3;
    RadioGroup rbg;
    TextView tv_title;
    ViewPager vp_tabs;
    TabLayout tabs;

    // View states
    boolean headerActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDatabase();
        assignViews();
        initTabAdapter();
    }

    private void initSharedPreference() {
        prefs = new SharedPrefs(getApplicationContext());
    }

    private void initDatabase() {
        dbManager = new DatabaseManager(MainActivity.this);
    }

    private void assignViews() {
        ll1 = findViewById(R.id.ll1_header);
        rb1 = findViewById(R.id.rb_whatsapp);
        rb2 = findViewById(R.id.rb_sms);
        rb3 = findViewById(R.id.rb_both);
        rbg = findViewById(R.id.rbg_fwd_type);
        tv_title = findViewById(R.id.tv_header_msg);
        vp_tabs = findViewById(R.id.vp_tablayout);
        tabs = findViewById(R.id.tabs);
    }

    private void initTabAdapter() {
        contactsAdapter = new ContactsAdapter(MainActivity.this, contactsList);
        blockedSenderAdapter = new BlockedSenderAdapter(MainActivity.this, blockedSendersList);

        List<RowsFragment> tabList = new ArrayList<>();
        RowsFragment rf = new RowsFragment(contactsAdapter);
        RowsFragment rf2 = new RowsFragment(blockedSenderAdapter);
        tabList.add(rf);
        tabList.add(rf2);

        TabAdapter tabAdapter = new TabAdapter(
                getSupportFragmentManager(),
                getApplicationContext(),
                tabList
        );
        vp_tabs.setAdapter(tabAdapter);
        tabs.setupWithViewPager(vp_tabs);
        refreshContactList();
        refreshBlockedSenderList();
    }

    private void refreshContactList() {
        Cursor contacts = dbManager.getAllContacts();
        contactsList.clear();
        while (contacts.moveToNext()) {
            Contacts cnt = new Contacts(
                    contacts.getInt(0),
                    contacts.getString(1),
                    contacts.getString(2)
            );
            contactsList.add(cnt);
        }

        contactsAdapter.notifyDataSetChanged();
    }

    private void refreshBlockedSenderList() {
        Cursor blocked_senders = dbManager.getAllBlockedSenders();
        blockedSendersList.clear();
        while (blocked_senders.moveToNext()) {
            BlockedSenders bs = new BlockedSenders(
                    blocked_senders.getInt(0),
                    blocked_senders.getString(1),
                    blocked_senders.getInt(2)
            );
            blockedSendersList.add(bs);
        }

        blockedSenderAdapter.notifyDataSetChanged();
    }

    private void setupHeader() {
        String forwardType = prefs.getSMSForwardType();
        if (forwardType.equals(getResources().getString(R.string.fwd_type_whatsapp)))
            rb1.setChecked(true);
        else if (forwardType.equals(getResources().getString(R.string.fwd_type_sms)))
            rb2.setChecked(true);
        else
            rb3.setChecked(true);
        rb1.setEnabled(false);
        rb2.setEnabled(false);
        rb3.setEnabled(false);
        headerActive = false;
    }

    private void makeHeaderEditable() {
        rb1.setEnabled(true);
        rb2.setEnabled(true);
        rb3.setEnabled(true);
        headerActive = true;
        tv_title.setText(getResources().getString(R.string.tv_long_press_s2));
    }

    private void saveCurrentHeader() {
        RadioButton rBtn = findViewById(rbg.getCheckedRadioButtonId());
        prefs.setSMSForwardType(rBtn.getText().toString());
        rb1.setEnabled(false);
        rb2.setEnabled(false);
        rb3.setEnabled(false);
        headerActive = false;
        tv_title.setText(getResources().getString(R.string.tv_long_press_s1));
    }

    private void setUpListeners() {
        ll1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (headerActive) {
                    saveCurrentHeader();
                } else {
                    makeHeaderEditable();
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id = item.getItemId();
        if (item_id == R.id.m1_add_contact) {
            new CreateContact(
                    MainActivity.this,
                    (ActionInterface) MainActivity.this
            );
            return true;
        } else if (item_id == R.id.m2_blocked_user) {
            new BlockSender(
                    MainActivity.this,
                    (ActionInterface) MainActivity.this
            );
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void createNewContact(String extension, String contact) {
        long res = dbManager.insertContact(extension, contact);
        if (res == -1) {
            Toast.makeText(getApplicationContext(), "Failed to add entry", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(getApplicationContext(), "Successfully added the entry",
                    Toast.LENGTH_SHORT).show();
        }
        refreshContactList();
    }

    @Override
    public void createNewBlockedSender(String sender, int block_type) {
        long res = dbManager.insertBlockedSender(sender, block_type);
        if (res == -1) {
            Toast.makeText(getApplicationContext(), "Failed to add entry", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(getApplicationContext(), "Successfully added the entry",
                    Toast.LENGTH_SHORT).show();
        }
        refreshBlockedSenderList();
    }

    @Override
    public void deleteBlockedSender(int id) {
        dbManager.deleteBlockedSender(id);
        refreshBlockedSenderList();
    }

    @Override
    public void deleteContact(int id) {
        dbManager.deleteContact(id);
        refreshContactList();
    }

}