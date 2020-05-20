package com.example.caustudy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class QuestionActivity extends AppCompatActivity {

    EditText etTitle, etMessage;
    Button send_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_activiity);
        etTitle = (EditText) findViewById(R.id.et_title);
        etMessage = (EditText) findViewById(R.id.et_message);
        send_btn = (Button)findViewById(R.id.bt_send);

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
                finish();
            }
        });
    }

    protected void sendEmail() {
        String subject = etTitle.getText().toString();
        String message = etMessage.getText().toString();

        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        try {
            // Email sender
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ys_7197@naver.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, message);
            emailIntent.setType("text/html");
            emailIntent.setPackage("com.google.android.gm");
            if(emailIntent.resolveActivity(getPackageManager())!=null)
                startActivity(emailIntent);

            startActivity(emailIntent);
        } catch (Exception e) {
            e.printStackTrace();

            emailIntent.setType("text/html");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ys_7197@naver.com"});
            startActivity(Intent.createChooser(emailIntent, "Send Email"));
        }
    }
}
