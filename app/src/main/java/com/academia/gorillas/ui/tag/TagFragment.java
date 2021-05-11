package com.academia.gorillas.ui.tag;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.academia.gorillas.Config;
import com.academia.gorillas.R;
import com.academia.gorillas.activity.TagPostsActivity;
import com.academia.gorillas.model.Tag;
import com.academia.gorillas.model.Tags;
import com.academia.gorillas.service.AppController;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TagFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TagFragment extends Fragment {

    private ChipGroup chipGroup;
    private List<Tag> mTagList;
    private List<Tag> selectedTags;
    private CircularProgressIndicator progressBar;

    public TagFragment() {
        // Required empty public constructor
    }


    public static TagFragment newInstance(String param1, String param2) {
        TagFragment fragment = new TagFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_tag, container, false);
        setHasOptionsMenu(true);
        chipGroup = root.findViewById(R.id.chipGroup);
        progressBar = root.findViewById(R.id.progress_circular);
        mTagList = new ArrayList<>();
        selectedTags = new ArrayList<>();
        getTags();
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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


        inflater.inflate(R.menu.tags_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        }else if(item.getItemId() == R.id.action_next) {

            if(selectedTags.size() == 0){
                new AlertDialog.Builder(getActivity())
                        .setTitle("Selected Tags Empty")
                        .setMessage("Please select tags")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                            }
                        })

                        .show();
            }else{
                Intent intent = new Intent(getActivity(), TagPostsActivity.class);
                intent.putExtra("tags", new Tags(selectedTags));
                startActivity(intent);
            }

            Log.d("selected_tags", String.valueOf(selectedTags.size()));

            return true;

        }
        return super.onOptionsItemSelected(item);
    }


    private void getTags() {
        String url = Config.URL_TAGS ;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for(int i = 0 ; i < jsonArray.length() ; i++){
                                JSONObject jsonObjectPost = jsonArray.getJSONObject(i);

                                int id = jsonObjectPost.getInt("id");
                                String name = jsonObjectPost.getString("name");

                                Tag tag = new Tag(id, name);

                                mTagList.add(tag);
                            }
                            progressBar.hide();

                            updateTags();

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


    private void updateTags() {
        int index = 0;
        for (Tag tag: mTagList) {


            Chip chip = new Chip(mContext);
            chip.setId(tag.getId());
            chip.setTag(index);
            chip.setTextAppearance(R.style.TextAppearance_AppCompat_Menu);
            chip.setText(tag.getName());
            chip.setCheckable(true);
            chip.setCheckedIconVisible(false);
            chip.setChipBackgroundColorResource(R.color.chip_bg_states);


            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    int tag = (int) compoundButton.getTag();
                    if (b){
                        selectedTags.add(mTagList.get(tag));
                    }else{
                        selectedTags.remove(mTagList.get(tag));
                    }

                }
            });
            chipGroup.addView(chip);

            index = index + 1;

        }

        chipGroup.invalidate();
    }
}