package code.vietduong.presenter;

import android.content.Context;

import java.util.ArrayList;

import code.vietduong.impl.MainCallbacks;
import code.vietduong.impl.LoadSongListener;
import code.vietduong.interator.SongInterator;
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
    }

    @Override
    public void onLoadSongFailure(String message) {

    }
}
