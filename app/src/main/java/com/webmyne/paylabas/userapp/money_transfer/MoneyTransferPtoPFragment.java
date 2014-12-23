package com.webmyne.paylabas.userapp.money_transfer;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webmyne.paylabas_user.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoneyTransferPtoPFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoneyTransferPtoPFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public static MoneyTransferPtoPFragment newInstance(String param1, String param2) {
        MoneyTransferPtoPFragment fragment = new MoneyTransferPtoPFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MoneyTransferPtoPFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.parent_moneytransfer_ptop,new PtoPHomeFragment(),"ptop_one");
        ft.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View convertView = inflater.inflate(R.layout.fragment_money_transfer_pto, container, false);

        return convertView;
    }




}
