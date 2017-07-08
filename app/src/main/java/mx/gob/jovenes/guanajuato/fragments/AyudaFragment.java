package mx.gob.jovenes.guanajuato.fragments;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import mx.gob.jovenes.guanajuato.R;

/**
 * Created by code on 26/10/16.
 */
public class AyudaFragment extends CustomFragment {
    private VideoView videoView;
    private MediaController mediaController;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ayuda, container, false);

        videoView = (VideoView) v.findViewById(R.id.ayuda_video);
        mediaController = new MediaController(getContext());
        mediaController.setAnchorView(videoView);
        Uri uri = Uri.parse("rtsp://r6---sn-q4fl6n7s.googlevideo.com/Cj0LENy73wIaNAkqK5JKrTlWthMYDSANFC0ncF1ZMOCoAUIASARghonFwbWQteNYigELSVlkR3hXcTcyZm8M/57AE8D5D8DE10553CBC0FB1A08C2498E4769AEB0.154DD16FDE3A1C233E2778CEBB0AC17B2F0AB4E0/yt6/1/video.3gp");
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();

        return v;
    }

}
