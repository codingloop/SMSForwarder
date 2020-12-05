package in.codingloop.sms;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {
    String[] tabList;
    Context c;

    public TabAdapter(@NonNull FragmentManager fm, Context c, String[] tabList) {
        super(fm);
        this.c = c;
        this.tabList = tabList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new RowsFragment(tabList[position]);
    }

    @Override
    public int getCount() {
        return tabList.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return c.getResources().getStringArray(R.array.tab_titles)[position];
    }
}
