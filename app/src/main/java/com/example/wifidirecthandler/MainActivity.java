package com.example.wifidirecthandler;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String profile;
    public TextView text;

    private WifiDirectHandler handler;

    private Button button;
    private Button button2;
    private Button button3;

    private EditText profileText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profile = createProfile();

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.searchForPhonesOnce();
                text.setText(handler.peersToString());
                //handler.services();
            }
        });

        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.setInfoString(createProfile());
                handler.startRegistration();
            }
        });
        button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.services();
            }
        });

        text = (TextView) findViewById(R.id.text);
        Context context = this;
        WifiP2pManager manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        handler = new WifiDirectHandler(context, manager, profile, this);

    }

    private String createProfile() {
        String out = "";
        profileText = (EditText) findViewById(R.id.profileText);
        //TODO add all information from user profile
        //place holder
        out += profileText.getText();
        System.out.println("PROFILE" + out);
        return out;
    }

    @Override
    protected void onDestroy(){
        handler.handlerUnRegisterReciever();
        super.onDestroy();
    }
}
