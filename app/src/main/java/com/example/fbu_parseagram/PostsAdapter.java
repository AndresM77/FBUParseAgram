package com.example.fbu_parseagram;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fbu_parseagram.model.Like;
import com.example.fbu_parseagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;
    public final static String TAG = "PostsAdapter";

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvHandle;
        private ImageView ivProfile;
        private ImageView ivImage;
        private ImageButton ibLikes;
        private ImageButton ibComment;
        private TextView tvDescription;
        private TextView tvTime;
        private TextView tvLikes;
        private LinearLayout llBody;
        private List<Like> mLikes;
        private boolean liked;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            tvHandle = itemView.findViewById(R.id.tvHandle);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTime = itemView.findViewById(R.id.tvTimePosted);
            tvLikes = itemView.findViewById(R.id.tvTotalLikes);
            llBody = itemView.findViewById(R.id.llPost);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            ibLikes = itemView.findViewById(R.id.btnLike);
            ibComment = itemView.findViewById(R.id.btnComment);

            mLikes = new ArrayList<>();

        }

        public void bind (final Post post) {
            //bind view elements to post
            tvHandle.setText(post.getUser().getUsername());
//            ParseFile image = post.getImage();
//            if(image != null) {
//                Glide.with(context).load(image.getUrl()).into(ivImage);
//            }
            Glide.with(context).load(post.getImage().getUrl()).into(ivImage);
            //Make post clickable
            llBody.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, DetailsActivity.class);
                    i.putExtra("post", post);
                    context.startActivity(i);
                }
            });

            tvDescription.setText(post.getDescription());
            tvTime.setText(post.getCreatedAt().toString());

            ParseFile image = (ParseFile) ParseUser.getCurrentUser().get("proImage");
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivProfile);
            }

            ibLikes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    like(post);
                }
            });

            ibComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            queryLikes(post);
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
}
