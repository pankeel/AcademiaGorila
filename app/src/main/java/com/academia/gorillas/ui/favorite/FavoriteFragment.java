package com.academia.gorillas.ui.favorite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.academia.gorillas.R;
import com.academia.gorillas.adapter.PostAdapter;
import com.academia.gorillas.helper.DatabaseHelper;

public class FavoriteFragment extends Fragment {


    private PostAdapter postAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        RecyclerView mRecyclerView = root.findViewById(R.id.recyclerview);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        DatabaseHelper db = new DatabaseHelper(getActivity());
        postAdapter = new PostAdapter(getContext(), db.getFavoriteList());
        mRecyclerView.setAdapter(postAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);


        return root;
    }
}