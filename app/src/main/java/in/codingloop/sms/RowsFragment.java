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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.codingloop.sms.adapters.BlockedSenderAdapter;
import in.codingloop.sms.adapters.ContactsAdapter;

public class RowsFragment extends Fragment {
    ContactsAdapter contactsAdapter;
    BlockedSenderAdapter bsAdapter;
    View v = null;

    public RowsFragment(ContactsAdapter contactsAdapter) {
        this.contactsAdapter = contactsAdapter;
    }

    public RowsFragment(BlockedSenderAdapter bsAdapter) {
        this.bsAdapter = bsAdapter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_view, container, false);
        RecyclerView rv = v.findViewById(R.id.fragment_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
        if (contactsAdapter != null)
            rv.setAdapter(contactsAdapter);
        else
            rv.setAdapter(bsAdapter);
        return v;
    }
}
