package code.vietduong.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import code.vietduong.custom.CircleTransform;
import code.vietduong.data.Contanst;
import code.vietduong.oneplayer.R;

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

        //ImageView img = (ImageView) inflater.inflate(R.layout.song_item_fragment, null);

        Picasso.with(context).load(Contanst.list_songs.get(position).getAlbumArtPath())
                .resize(350, 350)
                .centerCrop()
                .transform(new CircleTransform(42))
                .error(R.drawable.noalbum_round_256)
                .into(img);

        return layout;
    }
}
