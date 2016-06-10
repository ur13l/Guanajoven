package code.guanajuato.gob.mx.activatecode.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import code.guanajuato.gob.mx.activatecode.R;

/**
 * Created by code on 30/05/16.
 */
public class VideoScreenFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_video_screen, parent, false);

        MediaController mc= new MediaController(getActivity());

        VideoView view = (VideoView)v.findViewById(R.id.video_view);
        String path = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.prueba;
        view.setVideoURI(Uri.parse(path));
        view.setMediaController(mc);

        view.start();
        return v;
    }
}
