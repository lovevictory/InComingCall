package app.food.myapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

public class CallReceiver extends PhonecallReceiver {
    private static final String TAG = "CallReceiver";
    @Override
    protected void onIncomingCallStarted(Context ctx, String number, Date start) {
        Toast.makeText(ctx, ""+number, Toast.LENGTH_LONG).show();
        Intent i = new Intent();
        i.setClassName("app.food.myapplication", "app.food.myapplication.MainActivity");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("number",number);
        ctx.startActivity(i);
        Log.e(TAG, "onIncomingCallStarted: ");
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        Intent i = new Intent();
        i.setClassName("app.food.myapplication", "app.food.myapplication.MainActivity");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("number",number);
        ctx.startActivity(i);
        Log.e(TAG, "onOutgoingCallStarted: ");
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        Intent i = new Intent();
        i.setClassName("app.food.myapplication", "app.food.myapplication.MainActivity");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("number",number);
        ctx.startActivity(i);
        Log.e(TAG, "onIncomingCallEnded: " );
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
        Intent i = new Intent();
        i.setClassName("app.food.myapplication", "app.food.myapplication.MainActivity");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("number",number);
        ctx.startActivity(i);
        Log.e(TAG, "onOutgoingCallEnded: " );
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
        Intent i = new Intent();
        i.setClassName("app.food.myapplication", "app.food.myapplication.MainActivity");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("number",number);
        ctx.startActivity(i);
        Log.e(TAG, "onMissedCall: " );
    }

}