package works.avijay.com.ipl2018.helper;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import works.avijay.com.ipl2018.LiveMatch1;
import works.avijay.com.ipl2018.LiveMatch2;

/**
 * Created by avinashk on 21/03/18.
 */

public class MatchesTabsAdapter extends FragmentPagerAdapter {


    public MatchesTabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "MATCH 1";

            case 1: return "MATCH 2";

            default: return null;
        }
    }


    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new LiveMatch1();

            case 1:
                return new LiveMatch2();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }



}
