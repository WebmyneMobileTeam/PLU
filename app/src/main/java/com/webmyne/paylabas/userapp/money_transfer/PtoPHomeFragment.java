package com.webmyne.paylabas.userapp.money_transfer;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gc.materialdesign.views.ButtonRectangle;
import com.webmyne.paylabas_user.R;



public class PtoPHomeFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private ButtonRectangle btnCheckpricePtoPHome;

    public static PtoPHomeFragment newInstance(String param1, String param2) {
        PtoPHomeFragment fragment = new PtoPHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PtoPHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View convertView = inflater.inflate(R.layout.fragment_pto_phome, container, false);
        btnCheckpricePtoPHome = (ButtonRectangle)convertView.findViewById(R.id.btnCheckpricePtoPHome);
        btnCheckpricePtoPHome.setOnClickListener(this);
        return convertView;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnCheckpricePtoPHome:

                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.parent_moneytransfer_ptop,new PtoPSecondScreen(),"ptop_two");
                ft.addToBackStack("");
                ft.commit();


                break;

        }

    }
}
