package com.academia.gorillas.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.academia.gorillas.Config;
import com.academia.gorillas.R;
import com.academia.gorillas.adapter.CommentAdapter;
import com.academia.gorillas.model.Comment;
import com.academia.gorillas.model.Post;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentsActivity extends AppCompatActivity {


    private Post post;
    private List<Comment> mList;
    private CommentAdapter commentAdapter;
    private View addComment;
    private Button sendComment;
    private TextInputEditText name, email, comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");

         post = (Post) getIntent().getSerializableExtra("post");
        addComment = findViewById(R.id.addComment);
        addComment.setVisibility(View.GONE);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        comment = findViewById(R.id.comment);
        sendComment = findViewById(R.id.sendComment);
        RecyclerView mRecyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, mList);
        mRecyclerView.setAdapter(commentAdapter);

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment.setVisibility(View.GONE);
                sendComment();
            }
        });
        getComments();
    }

    private void getComments() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Config.URL_POST_COMMENTS + String.valueOf(post.getId());

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
                                String author_name = jsonObjectPost.getString("author_name");
                                String content = jsonObjectPost.getJSONObject("content").getString("rendered");
                                String date = jsonObjectPost.getString("date");
                                String author_avatar_url = jsonObjectPost.getJSONObject("author_avatar_urls").getString("96");

                                Comment comment = new Comment(id,author_name,content,date,author_avatar_url);

                                mList.add(comment);
                            }

                            commentAdapter.notifyDataSetChanged();


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

    private void sendComment(){


//        try {
//            RequestQueue requestQueue = Volley.newRequestQueue(this);
//            String URL =Config.URL_COMMENTS + "?post=" + String.valueOf(post.getId()) + "&author_name="
//                + name.getText().toString().trim().replace(" ", "%20") + "&author_email="
//                + email.getText().toString().trim() + "&content=" + comment.getText().toString().trim();
//            JSONObject jsonBody = new JSONObject();
//            jsonBody.put("post", post.getId());
//            jsonBody.put("author_name", name.getText().toString().trim().replace(" ", "%20"));
//            jsonBody.put("author_email", email.getText().toString().trim());
//            jsonBody.put("content", comment.getText().toString().trim());
//
//
//            final String requestBody = jsonBody.toString();
//
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    Log.i("VOLLEY", response);
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.e("VOLLEY", error.toString());
//                }
//            }) {
//                @Override
//                public String getBodyContentType() {
//                    return "application/json; charset=utf-8";
//                }
//
//                @Override
//                public byte[] getBody() throws AuthFailureError {
//                    try {
//                        return requestBody == null ? null : requestBody.getBytes("utf-8");
//                    } catch (UnsupportedEncodingException uee) {
//                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
//                        return null;
//                    }
//                }
//
//                @Override
//                protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                    String responseString = "";
//                    if (response != null) {
//                        responseString = String.valueOf(response.statusCode);
//                        // can get more details such as response.headers
//                    }
//                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
//                }
//
//                @Override
//                public Map<String,String> getHeaders() throws AuthFailureError {
//                    Map<String,String> params = new HashMap<String,String>();
//                    params.put("content-type","application/fesf");
//                    return params;
//                }
//
//                            @Override
//            protected Map<String,String> getParams(){
//                Map<String,String> params = new HashMap<String, String>();
//                params.put("post", String.valueOf(post.getId()));
//                params.put("author_name",name.getText().toString().trim().replace(" ", "%20"));
//                params.put("author_email",email.getText().toString().trim());
//                params.put("content",comment.getText().toString().trim());
//                return params;
//            }
//
//            };
//
//            requestQueue.add(stringRequest);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject params = new JSONObject();

        try {
            params.put("post", String.valueOf(post.getId()));
            params.put("author_name", name.getText().toString().trim().replace(" ", "%20"));
            params.put("author_email", email.getText().toString().trim() );
            params.put("content", comment.getText().toString().trim() );
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Config.URL_COMMENTS , params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", response.toString());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

// Adding request to request queue
        queue.add(jsonObjReq);




//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url = Config.URL_COMMENTS ;
//
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        Log.d("response",response);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                        // As of f605da3 the following should work
//                        NetworkResponse response = error.networkResponse;
//                        if (error instanceof ServerError && response != null) {
//                            try {
//                                String res = new String(response.data,
//                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
//                                // Now you can use any deserializer to make sense of data
//                                JSONObject obj = new JSONObject(res);
//                            } catch (UnsupportedEncodingException e1) {
//                                // Couldn't properly decode data to string
//                                e1.printStackTrace();
//                            } catch (JSONException e2) {
//                                // returned data is not JSONObject?
//                                e2.printStackTrace();
//                            }
//                        }
//                    }
//                }){
//            @Override
//            protected Map<String,String> getParams(){
//                Map<String,String> params = new HashMap<String, String>();
//                params.put("post", String.valueOf(post.getId()));
//                params.put("author_name",name.getText().toString().trim().replace(" ", "%20"));
//                params.put("author_email",email.getText().toString().trim());
//                params.put("content",comment.getText().toString().trim());
//                return params;
//            }
//
//
//
//
//
//        };
//
//        queue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.comments_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return false;
            case R.id.action_comment:
                addComment.setVisibility(View.VISIBLE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return true;
    }
}