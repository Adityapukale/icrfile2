package com.pukale.incomingcall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.widget.Toast;


import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class incomings extends BroadcastReceiver {
    static String msg = "";
    static String  msg1 = "I am Busy,I will call you after some time";
    static String phno="";
    SmsMessage sm;

    private class MyphoneStateListner extends PhoneStateListener {

        public void onCallStateChanged(int state, String incomingcall) {
            if (state == 1) {
                phno = incomingcall;
                msg = "New Phone" + incomingcall;
                SmsManager msg = SmsManager.getDefault();
                msg.sendTextMessage(phno, null, msg1, null, null);
            }

        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        {
            try {
                TelephonyManager tmgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                MyphoneStateListner myphoneStateListner = new MyphoneStateListner();
                tmgr.listen(myphoneStateListner, PhoneStateListener.LISTEN_CALL_STATE);

            } catch (Exception e) {
                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();

            }
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();


            if (msg != null) {
                String path = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
                File f = new File(path + "/pessageBackup.txt");
                if (!f.exists()) {
                    try {
                        f.createNewFile();
                        Toast.makeText(context, "File creation successful", Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        //e.printStackTrace();
                        Toast.makeText(context, "File Creation Failed", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(context, "File already exist", Toast.LENGTH_SHORT).show();
                if (f != null)

                {

                    DataOutputStream dos = null;
                    try {

                        dos = new DataOutputStream(new FileOutputStream(f, true));

                    } catch (Exception e) {
                        Toast.makeText(context, "Stream Connection Failed", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (dos != null) {
                        try {
                            dos.writeUTF("mob no:-"+phno);
                            Toast.makeText(context, "Contents written", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                dos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }


                }

            }
        }
    }
}