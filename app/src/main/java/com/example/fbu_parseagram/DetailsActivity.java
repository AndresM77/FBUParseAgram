package com.example.fbu_parseagram;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.fbu_parseagram.model.Post;

public class DetailsActivity extends AppCompatActivity {

    private TextView tvHandle;
    private ImageView ivImage;
    private TextView tvDescription;
    private LinearLayout llBody;
    private TextView tvTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Post post = getIntent().getParcelableExtra("post");

        tvHandle = findViewById(R.id.tvHandle);
        ivImage = findViewById(R.id.ivImage);
        tvDescription = findViewById(R.id.tvDescription);
        tvTime = findViewById(R.id.tvTimePosted);
        llBody = findViewById(R.id.llPost);

        llBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailsActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });

        tvHandle.setText(post.getUser().getUsername());
        Glide.with(this).load(post.getImage().getUrl()).into(ivImage);
        tvDescription.setText(post.getDescription());
        tvTime.setText(post.getCreatedAt().toString());
    }

}
