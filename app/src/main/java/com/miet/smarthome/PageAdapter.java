package com.miet.smarthome;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.miet.smarthome.models.ITitleable;

class PageAdapter extends FragmentPagerAdapter {
    private int tabsCount;

    PageAdapter(FragmentManager fm, int tabsCount) {
        super(fm);
        this.tabsCount = tabsCount;
    }

    private Fragment[] tabs = new Fragment[] {
            new SensorsFragment(),
            new SettingsFragment(),
    };

    @Override
    public CharSequence getPageTitle(int position) {
        Fragment frag = tabs[position];
        if (frag instanceof ITitleable) {
            return ((ITitleable) frag).getTitle();
        }
        return "unnamed";
    }

    @Override
    public Fragment getItem(int position) {
        if (position >= 0 && position <= tabs.length - 1) {
            return tabs[position];
        }

        return null;
    }

    @Override
    public int getCount() {
        return tabsCount;
    }
}
