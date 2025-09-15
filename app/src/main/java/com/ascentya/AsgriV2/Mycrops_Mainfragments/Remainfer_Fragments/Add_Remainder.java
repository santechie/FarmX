package com.ascentya.AsgriV2.Mycrops_Mainfragments.Remainfer_Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ascentya.AsgriV2.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Add_Remainder extends Fragment {
    View root_view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        root_view = inflater.inflate(R.layout.addremainder_layout, container, false);

        return root_view;
    }
}
