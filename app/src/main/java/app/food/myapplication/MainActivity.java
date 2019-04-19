package app.food.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import net.gotev.speech.GoogleVoiceTypingDisabledException;
import net.gotev.speech.Logger;
import net.gotev.speech.Speech;
import net.gotev.speech.SpeechDelegate;
import net.gotev.speech.SpeechRecognitionNotAvailable;
import net.gotev.speech.TextToSpeechCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    String newString = "testing";
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Bundle extras = getIntent().getExtras();
            newString = extras.getString("number");

        } catch (Exception e) {

        }
        Log.e(TAG, "Testingggggg-> " );
        Toast.makeText(MainActivity.this, "" + newString, Toast.LENGTH_SHORT).show();
        Speech.init(this, getPackageName());
        Logger.setLogLevel(Logger.LogLevel.DEBUG);
        Speech.getInstance().say("say something");
        Logger.setLoggerDelegate(new Logger.LoggerDelegate() {
            @Override
            public void error(String tag, String message) {
                //your own implementation here
            }

            @Override
            public void error(String tag, String message, Throwable exception) {
                //your own implementation here
            }

            @Override
            public void debug(String tag, String message) {
                //your own implementation here
            }

            @Override
            public void info(String tag, String message) {
                //your own implementation here
            }
        });


        try {
            // you must have android.permission.RECORD_AUDIO granted at this point
            Speech.getInstance().startListening(new SpeechDelegate() {
                @Override
                public void onStartOfSpeech() {
                    Log.i("speech", "speech recognition is now active");
                }

                @Override
                public void onSpeechRmsChanged(float value) {
                    Log.d("speech", "rms is now: " + value);
                }


                @Override
                public void onSpeechPartialResults(List<String> results) {
                    StringBuilder str = new StringBuilder();
                    for (String res : results) {
                        str.append(res).append(" ");
                    }

                    Log.i("speech", "partial result: " + str.toString().trim());
                }

                @Override
                public void onSpeechResult(String result) {
                    Log.i("speech", "result: " + result);
                }
            });
        } catch (SpeechRecognitionNotAvailable exc) {
            Log.e("speech", "Speech recognition is not available on this device!");
            // You can prompt the user if he wants to install Google App to have
            // speech recognition, and then you can simply call:
            //
            // SpeechUtil.redirectUserToGoogleAppOnPlayStore(this);
            //
            // to redirect the user to the Google App page on Play Store
        } catch (GoogleVoiceTypingDisabledException exc) {
            Log.e("speech", "Google voice typing must be enabled!");
        }

        Speech.getInstance().say("say something", new TextToSpeechCallback() {
            @Override
            public void onStart() {
                Log.i("speech", "speech started");
            }

            @Override
            public void onCompleted() {
                Log.i("speech", "speech completed");
            }

            @Override
            public void onError() {
                Log.i("speech", "speech error");
            }
        });
    }

    @Override
    protected void onDestroy() {
        // prevent memory leaks when activity is destroyed
        super.onDestroy();
        Speech.getInstance().shutdown();
    }
}
