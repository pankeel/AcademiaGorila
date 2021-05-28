package com.academia.gorillas.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.academia.gorillas.R;
import com.academia.gorillas.activity.PostDetailActivity;
import com.academia.gorillas.model.Post;
import com.academia.gorillas.util.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {

    private Context mContext;
    private List<Post> mPostList;

    public PostAdapter(Context mContext, List<Post> mPostList) {
        this.mContext = mContext;
        this.mPostList = mPostList;
    }


    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_post_row_item, parent, false);
        return new PostViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
//        final Ad ad = mAdList.get(position);
//        holder.title.setText(ad.getTitle());
//        if (ad.getImages().size() > 0 ){
//            Picasso.get().load(ad.getImages().get(0)).into(holder.imageView);
//        }
        //holder.excerpt.setText(ad.getCity().getName());
//        holder.price.setText(Util.currencyFormat(ad.getPrice()));
//



        final Post post = mPostList.get(position);

        holder.title.setText(Html.fromHtml(post.getTitle()));
        holder.excerpt.setText(Html.fromHtml(post.getExcerpt()));
        holder.date.setText(Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd MMM yyyy",post.getDate()));
        if(post.getFeaturedMedia() != null){
            Picasso mPicasso = Picasso.get();
            mPicasso.setIndicatorsEnabled(true);
            mPicasso.get().load(post.getFeaturedMedia()).into(holder.imageView);
            mPicasso.load(post.getFeaturedMedia()).into(holder.imageView);
        }else{
            holder.imageView.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //EventBus.getDefault().postSticky(new AdEvent(ad));

                //replaceFragment(new FeaturedAdDetailFragment());

                Intent intent = new Intent(mContext, PostDetailActivity.class);
                intent.putExtra("post", post);
                mContext.startActivity(intent);

            }
        });
    }



    @Override
    public int getItemCount() {
        return mPostList.size();
    }


}

class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public ImageView imageView;
    public TextView excerpt;
    public TextView date;
    PostViewHolder(View itemView) {
        super(itemView);

        title = (TextView)itemView.findViewById(R.id.title);
        imageView = (ImageView) itemView.findViewById(R.id.imagev);
        excerpt = (TextView)itemView.findViewById(R.id.excerpt);
        date = (TextView)itemView.findViewById(R.id.date);
    }
}


