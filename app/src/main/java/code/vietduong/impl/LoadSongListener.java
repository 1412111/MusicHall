package code.vietduong.impl;

import java.util.ArrayList;
import java.util.List;

import code.vietduong.model.entity.Song;

/**
 * Created by Codev on 29/01/2018.
 */

public interface LoadSongListener {
    void onLoadSongSuccess(ArrayList<Song> listSong);
    void onLoadSongFailure(String message);
}
