package code.vietduong.model.entity;

import java.util.ArrayList;

public class Artist {

    private ArrayList<Song> songs = new ArrayList<>();
    private ArrayList<Album> albums = new ArrayList<>();
    private String name;
    private String picture;

    private int id;

    public void addSong(Song s){
        songs.add(s);
    }

    public void addAlbum(Album a){
        albums.add(a);
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public ArrayList<Album> getAlbums() {
        return albums;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Artist(int id) {
        this.id = id;
    }
}
