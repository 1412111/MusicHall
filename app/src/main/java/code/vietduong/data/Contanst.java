package code.vietduong.data;

import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Virtualizer;

import java.util.ArrayList;
import java.util.HashSet;

import code.vietduong.model.entity.Album;
import code.vietduong.model.entity.Artist;
import code.vietduong.model.entity.Genres;
import code.vietduong.model.entity.Song;

/**
 * Created by codev on 4/15/2018.
 */

public class Contanst {
    public static int[] listPreset = new int[]{1500, 1500, 1500, 1500, 1500};
    public static int positionPreset = -1;
    public static Equalizer mEqualizer = null;
    public static BassBoost bassBoost = null;
    public static Virtualizer virtualizer = null;
    public static ArrayList<Song> list_songs = new ArrayList<>();
    public static ArrayList<Album> list_albums = new ArrayList<>();
    public static ArrayList<Artist> list_artists = new ArrayList<>();
    public static ArrayList<Genres> list_genres = new ArrayList<>();
    public static HashSet<Song> list_Search = new HashSet<>();

    public static int width = 0;
    public static int height = 0;
    public static int position=-1;

    public static String MSG_MAIN_ALBUM_ACTIVITY = "MSG_MAIN_ALBUM_ACTIVITY";

    public static String MSG_MAIN_ARTIST_ACTIVITY = "MSG_MAIN_ARTIST_ACTIVITY";

    public static String MSG_MAIN_GENRES_ACTIVITY = "MSG_MAIN_GENRES_ACTIVITY";

}
