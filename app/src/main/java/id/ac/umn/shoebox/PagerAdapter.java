package id.ac.umn.shoebox;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Adapter pager untuk Utama Activity
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    int index_menu;
    public PagerAdapter(FragmentManager fm, int index){
        super(fm);
        this.index_menu=index;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0 :
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                OrderFragment orderFragment = new OrderFragment();
                return orderFragment;
            case 2:
                HelpFragment helpFragment = new HelpFragment();
                return helpFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return index_menu;
    }
}
