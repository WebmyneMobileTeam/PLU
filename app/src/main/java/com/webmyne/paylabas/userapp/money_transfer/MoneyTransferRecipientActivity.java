package com.webmyne.paylabas.userapp.money_transfer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.widgets.SnackBar;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.webmyne.paylabas.userapp.base.DatabaseWrapper;
import com.webmyne.paylabas.userapp.custom_components.CircleDialog;
import com.webmyne.paylabas.userapp.custom_components.InternationalNumberValidation;
import com.webmyne.paylabas.userapp.helpers.AppConstants;
import com.webmyne.paylabas.userapp.helpers.CallWebService;
import com.webmyne.paylabas.userapp.helpers.ComplexPreferences;
import com.webmyne.paylabas.userapp.model.City;
import com.webmyne.paylabas.userapp.model.Country;
import com.webmyne.paylabas.userapp.model.LanguageStringUtil;
import com.webmyne.paylabas.userapp.model.Receipient;
import com.webmyne.paylabas.userapp.model.State;
import com.webmyne.paylabas.userapp.model.User;
import com.webmyne.paylabas_user.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class MoneyTransferRecipientActivity extends ActionBarActivity {

    Toolbar toolbar_actionbar;
    ButtonRectangle btnAddRecipient;
    TextView txtSelectRecipient;
    Spinner spinnerRecipientContact,spCountry,spState,spCity;
    EditText edFirstname,edLastname,edEmail,edAddress,edZipcode,edCountryCode,edMobileno;

    private ArrayList<Receipient> receipients;

    private User user;

    ArrayList<Country> countrylist;
    ArrayList<State> statelist;
    ArrayList<City> cityList;
    int temp_CountryID;
    int temp_CountryID1;
    int temp_StateID;
    int temp_CityID;
    int getCountryID;
    int RecipientId;
    private DatabaseWrapper db_wrapper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_transfer_recipient);

        toolbar_actionbar = (Toolbar)findViewById(R.id.toolbar);
        /* setting up the toolbar starts*/
        if (toolbar_actionbar != null) {
            toolbar_actionbar.setTitle(getString(R.string.code_TITLERECIPIENT));
            toolbar_actionbar.setNavigationIcon(R.drawable.icon_back);
            toolbar_actionbar.setBackgroundColor(getResources().getColor(R.color.paylabas_green));

            setSupportActionBar(toolbar_actionbar);

        }
        toolbar_actionbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  finish();
            }
        });

        intView();





    }

private void intView(){
    btnAddRecipient = (ButtonRectangle)findViewById(R.id.btnAddRecipient);
    spinnerRecipientContact = (Spinner)findViewById(R.id.spinnerRecipient);
    edFirstname = (EditText)findViewById(R.id.edFirstname);
    edLastname = (EditText)findViewById(R.id.edLastname);
    edAddress = (EditText)findViewById(R.id.edAddress);
    edZipcode  = (EditText)findViewById(R.id.edZipcode);
    edCountryCode  = (EditText)findViewById(R.id.edCountryCode);
    edEmail = (EditText)findViewById(R.id.edEmail);
    edMobileno = (EditText)findViewById(R.id.edMobileno);

    spCountry = (Spinner)findViewById(R.id.spCountry);
    spState = (Spinner)findViewById(R.id.spState);
    spCity = (Spinner)findViewById(R.id.spCity);





    btnAddRecipient.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (isEmptyField(edFirstname)) {
                SnackBar bar = new SnackBar(MoneyTransferRecipientActivity.this, getString(R.string.code_ERR1));
                bar.show();
            } else if (isEmptyField(edLastname)) {
                SnackBar bar = new SnackBar(MoneyTransferRecipientActivity.this, getString(R.string.code_ERR2));
                bar.show();
            } else if (isEmptyField(edAddress)) {
                SnackBar bar = new SnackBar(MoneyTransferRecipientActivity.this, getString(R.string.code_ERR3));
                bar.show();

            } else if (isEmptyField(edZipcode)) {

                SnackBar bar = new SnackBar(MoneyTransferRecipientActivity.this, getString(R.string.code_ERR4));
                bar.show();

            } else if (!isZipcodeMatch(edZipcode)) {

                SnackBar bar = new SnackBar(MoneyTransferRecipientActivity.this, getString(R.string.code_ERR5));
                bar.show();

            }  else if(InternationalNumberValidation.isPossibleNumber(edMobileno.getText().toString().toString(), countrylist.get(spCountry.getSelectedItemPosition()).ShortCode.toString().trim())==false){

                SnackBar bar = new SnackBar(MoneyTransferRecipientActivity.this, getString(R.string.code_ERR6));
                bar.show();
            }else if(InternationalNumberValidation.isValidNumber(edMobileno.getText().toString().toString(), countrylist.get(spCountry.getSelectedItemPosition()).ShortCode.toString().trim())==false){

                SnackBar bar = new SnackBar(MoneyTransferRecipientActivity.this, "Please Enter Valid Mobile Number");
                bar.show();
            }else {
                MoneyTransferFinalActivity.recObj = new Receipient();
                MoneyTransferFinalActivity.recObj.FirstName = edFirstname.getText().toString();
                MoneyTransferFinalActivity.recObj.LastName = edLastname.getText().toString();
                MoneyTransferFinalActivity.recObj.EmailId = edEmail.getText().toString();
                MoneyTransferFinalActivity.recObj.Address = edAddress.getText().toString();
                MoneyTransferFinalActivity.recObj.ZipCode = edZipcode.getText().toString();
                MoneyTransferFinalActivity.recObj.MobileNo = edMobileno.getText().toString();

                MoneyTransferFinalActivity.recObj.Country = countrylist.get(spCountry.getSelectedItemPosition()).CountryID;
                MoneyTransferFinalActivity.recObj.City = cityList.get(spCity.getSelectedItemPosition()).CityID;
                MoneyTransferFinalActivity.recObj.State = statelist.get(spState.getSelectedItemPosition()).StateID;

                finish();
            }
        }
    });

    spinnerRecipientContact.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position==0){
                            clearall();
                    }else{
                        fillRecipientDetails(position);

                    }


        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });


    spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            int pos = countrylist.get(position).CountryID;

            fetchStateAndDisplay(pos,spinnerRecipientContact.getSelectedItemPosition());
            temp_CountryID1=position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });

    spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            temp_CityID=position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });



}

    @Override
    protected void onResume() {
        super.onResume();
        fetchRecipientDisplay();
      //  CountryName
        fetchCountryAndDisplay(1);

    }


private void clearall(){

    fetchCountryAndDisplay(1);

    edFirstname.setText("");
    edLastname.setText("");
    edEmail.setText("");
    edMobileno.setText("");
    edCountryCode.setText("");
    edAddress.setText("");
    edZipcode.setText("");

}
private void fillRecipientDetails(int pos){

    getCountryID=(int)receipients.get(spinnerRecipientContact.getSelectedItemPosition()).Country;

    fetchCountryAndDisplay(spinnerRecipientContact.getSelectedItemPosition());

edFirstname.setText(receipients.get(pos).FirstName);
edLastname.setText(receipients.get(pos).LastName);
edEmail.setText(receipients.get(pos).EmailId);
edMobileno.setText(receipients.get(pos).MobileNo);
edAddress.setText(receipients.get(pos).Address);
edZipcode.setText(receipients.get(pos).ZipCode);

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
 public boolean isEmptyField(EditText param1) {

        boolean isEmpty = false;
        if (param1.getText() == null || param1.getText().toString().equalsIgnoreCase("")) {
            isEmpty = true;
        }
        return isEmpty;
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
private void fetchRecipientDisplay(){
    final CircleDialog circleDialog = new CircleDialog(MoneyTransferRecipientActivity.this, 0);
    circleDialog.setCancelable(true);
    circleDialog.show();

    ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(MoneyTransferRecipientActivity.this, "user_pref", 0);
    user = complexPreferences.getObject("current_user", User.class);

    new CallWebService(AppConstants.GET_MONEYTRANSFER_RECEIPIENTS + user.UserID+"/"+getIntent().getStringExtra("cc")+"/"+ LanguageStringUtil.CultureString(MoneyTransferRecipientActivity.this), CallWebService.TYPE_JSONARRAY) {

        @Override
        public void response(String response) {

            circleDialog.dismiss();
            Log.e("Receipients List", response);
            if (response == null) {

            } else {

                Type listType = new TypeToken<List<Receipient>>() {
                }.getType();

                receipients = new GsonBuilder().create().fromJson(response, listType);


                Receipient r1 = new Receipient();
                r1.FirstName = getString(R.string.code_ESELECT);
                r1.LastName = getString(R.string.code_ERECIPEITN);

                receipients.add(0,r1);


                RecipientAdapter adapter = new RecipientAdapter(MoneyTransferRecipientActivity.this,
                        android.R.layout.simple_spinner_item,receipients);
                spinnerRecipientContact.setAdapter(adapter);

            }

        }

        @Override
        public void error(VolleyError error) {
            circleDialog.dismiss();
            SnackBar bar = new SnackBar(MoneyTransferRecipientActivity.this, getString(R.string.code_SSYNCERR));
            bar.show();
        }
    }.start();

}

private void fetchCountryAndDisplay(final int pos) {

        new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                db_wrapper = new DatabaseWrapper(MoneyTransferRecipientActivity.this);
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

                CountryAdapter countryAdapter = new CountryAdapter(MoneyTransferRecipientActivity.this,R.layout.spinner_country, countrylist);
                spCountry.setAdapter(countryAdapter);


                int posCountry = 0;
                try {
                    for (int i = 0; i < countrylist.size(); i++) {
                        if (countrylist.get(i).CountryName.toString().trim().equalsIgnoreCase(MoneyTrtansferChildFragment.CountryName)) {
                            posCountry = i;
                            break;
                        }
                    }
                }catch (Exception e){
                    Log.e("error ","recipient-prof is not loaded");
                }
                spCountry.setSelection(posCountry);
               // spCountry.setSelection(getCountryID-1);
            }
        }.execute();

    }
    private void fetchStateAndDisplay(int CountryID,final int pos) {

        statelist = new ArrayList<State>();
        edCountryCode.setText(String.valueOf(countrylist.get(spCountry.getSelectedItemPosition()).CountryCode));
        temp_CountryID=CountryID;

        new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                db_wrapper = new DatabaseWrapper(MoneyTransferRecipientActivity.this);
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
                StateAdapter stateAdapter = new StateAdapter(MoneyTransferRecipientActivity.this,R.layout.spinner_state, statelist);
                spState.setAdapter(stateAdapter);

                int posState = 0;
                try {
                    for (int i = 0; i < statelist.size(); i++) {
                        if (statelist.get(i).StateID == receipients.get(pos).State) {
                            posState = i;
                            break;
                        }
                    }
                }catch (Exception e){
                    Log.e("error ","recipient-prof is not loaded");
                }

                spState.setSelection(posState);


                spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        temp_StateID=position;
                        try {
                            fetchAndDisplayCity(statelist.get(position).StateID, spinnerRecipientContact.getSelectedItemPosition());
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


            }
        }.execute();
    }

    private void fetchAndDisplayCity(final int stateID,final int pos) {

        cityList = new ArrayList<City>();
        boolean isAlreadyThere = false;
        db_wrapper = new DatabaseWrapper(MoneyTransferRecipientActivity.this);
        try {
            db_wrapper.openDataBase();
            isAlreadyThere = db_wrapper.isAlreadyInDatabase(stateID);
            db_wrapper.close();
        }catch(Exception e){e.printStackTrace();}

        if(isAlreadyThere == true){

            System.out.println("Cities are already in database");
            new AsyncTask<Void,Void,Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    db_wrapper = new DatabaseWrapper(MoneyTransferRecipientActivity.this);
                    try {
                        db_wrapper.openDataBase();
                        cityList = db_wrapper.getCityData(stateID);
                        db_wrapper.close();
                    }catch(Exception e){e.printStackTrace();}
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);

                    try{
                        CityAdapter cityAdapter = new CityAdapter(MoneyTransferRecipientActivity.this,R.layout.spinner_country, cityList);
                        spCity.setAdapter(cityAdapter);

                        int posCity = 0;
                        for(int i=0;i<cityList.size();i++){
                            if(cityList.get(i).CityID == receipients.get(pos).City){
                                posCity = i;
                                break;
                            }
                        }
                        spCity.setSelection(posCity);
                    }catch(Exception e){
                        e.printStackTrace();

                    }
                }
            }.execute();


        }else{

            final CircleDialog circleDialog=new CircleDialog(MoneyTransferRecipientActivity.this,0);
            circleDialog.setCancelable(true);
            circleDialog.show();


            System.out.println("Cities are not there");
            new CallWebService(AppConstants.GETCITIES +stateID+"/"+LanguageStringUtil.CultureString(MoneyTransferRecipientActivity.this),CallWebService.TYPE_JSONARRAY) {

                @Override
                public void response(String response) {

                    circleDialog.dismiss();

                    try{
                        Type listType=new TypeToken<List<City>>(){
                        }.getType();
                        cityList =  new GsonBuilder().create().fromJson(response, listType);
                        CityAdapter cityAdapter = new CityAdapter(MoneyTransferRecipientActivity.this,R.layout.spinner_country, cityList);
                        spCity.setAdapter(cityAdapter);

                        db_wrapper = new DatabaseWrapper(MoneyTransferRecipientActivity.this);
                        try {
                            db_wrapper.openDataBase();
                            db_wrapper.insertCities(cityList);
                            db_wrapper.close();
                        }catch(Exception e){e.printStackTrace();}

                        int posCity = 0;
                        for(int i=0;i<cityList.size();i++){
                            if(cityList.get(i).CityID == receipients.get(pos).City){
                                posCity = i;
                                break;
                            }
                        }

                        spCity.setSelection(posCity);
                    }catch(Exception e){

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


    public class CityAdapter extends ArrayAdapter<City> {

        Context context;
        int layoutResourceId;
        ArrayList<City> values;
        // int android.R.Layout.

        public CityAdapter(Context context, int resource, ArrayList<City> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values=objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(MoneyTransferRecipientActivity.this);
            txt.setPadding(16,16,16,16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).CityName);
            return  txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(MoneyTransferRecipientActivity.this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setText(values.get(position).CityName);
            return  txt;
        }
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

            TextView txt = new TextView(MoneyTransferRecipientActivity.this);
            txt.setPadding(16,16,16,16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).StateName);
            return  txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(MoneyTransferRecipientActivity.this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setText(values.get(position).StateName);
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
            this.values = objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(MoneyTransferRecipientActivity.this);
            txt.setPadding(16, 16, 16, 16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).CountryName);
            return txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(MoneyTransferRecipientActivity.this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16, 16, 16, 16);
            txt.setText(values.get(position).CountryName);
            return txt;
        }
    }

    public class RecipientAdapter extends ArrayAdapter<Receipient> {

        Context context;
        int layoutResourceId;
        ArrayList<Receipient> values;
        // int android.R.Layout.

        public RecipientAdapter(Context context, int resource, ArrayList<Receipient> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values=objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(MoneyTransferRecipientActivity.this);
            txt.setPadding(16,16,16,16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).FirstName+" "+values.get(position).LastName);
            return  txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(MoneyTransferRecipientActivity.this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setText(values.get(position).FirstName+" "+values.get(position).LastName);
            return  txt;
        }
    }



    //end of main class
}
