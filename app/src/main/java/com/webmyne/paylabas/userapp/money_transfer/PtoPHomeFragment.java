package com.webmyne.paylabas.userapp.money_transfer;


import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.Selection;
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
import com.webmyne.paylabas.userapp.base.PrefUtils;
import com.webmyne.paylabas.userapp.custom_components.CircleDialog;
import com.webmyne.paylabas.userapp.helpers.AppConstants;
import com.webmyne.paylabas.userapp.helpers.CallWebService;
import com.webmyne.paylabas.userapp.helpers.ComplexPreferences;
import com.webmyne.paylabas.userapp.model.LanguageStringUtil;
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
    boolean isEnglisSelected;
    CharSequence ch=".";

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
                //original pattern
//if(!s.toString().matches("^\\ (\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?$"))
                if (!s.toString().matches("^\\ (\\d{1,3}(\\d{3})*|(\\d+))(\\" + ch + "\\d{2})?$")) {
                    //original pattern
                    //String userInput= ""+s.toString().replaceAll("[^\\d]", "");
                    String userInput = "" + s.toString().replaceAll("[^\\d]+", "");

                    StringBuilder cashAmountBuilder = new StringBuilder(userInput);

                    while (cashAmountBuilder.length() > 3 && cashAmountBuilder.charAt(0) == '0') {
                        cashAmountBuilder.deleteCharAt(0);
                    }
                    while (cashAmountBuilder.length() < 3) {
                        cashAmountBuilder.insert(0, '0');
                    }
                    cashAmountBuilder.insert(cashAmountBuilder.length() - 2, ch);
                    cashAmountBuilder.insert(0, ' ');

                    edAmountPtoP.setText(cashAmountBuilder.toString());
                    // keeps the cursor always to the right
                    Selection.setSelection(edAmountPtoP.getText(), cashAmountBuilder.toString().length());


                }
                isChargesShown = false;
                btnCheckpricePtoPHome.setText(getString(R.string.code_CHKPRICED));
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

        isEnglisSelected= PrefUtils.isEnglishSelected(getActivity());

        if(isEnglisSelected)
            ch=",";
        else
            ch=".";

        callSendMoneyToPaylabasUser();
    }

    private void callSendMoneyToPaylabasUser(){

        circleDialog=new CircleDialog(getActivity(),0);
        circleDialog.setCancelable(true);
        circleDialog.show();

        String postfix = user.UserID+"";
        Log.e("response",AppConstants.SEND_MONEY_TO_PAYLABAS_USER+postfix+"");
        new CallWebService(AppConstants.SEND_MONEY_TO_PAYLABAS_USER+postfix, CallWebService.TYPE_JSONOBJECT) {

            @Override
            public void response(String response) {
                circleDialog.dismiss();

                    Log.e("P2P response", response);
                    sendMoneyToPaylabasUser = new GsonBuilder().create().fromJson(response, SendMoneyToPaylabasUser.class);
                if(sendMoneyToPaylabasUser.ResponseCode.equalsIgnoreCase("1")) {
                    txtExchangeRate.setText("Exchange Costs (%) : "+sendMoneyToPaylabasUser.PercentageCharge +"% + "+ LanguageStringUtil.languageString(getActivity(), String.valueOf(sendMoneyToPaylabasUser.FixCharge)));
                } else {
                    SnackBar bar112 = new SnackBar(getActivity(), "Error");
                    bar112.show();
                }
                }


            @Override
            public void error(VolleyError error) {
                circleDialog.dismiss();
                SnackBar bar = new SnackBar(getActivity(),getString(R.string.code_ERRORR));
                bar.show();

            }
        }.start();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnCheckpricePtoPHome:
                if(isEmptyField(edAmountPtoP)){
                    SnackBar bar = new SnackBar(getActivity(),getString(R.string.code_PLSENTERAMT));
                    bar.show();
                } else {
                    if (validateChagresAndDisplay() && sendMoneyToPaylabasUser.ResponseCode.equalsIgnoreCase("1")) {


                        if (isChargesShown) {
                            //TODO
//                            FragmentManager manager = getActivity().getSupportFragmentManager();
//                            FragmentTransaction ft = manager.beginTransaction();
//                            ft.replace(R.id.parent_moneytransfer_ptop, new PtoPSecondScreen(), "ptop_two");
//                            ft.addToBackStack("");
//                            ft.commit();

                            Intent i = new Intent(getActivity(),PtoPSecondScreen.class);
                            startActivity(i);
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

        String ednewamount= edAmountPtoP.getText().toString().trim();
        ednewamount = ednewamount.replaceAll("\\,", ".");

        String valuefortxtExchangecost = String.format("%.2f",((Double.parseDouble(ednewamount)*Double.parseDouble(sendMoneyToPaylabasUser.PercentageCharge))/100)+Double.parseDouble(sendMoneyToPaylabasUser.FixCharge));
        txtExchangeCost.setText(LanguageStringUtil.languageString(getActivity(), String.valueOf(valuefortxtExchangecost)));

        String valuefortxtwithdrawAmount = String.format("%.2f", Double.parseDouble(ednewamount));
        txtWithdrawAmount.setText(LanguageStringUtil.languageString(getActivity(), String.valueOf(valuefortxtwithdrawAmount)));


        Double payableAmount = Double.parseDouble(valuefortxtExchangecost)+Double.parseDouble(valuefortxtwithdrawAmount);
        String valueforpayableAmount = String.format("%.2f",payableAmount);
        txtPayable.setText(LanguageStringUtil.languageString(getActivity(), String.valueOf(valueforpayableAmount)));


        String newPayablAmount= txtPayable.getText().toString().trim();
        newPayablAmount = newPayablAmount.replaceAll("\\,", ".");

        String newWithdrawAmount= txtWithdrawAmount.getText().toString().trim();
        newWithdrawAmount = newWithdrawAmount.replaceAll("\\,", ".");

        String newExchnageAmount= txtExchangeCost.getText().toString().trim();
        newExchnageAmount = newExchnageAmount.replaceAll("\\,", ".");


        sendMoneyToPaylabasUser.PayableAmount=newPayablAmount.trim();
        sendMoneyToPaylabasUser.tempExchangeCost=newExchnageAmount.trim();
        sendMoneyToPaylabasUser.tempWithdrawAmount=newWithdrawAmount.trim();
        sendMoneyToPaylabasUser.temppayableAmount=newPayablAmount.trim();


        complexPreferences= ComplexPreferences.getComplexPreferences(getActivity(), "send_to_p2p_user_pref", 0);
        complexPreferences.putObject("p2p_user",sendMoneyToPaylabasUser);
        complexPreferences.commit();

        isChargesShown=true;
        btnCheckpricePtoPHome.setText(getString(R.string.code_NNEXT));


    }

    private boolean validateChagresAndDisplay(){

        boolean isComplete = false;

        String ednewamount= edAmountPtoP.getText().toString().trim();
        ednewamount = ednewamount.replaceAll("\\,", ".");


        double value = Double.parseDouble(ednewamount);
        double user_value = Double.parseDouble(sendMoneyToPaylabasUser.LemonwayBal);

        if(value<Double.parseDouble(sendMoneyToPaylabasUser.MinLimit)){

            isComplete = false;
            edAmountPtoP.setError("Minimum Amount is € "+sendMoneyToPaylabasUser.MinLimit+" For This Service");

        }else if(value > Double.parseDouble(sendMoneyToPaylabasUser.MaxLimit)){

            isComplete = false;
            edAmountPtoP.setError("Maximum Amount is € "+sendMoneyToPaylabasUser.MaxLimit+" For This Service");


        }else if(value>user_value){

            isComplete = false;
            edAmountPtoP.setError(getString(R.string.code_INSUFFICENTBALACNE));

        }else{
            isComplete = true;
        }

        return isComplete;
    }



}
