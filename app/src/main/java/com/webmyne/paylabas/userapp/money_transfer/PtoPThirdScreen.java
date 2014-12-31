package com.webmyne.paylabas.userapp.money_transfer;


import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.widgets.SnackBar;
import com.google.gson.GsonBuilder;
import com.webmyne.paylabas.userapp.base.MyDrawerActivity;
import com.webmyne.paylabas.userapp.custom_components.CircleDialog;
import com.webmyne.paylabas.userapp.helpers.API;
import com.webmyne.paylabas.userapp.helpers.AppConstants;
import com.webmyne.paylabas.userapp.helpers.CallWebService;
import com.webmyne.paylabas.userapp.helpers.ComplexPreferences;
import com.webmyne.paylabas.userapp.home.MyAccountFragment;
import com.webmyne.paylabas.userapp.model.SendMoneyToPaylabasUser;
import com.webmyne.paylabas.userapp.model.SendPaymentResponse;
import com.webmyne.paylabas.userapp.model.User;
import com.webmyne.paylabas_user.R;

import org.json.JSONObject;

import java.io.Reader;


public class PtoPThirdScreen extends Fragment implements View.OnClickListener {

    private ButtonRectangle btnBack;
    private ButtonRectangle btnNext;
    private ComplexPreferences complexPreferences;
    private SendMoneyToPaylabasUser sendMoneyToPaylabasUser;
    private TextView txtNameP2P, txtCountryP2P, txtCityP2P, txtExchangeCostP2P, txtWithdrawAmount, txtPayableAmountP2P;
    private CircleDialog circleDialog;
    private User user;
    private SendPaymentResponse sendPaymentResponse;

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
        View convertView = inflater.inflate(R.layout.fragment_pto_pthird_screen, container, false);
        txtNameP2P = (TextView) convertView.findViewById(R.id.txtNameP2P);
        txtCountryP2P = (TextView) convertView.findViewById(R.id.txtCountryP2P);
        txtCityP2P = (TextView) convertView.findViewById(R.id.txtCityP2P);
        txtExchangeCostP2P = (TextView) convertView.findViewById(R.id.txtExchangeCostP2P);
        txtWithdrawAmount = (TextView) convertView.findViewById(R.id.txtWithdrawAmount);
        txtPayableAmountP2P = (TextView) convertView.findViewById(R.id.txtPayableAmountP2P);
        btnBack = (ButtonRectangle) convertView.findViewById(R.id.btnBackPtoPThirdScreen);
        btnNext = (ButtonRectangle) convertView.findViewById(R.id.btnNextPtoPThirdScreen);
        btnNext.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onResume() {
        super.onResume();
        complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "send_to_p2p_user_pref", 0);
        sendMoneyToPaylabasUser = complexPreferences.getObject("p2p_user", SendMoneyToPaylabasUser.class);
        complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
        user = complexPreferences.getObject("current_user", User.class);
        txtNameP2P.setText(sendMoneyToPaylabasUser.tempFirstName + " " + sendMoneyToPaylabasUser.tempLastName);
        txtExchangeCostP2P.setText(sendMoneyToPaylabasUser.tempExchangeCost);
        txtWithdrawAmount.setText(sendMoneyToPaylabasUser.tempWithdrawAmount);
        txtPayableAmountP2P.setText(sendMoneyToPaylabasUser.temppayableAmount);
        txtCityP2P.setText(sendMoneyToPaylabasUser.tempCityName);
        txtCountryP2P.setText(sendMoneyToPaylabasUser.tempCountryName);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.btnBackPtoPThirdScreen:

                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.popBackStack();

                break;

            case R.id.btnNextPtoPThirdScreen:

                postReciptData();

                break;
        }
    }

    private void postReciptData() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                circleDialog = new CircleDialog(getActivity(), 0);
                circleDialog.setCancelable(true);
                circleDialog.show();
            }


            @Override
            protected Void doInBackground(Void... params) {
                try {


                    JSONObject paymentObject = new JSONObject();
                    paymentObject.put("Amount", sendMoneyToPaylabasUser.tempWithdrawAmount + "");
                    paymentObject.put("CityID", sendMoneyToPaylabasUser.tempCityId + "");
                    paymentObject.put("CountryID", sendMoneyToPaylabasUser.tempCountryId + "");
                    paymentObject.put("EmailID", sendMoneyToPaylabasUser.tempEmailId + "");
                    paymentObject.put("FirstName", sendMoneyToPaylabasUser.tempFirstName + "");
                    paymentObject.put("LastName", sendMoneyToPaylabasUser.tempLastName + "");
                    paymentObject.put("MobileCountryCode", sendMoneyToPaylabasUser.tempCountryCodeId + "");

                    paymentObject.put("MobileNo", sendMoneyToPaylabasUser.tempMobileId + "");

                    paymentObject.put("StateID", sendMoneyToPaylabasUser.tempStateId + "");
                    paymentObject.put("UserID", user.UserID + "");

                    Log.e("paymentObject", paymentObject + "");
                    Reader reader = API.callWebservicePost(AppConstants.SEND_PAYMENT, paymentObject.toString());
                    sendPaymentResponse = new GsonBuilder().create().fromJson(reader, SendPaymentResponse.class);

                    Log.e("sendPaymentResponse:........", sendPaymentResponse.ResponseMsg + "" + sendPaymentResponse.ResponseCode);
                    handlePostData();


                } catch (Exception e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            circleDialog.dismiss();
                            SnackBar bar = new SnackBar(getActivity(), "Network Error\n" +
                                    "Please try again");
                            bar.show();
                        }
                    });

                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                circleDialog.dismiss();
            }
        }.execute();


    }

    private void handlePostData() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (sendPaymentResponse.ResponseCode.equalsIgnoreCase("1")) {
                    SnackBar bar = new SnackBar(getActivity(), "Payment Successfully");
                    bar.show();

                    complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "send_to_p2p_user_pref", 0);

                    complexPreferences.remove("p2p_user");

                    complexPreferences.commit();


                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = manager.beginTransaction();
                    ft.replace(R.id.main_container, new MyAccountFragment());
                    ft.commit();


                } else {
                    //TODO change error message
                    String errorMSG = sendPaymentResponse.ResponseMsg;
                    if (sendPaymentResponse.ResponseCode.equalsIgnoreCase("-2")) {
//                        errorMSG = "Error In While Generating GiftCode";
                    } else if (sendPaymentResponse.ResponseCode.equalsIgnoreCase("-1")) {
//                        errorMSG = "Error";
                    } else if (sendPaymentResponse.ResponseCode.equalsIgnoreCase("2")) {

//                        errorMSG = "User not Exist with Paylabas";
                    } else if (sendPaymentResponse.ResponseCode.equalsIgnoreCase("3")) {
//                        errorMSG = "User will blocked for next 24 hours";
                    } else if (sendPaymentResponse.ResponseCode.equalsIgnoreCase("4")) {
//                        errorMSG = "User Deleted";
                    } else if (sendPaymentResponse.ResponseCode.equalsIgnoreCase("5")) {
//                        errorMSG = "User is not verified";
                    } else {
                        errorMSG = "Network Error\nPlease try again";
                    }
                    SnackBar bar = new SnackBar(getActivity(), errorMSG);
                    bar.show();
                }
            }
        });

    }


}
