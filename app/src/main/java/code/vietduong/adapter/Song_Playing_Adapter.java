package code.vietduong.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import code.vietduong.data.Contanst;
import code.vietduong.fragment.Song_Item_Fragment;

/**
 * Created by codev on 4/18/2018.
 */

public class Song_Playing_Adapter extends FragmentStatePagerAdapter {

    private int size = 0;
    public Song_Playing_Adapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
        notifyDataSetChanged();
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        return Song_Item_Fragment.newInstance(position);
    }


    @Override
    public float getPageWidth(int position) {
        return 1.0f;
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }



}
