package code.vietduong.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.futuremind.recyclerviewfastscroll.FastScroller;

import code.vietduong.adapter.SongAdapter;
import code.vietduong.model.entity.Song;
import code.vietduong.oneplayer.R;
import code.vietduong.view.MainActivity;


/**
 * Created by codev on 4/14/2018.
 */

public class List_Fragment extends Fragment {
    // this fragment shows a ListView
    Context context = null;
    String message = "";
    SongAdapter songAdapter;

    // data to fill-up the ListView
    // convenient constructor(accept arguments, copy them to a bundle, binds bundle to fragment)
    public static List_Fragment newInstance() {
        List_Fragment fragment = new List_Fragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            context = getActivity(); // use this reference to invoke main callbacks
            songAdapter = new SongAdapter(context);
            songAdapter.setOnItemClickListener(new SongAdapter.RecyclerItemClickListener() {
                @Override
                public void onItemClick(Song song) {
                    sendDataToMain(song);
                }
            });
        } catch (IllegalStateException e) {
            throw new IllegalStateException(
                    "MainActivity must implement callbacks" );
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    // inflate res/layout_blue.xml to make GUI holding a TextView and a ListView
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.list_song_pager,null);
    // plumbing – get a reference to textview and listview

        RecyclerView recyclerView = layout.findViewById(R.id.list_song);

        /*Scroll fast*/
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(songAdapter);
        FastScroller fastScroller = layout.findViewById(R.id.fastscroll);
        //has to be called AFTER RecyclerView.setAdapter()
        fastScroller.setRecyclerView(recyclerView);
/*
        fastScroller.setBubbleColor(Color.parseColor("#282828"));
        fastScroller.setHandleColor(Color.parseColor("#282828"));
        fastScroller.setBubbleTextAppearance(R.style.StyledScrollerTextAppearance);*/

        return layout;
    }



    private void determineScroll(RecyclerView recyclerView, int dx, int dy) {
        //up

        if (dy > 0) {

        } else {

        }

    }

    private void sendDataToMain(Song song) {
        ((MainActivity)context).onMsgFromListFragToMain(song);
    }
}
