package io.pcyan.drawersample;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 *
 * Created by yan on 2015/10/30.
 */
public class PagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public PagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return new TabFragment("Tab "+(position+1));
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = mContext.getString(R.string.no_title);
        switch (position) {
            case 0:
                title = mContext.getString(R.string.item1);
                break;
            case 1:
                title = mContext.getString(R.string.item2);
                break;
            case 2:
                title = mContext.getString(R.string.item3);
                break;
            case 3:
                title = mContext.getString(R.string.item4);
                break;
            case 4:
                title = mContext.getString(R.string.item5);
                break;
        }
        return title;
    }
}
