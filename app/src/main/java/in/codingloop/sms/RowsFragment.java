package in.codingloop.sms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class RowsFragment extends Fragment {
    String actionFor;
    List<View> listViews = new ArrayList<>();
    View v = null;

    public RowsFragment(String tabType) {
        actionFor = tabType;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_view, container, false);
        refreshView();
        return v;
    }

    private void refreshView() {
        if (v != null) {
            LinearLayout linearLayout = v.findViewById(R.id.ll_fragment);
            linearLayout.removeAllViews();
            String savedEntries = new SharedPrefs(getContext()).getSavedEntries(actionFor);
            initiateListViews(savedEntries.split("\n"));
            for (View v: listViews){
                linearLayout.addView(v);
            }
        }
    }

    private void initiateListViews(String[] savedEntries) {
        listViews.clear();
        for(String i: savedEntries) {
            if(i.trim().equals("")){
                continue;
            }
            listViews.add(getActionRow(i, false));
        }
        listViews.add(getActionRow("", true));
    }

    private String getHint() {
        if (actionFor.equals(getContext().getResources().getString(R.string.shared_pref_contacts)))
            return getContext().getResources().getString(R.string.hint_for_contacts);
        return getContext().getResources().getString(R.string.hint_for_blocked_sender);
    }

    private View getActionRow(final String dsp, boolean addNew) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.action_row, null);
        Button btn = v.findViewById(R.id.cr_b1);
        final EditText et = v.findViewById(R.id.cr_e1);
        if (addNew) {
            btn.setText(R.string.btn_action_add);
            et.setHint(getHint());
            et.setText("");
            btn.setOnClickListener(view -> {
                new SharedPrefs(getContext()).addEntry(
                    et.getText().toString().trim(), actionFor
                );
                refreshFragment();
            });
        } else {
            btn.setText(R.string.btn_action_delete);
            et.setText(dsp);
            et.setEnabled(false);
            btn.setOnClickListener(view -> {
                new SharedPrefs(getContext()).deleteEntry(
                        dsp, actionFor
                );
                refreshFragment();
            });
        }
        return v;
    }

    private void refreshFragment() {
        refreshView();
    }

}
