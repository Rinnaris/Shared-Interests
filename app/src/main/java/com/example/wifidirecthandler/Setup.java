package com.example.wifidirecthandler;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

//When there is no current profile, launch set-up, to begin the setting up of the profile?
public class Setup extends AppCompatActivity {

    ArrayList<String> movie = new ArrayList<String>();
    ArrayList<String> music = new ArrayList<String>();
    ArrayList<String> book = new ArrayList<String>();
    ArrayList<String> sport = new ArrayList<String>();
    ArrayList<String> hobby = new ArrayList<String>();

    Button movieButton;
    Button musicButton;
    Button bookButton;
    Button sportButton;
    Button hobbyButton;
    Button finishButton;

    EditText movieField;
    EditText musicField;
    EditText bookField;
    EditText sportField;
    EditText hobbyField;

    TextView movieView;
    TextView musicView;
    TextView bookView;
    TextView sportView;
    TextView hobbyView;

    String name;
    String phone;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.CYAN);

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        phone = bundle.getString("phone");
        email = bundle.getString("email");

        movieButton = (Button) findViewById(R.id.movieAdd);
        musicButton = (Button) findViewById(R.id.musicAdd);
        bookButton = (Button) findViewById(R.id.bookAdd);
        sportButton = (Button) findViewById(R.id.sportAdd);
        hobbyButton = (Button) findViewById(R.id.hobbyAdd);
        finishButton = (Button) findViewById(R.id.Finish);

        movieField = (EditText) findViewById(R.id.newMovie);
        musicField = (EditText) findViewById(R.id.newMusic);
        bookField = (EditText) findViewById(R.id.newBook);
        sportField = (EditText) findViewById(R.id.newSport);
        hobbyField = (EditText) findViewById(R.id.newHobby);

        movieView = (TextView) findViewById(R.id.movieList);
        musicView = (TextView) findViewById(R.id.musicList);
        bookView = (TextView) findViewById(R.id.bookList);
        sportView = (TextView) findViewById(R.id.sportList);
        hobbyView = (TextView) findViewById(R.id.hobbyList);


        movieButton.setOnClickListener(new View.OnClickListener() {

            @Override
           public void onClick(View v) {
                if(!movieField.getText().equals(""))
                    movie.add(movieField.getText() + "");

                for(int i = 0; i < movie.size(); i++) {
                    String interest = movie.get(i);
                    movieView.setText(movieView  + " " + interest + ", ");
                }
            }
        });

        musicButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!musicField.getText().equals(""))
                    music.add(musicField.getText() + "");

                for(int i = 0; i < music.size(); i++) {
                    String interest = music.get(i);
                    musicView.setText(musicView  + " " + interest + ", ");
                }
            }
        });

        bookButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!bookField.getText().equals(""))
                    book.add(bookField.getText() + "");

                for(int i = 0; i < book.size(); i++) {
                    String interest = book.get(i);
                    bookView.setText(bookView + " " + interest + ", ");
                }
            }
        });

        sportButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!sportField.getText().equals(""))
                    sport.add(sportField.getText() + "");

                for(int i = 0; i < sport.size(); i++) {
                    String interest = sport.get(i);
                    sportView.setText(sportView + " " + interest + ", ");
                }
            }
        });

        hobbyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!hobbyField.getText().equals(""))
                    hobby.add(hobbyField.getText() + "");

                for(int i = 0; i < hobby.size(); i++) {
                    String interest = hobby.get(i);
                    hobbyView.setText(hobbyView + " " + interest + ", ");
                }
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            Profile profile = new Profile(name, phone, email, movie, music, sport, book, hobby);
            }
        });
    }


}
