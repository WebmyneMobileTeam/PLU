package com.webmyne.paylabas.userapp.registration;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.webmyne.paylabas.userapp.base.MyApplication;
import com.webmyne.paylabas.userapp.base.MyDrawerActivity;
import com.webmyne.paylabas.userapp.custom_components.CircleDialog;
import com.webmyne.paylabas.userapp.model.AppConstants;
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

    private ButtonFlat btnRegisterFromLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        spCountry = (Spinner)findViewById(R.id.spinner_country);
        edLoginEnterMobileNo = (EditText)findViewById(R.id.edEnterMobileNo);

        btnConfirmSignIn = (ButtonRectangle)findViewById(R.id.btnConfirmSignIn);
        btnConfirmSignIn.setOnClickListener(this);

        edLoginCountryCode = (EditText)findViewById(R.id.edCodeCountry);
        edLoginEnterMobileNo = (EditText)findViewById(R.id.edEnterMobileNo);
        edLoginPassword = (EditText)findViewById(R.id.edPassword);

        btnRegisterFromLogin = (ButtonFlat)findViewById(R.id.btnRegisterFromLogin);
        btnRegisterFromLogin.setOnClickListener(this);

        setUpCountry();

    }


    @SuppressWarnings("static-access")
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

        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                // new CustomAsyncTask(EnterMobileNoPage.this, "pre_findcode","bck_findcode", "post_findcode").execute();
                new fetch_display_code().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {


            }
        });

    }


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

            ((EditText)findViewById(R.id.edCodeCountry)).setText("+"+codeString.split(",")[0]);
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){


            case R.id.btnConfirmSignIn:

                processSignIn();

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

            userObject.put("MobileCountryCode","91");
            userObject.put("MobileNo","9428427305");
            userObject.put("Password","milan");

            final CircleDialog circleDialog=new CircleDialog(LoginActivity.this,0);
            circleDialog.setCancelable(true);
            circleDialog.show();

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.USER_LOGIN, userObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jobj) {
                    circleDialog.dismiss();
                    String response = jobj.toString();
                    Log.e("Response : ", "" + response);
                    Intent i = new Intent(LoginActivity.this, MyDrawerActivity.class);
                    startActivity(i);
                    finish();


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    circleDialog.dismiss();
                    Log.e("error response: ",error+"");
                    Intent i = new Intent(LoginActivity.this, MyDrawerActivity.class);
                    startActivity(i);
                    finish();
                }
            });
            MyApplication.getInstance().addToRequestQueue(req);


        }catch (Exception e){

        }





    }

}
