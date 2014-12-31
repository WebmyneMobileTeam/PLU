package com.webmyne.paylabas.userapp.mobile_topup;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.widgets.SnackBar;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.webmyne.paylabas.userapp.base.DatabaseWrapper;
import com.webmyne.paylabas.userapp.base.MyApplication;
import com.webmyne.paylabas.userapp.base.MyDrawerActivity;
import com.webmyne.paylabas.userapp.custom_components.CircleDialog;
import com.webmyne.paylabas.userapp.helpers.AppConstants;
import com.webmyne.paylabas.userapp.helpers.CallWebService;
import com.webmyne.paylabas.userapp.helpers.ComplexPreferences;
import com.webmyne.paylabas.userapp.home.MyAccountFragment;
import com.webmyne.paylabas.userapp.model.City;
import com.webmyne.paylabas.userapp.model.MobileTopup_Main;
import com.webmyne.paylabas.userapp.model.MobileTopup_RechargeService;
import com.webmyne.paylabas.userapp.model.MobileTopup_TopUpProducts;
import com.webmyne.paylabas.userapp.model.User;
import com.webmyne.paylabas_user.R;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MobileTopupRechargeFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private EditText edRechargeMobileNumber;
    private TextView txtDollarRate;

    private ButtonRectangle btnRecharge;

    private Spinner spCountry;
    private Spinner spServiceProvider;
    private Spinner spRechargeAmount;

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
        txtDollarRate = (TextView)convertView.findViewById(R.id.txtDollarRate);
        btnRecharge = (ButtonRectangle)convertView.findViewById(R.id.btnRecharge);

        spCountry= (Spinner)convertView.findViewById(R.id.spCountryRecharge);
        spServiceProvider= (Spinner)convertView.findViewById(R.id.spServiceProvider);
        spRechargeAmount= (Spinner)convertView.findViewById(R.id.spRechargeAmount);



        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //setting the mobile top carriren name
                MobileTopup_TopupProducts_List = MobileTopup_List.get(position).TopUpProducts;

                MobileTopUp_TopupProductsAdapter TopupProductsadpater = new MobileTopUp_TopupProductsAdapter(getActivity(),R.layout.spinner_country, MobileTopup_TopupProducts_List);
                spServiceProvider.setAdapter(TopupProductsadpater);

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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isEmptyField(edRechargeMobileNumber)){
                    SnackBar bar = new SnackBar(getActivity(),"Please Enter Mobile Number");
                    bar.show();
                }

                else{
                      processRecharge();
                }
            }
        });
        return convertView;
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


            // setting the dollar rate
            txtDollarRate.setText("* 1 USD =  €");

        }

        @Override
        public void error(VolleyError error) {
            SnackBar bar = new SnackBar(getActivity(),"Server Error. Please Try Again");
            bar.show();
            circleDialog.dismiss();
        }
    }.start();



    Log.e("mobile topup","web service end");



}

public void processRecharge(){

        final com.gc.materialdesign.widgets.Dialog alert = new com.gc.materialdesign.widgets.Dialog(getActivity(),"Recharge","Are sure to Continue ?");
        alert.show();

        alert.setOnAcceptButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();

               try{
                   JSONObject userObject = new JSONObject();
                   ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
                   User user = complexPreferences.getObject("current_user", User.class);

                   userObject.put("mobileNo", edRechargeMobileNumber.getText().toString().trim());

                   userObject.put("countryCode", MobileTopup_List.get(spCountry.getSelectedItemPosition()).shortCode.trim());
                   userObject.put("topupCode", MobileTopup_TopupProducts_List.get(spServiceProvider.getSelectedItemPosition()).carrierName);
                   userObject.put("rechargeAmount",MobileTopup_rechargeservice_List.get(spRechargeAmount.getSelectedItemPosition()).rechargeAmount);

                   userObject.put("userID",user.UserID);

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

                                   SnackBar bar = new SnackBar(getActivity(),"Recharge Done");
                                   bar.show();

                                   CountDownTimer countDownTimer;
                                   countDownTimer = new MyCountDownTimer(3000, 1000); // 1000 = 1s
                                   countDownTimer.start();

                               }

                               else {
                                   if(obj.getString("ResponseCode").equalsIgnoreCase("-2")) {
                                       SnackBar bar112 = new SnackBar(getActivity(), "Payment deduction Fail");
                                       bar112.show();
                                   }
                                   else {
                                       SnackBar bar112 = new SnackBar(getActivity(), "Recharge Failed. Please Try again !!!");
                                       bar112.show();
                                   }
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
                           SnackBar bar = new SnackBar(getActivity(),"Network Error. Please Try Again");
                           bar.show();

                       }
                   });

                   MyApplication.getInstance().addToRequestQueue(req);

                   // end of main try block
               } catch(Exception e){
                   Log.e("error in recharge",e.toString());
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

            txt.setText(values.get(position).rechargeAmount);
            return  txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(getActivity());
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setText(values.get(position).rechargeAmount);
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
            txt.setText(values.get(position).countryName);
            return  txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(getActivity());
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setText(values.get(position).countryName);
            return  txt;
        }
    }

 // end of main class
}
