package code.vietduong.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import code.vietduong.adapter.SongAlbumAdapter;
import code.vietduong.data.Contanst;
import code.vietduong.model.entity.Genres;
import code.vietduong.model.entity.Song;
import code.vietduong.oneplayer.R;

public class GenresActivity extends AppCompatActivity {

    private Genres g;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genres);
        TextView txtGenres = findViewById(R.id.txtGenres);
        TextView txtCount = findViewById(R.id.txtCount);

        Intent intent = getIntent();
        String message = intent.getStringExtra(Contanst.MSG_MAIN_GENRES_ACTIVITY);
        g = Contanst.list_genres.get(Integer.parseInt(message));

        txtGenres.setText(g.getName());
        txtCount.setText(g.getSongs().size()+"");

        ImageView img = findViewById(R.id.imgGenres);

        Picasso.with(this).load(g.getPicture())
                .placeholder(R.drawable.noalbum)
                .resize(250, 250)
                .centerCrop()
                .error(R.drawable.noalbum)
                .into(img);

        Log.e("album id", message);


        ImageView imgBG = findViewById(R.id.imgGenresBG);

        Picasso.with(this).load(g.getPicture())
                .placeholder(R.drawable.noalbum)
                .resize(350, 400)
                .centerCrop()
                .error(R.drawable.noalbum)
                .into(imgBG);

        ListView lv = findViewById(R.id.lvGenres);

        SongAlbumAdapter adapter = new SongAlbumAdapter(this, R.layout.song_album_item, g.getSongs());

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Song song= g.getSongs().get(position);

                MainActivity.playSongMain(song);
            }
        });
    }
}
