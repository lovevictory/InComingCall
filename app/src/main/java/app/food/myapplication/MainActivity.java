package app.food.myapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.SpeechRecognizer;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ncorti.slidetoact.SlideToActView;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_READ_PHONE_STATE = 101;
    private SpeechRecognizer mSpeechRecognizer;
    private Intent mSpeechRecognizerIntent;
    private boolean mIslistening;
    private static final String TAG = "MainActivity";
    TextView txtSpeechText;
    public static final int MULTIPLE_PERMISSIONS = 10;
    public static final int REQUEST_PERMISSION = 10;
    CircleImageView btnEndCall;
    String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ANSWER_PHONE_CALLS};
    TelecomManager tm;
    SlideToActView btnAcceptCall;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialization();

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initialization() {
        btnAcceptCall = (SlideToActView) findViewById(R.id.example_gray_on_green);
        btnEndCall = findViewById(R.id.btnEndCall);
        call_permissions();
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M || Build.VERSION.SDK_INT == Build.VERSION_CODES.N || Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
            if (Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners").contains(getApplicationContext().getPackageName())) {

            } else {
                Intent i = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        }
        answerCall();
        endCall();
    }

    private void endCall() {
        btnEndCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    btnAcceptCall.setVisibility(View.VISIBLE);
                    btnEndCall.setVisibility(View.GONE);

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void answerCall() {
        tm = (TelecomManager) MainActivity.this
                .getSystemService(Context.TELECOM_SERVICE);

        if (tm == null) {
            // whether you want to handle this is up to you really
            Log.e(TAG, "Tm nullll ");
            throw new NullPointerException("tm == null");

        }

        btnAcceptCall.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSlideComplete(@NotNull SlideToActView slideToActView) {
                btnAcceptCall.setVisibility(View.GONE);
                btnEndCall.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M || Build.VERSION.SDK_INT == Build.VERSION_CODES.N || Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
                    try {
                        for (MediaController mediaController : ((MediaSessionManager) getApplicationContext().getSystemService("media_session")).getActiveSessions(new ComponentName(getApplicationContext(), NotificationCall.class))) {
                            if ("com.android.server.telecom".equals(mediaController.getPackageName())) {
                                mediaController.dispatchMediaButtonEvent(new KeyEvent(1, 79));
                                return;
                            }
                        }
                    } catch (SecurityException e2) {
                        e2.printStackTrace();
                    }
                }
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O || Build.VERSION.SDK_INT == Build.VERSION_CODES.O_MR1) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED) {
                        Log.e(TAG, "Permission problem");
                        return;
                    }
                    tm.acceptRingingCall();
                }
            }
        });

    }

    private void call_permissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getApplicationContext(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
        }
        return;
    }


}
