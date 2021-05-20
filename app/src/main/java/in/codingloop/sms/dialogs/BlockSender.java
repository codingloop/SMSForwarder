package in.codingloop.sms.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.textfield.TextInputEditText;

import in.codingloop.sms.ActionInterface;
import in.codingloop.sms.R;

import static in.codingloop.sms.Constants.BU_R1_EQUALS;
import static in.codingloop.sms.Constants.BU_R2_CONTAINS;

public class BlockSender extends BaseDialog{
    View view;
    ActionInterface ai;

    public BlockSender(Context context, ActionInterface ai) {
        super(context);
        this.ai = ai;
    }

    @Override
    void setupDialog() {
        this.view = LayoutInflater.from(context).inflate(R.layout.dialog_block_sender, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setView(this.view);
        builder.setTitle("Add sender name to be blocked ");
        builder.setPositiveButton("Add", null);
        builder.setNegativeButton("Cancel", null);
        this.alertDialog = builder.show();
    }

    @Override
    void onPositiveButton() {
        TextInputEditText e1 = view.findViewById(R.id.d_sb_ti_sender_to_block);
        String sender_to_block = e1.getText().toString().trim();
        if (sender_to_block.equals("")) {
            e1.setError("Value cannot be blank");
            return;
        }

        int block_type = BU_R2_CONTAINS;

        RadioGroup rg = view.findViewById(R.id.d_sb_rg_block_type);
        if (rg.getCheckedRadioButtonId() == R.id.d_sb_mrb_exact_match)
            block_type = BU_R1_EQUALS;

        ai.createNewBlockedSender(sender_to_block, block_type);

        this.alertDialog.dismiss();

    }

    @Override
    void onNegativeButton() {
        this.alertDialog.dismiss();
    }
}
