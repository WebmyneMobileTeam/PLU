package com.webmyne.paylabas.userapp.registration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.gc.materialdesign.views.ButtonRectangle;
import com.webmyne.paylabas.userapp.base.MyDrawerActivity;
import com.webmyne.paylabas.userapp.base.PrefUtils;
import com.webmyne.paylabas.userapp.helpers.ComplexPreferences;
import com.webmyne.paylabas.userapp.model.User;
import com.webmyne.paylabas_user.R;

import java.util.ArrayList;
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
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(BaseRegistrationActivity.this, "user_pref", 0);
        User user = complexPreferences.getObject("current_user", User.class);

        try {
            setLanguage();
            SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
            boolean isUserLogin = preferences.getBoolean("isUserLogin", false);

            if (isUserLogin == true) {

                Intent i = new Intent(BaseRegistrationActivity.this, EnterPinActivity.class);
                i.putExtra("cup",preferences.getString("cup",""));
                startActivity(i);
                finish();



             /*   Intent i = new Intent(BaseRegistrationActivity.this, MyDrawerActivity.class);
                startActivity(i);
                finish();
                */


            }
            else if(user.isVerified==false && user.VerificationCode!=null){

                Intent i = new Intent(BaseRegistrationActivity.this, ConfirmationActivity.class);
                startActivity(i);
                finish();
            }
        }catch(Exception e){

        }





    }


    private void init() {

        spCountry = (Spinner)findViewById(R.id.spinner_country);
        edLoginEnterMobileNo = (EditText)findViewById(R.id.edEnterMobileNo);
      //  loginLayout = (RelativeLayout)findViewById(R.id.loginLayout);
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


    private void setLanguage() {


        if(PrefUtils.isEnglishSelected(BaseRegistrationActivity.this)){
            Log.e("country","france");
            Configuration config = new Configuration();
            config.locale = Locale.FRANCE;
            getResources().updateConfiguration(config, null);

        } else {
            Log.e("country","english");
            Configuration config = new Configuration();
            config.locale = Locale.ENGLISH;
            getResources().updateConfiguration(config, null);

        }


       /* if(isLoggedIn(LoginActivity.this)){
            Intent intent =new Intent(LoginActivity.this,VerificationActivity.class);
            startActivity(intent);
            finish();
        }*/
    }

}
