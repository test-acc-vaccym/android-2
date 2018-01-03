package com.edroplet.sanetel.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.edroplet.sanetel.fragments.PlaceholderFragment;
import com.edroplet.sanetel.utils.hashMapUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by qxs on 2017/9/12.
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private LinkedHashMap<String,String> sbTabTitleMap = new LinkedHashMap<>();
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Map.Entry<String , String > entry = hashMapUtils.getElementFromLinkHashMap(sbTabTitleMap,position);
        String title = "";
        if (entry != null) {
            title = entry.getKey();
            return PlaceholderFragment.newInstance(entry.getValue(), title);
        }
        return null;
    }

    @Override
    public int getCount() {
        return sbTabTitleMap.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Map.Entry<String , String > entry = hashMapUtils.getElementFromLinkHashMap(sbTabTitleMap,position);

        if (entry != null){
            return entry.getKey();
        }
        return null;
    }

    public void setTab( LinkedHashMap map){
        this.sbTabTitleMap = map;
    }
}
