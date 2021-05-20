package in.codingloop.sms.dialogs;

import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.textfield.TextInputLayout;

import in.codingloop.sms.ActionInterface;
import in.codingloop.sms.R;

public class CreateContact extends BaseDialog{
    View view;
    ActionInterface ai;

    public CreateContact(Context context, ActionInterface ai) {
        super(context);
        this.ai = ai;
    }

    @Override
    void setupDialog() {
        this.view = LayoutInflater.from(context).inflate(R.layout.dialog_create_contact, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setView(this.view);
        builder.setTitle("Add new contact to forward");
        builder.setPositiveButton("Add", null);
        builder.setNegativeButton("Cancel", null);
        this.alertDialog = builder.show();
    }

    @Override
    void onPositiveButton() {
        TextInputLayout e1 = view.findViewById(R.id.d_create_contact_extension);
        TextInputLayout e2 = view.findViewById(R.id.d_create_contact_no);
        String extension = null;
        String contact_no = null;
        e1.setError("");
        e2.setError("");
        try {
            extension = e1.getEditText().getText().toString().trim();
        } catch (NullPointerException ignored) {
        }
        try {
            contact_no = e2.getEditText().getText().toString().trim();
        } catch (NullPointerException ignored) {
        }

        if (extension == null || extension.equals("")) {
            e1.setError("Extension cannot be blank");
            return;
        }

        if (contact_no == null || contact_no.equals("")) {
            e2.setError("Contact number cannot be blank");
            return;
        }

        ai.createNewContact(extension, contact_no);
        this.alertDialog.dismiss();
    }

    @Override
    void onNegativeButton() {
        this.alertDialog.dismiss();
    }
}
