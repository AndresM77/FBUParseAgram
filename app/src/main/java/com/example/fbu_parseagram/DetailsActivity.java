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

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.fbu_parseagram.model.Like;
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

    private TextView tvHandle;
    private ImageView ivImage;
    private TextView tvDescription;
    private LinearLayout llBody;
    private TextView tvTime;
    private List<Like> mLikes;
    private TextView tvLikes;
    private ImageButton ibLikes;
    private ImageButton ibComment;
    private boolean liked;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final Post post = getIntent().getParcelableExtra("post");
        mLikes = new ArrayList<>();

        tvHandle = findViewById(R.id.tvHandle);
        ivImage = findViewById(R.id.ivImage);
        tvDescription = findViewById(R.id.tvDescription);
        tvTime = findViewById(R.id.tvTimePosted);
        llBody = findViewById(R.id.llPost);
        tvLikes = findViewById(R.id.tvTotalLikes);
        ibLikes = findViewById(R.id.btnLike);
        ibComment = findViewById(R.id.btnComment);

        llBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailsActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });

        //Like functionality
        queryLikes(post);
        tvLikes.setText(String.valueOf(mLikes.size()));

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

            }
        });

        tvHandle.setText(post.getUser().getUsername());
        Glide.with(this).load(post.getImage().getUrl()).into(ivImage);
        tvDescription.setText(post.getDescription());
        tvTime.setText(post.getCreatedAt().toString());
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
