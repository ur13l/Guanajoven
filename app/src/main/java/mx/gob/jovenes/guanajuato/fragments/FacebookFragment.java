package mx.gob.jovenes.guanajuato.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mx.gob.jovenes.guanajuato.R;

/**
 * Created by codigus on 19/5/2017.
 */

public class FacebookFragment extends Fragment {
    TextView tvFacebook;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_facebook, container, false);
        tvFacebook = (TextView) v.findViewById(R.id.tv_facebook);
        return v;
    }
}
