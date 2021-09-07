package com.academia.gorillas.ui.category;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.academia.gorillas.Config;
import com.academia.gorillas.R;
import com.academia.gorillas.adapter.CategoryAdapter;
import com.academia.gorillas.model.Category;
import com.academia.gorillas.service.AppController;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

    private List<Category> mCategoryList;
    private CategoryAdapter categoryAdapter;
    private CircularProgressIndicator progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);


        progressBar = root.findViewById(R.id.progress_circular);
        RecyclerView mRecyclerView = root.findViewById(R.id.recyclerview);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mCategoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getContext(), mCategoryList);
        mRecyclerView.setAdapter(categoryAdapter);

        getCategories();

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


    private void getCategories() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = Config.URL_CATEGORIES + "&parent=" + Config.PARENT_CATEGORY_FILTER;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for(int i = 0 ; i < jsonArray.length() ; i++){
                                JSONObject jsonObjectPost = jsonArray.getJSONObject(i);

                                int id = jsonObjectPost.getInt("id");
                                int count = jsonObjectPost.getInt("count");
                                String name = jsonObjectPost.getString("name");
                                int parent = jsonObjectPost.getInt("parent");

                                Category category = new Category(id, count, name,parent);

                                mCategoryList.add(category);
                            }
                            progressBar.hide();
                            categoryAdapter.notifyDataSetChanged();


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

}