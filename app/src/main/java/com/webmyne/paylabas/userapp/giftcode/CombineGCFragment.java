package com.webmyne.paylabas.userapp.giftcode;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.widgets.SnackBar;
import com.webmyne.paylabas_user.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CombineGCFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CombineGCFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private LinearLayout linearCombineGiftCode;
    private ButtonRectangle btnAddCombineGiftCode;



    public static CombineGCFragment newInstance(String param1, String param2) {
        CombineGCFragment fragment = new CombineGCFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CombineGCFragment() {

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

        View convertView = inflater.inflate(R.layout.fragment_combine_gc, container, false);
        init(convertView);

        return convertView;
    }

    private void init(View convertView) {
        linearCombineGiftCode = (LinearLayout)convertView.findViewById(R.id.linearCombineGiftCode);
        btnAddCombineGiftCode = (ButtonRectangle)convertView.findViewById(R.id.btnAddCombineGiftCode);
        btnAddCombineGiftCode.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();

        addCombineStrip(false);
        addCombineStrip(false);

    }

    private void addCombineStrip(boolean isDeleteVisible) {

        View vStrip = getActivity().getLayoutInflater().inflate(R.layout.item_combinegiftcode,null);

        TextView txtDelete = (TextView)vStrip.findViewById(R.id.btnDeleteCombineGiftCode);
        if(isDeleteVisible == false){
            txtDelete.setVisibility(View.INVISIBLE);
        }else{
            txtDelete.setVisibility(View.VISIBLE);
        }
        txtDelete.setOnClickListener(deleteListner);

        EditText edEnterGiftCode = (EditText)vStrip.findViewById(R.id.entergiftcode_combinegiftcode);

        edEnterGiftCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.toString().length() == 9){
                    Toast.makeText(getActivity(), "call webservice", Toast.LENGTH_SHORT).show();
                }


            }
        });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearCombineGiftCode.addView(vStrip,params);
        linearCombineGiftCode.invalidate();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnAddCombineGiftCode:
                processAddCombineStrips();
                break;
        }
    }


    private View.OnClickListener deleteListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FrameLayout fp = (FrameLayout)v.getParent();
            LinearLayout second = (LinearLayout)fp.getParent();
            LinearLayout first = (LinearLayout)second.getParent();
            linearCombineGiftCode.removeViewAt(linearCombineGiftCode.indexOfChild(first));
            linearCombineGiftCode.invalidate();
        }
    };

    private void processAddCombineStrips() {

        if(linearCombineGiftCode.getChildCount() == 5){

        }else{

            addCombineStrip(true);

        }


    }
}
