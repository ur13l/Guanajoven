package mx.gob.jovenes.guanajuato.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.adapters.VPRedesSocialesAdapter;

/**
 * Created by codigus on 19/5/2017.
 */

public class RedesSocialesFragment extends CustomFragment {
    private TabLayout tabs;
    private ViewPager viewPager;
    private VPRedesSocialesAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_redes_sociales, container, false);
        tabs = (TabLayout) v.findViewById(R.id.tabs_redes_sociales);
        viewPager = (ViewPager) v.findViewById(R.id.vp_redes_sociales);
        adapter = new VPRedesSocialesAdapter(getFragmentManager());

        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);

        tabs.getTabAt(0).setIcon(R.drawable.ic_twitter);
        tabs.getTabAt(1).setIcon(R.drawable.ic_facebook);

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                System.err.println(tab.getText());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return v;
    }
}
