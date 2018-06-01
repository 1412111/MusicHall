package code.vietduong.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import code.vietduong.fragment.Album_Fragment;
import code.vietduong.fragment.Artist_Fragment;

import code.vietduong.fragment.Home_Fragment;
import code.vietduong.fragment.List_Fragment;
import code.vietduong.fragment.Playlist_Fragment;


/**
 * Created by codev on 4/15/2018.
 */
public class MyPagerAdapter extends FragmentStatePagerAdapter {
    private static int NUM_ITEMS = 5;

    public MyPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment

                return Home_Fragment.newInstance();
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return List_Fragment.newInstance();
            case 2: // Fragment # 1 - This will show SecondFragment
                return Artist_Fragment.newInstance();
            case 3: // Fragment # 1 - This will show SecondFragment
                return Album_Fragment.newInstance();
            case 4: // Fragment # 1 - This will show SecondFragment
                return Playlist_Fragment.newInstance();
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

}