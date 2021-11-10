package com.example.android.java;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AsyncFragment extends Fragment {

    private ParentActivity mParent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // to make a fragment stay in memory
        setRetainInstance(true);
    }

    // send a string back to parent activity when a task is running in the fragment
    public interface ParentActivity{
        void handleTaskUpdate(String message);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mParent = (ParentActivity) context;
    }
}
