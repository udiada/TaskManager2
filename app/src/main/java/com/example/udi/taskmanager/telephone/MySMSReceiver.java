package com.example.udi.taskmanager.telephone;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.example.udi.taskmanager.MngTaskMain;
import com.example.udi.taskmanager.R;
import com.example.udi.taskmanager.data.MyTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Calendar;
import java.util.Date;

public class MySMSReceiver extends BroadcastReceiver {
    public MySMSReceiver() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        // this proc oparate on any receive braodcast so we have to use if
        //  Toast.makeText(context,"Hiiiiiiiiiii",Toast.LENGTH_LONG).show();   // for debug purpase
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            Object[] pdus = (Object[]) bundle.get("pdus"); // pdus = const String for getting SMS data
            String smsInfo = "", inPhoneNum = "";

            for (int i = 0; i < pdus.length; i++) {
                SmsMessage smsMsg = SmsMessage.createFromPdu((byte[]) pdus[i]);
                inPhoneNum = smsMsg.getOriginatingAddress();
                smsInfo += smsMsg.getDisplayMessageBody() + ",";
            }

            if (FirebaseDatabase.getInstance() != null) {  // write value only if user loged in
                Date date = Calendar.getInstance().getTime();
                MyTask myTask = new MyTask(smsInfo, false, date, 1, "", inPhoneNum);
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                reference.child(email.replace(".", "_")).child("MyTasks").push().setValue(myTask, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            Toast.makeText(context, "Save successful",
                                    Toast.LENGTH_LONG).show();
                                    makeNotification(context);

                        } else {
                            Toast.makeText(context, "Save Failed" + databaseError.getMessage(),
                                    Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
            if (smsInfo.contains("hello")) // if the text "hello" is at the sms
            {
                //you need to add SEND_SMS uses permition
                SmsManager manager = SmsManager.getDefault();
                manager.sendTextMessage(inPhoneNum, null, "welcome", null, null);
            }

        }
    }
            // add notification msg (at the top of the device
        private void makeNotification(Context context) {
            //1. creat notification
            // chooze v7 at ctrl+scapce
            NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Task Manager")
                    .setContentText("New SMS added to your tasks");
            //2. respons to the notification (what to do if note clicked)
            Intent resIntent=new Intent(context, MngTaskMain.class);
            PendingIntent resPendingIntent=PendingIntent.getActivity(context,0,resIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            //3.
            builder.setContentIntent(resPendingIntent);
            //4.
            NotificationManager notificationManager= (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(22,builder.build());
            }


}
