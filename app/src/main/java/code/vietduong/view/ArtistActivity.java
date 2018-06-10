package code.vietduong.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import code.vietduong.adapter.SongAlbumAdapter;
import code.vietduong.data.Contanst;
import code.vietduong.model.entity.Artist;
import code.vietduong.oneplayer.R;

public class ArtistActivity extends AppCompatActivity {
    private TextView txtArtistName, txtArtistInfo;
    private CircularImageView imgArtist;
    private ImageView bg;
    private Artist artist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);

        txtArtistName = findViewById(R.id.txtArtistName);
        txtArtistInfo = findViewById(R.id.txtArtistInfo);

        imgArtist = findViewById(R.id.imgArtist);

        bg = findViewById(R.id.imgBackGround);


        Intent intent = getIntent();
        String message = intent.getStringExtra(Contanst.MSG_MAIN_ARTIST_ACTIVITY);

        artist = Contanst.list_artists.get(Integer.parseInt(message));

        txtArtistName.setText(artist.getName());

        Picasso.with(this).load(artist.getPicture())
                .placeholder(R.drawable.noalbum)
                .resize(300, 300)
                .centerCrop()
                .error(R.drawable.noalbum)
                .into(imgArtist);

        Picasso.with(this).load(artist.getPicture())
                .placeholder(R.drawable.noalbum)
                .resize(300, 300)
                .centerCrop()
                .error(R.drawable.noalbum)
                .into(bg);

        ListView listView = findViewById(R.id.list_song_artist);

        SongAlbumAdapter adapter = new SongAlbumAdapter(this, R.layout.song_album_item, artist.getSongs());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MainActivity.playSongMain(artist.getSongs().get(position));
            }
        });

    }
}
