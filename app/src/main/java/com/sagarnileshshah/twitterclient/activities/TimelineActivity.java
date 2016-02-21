package com.sagarnileshshah.twitterclient.activities;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.codepath.apps.twitterclient.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;
import com.sagarnileshshah.twitterclient.TwitterApplication;
import com.sagarnileshshah.twitterclient.adapters.TweetsRecyclerViewAdapter;
import com.sagarnileshshah.twitterclient.clients.TwitterClient;
import com.sagarnileshshah.twitterclient.decorations.DividerItemDecoration;
import com.sagarnileshshah.twitterclient.fragments.ComposeFragment;
import com.sagarnileshshah.twitterclient.listeners.EndlessRecyclerViewScrollListener;
import com.sagarnileshshah.twitterclient.models.Entities___;
import com.sagarnileshshah.twitterclient.models.ExtendedEntities_;
import com.sagarnileshshah.twitterclient.models.Medium______;
import com.sagarnileshshah.twitterclient.models.Tweet;
import com.sagarnileshshah.twitterclient.models.Url_____;
import com.sagarnileshshah.twitterclient.models.User;
import com.sagarnileshshah.twitterclient.models.Variant;
import com.sagarnileshshah.twitterclient.models.VideoInfo;
import com.sagarnileshshah.twitterclient.utils.Utils;

import org.apache.http.Header;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TimelineActivity extends AppCompatActivity implements ComposeFragment.OnFragmentInteractionListener {

    @Bind(R.id.rvTweets)
    RecyclerView rvTweets;

    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    private static TwitterClient mTwitterClient;
    private static TweetsRecyclerViewAdapter mTweetsRecyclerViewAdapter;
    private static ArrayList<Tweet> mTweets;

    private static long mTwitterMaxId;
    private long mTwitterSinceId;

    private static HashSet<Long> mTweetIdsHashSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.drawable.ic_twitter_bird);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                renderComposeFragment();
            }
        });


        mTwitterClient = TwitterApplication.getRestClient();

        mTweets = new ArrayList<>();
        mTweetsRecyclerViewAdapter = new TweetsRecyclerViewAdapter(this, mTweets);
        rvTweets.setAdapter(mTweetsRecyclerViewAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(linearLayoutManager);

        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore() {
                getOldTweets();
            }
        });

        rvTweets.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNewTweets();
            }
        });

        swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary);


        mTwitterMaxId = 0;
        mTwitterSinceId = 1;

        mTweetIdsHashSet = new HashSet<>();

        if (!Utils.isNetworkAvailable(this) || !Utils.isOnline(this)) {
            loadTweetsFromDB();
        } else {
            getNewTweets();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_logout:
                mTwitterClient.clearAccessToken();
                saveTweetsToDB(mTweets);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_compose:
                renderComposeFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getNewTweets() {
        boolean requestSent = mTwitterClient.getNewTweets(this, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {

                loadTweets(response, 0);
                if (mTweets.size() > 0) {
                    mTwitterSinceId = mTweets.get(0).getRemoteId();
                    saveTweetsToDB(mTweets);
                }

                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(this.toString(), String.valueOf(statusCode));
                swipeContainer.setRefreshing(false);
            }
        }, mTwitterSinceId);

        if (!requestSent) {
            swipeContainer.setRefreshing(false);
        }

    }

    private void getOldTweets() {
        mTwitterClient.getOldTweets(this, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {

                loadTweets(response, mTweets.size());
                if (mTweets.size() > 0) {
                    mTwitterMaxId = mTweets.get(mTweets.size() - 1).getRemoteId();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(this.toString(), String.valueOf(statusCode));
            }
        }, mTwitterMaxId);

    }

    public static void loadTweets(String response, int positionStart) {
        Type collectionType = new TypeToken<ArrayList<Tweet>>() {
        }.getType();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat(Tweet.DATE_FORMAT);
        Gson gson = gsonBuilder.create();
        ArrayList<Tweet> tweets = gson.fromJson(response, collectionType);
        Iterator<Tweet> iterator = tweets.iterator();
        while (iterator.hasNext()) {
            Tweet tweet = iterator.next();
            if (mTweetIdsHashSet.contains(tweet.getRemoteId())) {
                iterator.remove();
            } else {
                mTweetIdsHashSet.add(tweet.getRemoteId());
            }
        }
        if (mTweets.size() == 0 & tweets.size() > 0) {
            mTwitterMaxId = tweets.get(tweets.size() - 1).getRemoteId();
        }
        mTweets.addAll(positionStart, tweets);
        mTweetsRecyclerViewAdapter.notifyItemRangeInserted(positionStart, tweets.size());
    }

    public void saveTweetsToDB(List<Tweet> tweets) {
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            String dbName = bundle.getString("AA_DB_NAME");
            ActiveAndroid.dispose();
            this.deleteDatabase(dbName);
            Configuration dbConfiguration = new Configuration.Builder(this).setDatabaseName(dbName).create();
            ActiveAndroid.initialize(dbConfiguration);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        for (Tweet tweet : tweets) {
            tweet.save();
            User user = tweet.getUser();
            user.tweet = tweet;
            user.save();

            Entities___ entities = tweet.getEntities();
            entities.tweet = tweet;
            entities.save();

            for (Url_____ url : entities.getUrls()) {
                url.entities = entities;
                url.save();
            }

            ExtendedEntities_ extendedEntities = tweet.getExtendedEntities();
            if (extendedEntities != null) {
                extendedEntities.tweet = tweet;
                extendedEntities.save();

                for (Medium______ medium : extendedEntities.getMedia()) {
                    medium.extendedentities = extendedEntities;
                    medium.save();

                    VideoInfo videoInfo = medium.getVideoInfo();
                    if (videoInfo != null) {
                        videoInfo.medium = medium;
                        videoInfo.save();

                        for (Variant variant : videoInfo.getVariants()) {
                            variant.videoInfo = videoInfo;
                            variant.save();
                        }
                    }
                }
            }


        }
    }

    public void loadTweetsFromDB() {
        List<Tweet> tweets = Tweet.getAllTweetsFromDB();
        for (Tweet tweet : tweets) {
            tweet.populateUserFromDB();
            tweet.populateEntitiesFromDB();
            tweet.populateExtendedEntitiesFromDB();
        }
        mTweets.addAll(tweets);
        mTweetsRecyclerViewAdapter.notifyItemRangeInserted(0, tweets.size());
    }

    public void loadOneTweetFromDB() {
        Tweet tweet = Tweet.getOneTweetFromDB();
        tweet.populateUserFromDB();
        tweet.populateEntitiesFromDB();
        tweet.populateExtendedEntitiesFromDB();
        mTweets.add(tweet);
        mTweetsRecyclerViewAdapter.notifyItemInserted(0);
    }

    public void renderComposeFragment() {
        FragmentManager fm = getSupportFragmentManager();
        ComposeFragment composeFragment = ComposeFragment.newInstance();
        composeFragment.show(fm, "compose");
    }

    @Override
    public void postMessage(long id, String message) {
        mTwitterClient.postMessage(this, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                Toast.makeText(TimelineActivity.this, "Tweet sent Successfully!", Toast.LENGTH_LONG).show();
                response = "[" + response + "]";
                loadTweets(response, 0);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(TimelineActivity.this, "Sorry, Tweet wasn't sent. Please try again.", Toast.LENGTH_LONG).show();
            }
        }, id, message);
    }
}
