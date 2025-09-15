package com.ascentya.AsgriV2.farmx.log_market_warehouse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ascentya.AsgriV2.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Logistics extends Fragment {
    View root_view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.logistics, container, false);
        return root_view;
    }
}
