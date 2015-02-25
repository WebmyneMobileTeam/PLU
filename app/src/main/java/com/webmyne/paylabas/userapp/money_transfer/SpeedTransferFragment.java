package com.webmyne.paylabas.userapp.money_transfer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

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

import com.android.volley.VolleyError;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.widgets.SnackBar;
import com.google.gson.GsonBuilder;
import com.webmyne.paylabas.userapp.base.PrefUtils;
import com.webmyne.paylabas.userapp.custom_components.CircleDialog;
import com.webmyne.paylabas.userapp.custom_components.InternationalNumberValidation;
import com.webmyne.paylabas.userapp.helpers.AppConstants;
import com.webmyne.paylabas.userapp.helpers.CallWebService;
import com.webmyne.paylabas.userapp.helpers.ComplexPreferences;
import com.webmyne.paylabas.userapp.model.Country;
import com.webmyne.paylabas.userapp.model.LanguageStringUtil;
import com.webmyne.paylabas.userapp.model.RegionUtils;
import com.webmyne.paylabas.userapp.model.SendMoneyToPaylabasUser;
import com.webmyne.paylabas.userapp.model.User;
import com.webmyne.paylabas_user.R;

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
    private SendMoneyToPaylabasUser sendMoneyToPaylabasUser;
    boolean isEnglisSelected;
    CharSequence ch=".";
    private Spinner spinnerConfirmMobile,spinnerMobile;

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
                    if (validateChagresAndDisplay() && sendMoneyToPaylabasUser.ResponseCode.equalsIgnoreCase("1")) {


                        if (isChargesShown) {
                        //TODO
                        } else {
                            setChargeValues();
                        }
                    }
                }
            }
        });
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

        callSpeedTransfer();
 
    }

    private void callSpeedTransfer() {

        circleDialog=new CircleDialog(getActivity(),0);
        circleDialog.setCancelable(true);
        circleDialog.show();

        String postfix = user.UserID+"";
        Log.e("response", AppConstants.SEND_MONEY_TO_PAYLABAS_USER + postfix + "");
        new CallWebService(AppConstants.SEND_MONEY_TO_PAYLABAS_USER+postfix, CallWebService.TYPE_JSONOBJECT) {

            @Override
            public void response(String response) {
                circleDialog.dismiss();

                Log.e("Speed Transfer response", response);
                sendMoneyToPaylabasUser = new GsonBuilder().create().fromJson(response, SendMoneyToPaylabasUser.class);
//                if(sendMoneyToPaylabasUser.ResponseCode.equalsIgnoreCase("1")) {
//                    txtExchangeRate.setText("Exchange Costs (%) : "+sendMoneyToPaylabasUser.PercentageCharge +"% + "+ LanguageStringUtil.languageString(getActivity(), String.valueOf(sendMoneyToPaylabasUser.FixCharge)));
//                } else {
//                    SnackBar bar112 = new SnackBar(getActivity(), "Error");
//                    bar112.show();
//                }
            }

            @Override
            public void error(VolleyError error) {
                circleDialog.dismiss();
                SnackBar bar = new SnackBar(getActivity(),getString(R.string.code_ERRORR));
                bar.show();

            }
        }.start();
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
        double user_value = Double.parseDouble(sendMoneyToPaylabasUser.LemonwayBal);

        if(value<Double.parseDouble(sendMoneyToPaylabasUser.MinLimit)){

            isComplete = false;
            etAmountST.setError("Minimum Amount is € "+sendMoneyToPaylabasUser.MinLimit+" For This Service");

        }else if(value > Double.parseDouble(sendMoneyToPaylabasUser.MaxLimit)){

            isComplete = false;
            etAmountST.setError("Maximum Amount is € "+sendMoneyToPaylabasUser.MaxLimit+" For This Service");


        }else if(value>user_value){

            isComplete = false;
            etAmountST.setError(getString(R.string.code_INSUFFICENTBALACNE));

        }else{
            isComplete = true;
        }

        return isComplete;
    }

    private void setChargeValues() {

        Log.e("percentage",Double.parseDouble(sendMoneyToPaylabasUser.PercentageCharge)+"");
        Log.e("fix",Double.parseDouble(sendMoneyToPaylabasUser.FixCharge)+"");

        String ednewamount= etAmountST.getText().toString().trim();
        ednewamount = ednewamount.replaceAll("\\,", ".");

        String valuefortxtExchangecost = String.format("%.2f",((Double.parseDouble(ednewamount)*Double.parseDouble(sendMoneyToPaylabasUser.PercentageCharge))/100)+Double.parseDouble(sendMoneyToPaylabasUser.FixCharge));
        txtExchangeCostST.setText(LanguageStringUtil.languageString(getActivity(), String.valueOf(valuefortxtExchangecost)));

        String valuefortxtwithdrawAmount = String.format("%.2f", Double.parseDouble(ednewamount));
        txtWithdrawAmountST.setText(LanguageStringUtil.languageString(getActivity(), String.valueOf(valuefortxtwithdrawAmount)));


        Double payableAmount = Double.parseDouble(valuefortxtExchangecost)+Double.parseDouble(valuefortxtwithdrawAmount);
        String valueforpayableAmount = String.format("%.2f",payableAmount);
        txtPayableST.setText(LanguageStringUtil.languageString(getActivity(), String.valueOf(valueforpayableAmount)));


        String newPayablAmount= txtPayableST.getText().toString().trim();
        newPayablAmount = newPayablAmount.replaceAll("\\,", ".");

        String newWithdrawAmount= txtWithdrawAmountST.getText().toString().trim();
        newWithdrawAmount = newWithdrawAmount.replaceAll("\\,", ".");

        String newExchnageAmount= txtExchangeCostST.getText().toString().trim();
        newExchnageAmount = newExchnageAmount.replaceAll("\\,", ".");

        isChargesShown=true;
        btnCheckpriceST.setText(getString(R.string.code_PAY_NOW_ST));


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
