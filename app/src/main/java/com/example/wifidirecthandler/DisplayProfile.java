package com.example.wifidirecthandler;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class DisplayProfile extends AppCompatActivity {

    String name;
    String phone;
    String email;

    ArrayList<String> movie = new ArrayList<String>();
    ArrayList<String> music = new ArrayList<String>();
    ArrayList<String> book = new ArrayList<String>();
    ArrayList<String> sport = new ArrayList<String>();
    ArrayList<String> hobby = new ArrayList<String>();

    TextView nameView;
    TextView phoneView;
    TextView emailView;
    TextView movieView;
    TextView musicView;
    TextView bookView;
    TextView sportView;
    TextView hobbyView;

    Button editBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_profile);

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        phone = bundle.getString("phone");
        email = bundle.getString("email");
        movie = bundle.getStringArrayList("movie");
        music = bundle.getStringArrayList("music");
        sport = bundle.getStringArrayList("sport");
        book = bundle.getStringArrayList("book");
        hobby = bundle.getStringArrayList("hobby");

        Profile profile = new Profile(name, phone, email, movie, music, sport, book, hobby);
        System.out.println("A profile has been created.");

        nameView = (TextView) findViewById(R.id.nameText);
        phoneView = (TextView) findViewById(R.id.phoneText);
        emailView = (TextView) findViewById(R.id.emailText);
        movieView = (TextView) findViewById(R.id.movieText);
        musicView = (TextView) findViewById(R.id.musicText);
        bookView = (TextView) findViewById(R.id.bookText);
        sportView = (TextView) findViewById(R.id.sportText);
        hobbyView = (TextView) findViewById(R.id.hobbyText);

        MakeProfile(profile);

        editBtn = (Button) findViewById(R.id.editButton);
        editBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
    }

    void MakeProfile(Profile profile) {
        nameView.setText(profile.getName());
        phoneView.setText(profile.getPhone());
        emailView.setText(profile.getEmail());
        for (int i = 0; i < movie.size(); i++)
            movieView.setText(profile.getMovie().get(i));
        for (int i = 0; i < music.size(); i++)
            musicView.setText(profile.getMusic().get(i));
        for (int i = 0; i < book.size(); i++)
            bookView.setText(profile.getBook().get(i));
        for (int i = 0; i < sport.size(); i++)
            sportView.setText(profile.getSport().get(i));
        for (int i = 0; i < hobby.size(); i++)
            hobbyView.setText(profile.getHobby().get(i));
    }
}



