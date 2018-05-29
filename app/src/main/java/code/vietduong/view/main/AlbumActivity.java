package code.vietduong.view.main;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.futuremind.recyclerviewfastscroll.FastScroller;
import com.squareup.picasso.Picasso;

import code.vietduong.adapter.AlbumAdapter;
import code.vietduong.adapter.SongAdapter;
import code.vietduong.data.Contanst;
import code.vietduong.model.entity.Song;
import code.vietduong.oneplayer.R;

public class AlbumActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        initControls();
    }

    private void initControls() {

        ListView listView = findViewById(R.id.list_song_album);


    }


}
