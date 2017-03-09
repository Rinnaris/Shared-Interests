package com.example.wifidirecthandler;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

//When there is no current profile, launch set-up, to begin the setting up of the profile?
public class Setup extends AppCompatActivity {

    Button startButton;
    TextView instructionsText;
    TextView welcomeText;
    TextView skipText;
    EditText nameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.CYAN);

        //instructionsText = (TextView) findViewById(R.id.textInstructions);
        //welcomeText = (TextView) findViewById(R.id.textWelcome);
       // skipText = (TextView) findViewById(R.id.textSkip);
        //nameField = (EditText) findViewById(R.id.textName);

        //startButton = (Button) findViewById(R.id.buttonStart);
        //startButton.setOnClickListener(new View.OnClickListener() {

           // @Override
           // public void onClick(View v) {
              //  welcomeText.setVisibility(View.INVISIBLE);
               // nameField.setVisibility(View.VISIBLE);
               // instructionsText.setVisibility(View.VISIBLE);
               // skipText.setVisibility(View.VISIBLE);

               // startButton.setText("OK");
           // }
        //});
    }


}
