package com.example.wifidirecthandler;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created Alyssa on 2017-03-15.
 */

public class Profile implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getName());
        parcel.writeString(getPhone());
        parcel.writeString(getEmail());
        parcel.writeStringList(getMovie());
        parcel.writeStringList(getMusic());
        parcel.writeStringList(getBook());
        parcel.writeStringList(getSport());
        parcel.writeStringList(getHobby());
    }

    public static final Parcelable.Creator<Profile> CREATOR = new Parcelable.Creator<Profile>() {
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Profile (Parcel in) {
        name = in.readString();
        phone = in.readString();
        email = in.readString();
        movie = in.createStringArrayList();
        music = in.createStringArrayList();
        book = in.createStringArrayList();
        sport = in.createStringArrayList();
        hobby = in.createStringArrayList();
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
