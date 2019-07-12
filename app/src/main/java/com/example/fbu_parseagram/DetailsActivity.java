package com.example.fbu_parseagram;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.fbu_parseagram.model.Like;
import com.example.fbu_parseagram.model.ParseComment;
import com.example.fbu_parseagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    public final static String TAG = "DetailsActivity";
    public final static int REQUEST_CODE = 20;

    private TextView tvHandle;
    private ImageView ivImage;
    private TextView tvDescription;
    private LinearLayout llBody;
    private TextView tvTime;
    private List<Like> mLikes;
    private List<ParseComment> mComments;
    private TextView tvLikes;
    private ImageButton ibLikes;
    private ImageButton ibComment;
    private RecyclerView rvComments;
    private boolean liked;
    private Post post;
    private CommentsAdapter adapter;
    private SwipeRefreshLayout swipeContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        post = getIntent().getParcelableExtra("post");
        mLikes = new ArrayList<>();
        mComments = new ArrayList<>();

        //Setting view objects
        tvHandle = findViewById(R.id.tvHandle);
        ivImage = findViewById(R.id.ivImage);
        tvDescription = findViewById(R.id.tvDescription);
        tvTime = findViewById(R.id.tvTimePosted);
        llBody = findViewById(R.id.llPost);
        tvLikes = findViewById(R.id.tvTotalLikes);
        ibLikes = findViewById(R.id.btnLike);
        ibComment = findViewById(R.id.btnComment);
        rvComments = findViewById(R.id.rvPosts);
        swipeContainer = findViewById(R.id.swipeContainer);

        //Setting up linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        //set layout manager on recycler view
        rvComments.setLayoutManager(linearLayoutManager);
        //setting up adapter
        adapter = new CommentsAdapter(getApplicationContext(), mComments);
        //set adapter on recycler view
        rvComments.setAdapter(adapter);

        llBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailsActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });

        //Like functionality
        queryLikes(post);
        queryComment(post);
        tvLikes.setText(String.valueOf(mLikes.size()));

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                swipeContainer.setRefreshing(false);
                queryComment(post);
            }
        });

        ibLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Liked", Toast.LENGTH_LONG).show();
                like(post);
            }
        });

        ibComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailsActivity.this, CommentActivity.class);
                i.putExtra("post", post);
                startActivityForResult(i, REQUEST_CODE);
            }
        });

        tvHandle.setText(post.getUser().getUsername());
        Glide.with(this).load(post.getImage().getUrl()).into(ivImage);
        tvDescription.setText(post.getDescription());
        tvTime.setText(post.getCreatedAt().toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        queryComment(post);
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

    public void queryComment (Post post){
        ParseQuery<ParseComment> postQuery = new ParseQuery<ParseComment>(ParseComment.class);
        postQuery.whereEqualTo(Like.KEY_POST, post);

        postQuery.include(ParseComment.KEY_USER);
        postQuery.setLimit(20);
        postQuery.addDescendingOrder(ParseComment.KEY_CREATED_AT);

        postQuery.findInBackground(new FindCallback<ParseComment>() {
            @Override
            public void done(List<ParseComment> comments, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error with Querey");
                    e.printStackTrace();
                    return;
                }
                mComments.clear();
                mComments.addAll(comments);
                adapter.notifyDataSetChanged();
                tvLikes.setText(String.valueOf(mLikes.size()));
            }
        });
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
