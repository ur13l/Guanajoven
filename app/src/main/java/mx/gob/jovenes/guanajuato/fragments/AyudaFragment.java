package mx.gob.jovenes.guanajuato.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

import mx.gob.jovenes.guanajuato.R;

public class AyudaFragment extends Fragment {
    private YouTubePlayerFragment videoView;
    private YouTubePlayer.OnInitializedListener listener;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_ayuda, container, false);

        videoView = (YouTubePlayerFragment) getActivity().getFragmentManager().findFragmentById(R.id.fragment_youtube);

        listener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo("8qdgB_JUWL4");
                youTubePlayer.setShowFullscreenButton(false);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        videoView.initialize("AIzaSyDHDnSpC4e1VwrYYjQeb1sAQrr_d2U0zPY", listener);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.onDestroy();
        if (videoView != null) getActivity().getFragmentManager().beginTransaction().remove(videoView).commit();
    }
}
