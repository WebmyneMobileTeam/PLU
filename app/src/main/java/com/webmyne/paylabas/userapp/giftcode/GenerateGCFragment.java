package com.webmyne.paylabas.userapp.giftcode;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.widgets.SnackBar;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.webmyne.paylabas.userapp.base.AddRecipientActivity;
import com.webmyne.paylabas.userapp.base.DatabaseWrapper;
import com.webmyne.paylabas.userapp.base.MyApplication;
import com.webmyne.paylabas.userapp.base.MyDrawerActivity;
import com.webmyne.paylabas.userapp.custom_components.CircleDialog;
import com.webmyne.paylabas.userapp.helpers.AppConstants;
import com.webmyne.paylabas.userapp.helpers.CallWebService;
import com.webmyne.paylabas.userapp.helpers.ComplexPreferences;
import com.webmyne.paylabas.userapp.helpers.FormValidator;
import com.webmyne.paylabas.userapp.home.MyAccountFragment;
import com.webmyne.paylabas.userapp.model.Country;
import com.webmyne.paylabas.userapp.model.GiftCode;
import com.webmyne.paylabas.userapp.model.Receipient;
import com.webmyne.paylabas.userapp.model.User;
import com.webmyne.paylabas.userapp.registration.LoginActivity;
import com.webmyne.paylabas.userapp.registration.SignUpActivity;
import com.webmyne.paylabas_user.R;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GenerateGCFragment extends Fragment implements TextWatcher,View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private   AlertDialog alertDialogBuilder;

    private EditText edMobileNumberGenerateGC;
    private EditText edAmountGenerateGC;

    private Spinner spRecipients;
    private Spinner spCountry;

    private ButtonRectangle btnResetGenerateGC;
    private ButtonRectangle btnGenerateGCGenerateGC;

    private User user;
    private Spinner spinnerRecipientContactGenerateGc;
    private Spinner spinnerCountryGenerateGc;

    private ArrayList<Receipient> receipients;
    private ArrayList<Country> countries;
    private DatabaseWrapper db_wrapper;
    private TextView txtCCGenerateGC;
    DecimalFormat df = new DecimalFormat("#.00");
    private ServiceCharge charge;
    private LinearLayout mainLinear;
    private View viewService;



    int temp_posCountrySpinner;
    public static GenerateGCFragment newInstance(String param1, String param2) {

        GenerateGCFragment fragment = new GenerateGCFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }

    public GenerateGCFragment() {
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

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
        user = complexPreferences.getObject("current_user", User.class);

        receipients = new ArrayList<Receipient>();
        countries = new ArrayList<Country>();

        fetchReceipientsAndDisplay();
        fetchCountryAndDisplay();


        spinnerCountryGenerateGc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0){

                }else{
                    processCountrySelection(position);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerRecipientContactGenerateGc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0){

                }else{

                    processSelectionWholeReceipient(position);


                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void processCountrySelection(int position) {
        temp_posCountrySpinner=position;
        txtCCGenerateGC.setText(String.format("+%s",countries.get(position).CountryCode));

    }

    private void processSelectionWholeReceipient(int position) {

        Receipient resp = receipients.get(position);
        spinnerCountryGenerateGc.setSelection((int)resp.Country);
        edMobileNumberGenerateGC.setText(resp.MobileNo);

    }

    private void fetchCountryAndDisplay() {


        new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                db_wrapper = new DatabaseWrapper(getActivity());
                try {
                    db_wrapper.openDataBase();
                    countries= db_wrapper.getCountryData();
                    db_wrapper.close();


                    Country country = new Country(0,"Select Country",0,"",0,"","");
                    countries.add(0,country);


                }catch(Exception e){e.printStackTrace();}


                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                CountryAdapter countryAdapter = new CountryAdapter(getActivity(),R.layout.spinner_country, countries);
                spinnerCountryGenerateGc.setAdapter(countryAdapter);

            }
        }.execute();

    }

    private void fetchReceipientsAndDisplay() {

        new CallWebService(AppConstants.GETRECEIPIENTS +user.UserID,CallWebService.TYPE_JSONARRAY) {

            @Override
            public void response(String response) {

                Log.e("Receipients List",response);
                if(response == null){

                }else{

                    Type listType=new TypeToken<List<Receipient>>(){
                    }.getType();
                    receipients =  new GsonBuilder().create().fromJson(response, listType);

                    Receipient receipient = new Receipient();
                    receipient.FirstName = "Select";
                    receipient.LastName = "Receipient";
                    receipients.add(0,receipient);

                    ReceipientAdapter countryAdapter = new ReceipientAdapter(getActivity(),R.layout.spinner_country, receipients);
                    spinnerRecipientContactGenerateGc.setAdapter(countryAdapter);
                }

            }

            @Override
            public void error(VolleyError error) {

            }
        }.start();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View convertView = inflater.inflate(R.layout.fragment_generate_gc, container, false);
        init(convertView);
        return convertView;
    }

    private void init(View convertView) {


        mainLinear = (LinearLayout)convertView.findViewById(R.id.mainlineargenerategc);
        viewService = (View)convertView.findViewById(R.id.linearService);
        viewService.setVisibility(View.GONE);

        edAmountGenerateGC = (EditText)convertView.findViewById(R.id.edAmountGenerateGC);
        edMobileNumberGenerateGC = (EditText)convertView.findViewById(R.id.edMobileNumberGenerateGC);

        edMobileNumberGenerateGC.addTextChangedListener(this);
        edAmountGenerateGC.addTextChangedListener(this);

        btnResetGenerateGC = (ButtonRectangle)convertView.findViewById(R.id.btnResetGenerateGC);
        btnGenerateGCGenerateGC = (ButtonRectangle)convertView.findViewById(R.id.btnGenerateGCGenerateGC);

        btnGenerateGCGenerateGC.setOnClickListener(this);
        btnResetGenerateGC.setOnClickListener(this);

        spinnerRecipientContactGenerateGc = (Spinner)convertView.findViewById(R.id.spinnerRecipientContactGenerateGc);
        spinnerCountryGenerateGc = (Spinner)convertView.findViewById(R.id.spinnerCountryGenerateGc);

        txtCCGenerateGC = (TextView)convertView.findViewById(R.id.txtCCGenerateGC);

        edAmountGenerateGC.addTextChangedListener(new TextWatcher() {

            private String current = "";


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


             /*  if(!s.toString().equals(current)){

                    edAmountGenerateGC.removeTextChangedListener(this);
                    String cleanString = s.toString().replaceAll("[,.]", "");
                    NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.FRANCE);
                    String currencySymbol = formatter.getCurrency().getSymbol();
                    cleanString = cleanString.replace(currencySymbol,"");
                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getCurrencyInstance(Locale.FRANCE).format((parsed/100));

                    current = formatted;
                    edAmountGenerateGC.setText(formatted);

                    edAmountGenerateGC.setSelection(formatted.length());
                    edAmountGenerateGC.addTextChangedListener(this);
                }*/

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });




    }




    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {


    }

    @Override
    public void afterTextChanged(Editable s) {

        activeReset();
    }

    public void activeReset(){

        btnResetGenerateGC.setEnabled(true);
        btnResetGenerateGC.setBackgroundColor(getResources().getColor(R.color.paylabas_dkgrey));


    }

    public void passiveReset(){


        btnResetGenerateGC.setEnabled(false);
        btnResetGenerateGC.setBackgroundColor(getResources().getColor(R.color.paylabas_grey));
}

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnGenerateGCGenerateGC:

                if(viewService.isShown()){

                    processGenerate();

                }else{
                    int indexCountry = spinnerCountryGenerateGc.getSelectedItemPosition();
                    if(indexCountry == 0){

                        SnackBar bar = new SnackBar(getActivity(),"Please Select Country");
                        bar.show();

                    }else{
                        checkProcess();
                    }
                }

                break;

            case R.id.btnResetGenerateGC:

                if(viewService.isShown()){

                   setupMain();

                }else{
                    resetAll();

                }
                break;
        }

    }

    private void checkProcess() {

                    ArrayList<View> arr = new ArrayList<>();
                    arr.add(edMobileNumberGenerateGC);
                    arr.add(edAmountGenerateGC);
                    new FormValidator(new FormValidator.ResultValidationListner() {
                        @Override
                        public void complete() {



                            final CircleDialog circleDialog=new CircleDialog(getActivity(),0);
                            circleDialog.setCancelable(true);
                            circleDialog.show();

                            String postfix = "/"+user.UserID+"/"+edAmountGenerateGC.getText().toString();

                            new CallWebService(AppConstants.SERVICE_CHARGE+postfix, CallWebService.TYPE_JSONOBJECT) {

                                @Override
                                public void response(String response) {
                                    circleDialog.dismiss();
                                    Log.e("---- Service Charge Response ",response);
                                     charge = new GsonBuilder().create().fromJson(response,ServiceCharge.class);
                                    
                                if(validateChagresAndDisplay() == true){
                                           processDialog();
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
                        public void error(String error) {

                            SnackBar bar = new SnackBar(getActivity(),String.format("Please enter %s",error));
                            bar.show();
                        }
                    }).validate(arr);
    }

    private void processDialog() {

      /*  Dialog dialog = new Dialog(getActivity(),android.R.style.Theme_Translucent);
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.item_dialog_generategc,null);
        dialog.setContentView(dialogView);
        dialog.setCancelable(true);
        dialog.show();*/

        setupService();


    }

    private boolean validateChagresAndDisplay(){

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
        user = complexPreferences.getObject("current_user", User.class);
        boolean isComplete = false;
        double value = Double.parseDouble(edAmountGenerateGC.getText().toString());
        double user_value = Double.parseDouble(user.LemonwayAmmount);

        if(value<charge.MinLimit){

            isComplete = false;
            edAmountGenerateGC.setError("Minimum Amount is € "+charge.MinLimit+" For This Service");

        }else if(value > charge.MaxLimit){

            isComplete = false;
            edAmountGenerateGC.setError("Maximum Amount is € "+charge.MaxLimit+" For This Service");


        }else if(value>user_value){

            isComplete = false;
            edAmountGenerateGC.setError("Insufficient balance");

        }else{
            isComplete = true;
        }

        return isComplete;
    }


    private void setupMain(){

        mainLinear.setVisibility(View.VISIBLE);
        viewService.setVisibility(View.GONE);
        btnResetGenerateGC.setText("Reset");
        btnGenerateGCGenerateGC.setText("Check Price");
         refreshBalance();
    }

    public void refreshBalance(){

        ((MyDrawerActivity)getActivity()).setToolTitle("Hi, "+user.FName);
        ((MyDrawerActivity)getActivity()).showToolLoading();

        new CallWebService(AppConstants.USER_DETAILS+user.UserID,CallWebService.TYPE_JSONOBJECT) {

            @Override
            public void response(String response) {

                Log.e("Response User Details ",response);
                try{

                    JSONObject obj = new JSONObject(response);
                    try{
                        ((MyDrawerActivity)getActivity()).setToolSubTitle("Balance "+getResources().getString(R.string.euro)+" "+obj.getString("LemonwayBal"));
                        ((MyDrawerActivity)getActivity()).hideToolLoading();
                    }catch(Exception e){

                    }

                }catch(Exception e){

                }

            }

            @Override
            public void error(VolleyError error) {
                ((MyDrawerActivity)getActivity()).hideToolLoading();

            }
        }.start();


    }

    private void setupService(){

        mainLinear.setVisibility(View.GONE);
        viewService.setVisibility(View.VISIBLE);
        btnResetGenerateGC.setText("Back");
        btnGenerateGCGenerateGC.setText("Generate GC");

        TextView txtMobileGenerateGCService = (TextView)viewService.findViewById(R.id.txtMobileGenerateGCService);
        TextView txtAmountGenerateGCService = (TextView)viewService.findViewById(R.id.txtAmountGenerateGCService);
        TextView txtPayableAmountGenerateGCService = (TextView)viewService.findViewById(R.id.txtPayableAmountGenerateGCService);

        TextView txtPaylabasChargeGenerateGCService = (TextView)viewService.findViewById(R.id.txtPaylabasChargeGenerateGCService);
        TextView txtTransactionChargeGenerateGCService = (TextView)viewService.findViewById(R.id.txtTransactionChargeGenerateGCService);

        double percentageCharge = charge.PercentageCharge;
        double amount = Double.parseDouble(edAmountGenerateGC.getText().toString());
        double displayPercentageCharge = (amount*percentageCharge)/100;
        DecimalFormat df = new DecimalFormat("#.##");
        txtTransactionChargeGenerateGCService.setText(getResources().getString(R.string.euro)+" "+String.format("%1$.2f",displayPercentageCharge));
        txtMobileGenerateGCService.setText(edMobileNumberGenerateGC.getText().toString());
        txtAmountGenerateGCService.setText(getResources().getString(R.string.euro)+" "+edAmountGenerateGC.getText().toString());
        txtPayableAmountGenerateGCService.setText(getResources().getString(R.string.euro)+" "+charge.PayableAmount);
        txtPaylabasChargeGenerateGCService.setText(getResources().getString(R.string.euro)+" "+charge.FixCharge);
    }


    private void processGenerate() {

        try{

            JSONObject generateObject = new JSONObject();

     /*       "CountryCode":"String content",
                    "GCAmount":12678967.543233,
                    "MobileNo":"String content",
                    "ResponseCode":"String content",
                    "ResponseMsg":"String content",
                    "SenderID":9223372036854775807*/

            generateObject.put("CountryCode", txtCCGenerateGC.getText().toString().replace("+","").trim());
            generateObject.put("GCAmount",edAmountGenerateGC.getText().toString().trim());
            generateObject.put("MobileNo",edMobileNumberGenerateGC.getText().toString().trim());
            generateObject.put("ResponseCode","");
            generateObject.put("ResponseMsg","");
            generateObject.put("SenderID",user.UserID);

            final CircleDialog circleDialog=new CircleDialog(getActivity(),0);
            circleDialog.setCancelable(true);
            circleDialog.show();

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.GENERATE_GC, generateObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jobj) {

                    circleDialog.dismiss();
                    String response = jobj.toString();
                    Log.e("Response Generate GC: ", "" + response);
                    try{
                        JSONObject obj = new JSONObject(response);
                        String responsecode = obj.getString("ResponseCode");

                        if(responsecode.equalsIgnoreCase("1")){

                            SnackBar bar = new SnackBar(getActivity(),"Gift code generated Successfully");
                            bar.show();

                            processCheckMobileExists();
                         //   resetAll();
                            setupMain();

/*
                            try {
                                FragmentManager manager = getActivity().getSupportFragmentManager();
                                MyAccountFragment frag = (MyAccountFragment) manager.findFragmentByTag("MA");
                                if (frag != null) {
                                    frag.refreshBalance();
                                }
                            }catch (Exception e){};*/

                        }else{

                          String errorMSG = "";
                            if(responsecode.equalsIgnoreCase("-2")){
                                errorMSG = "Error In While Generating GiftCode";
                            }else if(responsecode.equalsIgnoreCase("-1")){
                                errorMSG = "Error";
                            }else if(responsecode.equalsIgnoreCase("2")){
                                processCheckMobileExists();
                                errorMSG = "User not Exist with Paylabas";
                            }else if(responsecode.equalsIgnoreCase("3")){
                                errorMSG = "User will blocked for next 24 hours";
                            }else if(responsecode.equalsIgnoreCase("4")){
                                errorMSG = "User Deleted";
                            }else if(responsecode.equalsIgnoreCase("5")){
                                errorMSG = "User is not verified";
                            }else{
                                errorMSG = "Network Error\nPlease try again";
                            }
                            SnackBar bar = new SnackBar(getActivity(),errorMSG);
                            bar.show();

                            setupMain();
                          //  resetAll();
                        }

                    }catch(Exception e){

                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    circleDialog.dismiss();

                    SnackBar bar = new SnackBar(getActivity(),"Network Error");
                    bar.show();

                }
            });

            req.setRetryPolicy(
                    new DefaultRetryPolicy(0,0,0));

            MyApplication.getInstance().addToRequestQueue(req);

        }catch (Exception e){

        }
    }
private void processCheckMobileExists(){


        if(checkIfExistsOrNot()){

        }else{

             final com.gc.materialdesign.widgets.Dialog alert = new com.gc.materialdesign.widgets.Dialog(getActivity(),"Add Recipient","Would you like to add this contact as your Recipient ?");
             alert.show();

             alert.setOnAcceptButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                    Intent i = new Intent(getActivity(), AddRecipientActivity.class);

                    i.putExtra("CountryID",(int)countries.get(temp_posCountrySpinner).CountryID);
                    i.putExtra("Mobileno",edMobileNumberGenerateGC.getText().toString());
                    startActivity(i);
                }
            });
        }

    }

    private boolean checkIfExistsOrNot() {

        boolean isExists = false;
        Log.e("mobile  ",edMobileNumberGenerateGC.getText().toString());
        Log.e("size of recipient  ",String.valueOf(receipients.size()));
        String mob=edMobileNumberGenerateGC.getText().toString();

        for(int i=1;i<receipients.size();i++){
            String temp = receipients.get(i).MobileNo;
            if(temp.equals(mob))
            {
               isExists = true;
               return isExists;
            }
            else{
                isExists = false;
            }
        }


       /* for(Receipient receipient:receipients){

            Log.e("values  ",receipient.MobileNo);

            if(receipient.MobileNo.equalsIgnoreCase(edMobileNumberGenerateGC.getText().toString())){
                Log.e("if block ","if block");
                isExists = true;
                return isExists;

            }else{
                isExists = false;
                Log.e("else block ","else block");
            }

        }*/
        return isExists;



    }

    private void resetAll() {

        edAmountGenerateGC.setText("");
        edMobileNumberGenerateGC.setText("");
        spinnerCountryGenerateGc.setSelection(0);
        spinnerRecipientContactGenerateGc.setSelection(0);
        passiveReset();

    }

    public class ReceipientAdapter extends ArrayAdapter<Receipient> {
        Context context;
        int layoutResourceId;
        ArrayList<Receipient> values;
        // int android.R.Layout.
        public ReceipientAdapter(Context context, int resource, ArrayList<Receipient> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values=objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(getActivity());
            txt.setPadding(16,16,16,16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            if(position == 0){
                txt.setText(values.get(position).FirstName +" "+values.get(position).LastName);

            }else{
                txt.setText(values.get(position).FirstName +" "+values.get(position).LastName + String.format("(+%s %s)",values.get(position).CountryCode,values.get(position).MobileNo));

            }
            return  txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(getActivity());
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            if(position == 0){
                txt.setText(values.get(position).FirstName +" "+values.get(position).LastName);

            }else{
                txt.setText(values.get(position).FirstName +" "+values.get(position).LastName + String.format("( +%s %s)",values.get(position).CountryCode,values.get(position).MobileNo));

            }

            return  txt;
        }
    }


    public class CountryAdapter extends ArrayAdapter<Country> {
        Context context;
        int layoutResourceId;
        ArrayList<Country> values;
        // int android.R.Layout.
        public CountryAdapter(Context context, int resource, ArrayList<Country> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values=objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(getActivity());
            txt.setPadding(16,16,16,16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).CountryName);
            return  txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(getActivity());
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setText(values.get(position).CountryName);
            return  txt;
        }
    }

    public static class ServiceCharge{

        @SerializedName("FixCharge")
        public double FixCharge;
        @SerializedName("LemonwayBal")
        public String LemonwayBal;
        @SerializedName("MaxLimit")
        public double MaxLimit;
        @SerializedName("MinLimit")
        public double MinLimit;
        @SerializedName("PayableAmount")
        public double PayableAmount;
        @SerializedName("PercentageCharge")
        public double PercentageCharge;
        @SerializedName("ResponseCode")
        public String ResponseCode;
        @SerializedName("ResponseMsg")
        public String ResponseMsg;

    }


}
