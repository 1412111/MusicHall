package code.vietduong.impl;

import java.util.ArrayList;

import code.vietduong.model.entity.Song;

/**
 * Created by codev on 4/11/2018.
 */

public interface MainCallbacks {
    public void onDisplaySongList(ArrayList<Song> listSong);
    public void onMsgFromListFragToMain (Song song);
    public void onMsgFromServiceToMain(Song Song);

    public void onControlFromServiceToMain(String msg);


}
