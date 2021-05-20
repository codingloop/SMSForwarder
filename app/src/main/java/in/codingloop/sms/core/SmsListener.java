package in.codingloop.sms.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import in.codingloop.sms.DatabaseManager;
import in.codingloop.sms.R;
import in.codingloop.sms.objects.BlockedSenders;
import in.codingloop.sms.objects.Contacts;

public class SmsListener extends BroadcastReceiver {
    Context c = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Toast.makeText(context, "Message received", Toast.LENGTH_SHORT).show();

            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;

            if (bundle != null){
                DatabaseManager databaseManager = new DatabaseManager(context);

                List<Contacts> contactsList = Contacts.getContactsFromCursor(
                        databaseManager.getAllContacts()
                );
                List<BlockedSenders> bSList = BlockedSenders.getBlockedSendersFromCursor(
                        databaseManager.getAllBlockedSenders()
                );

                //---retrieve the SMS message received---
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        String msg_from = msgs[i].getOriginatingAddress();
                        boolean send = true;
                        if (BlockedSenders.shouldSendSMS(msg_from, bSList)) {
                            // Send SMS
                            String msgBody  = msgs[i].getMessageBody();
                            sendMessage(msg_from, msgBody, contactsList);
                        } else {
                            // Toast sender is blocked
                        }
                    }
                } catch(Exception e) {
//                            Log.d("Exception caught",e.getMessage());
                }
            }
        }
    }

    private void sendMessage(String sender, String msg, List<Contacts> contactsList) {
        String msgBody = "Sender: " + sender + "\nContent:\n" + msg;
        for (Contacts cnt: contactsList) {
            sendTextMessage(cnt.getContactForSend(), msgBody);
        }
    }

    private void sendMessageLogic(String sender, String msg, String sendType, Set<String> contacts) {
        if (this.c == null)
            return;
        String msgBody = "Sender: " + sender + "\nContent:\n" + msg;
        for (String contact: contacts) {
            if (sendType.equals(c.getResources().getString(R.string.fwd_type_sms))) {
                sendTextMessage(contact, msgBody);
            } else if (sendType.equals(c.getResources().getString(R.string.fwd_type_whatsapp))) {
                sendWhatsAppMessage(contact, msgBody);
            } else {
                sendTextMessage(contact, msgBody);
                sendWhatsAppMessage(contact, msgBody);
            }
        }
    }

    private void sendTextMessage(String contact, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> msgArray = smsManager.divideMessage(msg);
            try {

//                    smsManager.sendTextMessage("+91" + contact, null, msg, NULL, null);
                smsManager.sendMultipartTextMessage("+91" + contact, null,msgArray, null, null);
            } catch (Exception e) {
                Toast.makeText(c, e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
        }
    }

    private void sendWhatsAppMessage(String contact, String msg) {
        try{
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setData(Uri.parse(
                    "http://api.whatsapp.com/send?phone="+"+91"+contact + "&text="+msg
            ));
            intent.setPackage("com.whatsapp");
            c.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(c, "" + e, Toast.LENGTH_SHORT).show();
        }
    }
}