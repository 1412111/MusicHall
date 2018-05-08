package code.vietduong.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import code.vietduong.custom.CircleTransform;
import code.vietduong.data.Contanst;
import code.vietduong.oneplayer.R;
import jp.wasabeef.blurry.Blurry;

/**
 * Created by codev on 4/18/2018.
 */

public class Song_Item_Fragment extends Fragment {

    Context context = null;
    int position;
    // data to fill-up the ListView
    // convenient constructor(accept arguments, copy them to a bundle, binds bundle to fragment)
    public static Song_Item_Fragment newInstance(int position) {
        Song_Item_Fragment fragment = new Song_Item_Fragment();

        Bundle args = new Bundle();
        args.putInt("pos", position);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            context = getActivity(); // use this reference to invoke main callbacks
            position = getArguments().getInt("pos", 0);

            Log.e("song pos",position+"");
        } catch (IllegalStateException e) {
            throw new IllegalStateException(
                    "MainActivity must implement callbacks" );
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate res/layout_blue.xml to make GUI holding a TextView and a ListView
        FrameLayout layout = (FrameLayout) inflater.inflate(R.layout.song_item_fragment, null);
        ImageView img = layout.findViewById(R.id.img_item_fragment);
        TextView txtSongName = layout.findViewById(R.id.txtSongName_VP);
        TextView txtSinger = layout.findViewById(R.id.txtSinger_VP);

        txtSongName.setText(Contanst.list_songs.get(position).getTitle().toUpperCase());
        txtSinger.setText(Contanst.list_songs.get(position).getArtist().toUpperCase());

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),
                    Uri.parse(Contanst.list_songs.get(position).getAlbumArtPath()));
            Blurry.with(getContext())
                    .radius(10)
                    .sampling(1)
                    .async()
                    .animate(2000).from(bitmap).into(img);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bitmap == null){
            Blurry.with(getContext())
                    .radius(40)
                    .sampling(1)
                    .async()
                    .animate(2000).from(BitmapFactory.decodeResource(getResources(), R.drawable.noalbum_100)).into(img);
        }

      /*  Picasso.with(context).load(Contanst.list_songs.get(position).getAlbumArtPath())
                .resize(1000, 1000)
                .centerCrop()
              *//*  .transform(new CircleTransform())*//*
                .error(R.drawable.noalbum_500)
                .into(img);*/



        return layout;
    }
}
