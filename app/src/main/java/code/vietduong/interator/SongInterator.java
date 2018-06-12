package code.vietduong.interator;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.regex.Pattern;

import code.vietduong.data.Contanst;
import code.vietduong.impl.LoadSongListener;
import code.vietduong.model.entity.Song;

/**
 * Created by Codev on 29/01/2018.
 */

public class SongInterator {
    private LoadSongListener songListener;
    private Context context;
    private ArrayList<Song> listSong;


    public SongInterator(Context context, LoadSongListener songListener){
        this.songListener = songListener;
        this.context = context;
        this.listSong = new ArrayList<>();
    }



    public void loadSongList() {
        ContentResolver musicResolver = context.getContentResolver();

        Uri genresUri = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI;
        Cursor genresCursor = musicResolver.query(genresUri, null, null, null, null);

        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
       /* Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);*/

        if(genresCursor!=null && genresCursor.moveToFirst()){

            do{
                int genresColumn = genresCursor.getColumnIndex(MediaStore.Audio.Genres._ID);
                int genresNameColumn = genresCursor.getColumnIndex(MediaStore.Audio.Genres.NAME);

                long genreId = genresCursor.getLong(genresColumn);
                String genresName = genresCursor.getString(genresNameColumn);

                Uri uri = MediaStore.Audio.Genres.Members.getContentUri("external", genreId);

                Cursor musicCursor = musicResolver.query(uri, null, null, null, null);

                if(musicCursor!=null && musicCursor.moveToFirst())
                {
                    //get columns
                    int idColumn = musicCursor.getColumnIndex
                            (MediaStore.Audio.Media._ID);

                    int titleColumn = musicCursor.getColumnIndex
                            (MediaStore.Audio.Media.TITLE);

                    int artistColumn = musicCursor.getColumnIndex
                            (MediaStore.Audio.Media.ARTIST);

                    int durationColumn = musicCursor.getColumnIndex
                            (MediaStore.Audio.Media.DURATION);

                    int albumColumn = musicCursor.getColumnIndex
                            (MediaStore.Audio.Media.ALBUM);

                    int albumIdColumn = musicCursor.getColumnIndex
                            (MediaStore.Audio.Media.ALBUM_ID);

                    int pathColumn = musicCursor.getColumnIndex
                            (MediaStore.Audio.Media.DATA);

                    int isMusicColumn = musicCursor.getColumnIndex
                            (MediaStore.Audio.Media.IS_MUSIC);

                    int sizeIdColumn = musicCursor.getColumnIndex
                            (MediaStore.Audio.Media.SIZE);


                    int mineType = musicCursor.getColumnIndex
                            (MediaStore.Audio.GenresColumns.NAME);


                    //add songs to list
                    do {
                        long thisId = musicCursor.getLong(idColumn);
                        String thisTitle = musicCursor.getString(titleColumn);
                        String thisArtist = musicCursor.getString(artistColumn);
                        String thisDuration = musicCursor.getString(durationColumn);
                        String thisAlbum = musicCursor.getString(albumColumn);
                        long thisAlbumId = musicCursor.getLong(albumIdColumn);
                        String path = musicCursor.getString(pathColumn);
                        int isMusic = musicCursor.getInt(isMusicColumn);
                        int thisSize = musicCursor.getInt(sizeIdColumn);
                        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                        Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, thisAlbumId);

                        thisTitle = thisTitle.substring(0, 1).toUpperCase() + thisTitle.substring(1).toLowerCase();

                        if (Integer.parseInt(thisDuration) <= 30) {
                            continue;
                        }

                        if (isMusic != 0 && thisSize > 0) {

                            String titleSearch = deAccent(thisTitle);
                            Song song = new Song(thisId, thisTitle, thisArtist
                                    , thisDuration, thisAlbum, albumArtUri.toString(), path, thisSize, genresName, titleSearch);
                            this.listSong.add(song);

                        }
                    }
                    while (musicCursor.moveToNext());
                }
                musicCursor.close();

            }while (genresCursor.moveToNext());

        }


        genresCursor.close();
        songListener.onLoadSongSuccess(this.listSong);
    }

    public static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

}
