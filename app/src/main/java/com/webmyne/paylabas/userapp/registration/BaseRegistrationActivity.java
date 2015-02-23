package com.webmyne.paylabas.userapp.registration;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ImageView;
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
    private ImageView imgUS,imgFrance;
    private boolean isEnglisSelected;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_registration);

        init();
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(BaseRegistrationActivity.this, "user_pref", 0);
        User user = complexPreferences.getObject("current_user", User.class);

        imgUS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isEnglisSelected= PrefUtils.isEnglishSelected(BaseRegistrationActivity.this);
                if(isEnglisSelected){
                    showLanguageAlert("en");
                }

            }
        });

        imgFrance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isEnglisSelected= PrefUtils.isEnglishSelected(BaseRegistrationActivity.this);
                if(!isEnglisSelected){
                    showLanguageAlert("fr");
                }

            }
        });




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

    private void setLanguage() {

        isEnglisSelected= PrefUtils.isEnglishSelected(BaseRegistrationActivity.this);
        if(PrefUtils.isEnglishSelected(BaseRegistrationActivity.this)){
            imgUS.setColorFilter(Color.argb(128, 0, 0, 0));
            Configuration config = new Configuration();
            config.locale = Locale.FRANCE;
            getResources().updateConfiguration(config, null);
           /* etMerchantId.setHint("Merchant ID");
            etSecretId.setHint("Password");
            btnLoginNext.setText("NEXT");*/
            btnSignIn.setText("SE CONNECTER");
            btnSignUp.setText("SIGNER");

        } else {
            imgFrance.setColorFilter(Color.argb(128, 0, 0, 0));
            Configuration config = new Configuration();
            config.locale = Locale.ENGLISH;
            getResources().updateConfiguration(config, null);
            /*etMerchantId.setHint("Merchant ID");
            etSecretId.setHint("Password");
            btnLoginNext.setText("NEXT");*/

            btnSignIn.setText("SIGN IN");
            btnSignUp.setText("SIGN UP");


        }


       /* if(isLoggedIn(LoginActivity.this)){
            Intent intent =new Intent(LoginActivity.this,VerificationActivity.class);
            startActivity(intent);
            finish();
        }*/
    }

    private void showLanguageAlert(final String languageType){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Change Language");
        if(languageType.equalsIgnoreCase("en")){
            alert.setMessage("Are you sure, you want to change language to English");
        } else {
            alert.setMessage("Are you sure, yo want to change language to French");
        }
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                if(languageType.equalsIgnoreCase("en")){

                    PrefUtils.setEnglishSelected(BaseRegistrationActivity.this, false);
                    imgUS.clearColorFilter();
                    imgFrance.setColorFilter(Color.argb(128, 0, 0, 0));


                } else {
                    PrefUtils.setEnglishSelected(BaseRegistrationActivity.this,true);
                    imgFrance.clearColorFilter();
                    imgUS.setColorFilter(Color.argb(128, 0, 0, 0));


                }
                changeLanguage(languageType);
                dialog.dismiss();

            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });

        alert.show();

    }

    private void changeLanguage(String languageType){

        if(languageType.equalsIgnoreCase("en")){
            Log.e("eng","eng");
            Configuration config = new Configuration();
            config.locale = Locale.ENGLISH;
            getResources().updateConfiguration(config, null);

            btnSignIn.setText("SIGN IN");
            btnSignUp.setText("SIGN UP");


        } else {
            Log.e("french","french");
            Configuration config = new Configuration();
            config.locale = Locale.FRANCE;
            getResources().updateConfiguration(config, null);

            btnSignIn.setText("SE CONNECTER");
            btnSignUp.setText("SIGNER");

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

        imgUS= (ImageView) findViewById(R.id.imgUS);
        imgFrance= (ImageView) findViewById(R.id.imgFrance);

        edLoginCountryCode = (EditText)findViewById(R.id.edCodeCountry);
        edLoginEnterMobileNo = (EditText)findViewById(R.id.edEnterMobileNo);
        edLoginPassword = (EditText)findViewById(R.id.edPassword);


    }


    @Override
    protected void onResume() {
        super.onResume();
        setLanguage();
        imgUS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isEnglisSelected= PrefUtils.isEnglishSelected(BaseRegistrationActivity.this);
                if(isEnglisSelected){
                    showLanguageAlert("en");
                }

            }
        });

        imgFrance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isEnglisSelected= PrefUtils.isEnglishSelected(BaseRegistrationActivity.this);
                if(!isEnglisSelected){
                    showLanguageAlert("fr");
                }

            }
        });
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
