package com.example.android.alertia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.widget.TextView;
import java.util.List;

public class StartVoiceRecognition extends Activity {
    private final int REQUEST_SPEECH_RECOGNIZER = 3000;
    private TextView mTextView;
    private final String[] mQuestion = {"Do you need any assistance?",
                                        "Are you feeling sleepy?"};
    private String mAnswer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voicerecog);
        mTextView = findViewById(R.id.tvstt);
        startSpeechRecognizer();
    }

    private void startSpeechRecognizer() {
        Intent intent = new Intent
                (RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, mQuestion[1]);
        startActivityForResult(intent, REQUEST_SPEECH_RECOGNIZER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SPEECH_RECOGNIZER) {
            if (resultCode == RESULT_OK) {
                List<String> results = data.getStringArrayListExtra
                        (RecognizerIntent.EXTRA_RESULTS);
                mAnswer = results.get(0);

                if (mAnswer.toUpperCase().contains("YES"))
                    mTextView.setText("Question: " + mQuestion[1] +
                            "\nYour answer is '" + mAnswer);
                else
                    mTextView.setText("Question: " + mQuestion[1] +
                            "\nYour answer is '" + mAnswer);
            }
            else{
                mTextView.setText("Couldn't understand you");
            }
        }
    }
}