package com.amitvikram.smartstudyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {
    private TextView txtTopicName, txtTopicDetails;
    private Button btnSpak, btnView;
    private TextToSpeech t1;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        txtTopicName = findViewById(R.id.txtTopicName);
        txtTopicDetails = findViewById(R.id.txtdesc);
        btnSpak = findViewById(R.id.btnSpeak);
        btnView = findViewById(R.id.btnView);
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
        Intent intent = getIntent();
        String topicName = intent.getStringExtra("topic_name");
        final String topicDetails = intent.getStringExtra("topic_details");
        String modelName = intent.getStringExtra("model_name");
        String modelLink = intent.getStringExtra("model_link");
        txtTopicName.setText(topicName);
        txtTopicDetails.setText(topicDetails);
        btnSpak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                t1.speak(topicDetails, TextToSpeech.QUEUE_FLUSH, savedInstanceState,null);
            }
        });
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, ViewActivity.class);
                intent.putExtra("model_name", modelName);
                intent.putExtra("model_link", modelLink);
                startActivity(intent);
            }
        });
    }

    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }
}