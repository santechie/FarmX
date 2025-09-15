package com.ascentya.AsgriV2.OnBoard_Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ascentya.AsgriV2.Event_Bus.DeleteBus;
import com.ascentya.AsgriV2.Event_Bus.DeleteEvent;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.login_activities.Formx_Login_Activity;
import com.squareup.otto.Bus;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WelcomeScreen_One extends Fragment {
    View root_view;
    Button next, skip;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        root_view = inflater.inflate(R.layout.welcomeone_layout, container, false);
        next = root_view.findViewById(R.id.next);
        skip = root_view.findViewById(R.id.skip);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Formx_Login_Activity.class);
                startActivity(i);
                getActivity().finish();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bus bus = DeleteBus.getInstance();
                bus.post(new DeleteEvent("true"));
            }
        });
        return root_view;
    }
}