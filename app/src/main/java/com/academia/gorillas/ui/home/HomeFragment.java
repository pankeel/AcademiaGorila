package com.academia.gorillas.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.academia.gorillas.Config;
import com.academia.gorillas.R;
import com.academia.gorillas.adapter.HeaderAdapter;
import com.academia.gorillas.adapter.PostAdapter;
import com.academia.gorillas.model.Post;
import com.academia.gorillas.service.AppController;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HeaderAdapter headerAdapter;
    private PostAdapter postAdapter;
    private List<Post> mHeaderPostList;
    private List<Post> mPostList;
    private CircularProgressIndicator progressBar;
    private TextView headlineView, latestView;
    private RecyclerView mRecyclerView;
    private int page = 1;
    private boolean isLoading = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        progressBar = root.findViewById(R.id.progress_circular);
        headlineView = root.findViewById(R.id.headlineView);
        latestView = root.findViewById(R.id.latestView);
        headlineView.setVisibility(View.GONE);
        latestView.setVisibility(View.GONE);
        // set up the RecyclerView
        RecyclerView recyclerView = root.findViewById(R.id.recyclerviewHeader);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        mHeaderPostList = new ArrayList<>();
        headerAdapter = new HeaderAdapter(getContext(),mHeaderPostList);
        recyclerView.setAdapter(headerAdapter);


        mRecyclerView = root.findViewById(R.id.recyclerview);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mPostList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), mPostList);
        mRecyclerView.setAdapter(postAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        NestedScrollView scroll = (NestedScrollView) root.findViewById(R.id.nestedScrollView);
        scroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = (View) scroll.getChildAt(scroll.getChildCount() - 1);

                int diff = (view.getBottom() - (scroll.getHeight() + scroll
                        .getScrollY()));

                if (diff == 0) {
                    Log.d("last","last");
                    if(mPostList.size() > 0){
                        page = page + 1;
                        getPosts();
                    }
                }
            }
        });


        getHeaderPosts();
        getPosts();


        return root;
    }

    // Declare Context variable at class level in Fragment
    private Context mContext;

    // Initialise it from onAttach()
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }


    private void getHeaderPosts() {
        String url = Config.URL_HEADLINE_POSTS ;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for(int i = 0 ; i < jsonArray.length() ; i++){
                                JSONObject jsonObjectPost = jsonArray.getJSONObject(i);

                                int id = jsonObjectPost.getInt("id");
                                String title = jsonObjectPost.getJSONObject("title").getString("rendered");
                                String excerpt = jsonObjectPost.getJSONObject("excerpt").getString("rendered");
                                String content = jsonObjectPost.getJSONObject("content").getString("rendered");
                                String date = jsonObjectPost.getString("date");
                                String link = jsonObjectPost.getString("link");
                                String featuredMedia = null;
                                if(!jsonObjectPost.getJSONObject("_embedded").isNull("wp:featuredmedia"))
                                {
                                    if(!jsonObjectPost.getJSONObject("_embedded").getJSONArray("wp:featuredmedia").getJSONObject(0).isNull("source_url")) {
                                        featuredMedia = jsonObjectPost.getJSONObject("_embedded").getJSONArray("wp:featuredmedia").getJSONObject(0).getString("source_url");
                                    }
                                }

                                Post post = new Post(id, title,excerpt,content,featuredMedia,date,link);
                                mHeaderPostList.add(post);
                            }
                            headlineView.setVisibility(View.VISIBLE);
                            progressBar.hide();
                            headerAdapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("response",response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error",error.toString());
                // mTextView.setText("That didn't work!");
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void getPosts() {
        isLoading = true;
        String url = Config.URL_POSTS + "&page=" + String.valueOf(page);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for(int i = 0 ; i < jsonArray.length() ; i++){
                                JSONObject jsonObjectPost = jsonArray.getJSONObject(i);

                                int id = jsonObjectPost.getInt("id");
                                String title = jsonObjectPost.getJSONObject("title").getString("rendered");
                                String excerpt = jsonObjectPost.getJSONObject("excerpt").getString("rendered");
                                String content = jsonObjectPost.getJSONObject("content").getString("rendered");
                                String date = jsonObjectPost.getString("date");
                                String link = jsonObjectPost.getString("link");
                                String featuredMedia = null;
                                if(!jsonObjectPost.getJSONObject("_embedded").isNull("wp:featuredmedia"))
                                {
                                    if(!jsonObjectPost.getJSONObject("_embedded").getJSONArray("wp:featuredmedia").getJSONObject(0).isNull("source_url")) {
                                        featuredMedia = jsonObjectPost.getJSONObject("_embedded").getJSONArray("wp:featuredmedia").getJSONObject(0).getString("source_url");
                                    }
                                }

                                Post post = new Post(id, title,excerpt,content,featuredMedia,date,link);
                                mPostList.add(post);
                            }
                            latestView.setVisibility(View.VISIBLE);
                            progressBar.hide();
                            postAdapter.notifyDataSetChanged();
                            isLoading = false;


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("response",response);
                        // Yanıt dizesinin ilk 500 karakterini görüntüleniyor.
                        //mTextView.setText("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error",error.toString());
                // mTextView.setText("That didn't work!");
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}