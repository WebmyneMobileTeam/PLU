package com.webmyne.paylabas.userapp.money_transfer;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.widgets.SnackBar;
import com.google.gson.GsonBuilder;
import com.webmyne.paylabas.userapp.base.MyDrawerActivity;
import com.webmyne.paylabas.userapp.custom_components.CircleDialog;
import com.webmyne.paylabas.userapp.helpers.AppConstants;
import com.webmyne.paylabas.userapp.helpers.CallWebService;
import com.webmyne.paylabas.userapp.helpers.ComplexPreferences;
import com.webmyne.paylabas.userapp.model.SendMoneyToPaylabasUser;
import com.webmyne.paylabas.userapp.model.User;
import com.webmyne.paylabas_user.R;

import org.json.JSONObject;


public class PtoPHomeFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private ButtonRectangle btnCheckpricePtoPHome;
    private EditText edAmountPtoP;
    private TextView txtExchangeCost,txtWithdrawAmount,txtPayable,txtExchangeRate;
    private CircleDialog circleDialog;
    private User user;
    private SendMoneyToPaylabasUser sendMoneyToPaylabasUser;
    private ComplexPreferences complexPreferences;
    private boolean isChargesShown=false;

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
        edAmountPtoP= (EditText)convertView.findViewById(R.id.edAmountPtoP);
        edAmountPtoP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isChargesShown=false;
                btnCheckpricePtoPHome.setText("Check Price");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        txtExchangeCost= (TextView)convertView.findViewById(R.id.txtExchangeCost);
        txtWithdrawAmount= (TextView)convertView.findViewById(R.id.txtWithdrawAmount);
        txtPayable= (TextView)convertView.findViewById(R.id.txtPayable);
        txtExchangeRate=(TextView)convertView.findViewById(R.id.txtExchangeRate);
        return convertView;
    }

    @Override
    public void onResume() {
        super.onResume();

        complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
        user = complexPreferences.getObject("current_user", User.class);
        isChargesShown=false;
        callSendMoneyToPaylabasUser();
    }

    private void callSendMoneyToPaylabasUser(){
        circleDialog=new CircleDialog(getActivity(),0);
        circleDialog.setCancelable(true);
        circleDialog.show();

        String postfix = user.UserID+"";

        new CallWebService(AppConstants.SEND_MONEY_TO_PAYLABAS_USER+postfix, CallWebService.TYPE_JSONOBJECT) {

            @Override
            public void response(String response) {
                circleDialog.dismiss();

                    Log.e("---- Service Charge Response ", response);
                    sendMoneyToPaylabasUser = new GsonBuilder().create().fromJson(response, SendMoneyToPaylabasUser.class);
                if(sendMoneyToPaylabasUser.ResponseCode.equalsIgnoreCase("1")) {
                    txtExchangeRate.setText("Exchange Costs (%) : "+sendMoneyToPaylabasUser.PercentageCharge +"% + "+sendMoneyToPaylabasUser.FixCharge);
                } else {
                    SnackBar bar112 = new SnackBar(getActivity(), "Error");
                    bar112.show();
                }
                }


            @Override
            public void error(VolleyError error) {
                circleDialog.dismiss();
                SnackBar bar = new SnackBar(getActivity(),"Error");
                bar.show();

            }
        }.start();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnCheckpricePtoPHome:
                if(isEmptyField(edAmountPtoP)){
                    SnackBar bar = new SnackBar(getActivity(),"Please Enter Amount");
                    bar.show();
                } else {
                    if (validateChagresAndDisplay() && sendMoneyToPaylabasUser.ResponseCode.equalsIgnoreCase("1")) {


                        if (isChargesShown) {
                            FragmentManager manager = getActivity().getSupportFragmentManager();
                            FragmentTransaction ft = manager.beginTransaction();
                            ft.replace(R.id.parent_moneytransfer_ptop, new PtoPSecondScreen(), "ptop_two");
                            ft.addToBackStack("");
                            ft.commit();
                        } else {
                            setChargeValues();
                        }
                    }
                }
                break;

        }

    }

    public boolean isEmptyField(EditText param1){

        boolean isEmpty = false;
        if(param1.getText() == null || param1.getText().toString().equalsIgnoreCase("")){
            isEmpty = true;
        }
        return isEmpty;
    }

    private void setChargeValues() {

        Log.e("percentage",Double.parseDouble(sendMoneyToPaylabasUser.PercentageCharge)+"");
        Log.e("fix",Double.parseDouble(sendMoneyToPaylabasUser.FixCharge)+"");
        txtExchangeCost.setText(String.format("%.2f",((Double.parseDouble(edAmountPtoP.getText().toString().trim())*Double.parseDouble(sendMoneyToPaylabasUser.PercentageCharge))/100)+Double.parseDouble(sendMoneyToPaylabasUser.FixCharge)));
        txtWithdrawAmount.setText(String.format("%.2f", Double.parseDouble(edAmountPtoP.getText().toString().trim())));
        Double payableAmount=Double.parseDouble(txtExchangeCost.getText().toString().trim())+Double.parseDouble(txtWithdrawAmount.getText().toString().trim());
        txtPayable.setText(String.format("%.2f",payableAmount));

        sendMoneyToPaylabasUser.PayableAmount=txtPayable.getText().toString().trim();
        sendMoneyToPaylabasUser.tempExchangeCost=txtExchangeCost.getText().toString().trim();
        sendMoneyToPaylabasUser.tempWithdrawAmount=txtWithdrawAmount.getText().toString().trim();
        sendMoneyToPaylabasUser.temppayableAmount=txtPayable.getText().toString().trim();
        complexPreferences= ComplexPreferences.getComplexPreferences(getActivity(), "send_to_p2p_user_pref", 0);
        complexPreferences.putObject("p2p_user",sendMoneyToPaylabasUser);
        complexPreferences.commit();

        isChargesShown=true;
        btnCheckpricePtoPHome.setText("Next");


    }

    private boolean validateChagresAndDisplay(){

        boolean isComplete = false;
        double value = Double.parseDouble(edAmountPtoP.getText().toString());
        double user_value = Double.parseDouble(sendMoneyToPaylabasUser.LemonwayBal);

        if(value<Double.parseDouble(sendMoneyToPaylabasUser.MinLimit)){

            isComplete = false;
            edAmountPtoP.setError("Minimum Amount is € "+sendMoneyToPaylabasUser.MinLimit+" For This Service");

        }else if(value > Double.parseDouble(sendMoneyToPaylabasUser.MaxLimit)){

            isComplete = false;
            edAmountPtoP.setError("Maximum Amount is € "+sendMoneyToPaylabasUser.MaxLimit+" For This Service");


        }else if(value>user_value){

            isComplete = false;
            edAmountPtoP.setError("Insufficient balance");

        }else{
            isComplete = true;
        }

        return isComplete;
    }



}
