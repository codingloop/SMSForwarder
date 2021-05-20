package in.codingloop.sms;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class TabAdapter extends FragmentPagerAdapter {
    List<RowsFragment> tabList;
    Context c;

    public TabAdapter(@NonNull FragmentManager fm, Context c, List<RowsFragment> tabList) {
        super(fm);
        this.c = c;
        this.tabList = tabList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return tabList.get(position);
    }

    @Override
    public int getCount() {
        return tabList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return c.getResources().getStringArray(R.array.tab_titles)[position];
    }
}
