package id.ac.umn.shoebox;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Stefanus K on 4/25/2018.
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
                NotifFragment notifFragment = new NotifFragment();
                return notifFragment;
            case 3:
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
