package in.codingloop.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class SmsListener extends BroadcastReceiver {
    Context c = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Toast.makeText(context, "Message received", Toast.LENGTH_SHORT).show();
            c = context;
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from;
            SharedPrefs sharedPrefs = new SharedPrefs(context);
            String fwdType = sharedPrefs.getSMSForwardType();
            Set<String> contacts= sharedPrefs.getSavedEntriesSet(
                    context.getResources().getString(R.string.shared_pref_contacts)
            );

            Set<String> senders_to_ignore = sharedPrefs.getSavedEntriesSet(
                    context.getResources().getString(R.string.shared_pref_words_to_ignore)
            );

            if (bundle != null){
                //---retrieve the SMS message received---
                try{
                    String msgBody;
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        boolean send = true;
                        for (String ign: senders_to_ignore) {
                            if (msg_from.toLowerCase().contains(ign.toLowerCase())) {
                                send = false;
                                Toast.makeText(context, "Sender is blocked", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                        if (!send)
                            continue;
                        msgBody  = msgs[i].getMessageBody();
                        Toast.makeText(context, "Sending message", Toast.LENGTH_SHORT).show();
                        sendMessage(msg_from, msgBody, fwdType, contacts);
                    }
                }catch(Exception e){
//                            Log.d("Exception caught",e.getMessage());
                }
            }
        }
    }

    private void sendMessage(String sender, String msg, String sendType, Set<String> contacts) {
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

            if(contact.trim().equals(""))
                return;
            try {

//                    smsManager.sendTextMessage("+91" + contact, null, msg, NULL, null);
                smsManager.sendMultipartTextMessage("+91" + contact, null,msgArray, null, null);
            } catch (Exception e) {
                Toast.makeText(c, e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
            Toast.makeText(c, "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(c,ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
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
//    //So you can get the edittext value
//    String mobileNumber = editText_mobile.getText().toString();
//    String message = editText_msg.getText().toString();
//    boolean installed = appInstalledOrNot("com.whatsapp");
//              if (installed){
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+"+91"+mobileNumber + "&text="+message));
//        startActivity(intent);
//    }else {
//        Toast.makeText(MainActivity.this, "Whats app not installed on your device", Toast.LENGTH_SHORT).show();
//    }
//

//    private void sendMessage(String msg, Context c) {
//        try {
//            SmsManager smsManager = SmsManager.getDefault();
//            SharedPreferences pref = c.getSharedPreferences(
//                    c.getResources().getString(R.string.shrd_pref_name),
//                    Context.MODE_PRIVATE
//            );
//            ArrayList<String> msgArray = smsManager.divideMessage(msg);
//            String contacts = pref.getString(c.getResources().getString(R.string.shrd_pref_key), "");
//            String cList[] = contacts.split("\n");
//            for (String contact: cList) {
//                if(contact.trim().equals("")){
//                    continue;
//                }
//                try {
//
////                    smsManager.sendTextMessage("+91" + contact, null, msg, NULL, null);
//                    smsManager.sendMultipartTextMessage("+91" + contact, null,msgArray, null, null);
//                } catch (Exception e) {
//                    Toast.makeText(c, e.getMessage().toString(), Toast.LENGTH_LONG).show();
//                }
//            }
//            Toast.makeText(c, "Message Sent",
//                    Toast.LENGTH_LONG).show();
//        } catch (Exception ex) {
//            Toast.makeText(c,ex.getMessage().toString(),
//                    Toast.LENGTH_LONG).show();
//            ex.printStackTrace();
//        }
//    }
}