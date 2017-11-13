package mx.gob.jovenes.guanajuato.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import mx.gob.jovenes.guanajuato.fragments.TwitterFragment;

public class VPRedesSocialesAdapter extends FragmentPagerAdapter {

    public VPRedesSocialesAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new TwitterFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 1;
    }


}
