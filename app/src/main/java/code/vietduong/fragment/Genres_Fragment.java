package code.vietduong.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import android.widget.ListView;


import com.felipecsl.asymmetricgridview.library.Utils;
import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;

import java.util.ArrayList;
import java.util.List;

import code.vietduong.adapter.AlbumAdapter;
import code.vietduong.adapter.ArtistAdapter;
import code.vietduong.adapter.GenresAdapter;
import code.vietduong.data.Contanst;
import code.vietduong.model.entity.Album;
import code.vietduong.model.entity.Genres;
import code.vietduong.oneplayer.R;

public class Genres_Fragment extends Fragment {
    Context context = null;
    String message = "";
    private AsymmetricGridView listView;
    // data to fill-up the ListView
    // convenient constructor(accept arguments, copy them to a bundle, binds bundle to fragment)
    public static Genres_Fragment newInstance() {
        Genres_Fragment fragment = new Genres_Fragment();
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
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.genres_pager, null);
        // plumbing – get a reference to textview and listview


        return layout;
    }// onCreateView


}