package com.example.wifidirecthandler;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

<<<<<<< HEAD
//When there is no current profile, launch set-up, to begin the setting up of the profile?
public class Setup extends AppCompatActivity {

    Button startButton;
    TextView instructionsText;
    TextView welcomeText;
    TextView skipText;
    EditText nameField;
=======
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

>>>>>>> master

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.CYAN);

<<<<<<< HEAD
        instructionsText = (TextView) findViewById(R.id.textInstructions);
        welcomeText = (TextView) findViewById(R.id.textWelcome);
        skipText = (TextView) findViewById(R.id.textSkip);
        nameField = (EditText) findViewById(R.id.textName);

        startButton = (Button) findViewById(R.id.buttonStart);
        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                welcomeText.setVisibility(View.INVISIBLE);
                nameField.setVisibility(View.VISIBLE);
                instructionsText.setVisibility(View.VISIBLE);

                startButton.setText("OK");
            }
        });
    }
=======
        movieButton = (Button) findViewById(R.id.movieAdd);
        musicButton = (Button) findViewById(R.id.musicAdd);
        bookButton = (Button) findViewById(R.id.bookAdd);
        sportButton = (Button) findViewById(R.id.sportAdd);
        hobbyButton = (Button) findViewById(R.id.hobbyAdd);

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
                book.add(bookField.getText() + "");

                for(int i = 0; i < book.size(); i++) {
                    String interest = book.get(i);
                    bookView.setText(bookView + " " + interest + ", ");
                }
            }
        });
    }


>>>>>>> master
}
