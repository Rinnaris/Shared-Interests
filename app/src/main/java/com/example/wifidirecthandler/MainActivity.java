package com.example.wifidirecthandler;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private int callCount;

    public ArrayList<String> previousProfiles;
    private String profile;
    public TextView text;

    private WifiDirectHandler handler;

    private Button sendButton;
    private Button receiveButton;
    //private Button button3;

    private EditText profileText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        callCount = 0;
        previousProfiles = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendButton = (Button) findViewById(R.id.button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //incrementCallCount();
                //set up wifidirect
                handler.searchForPhonesOnce();
                //register service with profile info

                handler.setInfoString(createProfile());
                handler.startRegistration();
            }
        });

        receiveButton = (Button) findViewById(R.id.button2);
        receiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //incrementCallCount();
                //check for nearby services
                handler.services();
            }
        });

        /*button2 = (Button) findViewById(R.id.button2);
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
        });*/

        text = (TextView) findViewById(R.id.text);
        createNewWifiDirectHandler();

    }

    private void incrementCallCount() {
        if(callCount >= 3){
            System.out.println("resetting call count");
            callCount = 0;
            createNewWifiDirectHandler();
        }
        else{
            callCount += 1;
        }
    }

    private void createNewWifiDirectHandler(){
        if(handler != null) {
            handler.handlerUnRegisterReciever();
        }
        WifiP2pManager manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        handler = new WifiDirectHandler(this, manager, profile, this);
    }

    private String createProfile() {
        String out = "FNF";
        profileText = (EditText) findViewById(R.id.profileText);
        out += profileText.getText();
        previousProfiles.add(out);
        System.out.println("PROFILE" + out);
        return out;
    }

    @Override
    protected void onDestroy(){
        handler.handlerUnRegisterReciever();
        super.onDestroy();
    }
}
