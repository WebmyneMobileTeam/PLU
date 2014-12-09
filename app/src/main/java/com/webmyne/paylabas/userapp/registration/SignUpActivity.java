package com.webmyne.paylabas.userapp.registration;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.widgets.SnackBar;
import com.webmyne.paylabas.userapp.base.DatabaseWrapper;
import com.webmyne.paylabas.userapp.helpers.CallWebService;
import com.webmyne.paylabas.userapp.model.Country;
import com.webmyne.paylabas.userapp.model.State;
import com.webmyne.paylabas_user.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class SignUpActivity extends ActionBarActivity implements View.OnClickListener{

    private ButtonRectangle btnCreateAccount;
    private ButtonRectangle btnLoginFromRegister;
    private EditText edFirstName;
    private EditText edLastName;
    private EditText edPassword;
    private EditText edConfirmPassword;
    private EditText edEmail;
    private EditText edAddress;
    private EditText edZipcode;
    private EditText edMobileno;
    private EditText edBirthdate;
    private Spinner spCountry;
    private Spinner spState;
    ArrayList<Country> countrylist;
    ArrayList<State> statelist;
    int temp_CountryID;
    /* birthdate and country, state , city pending */
    private DatabaseWrapper db_wrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
    }
    private void init() {

        btnCreateAccount = (ButtonRectangle)findViewById(R.id.btnConfirmSignUp);
        btnLoginFromRegister = (ButtonRectangle)findViewById(R.id.btnLoginFromRegister);
        btnCreateAccount.setOnClickListener(this);
        btnLoginFromRegister.setOnClickListener(this);

       edFirstName = (EditText)findViewById(R.id.edFirstname);
       edLastName = (EditText)findViewById(R.id.edLastname);
       edPassword = (EditText)findViewById(R.id.edPassword);
       edConfirmPassword = (EditText)findViewById(R.id.edConfirmpassword);
       edEmail  = (EditText)findViewById(R.id.edEmail);
       edAddress = (EditText)findViewById(R.id.edAddress);
       edZipcode = (EditText)findViewById(R.id.edZipcode);
       edMobileno = (EditText)findViewById(R.id.edMobileno);
       edBirthdate = (EditText)findViewById(R.id.dgBirthdate);
       edBirthdate.setOnClickListener(this);
       spCountry = (Spinner)findViewById(R.id.Country);
       spState = (Spinner)findViewById(R.id.State);

        countrylist = new ArrayList<Country>();
        statelist = new ArrayList<State>();
        fetchCountryAndDisplay();

        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SnackBar bar = new SnackBar(SignUpActivity.this,countrylist.get(position).CountryName.toString()+","+String.valueOf(position+1));
                bar.show();
                fetchStateAndDisplay(position+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    private void fetchStateAndDisplay(int CountryID) {
       temp_CountryID=CountryID;
        new AsyncTask<Void,Void,Void>() {

            // countrylist = new ArrayList<Country>();

            @Override
            protected Void doInBackground(Void... voids) {

                db_wrapper = new DatabaseWrapper(SignUpActivity.this);
                try {
                    db_wrapper.openDataBase();
                    statelist= db_wrapper.getStateData(temp_CountryID);
                    db_wrapper.close();
                }catch(Exception e){e.printStackTrace();}


                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                StateAdapter stateAdapter = new StateAdapter(SignUpActivity.this,R.layout.spinner_state, statelist);
                spState.setAdapter(stateAdapter);

            }
        }.execute();
    }
    private void fetchCountryAndDisplay() {

      new AsyncTask<Void,Void,Void>() {


         // countrylist = new ArrayList<Country>();

            @Override
            protected Void doInBackground(Void... voids) {

                db_wrapper = new DatabaseWrapper(SignUpActivity.this);
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
                CountryAdapter countryAdapter = new CountryAdapter(SignUpActivity.this,R.layout.spinner_country, countrylist);
                spCountry.setAdapter(countryAdapter);

            }
        }.execute();

    }
    public class StateAdapter extends ArrayAdapter<State>{
        Context context;
        int layoutResourceId;
        ArrayList<State> values;
        // int android.R.Layout.
        public StateAdapter(Context context, int resource, ArrayList<State> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values=objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(SignUpActivity.this);
            txt.setPadding(16,16,16,16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).StateName);
            return  txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(SignUpActivity.this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setText(values.get(position).StateName);

            return  txt;

        }
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

            TextView txt = new TextView(SignUpActivity.this);
            txt.setPadding(16,16,16,16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).CountryName);
            return  txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(SignUpActivity.this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setText(values.get(position).CountryName);

               return  txt;

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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



    public boolean isEmptyField(EditText param1){

        boolean isEmpty = false;
        if(param1.getText() == null || param1.getText().toString().equalsIgnoreCase("")){
            isEmpty = true;
        }
        return isEmpty;
    }
    public boolean isPasswordMatch(EditText param1,EditText param2){
        boolean isMatch = false;
        if(param1.getText().toString().equals(param2.getText().toString())){
            isMatch = true;
        }
        return isMatch;
    }
    public boolean isEmailMatch(EditText param1){
       // boolean isMatch = false;
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(param1.getText().toString()).matches();
    }
    public boolean isZipcodeMatch(EditText param1){
        boolean isMatch = false;
        if(param1.getText().toString().matches("[a-zA-Z0-9]*")){
            isMatch = true;
        }
        return isMatch;
    }
    public boolean isMobileMatch(EditText param1){

        boolean isEmpty = false;
        if((param1.getText() == null || param1.getText().toString().equalsIgnoreCase(""))||(param1.getText().toString().length()!=10)){
            isEmpty = true;
        }
        return isEmpty;

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.dgBirthdate:
                // displaying the date picker dialog box
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePicker = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
                               edBirthdate.setText(dayOfMonth + "-"+ (monthOfYear+1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePicker.show();
                break;
            case R.id.btnConfirmSignUp:
                if(isEmptyField(edFirstName)){
                    SnackBar bar = new SnackBar(SignUpActivity.this,"Please Enter First Name");
                    bar.show();
                }
                else if(isEmptyField(edLastName)){
                    SnackBar bar = new SnackBar(SignUpActivity.this,"Please Enter Last Name");
                    bar.show();
                }
                else if(isEmptyField(edPassword)||isEmptyField(edConfirmPassword)){
                    SnackBar bar = new SnackBar(SignUpActivity.this,"Please Enter Password & Confirm Password");
                    bar.show();
                }
                else if(!isPasswordMatch(edPassword,edConfirmPassword)){
                    SnackBar bar = new SnackBar(SignUpActivity.this,"Password & Confirm Password don't match");
                    bar.show();
                }
                else if(isEmptyField(edBirthdate)){
                    SnackBar bar = new SnackBar(SignUpActivity.this,"Please Enter Birthdate");
                    bar.show();
                }
                else if(isEmptyField(edEmail)){
                    SnackBar bar = new SnackBar(SignUpActivity.this,"Please Enter Email Address");
                    bar.show();
                }
                else if(!isEmailMatch(edEmail)){
                    SnackBar bar = new SnackBar(SignUpActivity.this,"Please Enter Valid Email Address");
                    bar.show();
                }
                else if(isEmptyField(edAddress)){

                    SnackBar bar = new SnackBar(SignUpActivity.this,"Please Enter Street Address");
                    bar.show();

                }
                else if(isEmptyField(edZipcode)){

                    SnackBar bar = new SnackBar(SignUpActivity.this,"Please Enter Zipcode");
                    bar.show();

                }
                else if(!isZipcodeMatch(edZipcode)){

                    SnackBar bar = new SnackBar(SignUpActivity.this,"Please Enter Valid Zipcode");
                    bar.show();

                }
                else if(isMobileMatch(edMobileno)){

                    SnackBar bar = new SnackBar(SignUpActivity.this,"Please Enter 10 digit Mobile Number");
                    bar.show();

                }
                else{
                    SnackBar bar = new SnackBar(SignUpActivity.this,"validation ok...");
                    bar.show();
                    //processSignIn();
                }

                /*
                Intent iCOnfirmSignUp = new Intent( SignUpActivity.this ,ConfirmationActivity.class );
                startActivity(iCOnfirmSignUp);
                finish();
                */
                break;

            case R.id.btnLoginFromRegister:

                Intent i = new Intent( SignUpActivity.this ,LoginActivity.class );
                startActivity(i);
                finish();

                break;


        }


    }
}
