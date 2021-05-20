package in.codingloop.sms.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.codingloop.sms.ActionInterface;
import in.codingloop.sms.R;
import in.codingloop.sms.objects.Contacts;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {
    List<Contacts> contactsList;
    ActionInterface ai;

    public ContactsAdapter(ActionInterface ai, List<Contacts> cl) {
        this.contactsList = cl;
        this.ai = ai;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.contacts_row_display, parent, false
        );
        return new ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contacts contact = contactsList.get(position);
        holder.contact.setText(contact.getFullContact());
        holder.delete_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ai.deleteContact(contact.getId());
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView contact;
        Button delete_button;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            contact = itemView.findViewById(R.id.crd_tv_contact);
            delete_button = itemView.findViewById(R.id.crd_b_delete);
        }
    }
}
