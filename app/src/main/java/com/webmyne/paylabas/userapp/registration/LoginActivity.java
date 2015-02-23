package com.webmyne.paylabas.userapp.registration;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.widgets.SnackBar;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.GsonBuilder;
import com.webmyne.paylabas.userapp.base.DatabaseWrapper;
import com.webmyne.paylabas.userapp.base.MyApplication;
import com.webmyne.paylabas.userapp.base.MyDrawerActivity;
import com.webmyne.paylabas.userapp.base.PrefUtils;
import com.webmyne.paylabas.userapp.custom_components.CircleDialog;
import com.webmyne.paylabas.userapp.helpers.AppConstants;
import com.webmyne.paylabas.userapp.helpers.ComplexPreferences;
import com.webmyne.paylabas.userapp.model.Country;
import com.webmyne.paylabas.userapp.model.User;
import com.webmyne.paylabas_user.R;


import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
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
    private CircleDialog circleDialog;
    private GoogleCloudMessaging gcm;
    private String regid;
    private String PROJECT_NUMBER = "92884720384";
    private ImageView imgUS,imgFrance;
    private boolean isEnglisSelected;
    private TextView txtseleccountry;


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
        imgUS= (ImageView) findViewById(R.id.imgUS);
        imgFrance= (ImageView) findViewById(R.id.imgFrance);

        txtseleccountry = (TextView)findViewById(R.id.txtseleccountry);



        imgUS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isEnglisSelected= PrefUtils.isEnglishSelected(LoginActivity.this);
                if(isEnglisSelected){
                    showLanguageAlert("en");
                }

            }
        });

        imgFrance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isEnglisSelected= PrefUtils.isEnglishSelected(LoginActivity.this);
                if(!isEnglisSelected){
                    showLanguageAlert("fr");
                }

            }
        });


        //  setUpCountry();
        fetchCountryAndDisplay();



// opens the Forgot pin url
        btnForgotPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,ForgotPassword.class);
                startActivity(i);
                finish();
               // finish();
            }
        });

    }

    private void setLanguage() {

        isEnglisSelected= PrefUtils.isEnglishSelected(LoginActivity.this);
        if(PrefUtils.isEnglishSelected(LoginActivity.this)){
            imgUS.setColorFilter(Color.argb(128, 0, 0, 0));
            Configuration config = new Configuration();
            config.locale = Locale.FRANCE;
            getResources().updateConfiguration(config, null);
           /* etMerchantId.setHint("Merchant ID");
            etSecretId.setHint("Password");
            btnLoginNext.setText("NEXT");*/
            btnForgotPin.setText("Code PIN oublié?");
            btnRegisterFromLogin.setText("Se enregistrer");
            btnConfirmSignIn.setText("SE CONNECTER");
            edLoginPassword.setHint("Saisissez votre code PIN");
            edLoginEnterMobileNo.setHint("Entrez votre aucune mobiles");
            txtseleccountry.setText("Sélectionnez votre pays");

        } else {
            imgFrance.setColorFilter(Color.argb(128, 0, 0, 0));
            Configuration config = new Configuration();
            config.locale = Locale.ENGLISH;
            getResources().updateConfiguration(config, null);
            /*etMerchantId.setHint("Merchant ID");
            etSecretId.setHint("Password");
            btnLoginNext.setText("NEXT");*/


            btnForgotPin.setText("Forgot Pin?");
            btnRegisterFromLogin.setText("Register");
            btnConfirmSignIn.setText("SIGN IN");
            edLoginPassword.setHint("Enter your pin");
            edLoginEnterMobileNo.setHint("Enter your mobile no");
            txtseleccountry.setText("Select Country");
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

                    PrefUtils.setEnglishSelected(LoginActivity.this, false);
                    imgUS.clearColorFilter();
                    imgFrance.setColorFilter(Color.argb(128, 0, 0, 0));
                } else {
                    PrefUtils.setEnglishSelected(LoginActivity.this,true);
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

            btnForgotPin.setText("Forgot Pin?");
            btnRegisterFromLogin.setText("Register");
            btnConfirmSignIn.setText("SIGN IN");
            edLoginPassword.setHint("Enter your pin");
            edLoginEnterMobileNo.setHint("Enter your mobile no");
            txtseleccountry.setText("Select Country");



        } else {
            Log.e("french","french");
            Configuration config = new Configuration();
            config.locale = Locale.FRANCE;
            getResources().updateConfiguration(config, null);

            btnForgotPin.setText("Code PIN oublié?");
            btnRegisterFromLogin.setText("Se enregistrer");
            btnConfirmSignIn.setText("SE CONNECTER");
            edLoginPassword.setHint("Saisissez votre code PIN");
            edLoginEnterMobileNo.setHint("Entrez votre aucune mobiles");
            txtseleccountry.setText("Sélectionnez votre pays");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setLanguage();
        imgUS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isEnglisSelected= PrefUtils.isEnglishSelected(LoginActivity.this);
                if(isEnglisSelected){
                    showLanguageAlert("en");
                }

            }
        });

        imgFrance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isEnglisSelected= PrefUtils.isEnglishSelected(LoginActivity.this);
                if(!isEnglisSelected){
                    showLanguageAlert("fr");
                }

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


    private Bitmap getBitmapFromAsset(String strName)
    {
        AssetManager assetManager = getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open(strName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        return bitmap;
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
            txt.setText(" "+values.get(position).CountryName);

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

            TextView txt = new TextView(LoginActivity.this);

            txt.setPadding(16, 16, 16, 16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(" "+values.get(position).CountryName);
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


            if (values.get(position).ShortCode == null || values.get(position).ShortCode.equalsIgnoreCase("") || values.get(position).ShortCode.equalsIgnoreCase("NULL")) {
            } else {
                try {
                    img.setImageBitmap(getBitmapFromAsset(values.get(position).ShortCode.toString().trim().toLowerCase()+".png"));

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
        DisplayMetrics displayMetrics = LoginActivity.this.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
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
                    SnackBar bar = new SnackBar(LoginActivity.this,getString(R.string.code_ENTERMOBANDPASSWORD));
                    bar.show();

                }
                else if (isMobileMatch(edLoginEnterMobileNo)){
                    SnackBar bar = new SnackBar(LoginActivity.this,getString(R.string.code_ENETEVALIDMOBILENO));
                    bar.show();
                }
                else{
                    circleDialog=new CircleDialog(LoginActivity.this,0);
                    circleDialog.setCancelable(true);
                    circleDialog.show();
//                    processSignIn();
                    getRegId();
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
                            editor.putString("cup",edLoginPassword.getText().toString().trim());
                            editor.commit();

                            Intent i = new Intent(LoginActivity.this, MyDrawerActivity.class);
                            startActivity(i);
                            finish();

                        }else{

                            SnackBar bar = new SnackBar(LoginActivity.this,getString(R.string.code_INVALIDMOB));
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

    public void getRegId(){

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(LoginActivity.this);
                    }
                    regid = gcm.register(PROJECT_NUMBER);
                        Log.e("GCM ID :", regid);
                    if(regid==null || regid==""){
                        AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                        alert.setTitle(getString(R.string.code_ERRR));
                        alert.setMessage(getString(R.string.code_INTERNALSERVERR));
                        alert.setPositiveButton(getString(R.string.code_TRYAGAIN), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getRegId();
                                dialog.dismiss();
                            }
                        });
                        alert.setNegativeButton(getString(R.string.code_EXIT), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                        alert.show();
                    } else {
                        // Store GCM ID in sharedpreference
                        SharedPreferences sharedPreferences=getSharedPreferences("GCM",MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("GCM_ID",regid);
                        editor.commit();

                        processSignIn();

                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return null;
            }
        }.execute();

    } // end of getRegId

}
