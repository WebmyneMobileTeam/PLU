package com.webmyne.paylabas.userapp.giftcode;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.gc.materialdesign.views.ButtonRectangle;
import com.webmyne.paylabas_user.R;

public class GenerateGCFragment extends Fragment implements TextWatcher{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private EditText edMobileNumberGenerateGC;
    private EditText edAmountGenerateGC;

    private Spinner spRecipients;
    private Spinner spCountry;


    public static GenerateGCFragment newInstance(String param1, String param2) {
        GenerateGCFragment fragment = new GenerateGCFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public GenerateGCFragment() {

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

        View convertView = inflater.inflate(R.layout.fragment_generate_gc, container, false);
        init(convertView);
        return convertView;
    }

    private void init(View convertView) {

        edAmountGenerateGC = (EditText)convertView.findViewById(R.id.edAmountGenerateGC);
        edMobileNumberGenerateGC = (EditText)convertView.findViewById(R.id.edMobileNumberGenerateGC);

        edMobileNumberGenerateGC.addTextChangedListener(this);
        edAmountGenerateGC.addTextChangedListener(this);


    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {



    }
}
