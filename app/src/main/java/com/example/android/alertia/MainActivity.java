package com.example.android.alertia;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements RecognitionListener {

    private TextToSpeech tts;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "MainActivity/Recognition";

    private TextView regocText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        regocText = findViewById(R.id.voicetext);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.getDefault());
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Lang is not supported");
                    }
                    speak("Hello!!");
                } else {
                    Log.e("TTS", "Init Failed");
                }
            }
        });

        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }

        if (speech!=null){
            speech.destroy();
        }
        super.onDestroy();
    }


    private void speak(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public void voice(View view) {
        speak("Are you feeling sleepy?");
        while(tts.isSpeaking()){
            try{
                TimeUnit.SECONDS.sleep(1);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        speech.startListening(recognizerIntent);
    }

    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.d(LOG_TAG, "FAILED " + errorMessage);
        regocText.setText(errorMessage);
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";
        for (String result : matches) {
            text += result + "\n";
        }
        regocText.setText(text);
        if(text.toUpperCase().contains("YES")){

        } else {
          //TODO What happens if not feeling well
        }

    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }

//        buttonStartRecord = findViewById(R.id.startRecord);
//        buttonStopRecord = findViewById(R.id.stopRecord);
//        buttonPlayRecording = findViewById(R.id.playRecord);
//        buttonStopPlay = findViewById(R.id.stopPlayRecord);
//
//        buttonStopRecord.setEnabled(false);
//        buttonPlayRecording.setEnabled(false);
//        buttonStopPlay.setEnabled(false);
//
//        buttonStartRecord.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (checkPermission()) {
//                    AudioSavePathInDevice = Environment.getExternalStorageDirectory().getAbsolutePath() +
//                            "/" + "Recording.3gp";
//
//                    MediaRecorderReady();
//
//                    try {
//                        myRecorder.prepare();
//                        myRecorder.start();
//                    } catch (IllegalStateException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    buttonStartRecord.setEnabled(false);
//                    buttonStopRecord.setEnabled(true);
//
//                    Toast.makeText(MainActivity.this, "Recording started",
//                            Toast.LENGTH_LONG).show();
//                } else {
//                    requestPermission();
//                }
//
//            }
//        });
//
//        buttonStopRecord.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                myRecorder.stop();
//                buttonStopRecord.setEnabled(false);
//                buttonPlayRecording.setEnabled(true);
//                buttonStartRecord.setEnabled(true);
//                buttonStopRecord.setEnabled(false);
//
//                Toast.makeText(MainActivity.this, "Recording Completed",
//                        Toast.LENGTH_LONG).show();
//            }
//        });
//
//        buttonPlayRecording.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) throws IllegalArgumentException,
//                    SecurityException, IllegalStateException {
//
//                buttonStopRecord.setEnabled(false);
//                buttonStartRecord.setEnabled(false);
//                buttonStopPlay.setEnabled(true);
//
//                myMediaPlayer = new MediaPlayer();
//                try {
//                    myMediaPlayer.setDataSource(AudioSavePathInDevice);
//                    myMediaPlayer.prepare();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                myMediaPlayer.start();
//                Toast.makeText(MainActivity.this, "Recording Playing",
//                        Toast.LENGTH_LONG).show();
//            }
//        });
//
//        buttonStopPlay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                buttonStopPlay.setEnabled(false);
//                buttonStartRecord.setEnabled(true);
//                buttonStopRecord.setEnabled(false);
//                buttonPlayRecording.setEnabled(true);
//
//                if(myMediaPlayer != null){
//                    myMediaPlayer.stop();
//                    myMediaPlayer.release();
//                    MediaRecorderReady();
//                }
//            }
//        });
//
//
//    }
//
//    public void MediaRecorderReady() {
//        myRecorder = new MediaRecorder();
//        myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
//        myRecorder.setOutputFile(AudioSavePathInDevice);
//    }
//
//    public boolean checkPermission() {
//        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
//                WRITE_EXTERNAL_STORAGE);
//        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
//                RECORD_AUDIO);
//        return result == PackageManager.PERMISSION_GRANTED &&
//                result1 == PackageManager.PERMISSION_GRANTED;
//    }
//
//    private void requestPermission() {
//        ActivityCompat.requestPermissions(MainActivity.this, new
//                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case RequestPermissionCode:
//                if (grantResults.length> 0) {
//                    boolean StoragePermission = grantResults[0] ==
//                            PackageManager.PERMISSION_GRANTED;
//                    boolean RecordPermission = grantResults[1] ==
//                            PackageManager.PERMISSION_GRANTED;
//
//                    if (StoragePermission && RecordPermission) {
//                        Toast.makeText(MainActivity.this, "Permission Granted",
//                                Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(MainActivity.this,"Permission Denied",
//                                Toast.LENGTH_LONG).show();
//                    }
//                }
//                break;
//        }
//    }
}
