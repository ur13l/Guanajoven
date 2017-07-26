package mx.gob.jovenes.guanajuato.fragments;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import mx.gob.jovenes.guanajuato.R;

/**
 * Created by code on 26/10/16.
 */
public class AyudaFragment extends Fragment {
    private WebView videoView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ayuda, container, false);

        videoView = (WebView) v.findViewById(R.id.ayuda_video);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        String frameVideo = "<html><body><br> <iframe width=\"" + width + "\" height=\"" + height + "\" src=\"https://www.youtube.com/embed/Dd2xU7dawAY\" frameborder=\"0\" allowfullscreen></iframe></body></html>";

        //videoView.setWebChromeClient(new WebChromeClient());
        videoView.getSettings().setJavaScriptEnabled(true);
        videoView.getSettings().setLoadWithOverviewMode(true);
        videoView.getSettings().setUseWideViewPort(true);

        videoView.loadData(frameVideo, "text/html", "utf-8");

        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        videoView.destroy();
    }
}
