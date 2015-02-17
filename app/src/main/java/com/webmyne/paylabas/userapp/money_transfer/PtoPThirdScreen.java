package com.webmyne.paylabas.userapp.money_transfer;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.widgets.SnackBar;
import com.google.gson.GsonBuilder;
import com.webmyne.paylabas.userapp.base.MyApplication;
import com.webmyne.paylabas.userapp.base.MyDrawerActivity;
import com.webmyne.paylabas.userapp.custom_components.CircleDialog;
import com.webmyne.paylabas.userapp.custom_components.OTPDialog;
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


public class PtoPThirdScreen extends ActionBarActivity implements View.OnClickListener {
    private Toolbar toolbar;
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
        setContentView(R.layout.fragment_pto_pthird_screen);
        txtNameP2P = (TextView)findViewById(R.id.txtNameP2P);
        txtCountryP2P = (TextView)findViewById(R.id.txtCountryP2P);
        txtCityP2P = (TextView)findViewById(R.id.txtCityP2P);
        txtExchangeCostP2P = (TextView)findViewById(R.id.txtExchangeCostP2P);
        txtWithdrawAmount = (TextView)findViewById(R.id.txtWithdrawAmount);
        txtPayableAmountP2P = (TextView)findViewById(R.id.txtPayableAmountP2P);
        btnBack = (ButtonRectangle)findViewById(R.id.btnBackPtoPThirdScreen);
        btnNext = (ButtonRectangle) findViewById(R.id.btnNextPtoPThirdScreen);
        btnNext.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.icon_aboutus);
            toolbar.setTitle("PAYLABAS TO PAYLABAS");
            setSupportActionBar(toolbar);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        complexPreferences = ComplexPreferences.getComplexPreferences(PtoPThirdScreen.this, "send_to_p2p_user_pref", 0);
        sendMoneyToPaylabasUser = complexPreferences.getObject("p2p_user", SendMoneyToPaylabasUser.class);
        complexPreferences = ComplexPreferences.getComplexPreferences(PtoPThirdScreen.this, "user_pref", 0);
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
                FragmentManager manager = PtoPThirdScreen.this.getSupportFragmentManager();
                manager.popBackStack();
                break;

            case R.id.btnNextPtoPThirdScreen:

                JSONObject otpOBJ = null;
                try {

                    otpOBJ = new JSONObject();
                    otpOBJ.put("Amount", sendMoneyToPaylabasUser.tempWithdrawAmount + "");
                    otpOBJ.put("UserCountryCode", user.MobileCountryCode + "");
                    otpOBJ.put("UserID", user.UserID);
                    otpOBJ.put("UserMobileNo", user.MobileNo);
//                    Log.e("request OTP GEnerate GC GC: ", "" + otpOBJ);
                } catch (Exception e) {

                }

                final CircleDialog circleDialog = new CircleDialog(PtoPThirdScreen.this, 0);
                circleDialog.setCancelable(true);
                circleDialog.show();

                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.SEND_OTP, otpOBJ, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jobj) {

                        circleDialog.dismiss();
                        String response = jobj.toString();
//                        Log.e("Response OTP GEnerate GC GC: ", "" + response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            String responsecode = obj.getString("ResponseCode");

                            if (responsecode.equalsIgnoreCase("1")) {

                                OTPDialog otpDialog = new OTPDialog(PtoPThirdScreen.this, 0, obj.getString("VerificationCode"));
                                otpDialog.setOnConfirmListner(new OTPDialog.OnConfirmListner() {
                                    @Override
                                    public void onComplete() {
                                        postReciptData();
                                    }
                                });


                            } else {

                                SnackBar bar = new SnackBar(PtoPThirdScreen.this, "Error");
                                bar.show();
                                //  resetAll();
                            }

                        } catch (Exception e) {

                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        circleDialog.dismiss();

                        SnackBar bar = new SnackBar(PtoPThirdScreen.this, "Network Error");
                        bar.show();

                    }
                });

                req.setRetryPolicy(
                        new DefaultRetryPolicy(0, 0, 0));

                MyApplication.getInstance().addToRequestQueue(req);
                break;
        }
    }

    private void postReciptData() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                circleDialog = new CircleDialog(PtoPThirdScreen.this, 0);
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

//                    Log.e("sendPaymentResponse:........", sendPaymentResponse.ResponseMsg + "" + sendPaymentResponse.ResponseCode);
                    handlePostData();


                } catch (Exception e) {
                    e.printStackTrace();
                    PtoPThirdScreen.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            circleDialog.dismiss();
                            SnackBar bar = new SnackBar(PtoPThirdScreen.this, "Network Error\n" +
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
        PtoPThirdScreen.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (sendPaymentResponse.ResponseCode.equalsIgnoreCase("1")) {
                    SnackBar bar = new SnackBar(PtoPThirdScreen.this, "Payment Successfully");
                    bar.show();

                    complexPreferences = ComplexPreferences.getComplexPreferences(PtoPThirdScreen.this, "send_to_p2p_user_pref", 0);

                    complexPreferences.remove("p2p_user");

                    complexPreferences.commit();

                    CountDownTimer countDownTimer;
                    countDownTimer = new MyCountDownTimer(3000, 1000); // 1000 = 1s
                    countDownTimer.start();
//                    FragmentManager manager = getActivity().getSupportFragmentManager();
//                    FragmentTransaction ft = manager.beginTransaction();
//                    ft.replace(R.id.main_container, new MyAccountFragment());
//                    ft.commit();


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
                    SnackBar bar = new SnackBar(PtoPThirdScreen.this, errorMSG);
                    bar.show();
                }
            }
        });

    }


    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }
        @Override
        public void onFinish() {
            Log.e("counter","Time's up!");
            //TODO
            Intent i = new Intent(PtoPThirdScreen.this,MyDrawerActivity.class);
            startActivity(i);
            PtoPThirdScreen.this.finish();

           /* FragmentManager manager = MoneyTransferFinalActivity.this.getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.main_container,new MyAccountFragment());
            ft.commit();*/
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

    }

}
