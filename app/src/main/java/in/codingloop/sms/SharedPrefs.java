package in.codingloop.sms;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class SharedPrefs {
    SharedPreferences preferences;
    Context c;

    SharedPrefs(Context c) {
        this.c = c;
        preferences = c.getSharedPreferences(
                c.getResources().getString(R.string.shared_pref_name),
                Context.MODE_PRIVATE)
        ;
    }

    public String getSMSForwardType() {
        return preferences.getString(
                c.getResources().getString(R.string.shared_pref_fwd_type),
                c.getResources().getString(R.string.fwd_type_sms)
        );
    }

    public void setSMSForwardType(String fwdType) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(
                c.getResources().getString(R.string.shared_pref_fwd_type),
                fwdType
        );
        editor.apply();
    }

    public String getSavedEntries(String actionFor) {
        return preferences.getString(actionFor, "");
    }

    public Set<String> getSavedEntriesSet(String actionFor) {
        String[] entries = getSavedEntries(actionFor).split("\n");
        Set<String> set = new HashSet<>();
        for (String s: entries) {
            if (s.trim().length() != 0) set.add(s.trim());
        }
        return set;
    }

    public void deleteEntry(String valueToDelete, String actionFor) {
        String[] savedEntries = getSavedEntries(actionFor).split("\n");
        StringBuilder newString = new StringBuilder();
        for (String savedEntry : savedEntries) {
            if (savedEntry.trim().equals("")) continue;
            if (valueToDelete.trim().equals(savedEntry.trim())) {
                continue;
            }
            newString.append(savedEntry.trim()).append("\n");
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(actionFor, newString.toString());
        editor.apply();
    }

    public void addEntry(String valueToAdd, String actionFor) {
        if (valueToAdd.length() < 1)
            return;
        StringBuilder newValue = new StringBuilder();
        String[] savedEntries = getSavedEntries(actionFor).split("\n");
        for(String s: savedEntries) {
            if (s.trim().equals("")) continue;
            newValue.append(s.trim()).append("\n");
        }
        newValue.append(valueToAdd.trim()).append("\n");
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(actionFor, newValue.toString());
        editor.apply();
    }
}
