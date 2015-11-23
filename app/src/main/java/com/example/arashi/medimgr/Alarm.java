package com.example.arashi.medimgr;

/**
 * Created by Mist on 2015/11/24.
 */
public class Alarm {

    private int picture;
    private String name;
    private int hour, minute;
    private String musicPath;

    public Alarm(int picture, String name, String time) {
        setPicture(picture);
        setName(name);
        setTime(time);
    }


    public int getPicture() {
        return picture;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return String.format("%02d:%02d", hour, minute);
    }

    public String getMusicPath() {
        return musicPath;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        String[] t = time.split(":");
        this.hour = Integer.parseInt(t[0]);
        this.minute = Integer.parseInt(t[1]);
    }

    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }
}

