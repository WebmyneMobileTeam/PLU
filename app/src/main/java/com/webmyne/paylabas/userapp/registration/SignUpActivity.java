package com.webmyne.paylabas.userapp.registration;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.widgets.SnackBar;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.webmyne.paylabas.userapp.base.DatabaseWrapper;
import com.webmyne.paylabas.userapp.base.MyApplication;
import com.webmyne.paylabas.userapp.base.MyDrawerActivity;
import com.webmyne.paylabas.userapp.custom_components.CircleDialog;
import com.webmyne.paylabas.userapp.custom_components.InternationalNumberValidation;
import com.webmyne.paylabas.userapp.helpers.AppConstants;
import com.webmyne.paylabas.userapp.helpers.CallWebService;
import com.webmyne.paylabas.userapp.helpers.ComplexPreferences;
import com.webmyne.paylabas.userapp.model.City;
import com.webmyne.paylabas.userapp.model.Country;
import com.webmyne.paylabas.userapp.model.State;
import com.webmyne.paylabas.userapp.model.User;
import com.webmyne.paylabas_user.R;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;


public class SignUpActivity extends ActionBarActivity implements View.OnClickListener {

    private GoogleCloudMessaging gcm;
    private String regid;
    private String PROJECT_NUMBER = "92884720384";
    CircleDialog circleDialog;
    private ButtonRectangle btnCreateAccount;
    private ButtonRectangle btnLoginFromRegister;
    private EditText edFirstName;
    private EditText edLastName;
    private EditText edPassword;
    private EditText edConfirmPassword;
    private EditText edCountryCode;
    private EditText edEmail;
    private EditText edAddress;
    private EditText edZipcode;
    private EditText edMobileno;
    private EditText edBirthdate;
    private Spinner spCountry;
    private Spinner spState;
    ArrayList<Country> countrylist;
    ArrayList<State> statelist;
    ArrayList<City> cityList;
    int temp_CountryID;
    int temp_CountryID1;
    int temp_StateID;
    int temp_CityID;
    private Spinner spCity;
    /* birthdate and country, state , city pending */
    private DatabaseWrapper db_wrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();

    }

    private void init() {

        btnCreateAccount = (ButtonRectangle) findViewById(R.id.btnConfirmSignUp);
        btnLoginFromRegister = (ButtonRectangle) findViewById(R.id.btnLoginFromRegister);
        btnCreateAccount.setOnClickListener(this);
        btnLoginFromRegister.setOnClickListener(this);

        edFirstName = (EditText) findViewById(R.id.edFirstname);
        edLastName = (EditText) findViewById(R.id.edLastname);
        edPassword = (EditText) findViewById(R.id.edPassword);
        edConfirmPassword = (EditText) findViewById(R.id.edConfirmpassword);
        edEmail = (EditText) findViewById(R.id.edEmail);
        edAddress = (EditText) findViewById(R.id.edAddress);
        edZipcode = (EditText) findViewById(R.id.edZipcode);
        edMobileno = (EditText) findViewById(R.id.edMobileno);
        edBirthdate = (EditText) findViewById(R.id.dgBirthdate);
        spCountry = (Spinner) findViewById(R.id.Country);
        spState = (Spinner) findViewById(R.id.State);
        spCity = (Spinner) findViewById(R.id.spCity);
        countrylist = new ArrayList<Country>();
        edCountryCode = (EditText) findViewById(R.id.edCountryCode);

        temp_CountryID1 = 10;
        temp_StateID = 0;
        temp_CityID = 0;


        edBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePicker = new DatePickerDialog(SignUpActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        edBirthdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
                datePicker.show();
            }
        });


//        edBirthdate.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    // displaying the date picker dialog box
//                    final Calendar c = Calendar.getInstance();
//                    int mYear = c.get(Calendar.YEAR);
//                    int mMonth = c.get(Calendar.MONTH);
//                    int mDay = c.get(Calendar.DAY_OF_MONTH);
//
//                    DatePickerDialog datePicker = new DatePickerDialog(SignUpActivity.this, new DatePickerDialog.OnDateSetListener() {
//
//                        @Override
//                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                            edBirthdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//                        }
//                    }, mYear, mMonth, mDay);
//                    datePicker.show();
//                }
//                return false;
//            }
//        });


        fetchCountryAndDisplay();


        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = countrylist.get(position).CountryID;
                fetchStateAndDisplay(pos);
                temp_CountryID1 = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                temp_CityID = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void fetchStateAndDisplay(int CountryID) {

        statelist = new ArrayList<State>();
        edCountryCode.setText(String.valueOf(countrylist.get(spCountry.getSelectedItemPosition()).CountryCode));
        temp_CountryID = CountryID;

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                db_wrapper = new DatabaseWrapper(SignUpActivity.this);
                try {
                    db_wrapper.openDataBase();
                    statelist = db_wrapper.getStateData(temp_CountryID);
                    db_wrapper.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                StateAdapter stateAdapter = new StateAdapter(SignUpActivity.this, R.layout.spinner_state, statelist);
                spState.setAdapter(stateAdapter);

                spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        temp_StateID = position;
                        fetchAndDisplayCity(statelist.get(position).StateID);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


            }
        }.execute();
    }

    private void fetchAndDisplayCity(final int stateID) {
        cityList = new ArrayList<City>();
        boolean isAlreadyThere = false;
        db_wrapper = new DatabaseWrapper(SignUpActivity.this);
        try {
            db_wrapper.openDataBase();
            isAlreadyThere = db_wrapper.isAlreadyInDatabase(stateID);
            db_wrapper.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isAlreadyThere == true) {

            System.out.println("Cities are already in database");
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    db_wrapper = new DatabaseWrapper(SignUpActivity.this);
                    try {
                        db_wrapper.openDataBase();
                        cityList = db_wrapper.getCityData(stateID);
                        db_wrapper.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);

                    CityAdapter cityAdapter = new CityAdapter(SignUpActivity.this, R.layout.spinner_country, cityList);
                    spCity.setAdapter(cityAdapter);

                }
            }.execute();


        } else {

            final CircleDialog circleDialog = new CircleDialog(SignUpActivity.this, 0);
            circleDialog.setCancelable(true);
            circleDialog.show();


            System.out.println("Cities are not there");
            new CallWebService(AppConstants.GETCITIES + stateID, CallWebService.TYPE_JSONARRAY) {

                @Override
                public void response(String response) {

                    circleDialog.dismiss();
                    Type listType = new TypeToken<List<City>>() {
                    }.getType();
                    cityList = new GsonBuilder().create().fromJson(response, listType);
                    CityAdapter cityAdapter = new CityAdapter(SignUpActivity.this, R.layout.spinner_country, cityList);
                    spCity.setAdapter(cityAdapter);

                    db_wrapper = new DatabaseWrapper(SignUpActivity.this);
                    try {
                        db_wrapper.openDataBase();
                        db_wrapper.insertCities(cityList);
                        db_wrapper.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void error(VolleyError error) {

                    circleDialog.dismiss();
                }
            }.start();

        }

    }

    private void fetchCountryAndDisplay() {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                db_wrapper = new DatabaseWrapper(SignUpActivity.this);
                try {
                    db_wrapper.openDataBase();
                    countrylist = db_wrapper.getCountryData();
                    db_wrapper.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                CountryAdapter countryAdapter = new CountryAdapter(SignUpActivity.this, R.layout.spinner_country, countrylist);
                spCountry.setAdapter(countryAdapter);

            }
        }.execute();

    }

    public class CityAdapter extends ArrayAdapter<City> {

        Context context;
        int layoutResourceId;
        ArrayList<City> values;
        // int android.R.Layout.

        public CityAdapter(Context context, int resource, ArrayList<City> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values = objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(SignUpActivity.this);
            txt.setPadding(16, 16, 16, 16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).CityName);
            return txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(SignUpActivity.this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16, 16, 16, 16);
            txt.setText(values.get(position).CityName);
            return txt;
        }
    }


    public class StateAdapter extends ArrayAdapter<State> {

        Context context;
        int layoutResourceId;
        ArrayList<State> values;
        // int android.R.Layout.

        public StateAdapter(Context context, int resource, ArrayList<State> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values = objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(SignUpActivity.this);
            txt.setPadding(16, 16, 16, 16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).StateName);
            return txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(SignUpActivity.this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16, 16, 16, 16);
            txt.setText(values.get(position).StateName);
            return txt;
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
            this.values = objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(SignUpActivity.this);
            txt.setPadding(16, 16, 16, 16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).CountryName);
            return txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(SignUpActivity.this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16, 16, 16, 16);
            txt.setText(values.get(position).CountryName);
            return txt;
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


    public boolean isEmptyField(EditText param1) {

        boolean isEmpty = false;
        if (param1.getText() == null || param1.getText().toString().equalsIgnoreCase("")) {
            isEmpty = true;
        }
        return isEmpty;
    }

    public boolean isPasswordMatch(EditText param1, EditText param2) {
        boolean isMatch = false;
        if (param1.getText().toString().equals(param2.getText().toString())) {
            isMatch = true;
        }
        return isMatch;
    }

    public boolean isEmailMatch(EditText param1) {
        // boolean isMatch = false;
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(param1.getText().toString()).matches();
    }

    public boolean isZipcodeMatch(EditText param1) {
        boolean isMatch = false;
        if (param1.getText().toString().matches("[a-zA-Z0-9]*")) {
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

    // User Registration process
    private void processSignUP() {
        try {

            JSONObject userObject = new JSONObject();


            userObject.put("FName", edFirstName.getText().toString().trim());
            userObject.put("LName", edLastName.getText().toString().trim());
            userObject.put("Password", edPassword.getText().toString().trim());
            userObject.put("DOBString", edBirthdate.getText().toString().trim());
            userObject.put("EmailID", edEmail.getText().toString().trim());
            userObject.put("Address", edAddress.getText().toString().trim());
            userObject.put("Country", countrylist.get(spCountry.getSelectedItemPosition()).CountryID);
            userObject.put("State", statelist.get(temp_StateID).StateID);
            userObject.put("City", cityList.get(temp_CityID).CityID);
            userObject.put("Zip", edZipcode.getText().toString().trim());
            userObject.put("MobileNo", edMobileno.getText().toString().trim());

            //   userObject.put("Answer", "answer");
            //     userObject.put("CashOutPointName", "cashoutpointname");
            //  userObject.put("CreatedDate",null);
            //  userObject.put("CreatedDateInt", 2147483647);
            //   userObject.put("DeviceType", "Android");
            //   userObject.put("Gender", "Male");
            //    userObject.put("Image", "image");
            // userObject.put("IsDeleted", false);
            //  userObject.put("IsRegistered", false);
            //  userObject.put("IsSuperAdmin", false);
            //    userObject.put("LastTryDate", null);
            //    userObject.put("LastTryDateLogin", null);
            //   userObject.put("LemonwayBal", "lemon way amount");
            userObject.put("MobileCountryCode", edCountryCode.getText().toString().trim());
            //    userObject.put("NotificationID", "notification");
            //    userObject.put("PassportNo", "paspport");
            //    userObject.put("PaylabasMerchantID", "palabs merchant id");
            // userObject.put("QuestionId", 2147483647);
            //userObject.put("ResponseCode", "response code");
            // userObject.put("ResponseMsg", "response msg");
            //   userObject.put("RoleId", 2147483647);
            //   userObject.put("Status", true);
            //   userObject.put("StatusMsg", "status msg");
            //   userObject.put("TryCount", 2147483647);
            //    userObject.put("TryCountLogin", 2147483647);
            //  userObject.put("UpdateDate", null);
            //   userObject.put("UpdateDateInt", 2147483647);
            userObject.put("UserID", 0);
            // userObject.put("UserName", "user1");
            //   userObject.put("VerificationCode", "verficatino code");
            userObject.put("isVerified", false);


            Log.e("json obj", userObject.toString());




            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.USER_REGISTRATION, userObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jobj) {
                    circleDialog.dismiss();
                    String response = jobj.toString();
                    Log.e("Response : ", "" + response);

                    try {

                        JSONObject obj = new JSONObject(response);

                        if (obj.getString("ResponseCode").equalsIgnoreCase("1")) {

                            User currentUser = new GsonBuilder().create().fromJson(response, User.class);
                            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(SignUpActivity.this, "user_pref", 0);
                            complexPreferences.putObject("current_user", currentUser);
                            complexPreferences.commit();

                            // set login true
                            SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("VerificationCode", currentUser.VerificationCode.toString());
                            editor.commit();

                            Intent iCOnfirmSignUp = new Intent(SignUpActivity.this, ConfirmationActivity.class);
                            startActivity(iCOnfirmSignUp);
                            finish();

                        } else {

                            if (obj.getString("ResponseCode").equalsIgnoreCase("-2")) {
                                SnackBar bar112 = new SnackBar(SignUpActivity.this, getString(R.string.code_ERROOCCURSIGNUP));
                                bar112.show();
                            } else if (obj.getString("ResponseCode").equalsIgnoreCase("-1")) {
                                SnackBar bar112 = new SnackBar(SignUpActivity.this, getString(R.string.code_E));
                                bar112.show();

                            } else if (obj.getString("ResponseCode").equalsIgnoreCase("2")) {
                                SnackBar bar112 = new SnackBar(SignUpActivity.this, getString(R.string.code_SMOBILEALREADY));
                                bar112.show();
                            } else if (obj.getString("ResponseCode").equalsIgnoreCase("3")) {
                                SnackBar bar112 = new SnackBar(SignUpActivity.this, getString(R.string.code_SEMIALID));
                                bar112.show();
                            } else if (obj.getString("ResponseCode").equalsIgnoreCase("4")) {
                                SnackBar bar112 = new SnackBar(SignUpActivity.this, getString(R.string.code_MONANDEMAIL));
                                bar112.show();
                            } else {
                                SnackBar bar112 = new SnackBar(SignUpActivity.this, getString(R.string.code_TIMOUT));
                                bar112.show();
                            }

                        }

                    } catch (Exception e) {

                    }


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    circleDialog.dismiss();
                    Log.e("error responsegg: ", error + "");
                    SnackBar bar = new SnackBar(SignUpActivity.this,getString(R.string.code_STIMEOUTER));
                    bar.show();

                }
            });
            MyApplication.getInstance().addToRequestQueue(req);

        } catch (Exception e) {

        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnConfirmSignUp:

                if (isEmptyField(edFirstName)) {
                    SnackBar bar = new SnackBar(SignUpActivity.this, getString(R.string.code_PLZENTERFNAME));
                    bar.show();
                } else if (isEmptyField(edLastName)) {
                    SnackBar bar = new SnackBar(SignUpActivity.this, getString(R.string.code_ENTELASTNAEM));
                    bar.show();
                } else if (isEmptyField(edPassword) || isEmptyField(edConfirmPassword)) {
                    SnackBar bar = new SnackBar(SignUpActivity.this, getString(R.string.code_ENTERPASSWWORD));
                    bar.show();
                } else if (!isPasswordMatch(edPassword, edConfirmPassword)) {
                    SnackBar bar = new SnackBar(SignUpActivity.this, getString(R.string.code_PASSWORDODNOTMATCH));
                    bar.show();
                } else if (isEmptyField(edBirthdate)) {
                    SnackBar bar = new SnackBar(SignUpActivity.this, getString(R.string.code_BIRTHDAY));
                    bar.show();
                } else if (isEmptyField(edEmail)) {
                    SnackBar bar = new SnackBar(SignUpActivity.this, getString(R.string.code_emailadd));
                    bar.show();
                } else if (!isEmailMatch(edEmail)) {
                    SnackBar bar = new SnackBar(SignUpActivity.this, getString(R.string.code_valideMAILADD));
                    bar.show();
                } else if (isEmptyField(edAddress)) {

                    SnackBar bar = new SnackBar(SignUpActivity.this, getString(R.string.code_ADD));
                    bar.show();

                } else if (isEmptyField(edZipcode)) {

                    SnackBar bar = new SnackBar(SignUpActivity.this,  getString(R.string.code_ZIPCODE));
                    bar.show();

                } else if (!isZipcodeMatch(edZipcode)) {

                    SnackBar bar = new SnackBar(SignUpActivity.this, getString(R.string.code_VALIDZIPCODE));
                    bar.show();

                }  else if(InternationalNumberValidation.isPossibleNumber(edMobileno.getText().toString().toString(), countrylist.get(spCountry.getSelectedItemPosition()).ShortCode.toString().trim())==false){

                    SnackBar bar = new SnackBar(SignUpActivity.this, getString(R.string.code_validmobno));
                    bar.show();
                }else if(InternationalNumberValidation.isValidNumber(edMobileno.getText().toString().toString(), countrylist.get(spCountry.getSelectedItemPosition()).ShortCode.toString().trim())==false){

                    SnackBar bar = new SnackBar(SignUpActivity.this, "Please Enter Valid Mobile Number");
                    bar.show();
                }else {
                    circleDialog= new CircleDialog(SignUpActivity.this, 0);
                    circleDialog.setCancelable(true);
                    circleDialog.show();
                    processSignUP();
                }


                break;


            case R.id.btnLoginFromRegister:

                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);
                finish();

                break;


        }


    }


    public void getRegId(){

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(SignUpActivity.this);
                    }
                    regid = gcm.register(PROJECT_NUMBER);
                        Log.e("GCM ID :", regid);
                    if(regid==null || regid==""){
                        AlertDialog.Builder alert = new AlertDialog.Builder(SignUpActivity.this);
                        alert.setTitle(getString(R.string.ERR));
                        alert.setMessage(getString(R.string.INTERNALSEERVERR));
                        alert.setPositiveButton(getString(R.string.TRAGINA), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getRegId();
                                dialog.dismiss();
                            }
                        });
                        alert.setNegativeButton(getString(R.string.EXIT), new DialogInterface.OnClickListener() {
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

                        processSignUP();

                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return null;
            }
        }.execute();

    } // end of getRegId
}
