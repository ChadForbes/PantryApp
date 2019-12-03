package com.mobilesoftware.pantryapp.ui.main.adapters;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> lstFragments = new ArrayList<>();
    private final List<String> lstTitle = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return lstTitle.get(position);
    }

    public Fragment getItem(int position) {
        return lstFragments.get(position);
    }

    @Override
    public int getCount() {
        return lstTitle.size();
    }

    public void AddFragment(Fragment fragment, String title) {
        lstFragments.add(fragment);
        lstTitle.add(title);
    }
}