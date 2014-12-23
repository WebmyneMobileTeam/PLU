package com.webmyne.paylabas.userapp.mobile_topup;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.widgets.SnackBar;
import com.webmyne.paylabas_user.R;

public class MobileTopupRechargeFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private EditText edRechargeMobileNumber;
    private EditText edConfirmRechargeMobileNumber;

    private ButtonRectangle btnRecharge;

    private Spinner spCountry;
    private Spinner spServiceProvider;
    private Spinner spRechargeAmount;


    public static MobileTopupRechargeFragment newInstance(String param1, String param2) {
        MobileTopupRechargeFragment fragment = new MobileTopupRechargeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MobileTopupRechargeFragment() {
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

        View convertView = inflater.inflate(R.layout.fragment_mobiletopup_recharge, container, false);

        edRechargeMobileNumber = (EditText)convertView.findViewById(R.id.edRechargeMobileNumber);
        edConfirmRechargeMobileNumber= (EditText)convertView.findViewById(R.id.edConfirmRechargeMobileNumber);

        btnRecharge = (ButtonRectangle)convertView.findViewById(R.id.btnRecharge);

        spCountry= (Spinner)convertView.findViewById(R.id.spCountry);
        spServiceProvider= (Spinner)convertView.findViewById(R.id.spServiceProvider);
        spRechargeAmount= (Spinner)convertView.findViewById(R.id.spRechargeAmount);




        btnRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmptyField(edRechargeMobileNumber)){
                    SnackBar bar = new SnackBar(getActivity(),"Please Enter Mobile Number");
                    bar.show();
                }
                else if(isEmptyField(edConfirmRechargeMobileNumber)){
                    SnackBar bar = new SnackBar(getActivity(),"Please Enter Confirm Mobile Number");
                    bar.show();
                }
                else if(!isMobilenoMatch(edRechargeMobileNumber,edConfirmRechargeMobileNumber)){
                    SnackBar bar = new SnackBar(getActivity(),"Confirm Mobile Number Should match with Mobile Number");
                    bar.show();
                }
                else{
                    processRecharge();
                }
            }
        });
        return convertView;
    }


public void processRecharge(){
        final com.gc.materialdesign.widgets.Dialog alert = new com.gc.materialdesign.widgets.Dialog(getActivity(),"Recharge","Are sure to Continue ?");
        alert.show();

        alert.setOnAcceptButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                SnackBar bar = new SnackBar(getActivity(),"Recharge Done--temp msg");
                bar.show();
            }
        });
    }

public boolean isEmptyField(EditText param1){

        boolean isEmpty = false;
        if(param1.getText() == null || param1.getText().toString().equalsIgnoreCase("")){
            isEmpty = true;
        }
        return isEmpty;
}

public boolean isMobilenoMatch(EditText param1,EditText param2){
        boolean isMatch = false;
        if(param1.getText().toString().equals(param2.getText().toString())){
            isMatch = true;
        }
        return isMatch;
    }
// end of main class
}
