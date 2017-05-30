package mx.gob.jovenes.guanajuato.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.adapters.RVTweetAdapter;
import mx.gob.jovenes.guanajuato.api.Response;
import mx.gob.jovenes.guanajuato.api.TwitterAPI;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.model.Tweet;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by codigus on 19/5/2017.
 */

public class TwitterFragment extends Fragment {
    private TwitterAPI twitterAPI;
    private RecyclerView rvTweets;
    private TextView tvEmptyTweets;
    private RVTweetAdapter adapter;
    private Retrofit retrofit;
    private List<Tweet> tweets;
    private SharedPreferences sharedPreferences;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofit = ((MyApplication) getActivity().getApplication()).getRetrofitTwitterInstance();
        twitterAPI = retrofit.create(TwitterAPI.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplication());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_twitter, container, false);
        rvTweets = (RecyclerView) v.findViewById(R.id.rv_tweets);
        tvEmptyTweets = (TextView) v.findViewById(R.id.tv_empty_tweets);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rvTweets.setLayoutManager(llm);

        Call<ArrayList<Tweet>> call = twitterAPI.get(sharedPreferences.getString(MyApplication.URL_TWITTER, "guanajoven"));

        call.enqueue(new Callback<ArrayList<Tweet>>() {
            @Override
            public void onResponse(Call<ArrayList<Tweet>> call, retrofit2.Response<ArrayList<Tweet>> response) {
                tweets = response.body();
                adapter = new RVTweetAdapter(getActivity(), tweets);
                rvTweets.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Tweet>> call, Throwable t) {
                tvEmptyTweets.setVisibility(View.VISIBLE);
            }
        });

        return v;
    }
}
