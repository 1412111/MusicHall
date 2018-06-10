package code.vietduong.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


import com.ToxicBakery.viewpager.transforms.DepthPageTransformer;

import java.util.Random;

import code.vietduong.adapter.Song_Playing_Adapter;
import code.vietduong.data.Contanst;
import code.vietduong.impl.FragmentCallbacks;
import code.vietduong.oneplayer.R;
import code.vietduong.view.MainActivity;

/**
 * Created by codev on 4/18/2018.
 */

public class Song_Playing_Fragment extends Fragment implements FragmentCallbacks {
    Context context = null;
    MainActivity mainActivity = null;
    ViewPager mPager;
    private Song_Playing_Adapter adaper = null;
    boolean first_launch_already = false;

    private boolean random = false;


    int position;
    // data to fill-up the ListView
    // convenient constructor(accept arguments, copy them to a bundle, binds bundle to fragment)
    public static Song_Playing_Fragment newInstance() {
        Song_Playing_Fragment fragment = new Song_Playing_Fragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            context = getActivity(); // use this reference to invoke main callbacks
            mainActivity = (MainActivity)getActivity();
           // String strtext = getArguments().getString("pos");
            //position = Integer.parseInt(strtext);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(
                    "MainActivity must implement callbacks" );
        }
    }



    public Song_Playing_Adapter getAdaper() {
        return adaper;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate res/layout_blue.xml to make GUI holding a TextView and a ListView
        FrameLayout layout = (FrameLayout) inflater.inflate(R.layout.song_list_playing_fragment, null);

        mPager = layout.findViewById(R.id.mPager);
        adaper =  new Song_Playing_Adapter(mainActivity.getSupportFragmentManager());
        mPager.setAdapter(adaper);
       // mPager.setPageTransformer(false, new CustPagerTransformer(getContext()));
        mPager.setPageTransformer(false, new DepthPageTransformer());
        mPager.setDefaultFocusHighlightEnabled(true);
        mPager.setClipToPadding(false);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {

                if(first_launch_already) {
                    final int pos = position;
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1);
                                mainActivity.onMsgFromListFragToMain(Contanst.list_songs.get(pos));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    t.start();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return layout;
    }

    public boolean isRandom() {
        return random;
    }

    public void setRandom(boolean random) {
        this.random = random;
    }

    @Override
    public void onMsgFromMainToSlideFragment(String msg) {
        if(msg.equals(MainActivity.LOAD_SONG_FINISHED)){

            mPager.setOffscreenPageLimit(6);
            mPager.setCurrentItem(0,true);

            first_launch_already = true;

        }else if(msg.equals(MainActivity.UPDATE_SONG_UI)){

            mPager.setCurrentItem(Contanst.position);

        }else if(msg.equals(MainActivity.SLIDE_NEXT)){

            if(random){

                Random rand = new Random();
                int  n = rand.nextInt(Contanst.list_songs.size());
                mPager.setCurrentItem(n);

            }else{

                if(Contanst.position == Contanst.list_songs.size()-1){
                    mPager.setCurrentItem(0);
                }else{
                    mPager.setCurrentItem(Contanst.position + 1);
                }
            }

        }else if(msg.equals(MainActivity.SLIDE_PREVIOUS)){

            if(random){

                Random rand = new Random();
                int  n = rand.nextInt(Contanst.list_songs.size());
                mPager.setCurrentItem(n);
            }else{

                if(Contanst.position - 1 < 0){
                    mPager.setCurrentItem(Contanst.list_songs.size()-1);
                }else{
                    mPager.setCurrentItem(Contanst.position - 1);
                }
            }
        }
    }
}
