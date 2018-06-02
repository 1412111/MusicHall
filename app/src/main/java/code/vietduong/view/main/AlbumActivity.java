package code.vietduong.view.main;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.futuremind.recyclerviewfastscroll.FastScroller;
import com.squareup.picasso.Picasso;

import code.vietduong.adapter.AlbumAdapter;
import code.vietduong.adapter.SongAdapter;
import code.vietduong.adapter.SongAlbumAdapter;
import code.vietduong.adapter.SongPopUpAdapter;
import code.vietduong.data.Contanst;
import code.vietduong.model.entity.Album;
import code.vietduong.model.entity.Song;
import code.vietduong.oneplayer.R;

public class AlbumActivity extends AppCompatActivity{

    private  Album album;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        Intent intent = getIntent();
        String message = intent.getStringExtra(Contanst.MSG_MAIN_ALBUM_ACTIVITY);

        album = Contanst.list_albums.get(Integer.parseInt(message));

        ListView listView = findViewById(R.id.list_song_album);

        SongAlbumAdapter adapter = new SongAlbumAdapter(this, R.layout.song_album_item, album.getSongs());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                playSong(position);
            }
        });

        TextView txtAlbumName = findViewById(R.id.txtAlbumName);
        txtAlbumName.setLines(2);
        TextView txtAlbumSinger = findViewById(R.id.txtAlbumSinger);

        txtAlbumSinger.setLines(2);
        TextView txtCount = findViewById(R.id.txtCountSong);

        ImageView imgAlbum = findViewById(R.id.imgAlbum);

        txtCount.setText("PLAY ALL "+album.getSongs().size()+" SONGS");
        txtAlbumName.setText(album.getName());
        txtAlbumSinger.setText(album.getSinger());

        Picasso.with(this).load(album.getPicture())
                .placeholder(R.drawable.noalbum)
                .resize(180, 180)
                .centerCrop()
                .error(R.drawable.noalbum)
                .into(imgAlbum);

        Log.e("album id", message);
    }

    private void playSong(int position) {

        Song song= album.getSongs().get(position);

        MainActivity.playSongMain(song);
    }


}
