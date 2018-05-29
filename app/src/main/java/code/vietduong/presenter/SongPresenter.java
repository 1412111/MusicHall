package code.vietduong.presenter;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import code.vietduong.data.Contanst;
import code.vietduong.impl.MainCallbacks;
import code.vietduong.impl.LoadSongListener;
import code.vietduong.interator.SongInterator;
import code.vietduong.model.entity.Album;
import code.vietduong.model.entity.Artist;
import code.vietduong.model.entity.Genres;
import code.vietduong.model.entity.Song;

/**
 * Created by Codev on 29/01/2018.
 */

public class SongPresenter implements LoadSongListener{

    private MainCallbacks main;
    private SongInterator songInterator;
    private Context context;



    public SongPresenter(Context context, MainCallbacks main){
        this.main = main;
        this.context = context;
        songInterator = new SongInterator(this.context, this);

    }

    public void loadData(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                songInterator.loadSongList();
            }
        });
        t.start();


    }

    @Override
    public void onLoadSongSuccess(final ArrayList<Song> listSong) {
        main.onDisplaySongList(listSong);

        new Thread(new Runnable() {
            @Override
            public void run() {
                createListAlbum(listSong);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                createListArtist(listSong);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                createListGenres(listSong);
            }
        }).start();






    }

    private void createListGenres(ArrayList<Song> listSong) {
        ArrayList<Genres> genres = new ArrayList<>();
        for(Song s : listSong){
            boolean flag = false;
            for(Genres g:genres){

                if(s.getGenres().toUpperCase().equals(g.getName().toUpperCase())||
                        g.getName().toUpperCase().equals(s.getArtist().toUpperCase())) {
                    g.addSong(s);
                    flag = true;
                    break;
                }
            }

            if (!flag){
                Genres newGenres = new Genres();
                newGenres.setName(s.getGenres());
                newGenres.setPicture(s.getAlbumArtPath());
                newGenres.addSong(s);
                genres.add(newGenres);
            }

        }

        Contanst.list_genres = genres;
    }

    private void createListArtist(ArrayList<Song> listSong) {
        ArrayList<Artist> artists = new ArrayList<>();
        for(Song s : listSong){
            boolean flag = false;
            for(Artist a:artists){

                if(s.getArtist().toUpperCase().equals(a.getName().toUpperCase())||
                        a.getName().toUpperCase().equals(s.getArtist().toUpperCase())) {
                    a.addSong(s);
                    flag = true;
                    break;
                }
            }

            if (!flag){
                Artist newArtist = new Artist();
                newArtist.setName(s.getArtist());
                newArtist.setPicture(s.getAlbumArtPath());
                newArtist.addSong(s);
                artists.add(newArtist);
            }

        }
        Contanst.list_artists = artists;
    }

    private void createListAlbum(ArrayList<Song> listSong) {


        ArrayList<Album> albums = new ArrayList<>();
        for(Song s : listSong){
            boolean flag = false;
            for(Album a:albums){

                if(s.getAlbumname().toUpperCase().equals(a.getName().toUpperCase())||
                        a.getName().toUpperCase().equals(s.getAlbumname().toUpperCase())) {
                    a.addSong(s);
                    flag = true;
                    break;
                }
            }

            if (!flag){
                Album newAlbum = new Album();
                newAlbum.setName(s.getAlbumname());
                newAlbum.setPicture(s.getAlbumArtPath());
                newAlbum.addSong(s);
                albums.add(newAlbum);
            }

        }
        Contanst.list_albums = albums;
    }

    @Override
    public void onLoadSongFailure(String message) {

    }
}
