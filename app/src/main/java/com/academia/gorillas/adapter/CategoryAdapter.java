package com.academia.gorillas.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.academia.gorillas.Config;
import com.academia.gorillas.R;
import com.academia.gorillas.activity.CategoryPostsActivity;
import com.academia.gorillas.activity.SubCategoryActivity;
import com.academia.gorillas.model.Category;
import com.academia.gorillas.service.AppController;
import com.academia.gorillas.ui.category.CategoryFragment;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        //holder.numbers.setText(String.valueOf(category.getCount()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //EventBus.getDefault().postSticky(new AdEvent(ad));

                //replaceFragment(new CategoryFragment());
                String url = Config.URL_CATEGORIES + "&parent=" + category.getId();
                System.out.println(url);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println(response);
                                try {
                                    System.out.println("url1");
                                    JSONArray jsonArray = new JSONArray(response);
                                    System.out.println("url2");
                                    if(jsonArray.length() == 0)
                                    {
                                        Intent intent = new Intent(mContext, CategoryPostsActivity.class);
                                        intent.putExtra("category", category);
                                        mContext.startActivity(intent);
                                    }
                                    else
                                    {
                                        Intent intent = new Intent(mContext, SubCategoryActivity.class);
                                        intent.putExtra("category", category);
                                        mContext.startActivity(intent);
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.d("response",response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("url3");
                        Log.e("error",error.toString());
                        // mTextView.setText("That didn't work!");
                    }
                });
                AppController.getInstance().addToRequestQueue(stringRequest);

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
   // public TextView numbers;
    CategoryViewHolder(View itemView) {
        super(itemView);

        title = (TextView)itemView.findViewById(R.id.title);
        //numbers = (TextView)itemView.findViewById(R.id.numbers);
    }
}
