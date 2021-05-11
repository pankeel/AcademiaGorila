package com.academia.gorillas.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.academia.gorillas.R;
import com.academia.gorillas.model.Comment;
import com.academia.gorillas.util.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {

    private Context mContext;
    private List<Comment> mList;

    public CommentAdapter(Context mContext, List<Comment> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_comment_row_item, parent, false);
        return new CommentViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {

        final Comment comment = mList.get(position);

        holder.name.setText(Html.fromHtml(comment.getAuthorName()));
        holder.comment.setText(Html.fromHtml(comment.getContent()));
        holder.date.setText(Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd MMM yyyy",comment.getDate()));
        if(comment.getAvatarUrl() != null){
            Picasso.get().load(comment.getAvatarUrl()).into(holder.imageView);
        }else{
            holder.imageView.setVisibility(View.GONE);
        }


    }



    @Override
    public int getItemCount() {
        return mList.size();
    }
}

class CommentViewHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public ImageView imageView;
    public TextView comment;
    public TextView date;
    CommentViewHolder(View itemView) {
        super(itemView);

        name = (TextView)itemView.findViewById(R.id.name);
        imageView = (ImageView) itemView.findViewById(R.id.imagev);
        comment = (TextView)itemView.findViewById(R.id.comment);
        date = (TextView)itemView.findViewById(R.id.date);
    }
}
