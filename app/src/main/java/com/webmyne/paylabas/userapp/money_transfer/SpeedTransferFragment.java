package com.webmyne.paylabas.userapp.money_transfer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.webmyne.paylabas.userapp.base.PrefUtils;
import com.webmyne.paylabas.userapp.custom_components.CircleDialog;
import com.webmyne.paylabas.userapp.custom_components.InternationalNumberValidation;
import com.webmyne.paylabas.userapp.custom_components.OTPDialog;
import com.webmyne.paylabas.userapp.helpers.AppConstants;
import com.webmyne.paylabas.userapp.helpers.CallWebService;
import com.webmyne.paylabas.userapp.helpers.ComplexPreferences;
import com.webmyne.paylabas.userapp.model.CheckAmountBalance;
import com.webmyne.paylabas.userapp.model.Country;
import com.webmyne.paylabas.userapp.model.LanguageStringUtil;
import com.webmyne.paylabas.userapp.model.RegionUtils;
import com.webmyne.paylabas.userapp.model.User;
import com.webmyne.paylabas_user.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SpeedTransferFragment extends Fragment {
    private ArrayList<Country> countries;
    private EditText etMobileST,etConfirmMobileST,etAmountST;
    private TextView txtExchangeCostST,txtWithdrawAmountST,txtPayableST;
    private ButtonRectangle btnCheckpriceST;
    private CircleDialog circleDialog;
    private ComplexPreferences complexPreferences;
    private User user;
    private boolean isChargesShown=false;
//    private SendMoneyToPaylabasUser sendMoneyToPaylabasUser;
    boolean isEnglisSelected;
    CharSequence ch=".";
    private Spinner spinnerConfirmMobile,spinnerMobile;
    private CheckAmountBalance checkAmountBalance;

    public static SpeedTransferFragment newInstance(String param1, String param2) {
        SpeedTransferFragment fragment = new SpeedTransferFragment();

        return fragment;
    }

    public SpeedTransferFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View convertView= inflater.inflate(R.layout.fragment_speed_transfer, container, false);
        initView(convertView);
        setValues();
        return convertView;
    }

    private void initView(View convertView) {
        btnCheckpriceST= (ButtonRectangle)convertView. findViewById(R.id.btnCheckpriceST);
        etMobileST= (EditText)convertView. findViewById(R.id.etMobileST);
        etConfirmMobileST= (EditText)convertView. findViewById(R.id.etConfirmMobileST);
        etAmountST= (EditText)convertView. findViewById(R.id.etAmountST);
        txtExchangeCostST= (TextView)convertView. findViewById(R.id.txtExchangeCostST);
        txtWithdrawAmountST= (TextView)convertView. findViewById(R.id.txtWithdrawAmountST);
        txtPayableST= (TextView)convertView. findViewById(R.id.txtPayableST);
//        txtExchangeRate= (TextView)convertView. findViewById(R.id.txtExchangeRate);
        spinnerMobile= (Spinner)convertView. findViewById(R.id.spinnerMobile);
        spinnerConfirmMobile= (Spinner)convertView. findViewById(R.id.spinnerConfirmMobile);
    }

    private void setValues(){
        setCountryCode();
        etAmountST.addTextChangedListener(new TextWatcher() {
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

                    etAmountST.setText(cashAmountBuilder.toString());
                    // keeps the cursor always to the right
                    Selection.setSelection(etAmountST.getText(), cashAmountBuilder.toString().length());


                }
                isChargesShown = false;
                btnCheckpriceST.setText(getString(R.string.code_CHKPRICED));


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnCheckpriceST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(InternationalNumberValidation.isPossibleNumber(etMobileST.getText().toString().toString(), countries.get(spinnerMobile.getSelectedItemPosition()).ShortCode.toString().trim())==false){

                    SnackBar bar = new SnackBar(getActivity(), getString(R.string.code_ENTERVALIDNUMBER));
                    bar.show();
                }else if(InternationalNumberValidation.isValidNumber(etMobileST.getText().toString().toString(), countries.get(spinnerMobile.getSelectedItemPosition()).ShortCode.toString().trim())==false){

                    SnackBar bar = new SnackBar(getActivity(), getString(R.string.code_ENTERVALIDNUMBER));
                    bar.show();
                } else if ((isMobileMatch(etMobileST,etConfirmMobileST)==false) || (countries.get(spinnerMobile.getSelectedItemPosition()).CountryCode !=countries.get(spinnerConfirmMobile.getSelectedItemPosition()).CountryCode)){
                    SnackBar bar = new SnackBar(getActivity(), getString(R.string.code_Enter_Valid_confirm_Number));
                    bar.show();
                }

                else

                if(isEmptyField(etAmountST)){
                    SnackBar bar = new SnackBar(getActivity(),getString(R.string.code_PLSENTERAMT));
                    bar.show();
                } else {
                    if (validateChagresAndDisplay()) {


                        if (isChargesShown) {
                        //TODO
                           sendOTP();


                        } else {
                            //TODO
                            getServiceChargeForPToP();

                        }
                    }
                }
            }
        });
    }

    private void sendOTP() {
        JSONObject otpOBJ = null;
        try {
            String ednewamount= etAmountST.getText().toString().trim();
            ednewamount = ednewamount.replaceAll("\\,", ".");
            otpOBJ = new JSONObject();
            otpOBJ.put("Amount", ednewamount+ "");
            otpOBJ.put("Culture", LanguageStringUtil.CultureString(getActivity()));
            otpOBJ.put("UserCountryCode", user.MobileCountryCode + "");
            otpOBJ.put("UserID", user.UserID);
            otpOBJ.put("UserMobileNo", user.MobileNo);
            Log.e("request OTP: ", "" + otpOBJ);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final CircleDialog circleDialog = new CircleDialog(getActivity(), 0);
        circleDialog.setCancelable(true);
        circleDialog.show();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.SEND_OTP, otpOBJ, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jobj) {

                circleDialog.dismiss();
                String response = jobj.toString();
                Log.e("Response OTP: ", "" + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String responsecode = obj.getString("ResponseCode");

                    if (responsecode.equalsIgnoreCase("1")) {

                        OTPDialog otpDialog = new OTPDialog(getActivity(), 0, obj.getString("VerificationCode"));
                        otpDialog.setOnConfirmListner(new OTPDialog.OnConfirmListner() {
                            @Override
                            public void onComplete(String enteredString) {


                                checkOTPTimeout(enteredString);
                            }
                        });


                    } else {

                        SnackBar bar = new SnackBar(getActivity(), jobj.getString("ResponseMsg"));
                        bar.show();
                        //  resetAll();
                    }

                } catch (Exception e) {
                        e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                circleDialog.dismiss();

                SnackBar bar = new SnackBar(getActivity(), getString(R.string.code_PNWER));
                bar.show();

            }
        });

        req.setRetryPolicy(
                new DefaultRetryPolicy(0, 0, 0));

        MyApplication.getInstance().addToRequestQueue(req);
    }

    private void checkOTPTimeout(String enteredString) {

        JSONObject otpOBJ = null;
        try {
            otpOBJ = new JSONObject();
            otpOBJ.put("Culture", LanguageStringUtil.CultureString(getActivity()));
            otpOBJ.put("OPT", enteredString + "");
            otpOBJ.put("UserID", user.UserID);

            Log.e("request check OTP: ", "" + otpOBJ);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final CircleDialog circleDialog = new CircleDialog(getActivity(), 0);
        circleDialog.setCancelable(true);
        circleDialog.show();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.OTP_TIME_OUT, otpOBJ, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jobj) {

                circleDialog.dismiss();
                String response = jobj.toString();
                Log.e("Response OTP: ", "" + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String responsecode = obj.getString("ResponseCode");

                    if (responsecode.equalsIgnoreCase("1")) {

                        postSpeedTransferData();

                    } else {

                        SnackBar bar = new SnackBar(getActivity(), obj.getString("ResponseMsg"));
                        bar.show();
                        //  resetAll();
                        LayoutInflater li = LayoutInflater.from(getActivity());
                        View promptsView = li.inflate(R.layout.custom_resend_alert_dialog, null);
                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                        alert.setView(promptsView);

                        alert.setNeutralButton(getResources().getString(R.string.RESEND),new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // showVerificationAlert();
                                sendOTP();
                            }
                        });
                        alert.show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                circleDialog.dismiss();

                SnackBar bar = new SnackBar(getActivity(), getString(R.string.code_PNWER));
                bar.show();

            }
        });

        req.setRetryPolicy(
                new DefaultRetryPolicy(0, 0, 0));

        MyApplication.getInstance().addToRequestQueue(req);



    }

    private void postSpeedTransferData() {

        JSONObject object = null;
        try {

            object = new JSONObject();

            object.put("Amount", etAmountST.getText().toString().trim());
            object.put("Culture", LanguageStringUtil.CultureString(getActivity()));
            object.put("MobileCountryCode", countries.get(spinnerMobile.getSelectedItemPosition()).CountryCode + "");
            object.put("MobileNo", etMobileST.getText().toString() + "");
            object.put("UserID", user.UserID);


        } catch (Exception e) {
            e.printStackTrace();
        }

        final CircleDialog circleDialog = new CircleDialog(getActivity(), 0);
        circleDialog.setCancelable(true);
        circleDialog.show();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.SPEED_MONEY_TRANSFER, object, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jobj) {

                circleDialog.dismiss();
                String response = jobj.toString();
                Log.e("Response : ", "" + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String responsecode = obj.getString("ResponseCode");

                    if (responsecode.equalsIgnoreCase("1")) {
                        SnackBar bar = new SnackBar(getActivity(), obj.getString("ResponseMsg"));
                        bar.show();
                        new CountDownTimer(2000, 1000) {

                            public void onTick(long millisUntilFinished) {

                            }

                            public void onFinish() {
                                Intent intent=new Intent(getActivity(), MyDrawerActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }.start();

                    }

                        SnackBar bar = new SnackBar(getActivity(), obj.getString("ResponseMsg"));
                        bar.show();



                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                circleDialog.dismiss();

                SnackBar bar = new SnackBar(getActivity(), getString(R.string.code_PNWER));
                bar.show();

            }
        });

        req.setRetryPolicy(
                new DefaultRetryPolicy(0, 0, 0));

        MyApplication.getInstance().addToRequestQueue(req);

    }


    private void setCountryCode() {



        new RegionUtils() {

            @Override
            public void response(ArrayList response) {
                countries = response;

                CountryCodeAdapter countryAdapter = new CountryCodeAdapter(getActivity(), R.layout.spinner_country, countries);
                spinnerMobile.setAdapter(countryAdapter);
                CountryCodeAdapter countryConfirmAdapter = new CountryCodeAdapter(getActivity(), R.layout.spinner_country, countries);
                spinnerConfirmMobile.setAdapter(countryConfirmAdapter);
            }


            @Override
            public void response(String response) {

            }

            @Override
            public void error(VolleyError error) {

            }
        }.fetchCountry(getActivity());
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

        getAmountBalance();
    }

    private void getAmountBalance() {

        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put("Culture", LanguageStringUtil.CultureString(getActivity()));
            object.put("ServiceID", AppConstants.Send_Money_to_Wallet+"");
            object.put("UserID", user.UserID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final CircleDialog circleDialog = new CircleDialog(getActivity(), 0);
        circleDialog.setCancelable(true);
        circleDialog.show();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.CHECK_AMOUNT_BALANCE, object, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jobj) {

                circleDialog.dismiss();
                String response = jobj.toString();
                Log.e("Response : ", "" + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String responsecode = obj.getString("ResponseCode");

                    if (responsecode.equalsIgnoreCase("1")) {

                        checkAmountBalance = new GsonBuilder().create().fromJson(response, CheckAmountBalance.class);


                    } else {

                        SnackBar bar = new SnackBar(getActivity(), obj.getString("ResponseMsg"));
                        bar.show();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                circleDialog.dismiss();

                SnackBar bar = new SnackBar(getActivity(), getString(R.string.code_PNWER));
                bar.show();

            }
        });

        req.setRetryPolicy(
                new DefaultRetryPolicy(0, 0, 0));

        MyApplication.getInstance().addToRequestQueue(req);

    }

    private void getServiceChargeForPToP() {

        JSONObject object = null;
        try {

            object = new JSONObject();
            object.put("Culture", LanguageStringUtil.CultureString(getActivity()));
            object.put("MobileCountryCode", countries.get(spinnerMobile.getSelectedItemPosition()).CountryCode + "");
            object.put("MobileNo", etMobileST.getText().toString() + "");
            object.put("UserID", user.UserID);


        } catch (Exception e) {
            e.printStackTrace();
        }

        final CircleDialog circleDialog = new CircleDialog(getActivity(), 0);
        circleDialog.setCancelable(true);
        circleDialog.show();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.SERVICE_CHARGE_FOR_PTOP, object, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jobj) {

                circleDialog.dismiss();
                String response = jobj.toString();
                Log.e("Response : ", "" + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String responsecode = obj.getString("ResponseCode");

                    if (responsecode.equalsIgnoreCase("1")) {


                        setChargeValues(obj);

                    } else {

                        SnackBar bar = new SnackBar(getActivity(), obj.getString("ResponseMsg"));
                        bar.show();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                circleDialog.dismiss();

                SnackBar bar = new SnackBar(getActivity(), getString(R.string.code_PNWER));
                bar.show();

            }
        });

        req.setRetryPolicy(
                new DefaultRetryPolicy(0, 0, 0));

        MyApplication.getInstance().addToRequestQueue(req);

    }

    public boolean isEmptyField(EditText param1){

        boolean isEmpty = false;
        if(param1.getText() == null || param1.getText().toString().equalsIgnoreCase("")){
            isEmpty = true;
        }
        return isEmpty;
    }


    private boolean validateChagresAndDisplay(){

        boolean isComplete = false;

        String ednewamount= etAmountST.getText().toString().trim();
        ednewamount = ednewamount.replaceAll("\\,", ".");


        double value = Double.parseDouble(ednewamount);
//        double user_value = Double.parseDouble(sendMoneyToPaylabasUser.LemonwayBal);
//
//        if(value<Double.parseDouble(sendMoneyToPaylabasUser.MinLimit)){
//
//            isComplete = false;
//            etAmountST.setError("Minimum Amount is € "+sendMoneyToPaylabasUser.MinLimit+" For This Service");
//
//        }else if(value > Double.parseDouble(sendMoneyToPaylabasUser.MaxLimit)){
//
//            isComplete = false;
//            etAmountST.setError("Maximum Amount is € "+sendMoneyToPaylabasUser.MaxLimit+" For This Service");

        double user_value = Double.parseDouble(checkAmountBalance.LemonwayBal);

        if(value<Double.parseDouble(checkAmountBalance.MinLimit)){

            isComplete = false;
            etAmountST.setError("Minimum Amount is € "+checkAmountBalance.MinLimit+" For This Service");

        }else if(value > Double.parseDouble(checkAmountBalance.MaxLimit)){

            isComplete = false;
            etAmountST.setError("Maximum Amount is € "+checkAmountBalance.MaxLimit+" For This Service");

        }else if(value>user_value){

            isComplete = false;
            etAmountST.setError(getString(R.string.code_INSUFFICENTBALACNE));

        }else{
            isComplete = true;
        }

        return isComplete;
    }

    private void setChargeValues(JSONObject object) {
        try {
            Log.e("percentage", Double.parseDouble(object.getString("PerCharge")) + "");
            Log.e("fix", Double.parseDouble(object.getString("FixCharge")) + "");

            String ednewamount = etAmountST.getText().toString().trim();
            ednewamount = ednewamount.replaceAll("\\,", ".");

            String valuefortxtExchangecost = String.format("%.2f", ((Double.parseDouble(ednewamount) * Double.parseDouble(object.getString("PerCharge"))) / 100) + Double.parseDouble(object.getString("FixCharge")));
            txtExchangeCostST.setText(LanguageStringUtil.languageString(getActivity(), String.valueOf(valuefortxtExchangecost)));

            String valuefortxtwithdrawAmount = String.format("%.2f", Double.parseDouble(ednewamount));
            txtWithdrawAmountST.setText(LanguageStringUtil.languageString(getActivity(), String.valueOf(valuefortxtwithdrawAmount)));


            Double payableAmount = Double.parseDouble(valuefortxtExchangecost) + Double.parseDouble(valuefortxtwithdrawAmount);
            String valueforpayableAmount = String.format("%.2f", payableAmount);
            txtPayableST.setText(LanguageStringUtil.languageString(getActivity(), String.valueOf(valueforpayableAmount)));


            String newPayablAmount = txtPayableST.getText().toString().trim();
            newPayablAmount = newPayablAmount.replaceAll("\\,", ".");

            String newWithdrawAmount = txtWithdrawAmountST.getText().toString().trim();
            newWithdrawAmount = newWithdrawAmount.replaceAll("\\,", ".");

            String newExchnageAmount = txtExchangeCostST.getText().toString().trim();
            newExchnageAmount = newExchnageAmount.replaceAll("\\,", ".");

            isChargesShown = true;
            btnCheckpriceST.setText(getString(R.string.code_PAY_NOW_ST));

        } catch (JSONException e){
            e.printStackTrace();
            Log.e("error.....",e+"");
        }
    }


    public class CountryCodeAdapter extends ArrayAdapter<Country> {
        Context context;

        ArrayList<Country> values;

        // int android.R.Layout.
        public CountryCodeAdapter(Context context, int resource, ArrayList<Country> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values = objects;
        }


        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(getActivity());
            txt.setPadding(16,16,16,16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(" "+values.get(position).CountryName+" +"+values.get(position).CountryCode);

            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setGravity(Gravity.CENTER_VERTICAL);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 16;

            int w= (int)getResources().getDimension(R.dimen.flag_width1);
            int h= (int)getResources().getDimension(R.dimen.flag_height1);
            LinearLayout.LayoutParams params_image = new LinearLayout.LayoutParams(dpToPx(w),dpToPx(h));

            ImageView img = new ImageView(context);
            if (values.get(position).ShortCode == null || values.get(position).ShortCode.equalsIgnoreCase("") || values.get(position).ShortCode.equalsIgnoreCase("NULL")) {
            } else {
                try {
                    img.setImageBitmap(getBitmapFromAsset(values.get(position).ShortCode.toString().trim().toLowerCase()+".png"));

                } catch (Exception e) {
                    Log.e("MyTag dro down", "Failure to get drawable id.", e);
                }


            }

            layout.addView(img, params_image);
            layout.addView(txt, params);
            return  layout;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(getActivity());
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16, 16, 16, 16);
            txt.setText("+" + String.valueOf(values.get(position).CountryCode));


            return txt;
        }
    }

    private Bitmap getBitmapFromAsset(String strName) {
        AssetManager assetManager = getActivity().getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open(strName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        return bitmap;
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public boolean isMobileMatch(EditText param1, EditText param2) {
        boolean isMatch = false;
        if (param1.getText().toString().equals(param2.getText().toString())) {
            isMatch = true;
        }
        return isMatch;
    }


}
