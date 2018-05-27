package code.vietduong.presenter;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import code.vietduong.data.Contanst;
import code.vietduong.impl.MainCallbacks;
import code.vietduong.impl.LoadSongListener;
import code.vietduong.interator.SongInterator;
import code.vietduong.model.entity.Album;
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
    public void onLoadSongSuccess(ArrayList<Song> listSong) {
        main.onDisplaySongList(listSong);
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
