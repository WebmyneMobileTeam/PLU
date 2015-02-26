package com.webmyne.paylabas.userapp.mobile_topup;


import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.widgets.SnackBar;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.webmyne.paylabas.userapp.base.DatabaseWrapper;
import com.webmyne.paylabas.userapp.base.MyApplication;
import com.webmyne.paylabas.userapp.base.MyDrawerActivity;
import com.webmyne.paylabas.userapp.custom_components.CircleDialog;
import com.webmyne.paylabas.userapp.custom_components.OTPDialog;
import com.webmyne.paylabas.userapp.helpers.AppConstants;
import com.webmyne.paylabas.userapp.helpers.CallWebService;
import com.webmyne.paylabas.userapp.helpers.ComplexPreferences;
import com.webmyne.paylabas.userapp.home.MyAccountFragment;
import com.webmyne.paylabas.userapp.model.City;
import com.webmyne.paylabas.userapp.model.LanguageStringUtil;
import com.webmyne.paylabas.userapp.model.MobileTopup_Main;
import com.webmyne.paylabas.userapp.model.MobileTopup_RechargeService;
import com.webmyne.paylabas.userapp.model.MobileTopup_TopUpProducts;
import com.webmyne.paylabas.userapp.model.User;
import com.webmyne.paylabas_user.R;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MobileTopupRechargeFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;
    String roundup_total;
    private EditText edRechargeMobileNumber,edRechargeConfirmMobileNumber,edCountryCode2,edCountryCode1;
    private TextView amountPay,recipeintAmountGET;

    private ButtonRectangle btnRecharge;

    private Spinner spCountry;
    private Spinner spServiceProvider;
    private Spinner spRechargeAmount;

    private ImageView ProviderImg;

    ArrayList<MobileTopup_Main> MobileTopup_List;
    ArrayList<MobileTopup_TopUpProducts> MobileTopup_TopupProducts_List ;
    ArrayList<MobileTopup_RechargeService> MobileTopup_rechargeservice_List ;


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
    public void onResume() {
        super.onResume();
        //  fetching all  d details of rechrge;
        fetchMobileTopupDetials();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         View convertView = inflater.inflate(R.layout.fragment_mobiletopup_recharge, container, false);

        edRechargeMobileNumber = (EditText)convertView.findViewById(R.id.edRechargeMobileNumber);

        edRechargeConfirmMobileNumber = (EditText)convertView.findViewById(R.id.edRechargeConfirmMobileNumber);
        edCountryCode1 = (EditText)convertView.findViewById(R.id.edCountryCode1);
        edCountryCode2 = (EditText)convertView.findViewById(R.id.edCountryCode2);

        amountPay = (TextView)convertView.findViewById(R.id.amountPay);
        recipeintAmountGET = (TextView)convertView.findViewById(R.id.recipeintAmountGET);

        btnRecharge = (ButtonRectangle)convertView.findViewById(R.id.btnRecharge);

        spCountry= (Spinner)convertView.findViewById(R.id.spCountryRecharge);
        spServiceProvider= (Spinner)convertView.findViewById(R.id.spServiceProvider);
        spRechargeAmount= (Spinner)convertView.findViewById(R.id.spRechargeAmount);

        ProviderImg= (ImageView)convertView.findViewById(R.id.ProviderImg);

        //edRechargeMobileNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());



        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //setting the mobile top carriren name
                MobileTopup_TopupProducts_List = MobileTopup_List.get(position).TopUpProducts;

                MobileTopUp_TopupProductsAdapter TopupProductsadpater = new MobileTopUp_TopupProductsAdapter(getActivity(),R.layout.spinner_country, MobileTopup_TopupProducts_List);
                spServiceProvider.setAdapter(TopupProductsadpater);

                edCountryCode1.setText(String.valueOf(MobileTopup_List.get(spCountry.getSelectedItemPosition()).countryCode));
                edCountryCode2.setText(String.valueOf(MobileTopup_List.get(spCountry.getSelectedItemPosition()).countryCode));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spServiceProvider.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //setting the recharge amount
                MobileTopup_rechargeservice_List  = MobileTopup_TopupProducts_List.get(position).RechargeService;


                MobileTopUp_RechargeServiceAdapter RechargeServiceadpater = new MobileTopUp_RechargeServiceAdapter(getActivity(),R.layout.spinner_country, MobileTopup_rechargeservice_List);
                spRechargeAmount.setAdapter(RechargeServiceadpater);

                String temp= MobileTopup_TopupProducts_List.get(position).carrierName.toString();
                Log.e("Item Select",temp);

                SetProviderImage(temp);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spRechargeAmount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CalculateRechargePrice(position,spServiceProvider.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isEmptyField(edRechargeMobileNumber)){
                    SnackBar bar = new SnackBar(getActivity(),getString(R.string.code_ENTERMOBNO));
                    bar.show();
                }
                else if(isMobileMatch(edRechargeMobileNumber)){
                    SnackBar bar = new SnackBar(getActivity(),getString(R.string.code_ENTERVALIDMOBNO));
                    bar.show();
                }
                else if(isEmptyField(edRechargeConfirmMobileNumber)){
                    SnackBar bar = new SnackBar(getActivity(),getString(R.string.code_ENTERCORRECTNO));
                    bar.show();
                }
                else if(!isMobileMatchValue(edRechargeMobileNumber,edRechargeConfirmMobileNumber)){
                    SnackBar bar = new SnackBar(getActivity(),getString(R.string.code_MOBILEDONOTMATCH));
                    bar.show();
                }

                else{
                    processOTP();

                }
            }
        });
        return convertView;
    }
private void processOTP(){
    try{

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
        User user = complexPreferences.getObject("current_user", User.class);

        JSONObject userObject = new JSONObject();

        userObject.put("Amount",roundup_total);
        userObject.put("UserCountryCode",String.valueOf(user.MobileCountryCode));
        userObject.put("UserID",String.valueOf(user.UserID));
        userObject.put("UserMobileNo", user.MobileNo);
        userObject.put("Culture", LanguageStringUtil.CultureString(getActivity()));

        Log.e("otp object",userObject.toString());

        final CircleDialog circleDialog = new CircleDialog(getActivity(), 0);
        circleDialog.setCancelable(true);
        circleDialog.show();
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.SEND_OTP, userObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jobj) {
                circleDialog.dismiss();
                String response = jobj.toString();
                Log.e("cash out  Response", "" + response);
                 OTP otpobj= new GsonBuilder().create().fromJson(jobj.toString(), OTP.class);

                try{
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("ResponseCode").equalsIgnoreCase("1")){

                        OTPDialog otpDialog = new OTPDialog(getActivity(),0,otpobj.VerificationCode);
                        otpDialog.setOnConfirmListner(new OTPDialog.OnConfirmListner() {
                            @Override
                            public void onComplete(String response) {
                                processRecharge();
                            }
                        });


                    }

                    else {
                        SnackBar bar = new SnackBar(getActivity(),obj.getString("ResponseMsg"));
                        bar.show();
                    }

                } catch (Exception e) {
                    Log.e("error response recharge1: ", e.toString() + "");
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                circleDialog.dismiss();
                Log.e("error response live curreency: ", error + "");
                SnackBar bar = new SnackBar(getActivity(),"Network Error !!!");
                bar.show();
            }
        });


        req.setRetryPolicy(  new DefaultRetryPolicy(0,0,0));
        MyApplication.getInstance().addToRequestQueue(req);
    }catch(Exception e){
        Log.e("exception",e.toString());
    }

}
public boolean isMobileMatchValue(EditText param1, EditText param2) {
        boolean isMatch = false;
        if (param1.getText().toString().equals(param2.getText().toString())) {
            isMatch = true;
        }
        return isMatch;
    }
public boolean isMobileMatch(EditText param1) {

        boolean isEmpty = false;
        if ((param1.getText() == null || param1.getText().toString().equalsIgnoreCase(""))) {
            isEmpty = true;
        } else if (param1.getText().toString().length() < 9 || param1.getText().toString().length() > 10) {
            isEmpty = true;
        }


        return isEmpty;

    }
private void SetProviderImage(String  imageName){

    try {

        Log.e("full path",String.valueOf(AppConstants.providerImageURL+imageName.replaceAll(" ","")+".png"));
        Picasso.with(getActivity().getBaseContext()).load(AppConstants.providerImageURL + imageName.replaceAll(" ", "") + ".png").into(ProviderImg);

    }
    catch(Exception e){
        Log.e("Execpetion occurs loading provider image",e.toString());
    }
}

private void fetchMobileTopupDetials(){

    final CircleDialog circleDialog=new CircleDialog(getActivity(),0);
    circleDialog.setCancelable(true);
    circleDialog.show();

    new CallWebService(AppConstants.GET_MOBILE_TOPUP_DETAILS,CallWebService.TYPE_JSONARRAY) {

        @Override
        public void response(String response) {
            Log.e("mob top response",response);
            circleDialog.dismiss();

            Type listType=new TypeToken<List<MobileTopup_Main>>(){
            }.getType();
            MobileTopup_List =  new GsonBuilder().create().fromJson(response, listType);
            Log.e("size of mobile top list", String.valueOf(MobileTopup_List.size()));

            //setting the mobile top company
            MobileTopUp_CoutryAdapter countryadpater = new MobileTopUp_CoutryAdapter(getActivity(),R.layout.spinner_country, MobileTopup_List);
            spCountry.setAdapter(countryadpater);


        }

        @Override
        public void error(VolleyError error) {
            SnackBar bar = new SnackBar(getActivity(),getString(R.string.code_SERVERERROR));
            bar.show();
            circleDialog.dismiss();
        }
    }.start();



    Log.e("mobile topup","web service end");



}



private void CalculateRechargePrice(int rechargeAmountPosition,int serviceProviderPosition){

    String rechargePrice = String.valueOf(MobileTopup_rechargeservice_List.get(rechargeAmountPosition).rechargePrice);
    Float EuroRate = MobileTopup_List.get(serviceProviderPosition).USDtoEuro;

    Float charges = (Float.parseFloat(rechargePrice)/100);
    Log.e(" charges",String.valueOf(charges));

    Float Total = charges*EuroRate;
     roundup_total = String.format("%.2f", Total);

    Log.e("Total",String.valueOf(roundup_total));
    amountPay.setText("You have to Pay € "+ LanguageStringUtil.languageString(getActivity(), String.valueOf(String.valueOf(roundup_total))));
    recipeintAmountGET.setText("Your recipient gets "+MobileTopup_rechargeservice_List.get(rechargeAmountPosition).currency+" "+LanguageStringUtil.languageString(getActivity(),String.valueOf(String.valueOf(MobileTopup_rechargeservice_List.get(rechargeAmountPosition).LocalPrice))));

}

public void processRecharge(){


               try{

                   CalculateRechargePrice(spRechargeAmount.getSelectedItemPosition(),spServiceProvider.getSelectedItemPosition());

                   JSONObject userObject = new JSONObject();
                   ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
                   User user = complexPreferences.getObject("current_user", User.class);

                   userObject.put("mobileNo", edRechargeMobileNumber.getText().toString().trim());

                   userObject.put("countryCode", MobileTopup_List.get(spCountry.getSelectedItemPosition()).shortCode.trim());
                   userObject.put("topupCode", MobileTopup_TopupProducts_List.get(spServiceProvider.getSelectedItemPosition()).carrierCode);

                   String newvalue= MobileTopup_rechargeservice_List.get(spRechargeAmount.getSelectedItemPosition()).rechargePrice.trim();
                   newvalue = newvalue.replaceAll("\\,", ".");


                   userObject.put("rechargeAmount",newvalue);



                   userObject.put("LiveConAmt",MobileTopup_List.get(spServiceProvider.getSelectedItemPosition()).USDtoEuro);
                   userObject.put("userID",user.UserID);
                   userObject.put("Culture", LanguageStringUtil.CultureString(getActivity()));

                   Log.e("json obj rechrge",userObject.toString());

                   final CircleDialog circleDialog = new CircleDialog(getActivity(), 0);
                   circleDialog.setCancelable(true);
                   circleDialog.show();

                   JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.MOBILE_TOPUP, userObject, new Response.Listener<JSONObject>() {

                       @Override
                       public void onResponse(JSONObject jobj) {
                           circleDialog.dismiss();
                           String response = jobj.toString();
                           Log.e("Recharge Response", "" + response);


                           try{
                               JSONObject obj = new JSONObject(response);
                               if(obj.getString("ResponseCode").equalsIgnoreCase("1")){

                                   SnackBar bar = new SnackBar(getActivity(),getString(R.string.code_RECHARGEDONE));
                                   bar.show();

                                   CountDownTimer countDownTimer;
                                   countDownTimer = new MyCountDownTimer(3000, 1000); // 1000 = 1s
                                   countDownTimer.start();
                               }

                               else {
                                   SnackBar bar112 = new SnackBar(getActivity(), obj.getString("ResponseMsg"));
                                   bar112.show();

                               }

                           } catch (Exception e) {
                               Log.e("error response recharge1: ", e.toString() + "");
                           }


                       }
                   }, new Response.ErrorListener() {

                       @Override
                       public void onErrorResponse(VolleyError error) {

                           circleDialog.dismiss();
                           Log.e("error response recharge2: ", error + "");
                           SnackBar bar = new SnackBar(getActivity(),getString(R.string.code_NWERROR));
                           bar.show();

                       }
                   });


                   req.setRetryPolicy(  new DefaultRetryPolicy(0,0,0));
                   MyApplication.getInstance().addToRequestQueue(req);

                   // end of main try block
               } catch(Exception e){
                   Log.e("error in recharge",e.toString());
               }


    }


 public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }
        @Override
        public void onFinish() {
            Log.e("counter","Time's up!");

            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.main_container,new MyAccountFragment());
            ft.commit();
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

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

public class MobileTopUp_RechargeServiceAdapter extends ArrayAdapter<MobileTopup_RechargeService> {
        Context context;
        int layoutResourceId;
        ArrayList<MobileTopup_RechargeService> values;
        // int android.R.Layout.

        public MobileTopUp_RechargeServiceAdapter(Context context, int resource, ArrayList<MobileTopup_RechargeService> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values=objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(getActivity());
            txt.setPadding(16,16,16,16);
            txt.setGravity(Gravity.CENTER_VERTICAL);

            txt.setText(LanguageStringUtil.languageString(getActivity(),String.valueOf(values.get(position).LocalPrice)));
            return  txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(getActivity());
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setText(LanguageStringUtil.languageString(getActivity(),String.valueOf(values.get(position).LocalPrice)));
            return  txt;
        }
    }

public class MobileTopUp_TopupProductsAdapter extends ArrayAdapter<MobileTopup_TopUpProducts> {

        Context context;
        int layoutResourceId;
        ArrayList<MobileTopup_TopUpProducts> values;
        // int android.R.Layout.

 public MobileTopUp_TopupProductsAdapter(Context context, int resource, ArrayList<MobileTopup_TopUpProducts> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values=objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(getActivity());
            txt.setPadding(16,16,16,16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).carrierName);
            return  txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(getActivity());
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setText(values.get(position).carrierName);
            return  txt;
        }
    }

 public class MobileTopUp_CoutryAdapter extends ArrayAdapter<MobileTopup_Main> {

        Context context;
        int layoutResourceId;
        ArrayList<MobileTopup_Main> values;
        // int android.R.Layout.

        public MobileTopUp_CoutryAdapter(Context context, int resource, ArrayList<MobileTopup_Main> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values=objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(getActivity());
            txt.setPadding(16,16,16,16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(" "+values.get(position).countryName);

            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setGravity(Gravity.CENTER_VERTICAL);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 16;

            int w= (int)getResources().getDimension(R.dimen.flag_width1);
            int h= (int)getResources().getDimension(R.dimen.flag_height1);
            LinearLayout.LayoutParams params_image = new LinearLayout.LayoutParams(dpToPx(w),dpToPx(h));

            ImageView img = new ImageView(context);
            if (values.get(position).shortCode == null || values.get(position).shortCode.equalsIgnoreCase("") || values.get(position).shortCode.equalsIgnoreCase("NULL")) {
            } else {
                try {
                    img.setImageBitmap(getBitmapFromAsset(values.get(position).shortCode.toString().trim().toLowerCase()+".png"));

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

            txt.setPadding(16, 16, 16, 16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(" "+values.get(position).countryName);
            LinearLayout.LayoutParams main_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout layout = new LinearLayout(context);

            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setGravity(Gravity.CENTER_VERTICAL);
            layout.setLayoutParams(main_params);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 4;

            int w= (int)getResources().getDimension(R.dimen.flag_width1);
            int h= (int)getResources().getDimension(R.dimen.flag_height1);


            LinearLayout.LayoutParams params_image = new LinearLayout.LayoutParams(dpToPx(w),dpToPx(h));
            ImageView img = new ImageView(context);


            if (values.get(position).shortCode == null || values.get(position).shortCode.equalsIgnoreCase("") || values.get(position).shortCode.equalsIgnoreCase("NULL")) {
            } else {
                try {
                    img.setImageBitmap(getBitmapFromAsset(values.get(position).shortCode.toString().trim().toLowerCase()+".png"));

                } catch (Exception e) {
                    Log.e("MyTag", "Failure to get drawable id.", e);
                }


            }

            layout.addView(img, params_image);
            layout.addView(txt, params);
            return  layout;
        }
    }


    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }


    private Bitmap getBitmapFromAsset(String strName)
    {
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
private class OTP{
    @SerializedName("VerificationCode")
    public String VerificationCode;
}
 // end of main class
}
