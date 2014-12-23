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

public class PtoPSecondScreen extends Fragment implements View.OnClickListener{


    private ButtonRectangle btnBack;
    private ButtonRectangle btnNext;

    public PtoPSecondScreen() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View convertView = inflater.inflate(R.layout.fragment_pto_psecond_screen, container, false);

        btnBack = (ButtonRectangle)convertView.findViewById(R.id.btnBackPtoPSecondScreen);
        btnNext = (ButtonRectangle)convertView.findViewById(R.id.btnNextPtoPSecondScreen);
        btnNext.setOnClickListener(this);
        btnBack.setOnClickListener(this);


        return convertView;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnBackPtoPSecondScreen:

                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.popBackStack();

                break;

            case R.id.btnNextPtoPSecondScreen:

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.parent_moneytransfer_ptop,new PtoPThirdScreen(),"ptop_third");
                ft.addToBackStack("");
                ft.commit();

                break;


        }


    }
}
