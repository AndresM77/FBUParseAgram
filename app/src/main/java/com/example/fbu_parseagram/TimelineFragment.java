package com.example.fbu_parseagram;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.fbu_parseagram.model.Like;
import com.example.fbu_parseagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimelineFragment extends Fragment {

    private RecyclerView rvPosts;
    public final static String TAG = "TimelineFragment";
    protected PostsAdapter adapter;
    protected List<Post> mPosts;
    private SwipeRefreshLayout swipeContainer;
    // Store a member variable for the listener
    private EndlessRecyclerViewScrollListener scrollListener;


    public static TimelineFragment newInstance() {
        return new TimelineFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        //Setting view objects
        rvPosts = view.findViewById(R.id.rvPosts);
        //Instantiate posts list
        mPosts = new ArrayList<>();
        //set adapter on recycler view
        adapter = new PostsAdapter(getContext(), mPosts);
        //set adapter on recycler view
        rvPosts.setAdapter(adapter);
        //Setting up linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        //set layout manager on recycler view
        rvPosts.setLayoutManager(linearLayoutManager);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                swipeContainer.setRefreshing(false);
                queryPosts(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        //Setting up scroll listener
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };

        queryPosts(false);
    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int offset) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
        queryPosts(true);
    }


    public void queryPosts(boolean EndlessScrolling) {
        ParseQuery<Post> postQuery = new ParseQuery<Post>(Post.class);
        postQuery.include(Post.KEY_USER);
        postQuery.setLimit(20);
        postQuery.addDescendingOrder(Post.KEY_CREATED_AT);
        Date maxDate;
        //Endless Pagination Functionality
        if (EndlessScrolling) {
            maxDate = mPosts.get(mPosts.size() - 1).getCreatedAt();
            postQuery.whereLessThan(Post.KEY_CREATED_AT, maxDate);
        }

        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error with Querey");
                    e.printStackTrace();
                    return;
                }
                mPosts.clear();
                mPosts.addAll(posts);
                adapter.notifyDataSetChanged();
                for (int i = 0; i < posts.size(); i++) {
                    Post post = posts.get(i);
                    Log.d(TAG, "Post" + post.getDescription() + ", username: " + post.getUser().getUsername());
                }
            }
        });
    }

    public void like(final Post post) {
        if(!liked) {
            Like like = new Like();
            like.setUser(ParseUser.getCurrentUser());
            like.setPost(post);
            like.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e!=null) {
                        Log.d(TAG, "Error while saving");
                        e.printStackTrace();
                        return;
                    }
                    queryLikes(post);
                    Log.d(TAG, "Success, like saved");
                    ibLikes.setImageResource(R.drawable.ufi_heart_active);
                }
            });
        }else {
            for (int i = 0; i < mLikes.size(); i++) {
                if(mLikes.get(i).getUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                    ibLikes.setImageResource(R.drawable.ufi_heart);
                    mLikes.get(i).deleteInBackground();
                    queryLikes(post);
                }
            }
        }
    }

    public void queryLikes(Post post) {
        ParseQuery<Like> postQuery = new ParseQuery<Like>(Like.class);
        postQuery.whereEqualTo(Like.KEY_POST, post);

        postQuery.findInBackground(new FindCallback<Like>() {
            @Override
            public void done(List<Like> likes, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error with Querey");
                    e.printStackTrace();
                    return;
                }
                mLikes.clear();
                mLikes.addAll(likes);
                tvLikes.setText(String.valueOf(mLikes.size()));
                for (int i = 0; i < mLikes.size(); i++) {
                    if (mLikes.get(i).getUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                        liked = true;
                        ibLikes.setImageResource(R.drawable.ufi_heart_active);
                        return;
                    }
                }
                ibLikes.setImageResource(R.drawable.ufi_heart);
                liked = false;
            }
        });
    }



}
