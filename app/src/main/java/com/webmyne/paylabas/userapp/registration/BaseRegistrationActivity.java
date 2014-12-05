package com.webmyne.paylabas.userapp.registration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gc.materialdesign.views.ButtonRectangle;
import com.webmyne.paylabas.userapp.base.MyApplication;
import com.webmyne.paylabas.userapp.base.MyDrawerActivity;
import com.webmyne.paylabas.userapp.custom_components.CircleDialog;
import com.webmyne.paylabas.userapp.model.AppConstants;
import com.webmyne.paylabas.userapp.model.CallWebService;
import com.webmyne.paylabas_user.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;

public class BaseRegistrationActivity extends ActionBarActivity implements View.OnClickListener{



    ButtonRectangle btnSignIn;
    ButtonRectangle btnSignUp;
    LinkedHashMap<String, String> mapList = new LinkedHashMap<String, String>();
    Spinner spCountry;
    ArrayList<String> countryList = new ArrayList<String>();
    String codeString = new String();

    private EditText edLoginCountryCode;
    private EditText edLoginEnterMobileNo;
    private EditText edLoginPassword;


    private RelativeLayout loginLayout;
    private RelativeLayout registrationView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_registration);

        init();




    }

    private void init() {

        spCountry = (Spinner)findViewById(R.id.spinner_country);
        edLoginEnterMobileNo = (EditText)findViewById(R.id.edEnterMobileNo);
        loginLayout = (RelativeLayout)findViewById(R.id.loginLayout);
        registrationView = (RelativeLayout)findViewById(R.id.registrationView);
        btnSignIn = (ButtonRectangle)findViewById(R.id.btnSignIn);
        btnSignUp = (ButtonRectangle)findViewById(R.id.btnSignUp);
        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);


        edLoginCountryCode = (EditText)findViewById(R.id.edCodeCountry);
        edLoginEnterMobileNo = (EditText)findViewById(R.id.edEnterMobileNo);
        edLoginPassword = (EditText)findViewById(R.id.edPassword);

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base_registration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnSignIn:



                Intent i = new Intent(BaseRegistrationActivity.this,LoginActivity.class);
                startActivity(i);
                finish();


                break;

            case R.id.btnSignUp:


                Intent is = new Intent(BaseRegistrationActivity.this,SignUpActivity.class);
                startActivity(is);
                finish();

                break;




        }

    }




}
