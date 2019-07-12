package com.example.fbu_parseagram;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fbu_parseagram.model.ParseComment;
import com.example.fbu_parseagram.model.Post;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CommentActivity extends AppCompatActivity {

    public final static String TAG = "CommentActivity";
    public final static int RESULT_CODE = 20;
    public Post post;
    private Button btnComment;
    private Button btnReturn;
    private EditText etComment;
    private ParseUser user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        //Initialize view variables
        btnComment = findViewById(R.id.btnComment);
        etComment = findViewById(R.id.etComment);
        btnReturn = findViewById(R.id.btnReturn);

        post = getIntent().getParcelableExtra("post");

        user = ParseUser.getCurrentUser();

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment(post, user, etComment.getText().toString());
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void comment(final Post post, ParseUser user, String text) {
        ParseComment comment = new ParseComment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setParseComment(text);
        comment.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null) {
                    Log.d(TAG, "Error while saving");
                    e.printStackTrace();
                    return;
                }
                Log.d(TAG, "Success, comment saved");
                finish();
            }
        });
    }
}
