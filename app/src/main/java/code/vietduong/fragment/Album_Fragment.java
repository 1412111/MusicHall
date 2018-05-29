package code.vietduong.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import code.vietduong.adapter.AlbumAdapter;
import code.vietduong.data.Contanst;
import code.vietduong.model.entity.Album;
import code.vietduong.oneplayer.R;

/**
 * Created by codev on 4/16/2018.
 */

public class Album_Fragment extends Fragment {
    Context context = null;
    String message = "";
    // data to fill-up the ListView
    // convenient constructor(accept arguments, copy them to a bundle, binds bundle to fragment)
    public static Album_Fragment newInstance() {
        Album_Fragment fragment = new Album_Fragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            context = getActivity(); // use this reference to invoke main callbacks
        } catch (IllegalStateException e) {
            throw new IllegalStateException(
                    "MainActivity must implement callbacks" );
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate res/layout_blue.xml to make GUI holding a TextView and a ListView
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.album_pager, null);
        // plumbing – get a reference to textview and listview

        RecyclerView albumListview = layout.findViewById(R.id.albumListview);
        albumListview.setLayoutManager(new GridLayoutManager(context, 2));
        albumListview.setPadding(75,70,0,50);
        AlbumAdapter adapter = new AlbumAdapter(context);
        adapter.setOnAlbumItemClickListener(new AlbumAdapter.RecyclerAlbumItemClickListener() {
            @Override
            public void onItemClick(Album album) {

            }
        });
        albumListview.setAdapter(adapter);

        return layout;
    }// onCreateView


}
