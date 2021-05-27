package com.academia.gorillas.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.academia.gorillas.Config;
import com.academia.gorillas.R;
import com.academia.gorillas.adapter.PostAdapter;
import com.academia.gorillas.model.Post;
import com.academia.gorillas.model.Tag;
import com.academia.gorillas.model.Tags;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TagPostsActivity extends AppCompatActivity {

    private CircularProgressIndicator progressBar;
    private RecyclerView mRecyclerView;
    private List<Post> mPostList;
    private PostAdapter postAdapter;
    private int page = 1;
    private boolean isLoading = false;

    private Tags tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_posts);
        tags = (Tags) getIntent().getSerializableExtra("tags");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Selected Tags");

        progressBar = findViewById(R.id.progress_circular);


        mRecyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mPostList = new ArrayList<>();
        postAdapter = new PostAdapter(this, mPostList);
        mRecyclerView.setAdapter(postAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        NestedScrollView scroll = (NestedScrollView) findViewById(R.id.nestedScrollView);
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


        getPosts();
    }

    private String getTags(){
        String selectedTags = "";
        for (Tag tag : tags.getTags()) {
            selectedTags = selectedTags + "&tags[]=" + String.valueOf(tag.getId());
        }
        return selectedTags;
    }

    private void getPosts() {
        isLoading = true;
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Config.URL_TAG_POST + getTags() + "&page=" + String.valueOf(page);

// Sağlanan URL’den bir dize yanıtı istenmektedir.
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

//isteği RequestQueue ekliyoruz.
        queue.add(stringRequest);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return true;
    }
}