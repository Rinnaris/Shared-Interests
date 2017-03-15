package com.example.wifidirecthandler;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class StartUp extends AppCompatActivity {

    String name = "";
    String phone = "";
    String email = "";

    EditText nameText;
    EditText phoneText;
    EditText emailText;

    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.CYAN);

        nameText = (EditText) findViewById(R.id.name);
        phoneText = (EditText) findViewById(R.id.phone);
        emailText = (EditText) findViewById(R.id.email);

        next = (Button) findViewById(R.id.nextButton);

        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               name = nameText.getText() + "";
               phone = phoneText.getText() + "";
               email = emailText.getText() + "";

               Intent intent = new Intent(StartUp.this, Setup.class);
               Bundle bundle = new Bundle();
               bundle.putString("name", name);
               bundle.putString("phone", phone);
               bundle.putString("email", email);

               intent.putExtras(bundle);

               startActivity(intent);
            }
        });
    }
}
