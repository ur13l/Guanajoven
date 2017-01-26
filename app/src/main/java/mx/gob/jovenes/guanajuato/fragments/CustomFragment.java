package mx.gob.jovenes.guanajuato.fragments;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import mx.gob.jovenes.guanajuato.R;

/**
 * Created by Uriel on 01/02/2016.
 */
public class CustomFragment extends Fragment {

    private int menu_id;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        int string_title = getArguments().getInt("string_title");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(string_title));


    }

    public void verificarToolbar(){
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout)getActivity().findViewById(R.id.coordinatorLayout);
        AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.toolbar_container);
        //Se eliminan las toolbars existentes en caso de existir para generar la Toolbar de Home

            if (appBarLayout.getChildCount() > 1)
                appBarLayout.removeViewAt(1);
            if (appBarLayout.getChildCount() > 2)
                appBarLayout.removeViewAt(2);
            if (coordinatorLayout.findViewById(R.id.toolbar_bottom_home) != null)
                coordinatorLayout.removeViewAt(coordinatorLayout.getChildCount() - 1);

        //Bloqueando la toolbar principal
        Toolbar toolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);

        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        params.setScrollFlags(params.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED);
        toolbar.setLayoutParams(params);


    }


    public static CustomFragment newInstance(int menu_id, int string_title, Class classFragment) throws IllegalAccessException, java.lang.InstantiationException {
        CustomFragment myFragment =  (CustomFragment)classFragment.newInstance();
        Bundle args = new Bundle();
        args.putInt("menu_id", menu_id);
        args.putInt("string_title", string_title);
        myFragment.setArguments(args);

        return myFragment;
    }
}
