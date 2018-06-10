package code.vietduong.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;


import code.vietduong.adapter.GenresAdapter;
import code.vietduong.data.Contanst;
import code.vietduong.oneplayer.R;
import code.vietduong.view.MainActivity;

public class Genres_Fragment extends Fragment {
    Context context = null;

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
        ListView lvGenres = layout.findViewById(R.id.listGenres);

        lvGenres.setAdapter(new GenresAdapter(context, R.layout.genres_item, Contanst.list_genres));
        lvGenres.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity)context).onMsgFromGenresFragToMain(position);
            }
        });
        return layout;
    }


}