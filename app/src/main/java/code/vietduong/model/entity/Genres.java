package code.vietduong.model.entity;

import java.util.ArrayList;

public class Genres {
    private ArrayList<Song> songs = new ArrayList<>();
    private String name;
    private String picture;

    public void addSong(Song s){
        songs.add(s);
    }
    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Genres() {
    }
}
