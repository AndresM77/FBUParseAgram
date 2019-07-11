package com.example.fbu_parseagram;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fbu_parseagram.model.Post;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;


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
        private TextView tvDescription;
        private TextView tvTime;
        private LinearLayout llBody;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            tvHandle = itemView.findViewById(R.id.tvHandle);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTime = itemView.findViewById(R.id.tvTimePosted);
            llBody = itemView.findViewById(R.id.llPost);
            ivProfile = itemView.findViewById(R.id.ivProfile);
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
        }
    }
}
