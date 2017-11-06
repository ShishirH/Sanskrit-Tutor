package com.example.android.sanskrittutoring;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * Created by ohlordino on 8/10/17.
 */

public class SimpleFragmentAdapter extends FragmentPagerAdapter {

    private Context mContext;
    public SimpleFragmentAdapter(Context context, FragmentManager fm)
    {
        super(fm);
        mContext=context;
    }
    @Override
    public Fragment getItem(int position) {
        if(position==0)
            return new NumbersFragment();
        else if(position==1)
            return new FamilyFragment();
        else if(position==2)
            return new ColorsFragment();
        else
            return new PhrasesFragment();
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Numbers";
        } else if (position == 1) {
            return "Family";
        } else if (position == 2) {
            return "Colors";
        } else {
            return "Phrases";
        }
    }
}
