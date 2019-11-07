package org.tensorflow.lite.examples.detection.tflite;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.tensorflow.lite.examples.detection.CameraActivity;
import org.tensorflow.lite.examples.detection.DetectorActivity;
import org.tensorflow.lite.examples.detection.R;

import java.util.ArrayList;



public class MicActivity extends AppCompatActivity {
    // if debug is 1 => enable debug mode
    // if debug is 0 => off debug mode
    private int debug = 0;

    public MicActivity() {

    }

    private static String wantToSearchedLabel = "";

    // getter of wantToSearchedLabel
    public String getWantToSearchedLabel() {
        return wantToSearchedLabel;
    }

    // setter of wantToSearchedLabel
    public void setWantToSearchedLabel(String wantToSearchedLabel) {
        this.wantToSearchedLabel = wantToSearchedLabel;
    }

    final int PERMISSION = 1;


    SpeechRecognizer mRecognizer;

    //onCreate()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get rid of status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mic);

        if ( Build.VERSION.SDK_INT >= 23 ){
            // 퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO},PERMISSION);
        }


        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");






        // get btn in this context
        ImageView micButton = findViewById(R.id.micButton);


        micButton.setOnClickListener(v -> {
            mRecognizer=SpeechRecognizer.createSpeechRecognizer(this);
            mRecognizer.setRecognitionListener(listener);
            mRecognizer.startListening(i);

            if(debug == 1) {
                Log.d("myapp", "hello: " + getWantToSearchedLabel());
            }

        });


    } // end of onCreate()


    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            // 사용자가 말하기 시작할 준비가되면 호출됩니다.
            Toast.makeText(getApplicationContext(),"음성인식을 시작합니다.",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBeginningOfSpeech() {
            // 사용자가 말하기 시작했을 때 호출됩니다.
        }

        @Override
        public void onRmsChanged(float rmsdB) {
            // 입력받는 소리의 크기를 알려줍니다.
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            // 사용자가 말을 시작하고 인식이 된 단어를 buffer에 담습니다.
        }

        @Override
        public void onEndOfSpeech() {
            // 사용자가 말하기를 중지하면 호출됩니다.
        }

        @Override
        public void onError(int error) {
            // 네트워크 또는 인식 오류가 발생했을 때 호출됩니다.
            String message;

            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "오디오 에러";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "클라이언트 에러";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "퍼미션 없음";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "네트워크 에러";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "네트웍 타임아웃";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "찾을 수 없음";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RECOGNIZER가 바쁨";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "서버가 이상함";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "말하는 시간초과";
                    break;
                default:
                    message = "알 수 없는 오류임";
                    break;
            }

            Toast.makeText(getApplicationContext(), "에러가 발생하였습니다. : " + message,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle results) {
            // 인식 결과가 준비되면 호출됩니다.
            // 아래 코드는 음성인식된 결과를 ArrayList로 모아옵니다.
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            setWantToSearchedLabel(matches.get(0));

            if(debug == 1) {
                Log.d("myapp", getWantToSearchedLabel());
            }

            // wantToSearchedLabel is in trained set
            if(getWantToSearchedLabel().matches("코카콜라") == true) {
                Log.d("myapp", "+코카콜라+");

                //go to detector activity
                Intent intent = new Intent(getApplicationContext(), DetectorActivity.class);
                startActivity(intent);
            } else {
//               다시 말해 주세요.(음성으로 나와야 함)

            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            // 부분 인식 결과를 사용할 수 있을 때 호출됩니다.
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            // 향후 이벤트를 추가하기 위해 예약됩니다.
        }
    };



}
