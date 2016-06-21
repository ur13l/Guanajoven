package code.guanajuato.gob.mx.activatecode.fragments;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;

import code.guanajuato.gob.mx.activatecode.R;

/**
 * Created by code on 30/05/16.
 */
public class VideoScreenFragment extends Fragment implements SurfaceHolder.Callback {
    private SurfaceView surfaceView;
    private SurfaceHolder holder;
    private MediaPlayer mp;
    private String path;
    private ImageView imageView;
    private Button btnDetener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_video_screen, parent, false);


        surfaceView = (SurfaceView) v.findViewById(R.id.video_view);
        btnDetener = (Button) v.findViewById(R.id.btn_detener);
        imageView = (ImageView) v.findViewById(R.id.alarma_img);
        path = getActivity().getExternalCacheDir().toString()+"/video.mp4";
        mp = new MediaPlayer();
        File file = new File(path);
        if(file.exists()){
            holder = surfaceView.getHolder();
            holder.setFixedSize(640, 480);
            holder.addCallback(this);
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        else{
            surfaceView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            mp.setAudioStreamType(AudioManager.STREAM_ALARM);
            try {
                mp.setDataSource(getActivity(), Uri.parse("android.resource://code.guanajuato.gob.mx.activatecode/" + R.raw.alarm));
                mp.prepare();
                mp.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        btnDetener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });


        return v;
    }

    @Override
    public void onPause(){
        super.onPause();
        mp.release();
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mp.setAudioStreamType(AudioManager.STREAM_ALARM);
        mp.setDisplay(holder);
        play();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    void play(){
        try {
            mp.setDataSource(path);

            mp.prepare();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.start();
    }
}
