package code.vietduong.model.entity;

import java.io.Serializable;

/**
 * Created by Codev on 29/01/2018.
 */

public class Song implements Serializable {

    private long id;
    private String title;
    private String artist;
    private String duration;
    private String albumname;
    private String albumArtPath;
    private String pathSong;
    private String genres;
    private int size;
    private int position;
    private String titleSearch;

    public Song() {
       super();
    }

    public Song(long id, String title, String artist,
                String duration, String albumname, String albumArtPath,
                String pathSong, int size) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.albumname = albumname;
        this.albumArtPath = albumArtPath;
        this.pathSong = pathSong;
        this.size = size;
    }
    public Song(long id, String title, String artist,
                String duration, String albumname, String albumArtPath,
                String pathSong, int size, String genres, String titleSearch) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.albumname = albumname;
        this.albumArtPath = albumArtPath;
        this.pathSong = pathSong;
        this.size = size;
        this.genres = genres;
        this.titleSearch = titleSearch;
    }

    public String getTitleSearch() {
        return titleSearch;
    }

    public void setTitleSearch(String titleSearch) {
        this.titleSearch = titleSearch;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAlbumname() {
        return albumname;
    }

    public void setAlbumname(String albumname) {
        this.albumname = albumname;
    }

    public String getAlbumArtPath() {
        return albumArtPath;
    }

    public void setAlbumArtPath(String albumArtPath) {
        this.albumArtPath = albumArtPath;
    }

    public String getPathSong() {
        return pathSong;
    }

    public void setPathSong(String pathSong) {
        this.pathSong = pathSong;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return title;
    }
}
