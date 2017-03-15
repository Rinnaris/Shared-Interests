package com.example.wifidirecthandler;

import java.util.ArrayList;

/**
 * Created Alyssa on 2017-03-15.
 */

public class Profile {

    String name;
    String phone;
    String email;
    ArrayList<String> movie;
    ArrayList<String> music;
    ArrayList<String> sport;
    ArrayList<String> book;
    ArrayList<String> hobby;

    public Profile(String nameIn, String phoneIn, String emailIn, ArrayList<String> movieIn, ArrayList<String> musicIn, ArrayList<String> sportIn, ArrayList<String> bookIn, ArrayList<String> hobbyIn) {
        name = nameIn;
        phone = phoneIn;
        email = emailIn;
        movie = movieIn;
        music = musicIn;
        sport = sportIn;
        book = bookIn;
        hobby = hobbyIn;
    }

    public String getName(){
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail(){
        return email;
    }

    public ArrayList<String> getMovie(){
        return movie;
    }

    public ArrayList<String> getMusic(){
        return music;
    }

    public ArrayList<String> getSport(){
        return sport;
    }

    public ArrayList<String> getBook(){
        return book;
    }

    ArrayList<String> getHobby(){
        return hobby;
    }

}
