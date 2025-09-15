package com.ascentya.AsgriV2.OnBoard_Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.login_activities.Formx_Login_Activity;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WelcomeScreen_Two extends Fragment {
    View root_view;
    Button to_main;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        root_view = inflater.inflate(R.layout.welcometwo_layout, container, false);
        to_main = root_view.findViewById(R.id.to_main);
        to_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Formx_Login_Activity.class);
                startActivity(i);
                getActivity().finish();
            }
        });
        return root_view;
    }
}
