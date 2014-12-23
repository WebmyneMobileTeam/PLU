package com.webmyne.paylabas.userapp.registration;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.widgets.SnackBar;
import com.google.gson.GsonBuilder;
import com.webmyne.paylabas.userapp.base.DatabaseWrapper;
import com.webmyne.paylabas.userapp.base.MyApplication;
import com.webmyne.paylabas.userapp.base.MyDrawerActivity;
import com.webmyne.paylabas.userapp.custom_components.CircleDialog;
import com.webmyne.paylabas.userapp.helpers.AppConstants;
import com.webmyne.paylabas.userapp.helpers.ComplexPreferences;
import com.webmyne.paylabas.userapp.model.Country;
import com.webmyne.paylabas.userapp.model.User;
import com.webmyne.paylabas_user.R;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;

public class LoginActivity extends ActionBarActivity implements View.OnClickListener {

    private ButtonRectangle btnConfirmSignIn;
    LinkedHashMap<String, String> mapList = new LinkedHashMap<String, String>();
    Spinner spCountry;
    ArrayList<String> countryList = new ArrayList<String>();
    String codeString = new String();

    private EditText edLoginCountryCode;
    private EditText edLoginEnterMobileNo;
    private EditText edLoginPassword;

    private ButtonFlat btnForgotPin;
    private ButtonFlat btnRegisterFromLogin;
    private DatabaseWrapper db_wrapper;
    ArrayList<Country> countrylist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        spCountry = (Spinner)findViewById(R.id.spinner_country);
        edLoginEnterMobileNo = (EditText)findViewById(R.id.edEnterMobileNo);
        btnForgotPin = (ButtonFlat)findViewById(R.id.btnForgotPin);

        btnConfirmSignIn = (ButtonRectangle)findViewById(R.id.btnConfirmSignIn);
        btnConfirmSignIn.setOnClickListener(this);

        edLoginCountryCode = (EditText)findViewById(R.id.edCodeCountry);
        edLoginEnterMobileNo = (EditText)findViewById(R.id.edEnterMobileNo);
        edLoginPassword = (EditText)findViewById(R.id.edPassword);

        btnRegisterFromLogin = (ButtonFlat)findViewById(R.id.btnRegisterFromLogin);
        btnRegisterFromLogin.setOnClickListener(this);
      //  setUpCountry();
        fetchCountryAndDisplay();

// opens the Forgot pin url
        btnForgotPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                Uri ForgotPinURL = Uri.parse("http://ws-srv-net.in.webmyne.com/Applications/PayLabas_V02/Login/ForgotPassword#");
                i.setData(ForgotPinURL);
                startActivity(i);
               // finish();
            }
        });

    }

    private void fetchCountryAndDisplay() {

        new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                db_wrapper = new DatabaseWrapper(LoginActivity.this);
                try {
                    db_wrapper.openDataBase();
                    countrylist= db_wrapper.getCountryData();
                    db_wrapper.close();
                }catch(Exception e){e.printStackTrace();}

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                CountryAdapter countryAdapter = new CountryAdapter(LoginActivity.this,R.layout.spinner_country, countrylist);
                spCountry.setAdapter(countryAdapter);

                spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                         try{
                             edLoginCountryCode.setText(String.valueOf(countrylist.get(spCountry.getSelectedItemPosition()).CountryCode));
                         }catch(Exception e){

                         }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {


                    }
                });

            }
        }.execute();

    }

    public class CountryAdapter extends ArrayAdapter<Country>{
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

            TextView txt = new TextView(LoginActivity.this);
            txt.setPadding(16,16,16,16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).CountryName);
            return  txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(LoginActivity.this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setText(values.get(position).CountryName);
            return  txt;
        }
    }





  /*  @SuppressWarnings("static-access")
    private void setUpCountry() {

        String[] list = Locale.getISOCountries();
        for(int i=0;i<list.length;i++){

            Locale l = new Locale("",list[i]);
            //System.out.println("- Country Name : "+l.getDisplayCountry()+"- Code : "+list[i]);
            mapList.put(l.getDisplayCountry(), list[i]);
            countryList.add(l.getDisplayCountry());
        }

        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,countryList);
        spCountry.setAdapter(countryAdapter);
        spCountry.setSelection(countryList.indexOf(this.getResources().getConfiguration().locale.getDisplayCountry()));



    }*/

    public class fetch_display_code extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {

            String[] arrCodeList = getResources().getStringArray(R.array.CountryCodes);
            for(int k=0;k<arrCodeList.length;k++){

                String found = mapList.get(spCountry.getSelectedItem().toString());

                if(arrCodeList[k].contains(found)){
                    codeString = arrCodeList[k];

                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ((EditText)findViewById(R.id.edCodeCountry)).setText(""+codeString.split(",")[0]);

        }
    }

    public boolean isMobileEmpty(){

        boolean isEmpty = false;

        if(edLoginEnterMobileNo.getText() == null || edLoginEnterMobileNo.getText().toString().equalsIgnoreCase("")){
            isEmpty = true;
        }

        return isEmpty;

    }

    public boolean isMobileMatch(EditText param1){

        boolean isEmpty = false;
        if((param1.getText() == null || param1.getText().toString().equalsIgnoreCase(""))){
            isEmpty = true;
        }
        else if(param1.getText().toString().length()<9 || param1.getText().toString().length()>10){
            isEmpty = true;
        }


        return isEmpty;

    }




    public boolean isPasswordEmpty(){

        boolean isEmpty = false;

        if(edLoginPassword.getText() == null || edLoginPassword.getText().toString().equalsIgnoreCase("")){
            isEmpty = true;
        }

        return isEmpty;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){


            case R.id.btnConfirmSignIn:


                if(isMobileEmpty() || isPasswordEmpty()){

                    SnackBar bar = new SnackBar(LoginActivity.this,"Please enter mobile no. and password");
                    bar.show();

                }
                else if (isMobileMatch(edLoginEnterMobileNo)){
                    SnackBar bar = new SnackBar(LoginActivity.this,"Please Enter Valid Mobile Number");
                    bar.show();
                }
                else{
                    processSignIn();
                }




                break;

            case R.id.btnRegisterFromLogin:

                Intent i = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(i);
                finish();

                break;

        }

    }

    private void processSignIn() {
        try{

            JSONObject userObject = new JSONObject();

           /* userObject.put("MobileCountryCode","91");
            userObject.put("MobileNo","9428427305");
            userObject.put("Password","milan");*/

            userObject.put("MobileCountryCode",edLoginCountryCode.getText().toString().trim());
            userObject.put("MobileNo",edLoginEnterMobileNo.getText().toString().trim());
            userObject.put("Password",edLoginPassword.getText().toString().trim());

            final CircleDialog circleDialog=new CircleDialog(LoginActivity.this,0);
            circleDialog.setCancelable(true);
            circleDialog.show();

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.USER_LOGIN, userObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jobj) {

                    circleDialog.dismiss();
                    String response = jobj.toString();
                    Log.e("Response : ", "" + response);
                    try{

                        JSONObject obj = new JSONObject(response);

                        if(obj.getString("ResponseMsg").equalsIgnoreCase("Success")){

                            User currentUser = new GsonBuilder().create().fromJson(response,User.class);
                            //store current user and domain in shared preferences
                            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(LoginActivity.this, "user_pref", 0);
                            complexPreferences.putObject("current_user", currentUser);
                            complexPreferences.commit();

                            // set login true

                            SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("isUserLogin",true);
                            editor.commit();

                            Intent i = new Intent(LoginActivity.this, MyDrawerActivity.class);
                            startActivity(i);
                            finish();

                        }else{

                            SnackBar bar = new SnackBar(LoginActivity.this,"Invalid mobile or password");
                            bar.show();

                        }

                    }catch(Exception e){

                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    circleDialog.dismiss();
                    Log.e("error responsegg: ",error+"");
                    SnackBar bar = new SnackBar(LoginActivity.this,error.getMessage());
                    bar.show();

                }
            });
            MyApplication.getInstance().addToRequestQueue(req);


        }catch (Exception e){

        }





    }

}
