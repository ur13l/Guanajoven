package mx.gob.jovenes.guanajuato.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.adapters.RVTweetAdapter;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by codigus on 19/5/2017.
 */

public class TwitterFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayoutTwitter;
    private RecyclerView rvTweets;
    private TextView tvEmptyTweets;
    private RVTweetAdapter adapter;
    private LinearLayoutManager layoutManager;
    private ProgressBar progressBar;

    //Tamaño por página (número de tweets)
    private int PAGINA = 1;

    //Variables twitter4j
    private ConfigurationBuilder CONFIGURATION_BUILDER = new ConfigurationBuilder().setDebugEnabled(true).setOAuthConsumerKey("zHb326FK2xT666c0olifieF9h").setOAuthConsumerSecret("ImFmtlFoLNuWWFRo4yAqbUTKhdeZElZCJ0UVn48UtVj1NkwS7d").setOAuthAccessToken("865314952470560768-BZ5AXIzHyRndtxWqcYe8u2rKMIb25cF").setOAuthAccessTokenSecret("cBqlyZVCekQNnCE5v4LAdDzpzOqgsdqHjocTpw26JThn4");
    private Twitter TWITTER = new TwitterFactory(CONFIGURATION_BUILDER.build()).getInstance();
    private String USER = "@Strats1502";
    private List<Status> statuses;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Espera a que se termine el hilo de policy
        try {
            this.wait();
        } catch (Exception e) {
            e.printStackTrace();
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        statuses = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_twitter, container, false);
        swipeRefreshLayoutTwitter = (SwipeRefreshLayout) v.findViewById(R.id.swipelayout_twitter);
        rvTweets = (RecyclerView) v.findViewById(R.id.rv_tweets);
        tvEmptyTweets = (TextView) v.findViewById(R.id.tv_empty_tweets);
        layoutManager = new LinearLayoutManager(getActivity());
        progressBar = (ProgressBar) v.findViewById(R.id.progress_bar_twitter);
        rvTweets.setLayoutManager(layoutManager);

        //Cuando inicia la vista hace la llamada
        primerLlamada();

        //Cuando hace swipe hace la llamada para actualizar
        swipeRefreshLayoutTwitter.setOnRefreshListener(() -> actualizar());

        //Cuando hace pull hace llamada a antiguos tweets
        rvTweets.addOnScrollListener(new SetOnScrollListener());

        return v;
    }

    private void primerLlamada() {

        try {
            Paging page = new Paging(PAGINA, 10);
            statuses.addAll(TWITTER.getUserTimeline(USER, page));
            PAGINA++;
        } catch (TwitterException exception) {
            exception.printStackTrace();
            tvEmptyTweets.setVisibility(View.VISIBLE);
        }

        adapter = new RVTweetAdapter(getActivity(), statuses);
        swipeRefreshLayoutTwitter.setRefreshing(false);
        rvTweets.setAdapter(adapter);

    }

    private void generarLlamada() {
        List<Status> auxiliar = new ArrayList<>();

        try {
            Paging page = new Paging(PAGINA, 10);
            auxiliar.addAll(TWITTER.getUserTimeline(USER, page));
            PAGINA++;

            if (auxiliar.isEmpty()) {
                Snackbar.make(getView(), "No se encontraron mas tweets", Snackbar.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }

        } catch (TwitterException exception) {
            exception.printStackTrace();

            AlertDialog.Builder b = new AlertDialog.Builder(getContext());
            b.create();
            b.setMessage("error primera llamada");
            b.show();
        }

        adapter.agregarTweets(auxiliar);
    }

    private void actualizar() {
        PAGINA = 1;
        statuses.clear();
        adapter.notifyDataSetChanged();
        adapter = null;

        try {
            Paging page = new Paging(PAGINA, 10);
            statuses.addAll(TWITTER.getUserTimeline(USER, page));
            PAGINA++;
        } catch (TwitterException exception) {
            exception.printStackTrace();
            tvEmptyTweets.setVisibility(View.VISIBLE);
        }

        adapter = new RVTweetAdapter(getActivity(), statuses);
        rvTweets.setAdapter(adapter);
        swipeRefreshLayoutTwitter.setRefreshing(false);
    }

    private class SetOnScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                progressBar.setVisibility(View.VISIBLE);
                generarLlamada();
            } else {
                progressBar.setVisibility(View.GONE);
            }

        }
    }

}
