package com.ascentya.AsgriV2.e_market.fragments;

import com.ascentya.AsgriV2.e_market.activities.BaseActivity;

import androidx.fragment.app.Fragment;

class BaseFragment extends Fragment {

    protected BaseActivity getBaseActivity(){
        return ((BaseActivity) getActivity());
    }
}
