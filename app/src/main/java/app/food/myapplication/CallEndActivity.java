package app.food.myapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;

import java.lang.reflect.Method;

import de.hdodenhof.circleimageview.CircleImageView;

public class CallEndActivity extends AppCompatActivity {
    CircleImageView btnEndCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_end);
        initialization();
    }

    private void initialization() {
        btnEndCall = findViewById(R.id.btnEndCall);
        endCall();
    }

    private void endCall() {
        btnEndCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    TelephonyManager tm = (TelephonyManager)
                            getSystemService(Context.TELEPHONY_SERVICE);
                    try {
                        Class c = Class.forName(tm.getClass().getName());
                        Method m = c.getDeclaredMethod("getITelephony");
                        m.setAccessible(true);
                        Object telephonyService = m.invoke(tm); // Get the internal ITelephony object
                        c = Class.forName(telephonyService.getClass().getName()); // Get its class
                        m = c.getDeclaredMethod("endCall"); // Get the "endCall()" method
                        m.setAccessible(true); // Make it accessible
                        m.invoke(telephonyService); // invoke endCall()

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {

                }
            }

        });
    }
}
