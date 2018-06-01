package code.vietduong.model.entity;

import java.util.ArrayList;

public class Album {
    private int id;
    private ArrayList<Song> songs = new ArrayList<>();
    private String name;
    private String picture;
    private String singer;

    public void addSong(Song s){
        songs.add(s);
    }
    public ArrayList<Song> getSongs() {
        return songs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
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

    public Album(int id) {
        this.id = id;
    }
}
