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

import com.example.fbu_parseagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class TimelineFragment extends Fragment {

    private RecyclerView rvPosts;
    public final static String TAG = "TimelineFragment";
    protected PostsAdapter adapter;
    protected List<Post> mPosts;
    private SwipeRefreshLayout swipeContainer;


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
        //set layout manager on recycler view
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));

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
                queryPosts();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        queryPosts();
    }


    public void queryPosts() {
        ParseQuery<Post> postQuery = new ParseQuery<Post>(Post.class);
        postQuery.include(Post.KEY_USER);
        postQuery.setLimit(20);
        postQuery.addDescendingOrder(Post.KEY_CREATED_AT);
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



}
