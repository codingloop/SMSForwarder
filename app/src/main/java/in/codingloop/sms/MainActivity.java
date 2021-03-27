package in.codingloop.sms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    // Object references
    SharedPrefs prefs;

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
        initSharedPreference();
        assignViews();
        setupHeader();
        setUpListeners();
        initTabAdapter();
    }

    private void initSharedPreference() {
        prefs = new SharedPrefs(getApplicationContext());
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
        String[] tabList = {
                getResources().getString(R.string.shared_pref_contacts),
                getResources().getString(R.string.shared_pref_words_to_ignore),
        };
        TabAdapter tabAdapter = new TabAdapter(
                getSupportFragmentManager(),
                getApplicationContext(),
                tabList
        );
        vp_tabs.setAdapter(tabAdapter);
        tabs.setupWithViewPager(vp_tabs);
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
}