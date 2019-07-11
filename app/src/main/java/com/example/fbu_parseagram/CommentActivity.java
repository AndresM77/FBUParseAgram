package com.example.fbu_parseagram;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fbu_parseagram.model.ParseComment;
import com.example.fbu_parseagram.model.Post;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CommentActivity extends AppCompatActivity {

    public final static String TAG = "CommentActivity";
    public Post post;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_comment);
        post = getIntent().getParcelableExtra("post");

    }

    public void comment(final Post post) {
        ParseComment comment = new ParseComment();
        comment.setUser(ParseUser.getCurrentUser());
        comment.setPost(post);
        comment.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null) {
                    Log.d(TAG, "Error while saving");
                    e.printStackTrace();
                    return;
                }
                Log.d(TAG, "Success, comment saved");
            }
        });
    }
}
