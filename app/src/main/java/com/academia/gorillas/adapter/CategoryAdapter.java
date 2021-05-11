package com.academia.gorillas.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.academia.gorillas.R;
import com.academia.gorillas.activity.CategoryPostsActivity;
import com.academia.gorillas.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    private Context mContext;
    private List<Category> mList;

    public CategoryAdapter(Context mContext, List<Category> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_category_row_item, parent, false);
        return new CategoryViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {


        Category category = mList.get(position);
        holder.title.setText(category.getName());
        holder.numbers.setText(String.valueOf(category.getCount()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //EventBus.getDefault().postSticky(new AdEvent(ad));

                //replaceFragment(new FeaturedAdDetailFragment());

                Intent intent = new Intent(mContext, CategoryPostsActivity.class);
                intent.putExtra("category", category);
                mContext.startActivity(intent);

            }
        });
//        holder.detail.setText(category.getContent());
//
//        if(category.getImage() == null){
//            holder.image.setVisibility(View.GONE);
//        }else{
//            int resID = mContext.getResources().getIdentifier(category.getImage() , "drawable", mContext.getPackageName());
//            holder.image.setImageResource(resID);
//        }
//
//
//        LinkAdapter linkAdapter = new LinkAdapter(mContext,category.getLinks());
//        holder.listView.setAdapter(linkAdapter);
//        holder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Link link = category.getLinks().get(position);
//                if(link.getLink() == null){
//                    Intent intent = new Intent(mContext, SubcategoryActivity.class);
//                    intent.putExtra("link", link);
//                    mContext.startActivity(intent);
//                }else{
//                    Intent intent = new Intent(mContext, WebViewActivity.class);
//                    intent.putExtra("link", link);
//                    mContext.startActivity(intent);
//                }
//            }
//        });
//        Utils.setListViewHeightBasedOnChildren(holder.listView);
    }



    @Override
    public int getItemCount() {
        return mList.size();
    }
}

class CategoryViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView numbers;
    CategoryViewHolder(View itemView) {
        super(itemView);

        title = (TextView)itemView.findViewById(R.id.title);
        numbers = (TextView)itemView.findViewById(R.id.numbers);
    }
}
