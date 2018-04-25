package code.vietduong.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import code.vietduong.oneplayer.R;

/**
 * Created by codev on 4/15/2018.
 */
public class Artist_Fragment extends Fragment {
    // this fragment shows a ListView
    Context context = null;
    String message = "";
    // data to fill-up the ListView
    // convenient constructor(accept arguments, copy them to a bundle, binds bundle to fragment)
    public static Artist_Fragment newInstance() {
        Artist_Fragment fragment = new Artist_Fragment();
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
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.artist_pager, null);
        // plumbing – get a reference to textview and listview
        return layout;
    }// onCreateView
}
