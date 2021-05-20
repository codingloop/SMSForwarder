package in.codingloop.sms.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public abstract  class BaseDialog {
    Context context;
    AlertDialog alertDialog;

    protected BaseDialog(@NonNull Context context) {
        this.context = context;
        this.setupDialog();
        this.alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onPositiveButton();
                    }
                });
        this.alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onNegativeButton();
                    }
                });
    }

    abstract void setupDialog();
    abstract void onPositiveButton();
    abstract void onNegativeButton();
}
