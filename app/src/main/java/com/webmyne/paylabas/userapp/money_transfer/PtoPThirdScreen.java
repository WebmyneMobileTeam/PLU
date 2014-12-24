package com.webmyne.paylabas.userapp.money_transfer;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonRectangle;
import com.webmyne.paylabas.userapp.helpers.ComplexPreferences;
import com.webmyne.paylabas.userapp.model.SendMoneyToPaylabasUser;
import com.webmyne.paylabas_user.R;


public class PtoPThirdScreen extends Fragment implements View.OnClickListener{

    private ButtonRectangle btnBack;
    private ButtonRectangle btnNext;
    private ComplexPreferences complexPreferences;
    private SendMoneyToPaylabasUser sendMoneyToPaylabasUser;
    private TextView txtNameP2P,txtCountryP2P,txtCityP2P,txtExchangeCostP2P,txtWithdrawAmount,txtPayableAmountP2P;


    public static PtoPThirdScreen newInstance(String param1, String param2) {
        PtoPThirdScreen fragment = new PtoPThirdScreen();

        return fragment;
    }

    public PtoPThirdScreen() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView=inflater.inflate(R.layout.fragment_pto_pthird_screen, container, false);
        txtNameP2P=(TextView)convertView.findViewById(R.id.txtNameP2P);
        txtCountryP2P=(TextView)convertView.findViewById(R.id.txtCountryP2P);
        txtCityP2P=(TextView)convertView.findViewById(R.id.txtCityP2P);
        txtExchangeCostP2P=(TextView)convertView.findViewById(R.id.txtExchangeCostP2P);
        txtWithdrawAmount=(TextView)convertView.findViewById(R.id.txtWithdrawAmount);
        txtPayableAmountP2P=(TextView)convertView.findViewById(R.id.txtPayableAmountP2P);
        btnBack = (ButtonRectangle)convertView.findViewById(R.id.btnBackPtoPThirdScreen);
        btnNext = (ButtonRectangle)convertView.findViewById(R.id.btnNextPtoPThirdScreen);
        btnNext.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onResume() {
        super.onResume();
        complexPreferences= ComplexPreferences.getComplexPreferences(getActivity(), "send_to_p2p_user_pref", 0);
        sendMoneyToPaylabasUser=complexPreferences.getObject("p2p_user", SendMoneyToPaylabasUser.class);

        txtNameP2P.setText(sendMoneyToPaylabasUser.tempFirstName+" "+sendMoneyToPaylabasUser.tempLastName);
        txtExchangeCostP2P.setText(sendMoneyToPaylabasUser.tempExchangeCost);
        txtWithdrawAmount.setText(sendMoneyToPaylabasUser.tempWithdrawAmount);
        txtPayableAmountP2P.setText(sendMoneyToPaylabasUser.temppayableAmount);
        txtCityP2P.setText(sendMoneyToPaylabasUser.tempCityName);
        txtCountryP2P.setText(sendMoneyToPaylabasUser.tempCountryName);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){


            case R.id.btnBackPtoPThirdScreen:

                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.popBackStack();

                break;

            case R.id.btnNextPtoPThirdScreen:
//TODO
                break;
        }
    }
}
