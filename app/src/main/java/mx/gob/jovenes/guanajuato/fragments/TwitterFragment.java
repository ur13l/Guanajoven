package mx.gob.jovenes.guanajuato.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.adapters.RVTweetAdapter;
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

        Call<ArrayList<Tweet>> call = twitterAPI.get(sharedPreferences.getString(MyApplication.URL_TWITTER, "guanajoven"), authorizationHeader());

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

    private String authorizationHeader() {
        //Static parameters
        String oauthConsumerKey = "zHb326FK2xT666c0olifieF9h";
        String oauthConsumerSecret = "ImFmtlFoLNuWWFRo4yAqbUTKhdeZElZCJ0UVn48UtVj1NkwS7d";
        String oauthToken = "865314952470560768-BZ5AXIzHyRndtxWqcYe8u2rKMIb25cF";
        String oauthTokenSecret = "cBqlyZVCekQNnCE5v4LAdDzpzOqgsdqHjocTpw26JThn4";
        String oauthSignatureMethod = "HMAC-SHA1";
        String oauthVersion = "1.0";

        //Dynamic parameters
        String oauthTimeStamp = String.valueOf(System.currentTimeMillis());

        //Generate a random Universally Unique Identifier
        String uuiString = UUID.randomUUID().toString();
        //Change all "-" characters to ""
        String oauthNonce = uuiString.replaceAll("-", "");

        //Calculated parameters
        String header = "";
        String parameterString = "";
        String signatureBaseString = "";
        String oauthSignature = "";

        parameterString = "oauth_consumer_key=" + oauthConsumerKey + "&oauth_nonce=" + oauthNonce + "&oauth_signature_method=" + oauthSignatureMethod + "&oauth_timestamp=" + oauthTimeStamp + "&oauth_version=" + oauthVersion;
        System.err.println("--------------------------------------------------------------------------");
        System.err.println("Parameter String: " + parameterString);
        System.err.println("Paremeter String encode: " + encode(parameterString));

        signatureBaseString = "GET&" + encode("https://api.twitter.com/1.1/statuses/user_timeline.json") + "&" + encode(parameterString);

        String keyString = oauthConsumerSecret + "&" + oauthTokenSecret;

        try {
            oauthSignature = computeSignature(signatureBaseString, keyString);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.err.println("---------------------------");
        System.err.println("Signature base string= "  + signatureBaseString);

        header = "OAuth oauth_consumer_key=\"" + oauthConsumerKey + "\",oauth_token=\"" + oauthToken + "\",oauth_signature_method=\"" + oauthSignatureMethod + "\",oauth_timestamp=\"" + oauthTimeStamp + "\",oauth_nonce=\"" + oauthNonce + "\",oauth_version=\"" + oauthVersion + "\",oauth_signature=\"" + oauthSignature + "\"";

        System.err.println("------------------------------------------------------------------");
        System.err.println("Header: " + header);

        //return "OAuth oauth_consumer_key=\"zHb326FK2xT666c0olifieF9h\",oauth_token=\"865314952470560768-BZ5AXIzHyRndtxWqcYe8u2rKMIb25cF\",oauth_signature_method=\"HMAC-SHA1\",oauth_timestamp=\"1496679547\",oauth_nonce=\"Jfg5rB\",oauth_version=\"1.0\",oauth_signature=\"NPKa%2BPs8fXeJQaGoHmy389zC%2Fyw%3D\"";
        return header;
    }


    private static String computeSignature(String baseString, String keyString) throws GeneralSecurityException, UnsupportedEncodingException {

        SecretKey secretKey = null;

        byte[] keyBytes = keyString.getBytes();
        secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");

        Mac mac = Mac.getInstance("HmacSHA1");

        mac.init(secretKey);

        byte[] text = baseString.getBytes();

        return new String(org.apache.commons.codec.binary.Base64.encodeBase64(mac.doFinal(text))).trim();
    }

    private String encode(String value) {
        String encoded = "";
        try {
            encoded = URLEncoder.encode(value, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sb = "";
        char focus;
        for (int i = 0; i < encoded.length(); i++) {
            focus = encoded.charAt(i);
            if (focus == '*') {
                sb += "%2A";
            } else if (focus == '+') {
                sb += "%20";
            } else if (focus == '%' && i + 1 < encoded.length()
                    && encoded.charAt(i + 1) == '7' && encoded.charAt(i + 2) == 'E') {
                sb += '~';
                i += 2;
            } else {
                sb += focus;
            }
        }
        return sb.toString();
    }

}
