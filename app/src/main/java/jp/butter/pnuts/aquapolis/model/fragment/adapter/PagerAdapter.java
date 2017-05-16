package jp.butter.pnuts.aquapolis.model.fragment.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pnuts on 2016/08/23.
 *
 */
public class PagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragments;

    public PagerAdapter(FragmentManager adapter) {
        super(adapter);
        mFragments = new ArrayList();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    public void addFragment(Fragment fragment) {
        mFragments.add(fragment);
    }

    public void destroyAllItem(ViewPager pager) {
        int count = getCount();
        for (int i = 0; i < count; i++) {
            Object obj = this.instantiateItem(pager, i);
            if (obj != null) {
                this.destroyItem(pager, i, obj);
            }
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);

        if (position <= getCount()) {
            FragmentManager manager = ((Fragment) object).getFragmentManager();
            FragmentTransaction trans = manager.beginTransaction();
            trans.remove((Fragment) object);
            trans.commit();
        }
    }
}