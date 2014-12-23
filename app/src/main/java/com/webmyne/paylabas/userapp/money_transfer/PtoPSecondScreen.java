package com.webmyne.paylabas.userapp.money_transfer;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.gc.materialdesign.views.ButtonRectangle;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.webmyne.paylabas.userapp.base.DatabaseWrapper;
import com.webmyne.paylabas.userapp.custom_components.CircleDialog;
import com.webmyne.paylabas.userapp.helpers.AppConstants;
import com.webmyne.paylabas.userapp.helpers.CallWebService;
import com.webmyne.paylabas.userapp.helpers.ComplexPreferences;
import com.webmyne.paylabas.userapp.model.City;
import com.webmyne.paylabas.userapp.model.Country;
import com.webmyne.paylabas.userapp.model.P2PReceipient;
import com.webmyne.paylabas.userapp.model.Receipient;
import com.webmyne.paylabas.userapp.model.SendMoneyToPaylabasUser;
import com.webmyne.paylabas.userapp.model.State;
import com.webmyne.paylabas_user.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PtoPSecondScreen extends Fragment implements View.OnClickListener{


    private ButtonRectangle btnBack;
    private ButtonRectangle btnNext;
    private Spinner spinnerRecipientP2P;
    private Spinner spinnerCountryP2P;
    private Spinner spinnerStateP2P;
    private Spinner spinnerCityP2P;
    private Spinner spinnerCountryCodeP2P;
    private ArrayList<P2PReceipient> receipients;
    private ArrayList<Country> countryCodes;
    private ArrayList<Country> countries;
    ArrayList<State> statelist;
    ArrayList<City> cityList;
    private EditText etMobileNumberP2P,etEmailP2P,etFirstName,etLastName;

    private ComplexPreferences complexPreferences;
    private SendMoneyToPaylabasUser sendMoneyToPaylabasUser;
    private DatabaseWrapper db_wrapper;
//    private int temp_CountryID;
//    private int temp_StateID;

    public PtoPSecondScreen() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View convertView = inflater.inflate(R.layout.fragment_pto_psecond_screen, container, false);
        spinnerRecipientP2P = (Spinner)convertView.findViewById(R.id.spinnerRecipientP2P);
        spinnerCountryP2P = (Spinner)convertView.findViewById(R.id.spinnerCountryP2P);
        spinnerStateP2P = (Spinner)convertView.findViewById(R.id.spinnerStateP2P);
        spinnerCityP2P = (Spinner)convertView.findViewById(R.id.spinnerCityP2P);
        spinnerCountryCodeP2P= (Spinner)convertView.findViewById(R.id.spinnerCountryCodeP2P);
        etMobileNumberP2P=(EditText)convertView.findViewById(R.id.etMobileNumberP2P);
        etEmailP2P=(EditText)convertView.findViewById(R.id.etEmailP2P);
        etFirstName=(EditText)convertView.findViewById(R.id.etFirstName);
        etLastName=(EditText)convertView.findViewById(R.id.etLastName);
        btnBack = (ButtonRectangle)convertView.findViewById(R.id.btnBackPtoPSecondScreen);
        btnNext = (ButtonRectangle)convertView.findViewById(R.id.btnNextPtoPSecondScreen);
        btnNext.setOnClickListener(this);
        btnBack.setOnClickListener(this);

//        fetchCountryAndDisplay();

        return convertView;
    }

    @Override
    public void onResume() {
        super.onResume();
        receipients = new ArrayList<P2PReceipient>();
        countries = new ArrayList<Country>();
        countryCodes= new ArrayList<Country>();
        statelist= new ArrayList<State>();
        cityList= new ArrayList<City>();
        fetchReceipientsAndDisplay();
        fetchCountryAndDisplay();
        fetchCountryCodeAndDisplay();
        spinnerRecipientP2P.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        spinnerCountryP2P.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fetchStateAndDisplay(position+1);
                processCountrySelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerCountryCodeP2P.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                processCountryCodeSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerStateP2P.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                fetchAndDisplayCity(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void processCountrySelection(final int position) {
        spinnerCountryCodeP2P.setSelection(position);

    }

    private void processCountryCodeSelection(final int position) {
        spinnerCountryP2P.setSelection(position);

    }

    private void processSelectionWholeReceipient(int position) {

        int countryPosition=0;
        int countrycodePosition=0;
        int statePosition=0;
        int cityPosition=0;

        for(int i=0;i<countries.size();i++){
            if(receipients.get(position).Country.equalsIgnoreCase(countries.get(i).CountryID+"")){
                countryPosition=i;
            }
            if(receipients.get(position).CountryCode.equalsIgnoreCase(countries.get(i).CountryCode+"")){
                countrycodePosition=i;
                Log.e("country ","country found");
            }

        }
        spinnerCountryP2P.setSelection(countryPosition);
        spinnerCountryCodeP2P.setSelection(countrycodePosition);

        for(int i=0;i<statelist.size();i++){
            Log.e("state ",statelist.get(i).StateID+"state found");
            if(receipients.get(position).State.equalsIgnoreCase(statelist.get(i).StateID+"")){
                statePosition=i;

                Log.e("state ","state found");
            }
        }

        for(int i=0;i<cityList.size();i++){
            if(receipients.get(position).City.equalsIgnoreCase(cityList.get(i).CityID+"")){
                cityPosition=i;
                Log.e("city ","city found");
            }
        }
        spinnerStateP2P.setSelection(statePosition);
        spinnerCityP2P.setSelection(cityPosition);

        etFirstName.setText(receipients.get(position).FirstName);
        etLastName.setText(receipients.get(position).LastName);
        etMobileNumberP2P.setText(receipients.get(position).MobileNo);
        etEmailP2P.setText(receipients.get(position).EmailId);

//        sendMoneyToPaylabasUser.PayLabasRecipientList.get(position).FirstName=etFirstName.getText().toString().trim();
//        sendMoneyToPaylabasUser.PayLabasRecipientList.get(position).LastName=etLastName.getText().toString().trim();
//        sendMoneyToPaylabasUser.PayLabasRecipientList.get(position).MobileNo=etMobileNumberP2P.getText().toString().trim();
//        sendMoneyToPaylabasUser.PayLabasRecipientList.get(position).EmailId=etEmailP2P.getText().toString().trim();
//        sendMoneyToPaylabasUser.PayLabasRecipientList.get(position).RecipientID=receipients.get(position).RecipientID;
//        sendMoneyToPaylabasUser.PayLabasRecipientList.get(position).Country
//        sendMoneyToPaylabasUser.PayLabasRecipientList.get(position).CountryCode
//        sendMoneyToPaylabasUser.PayLabasRecipientList.get(position).State
//        sendMoneyToPaylabasUser.PayLabasRecipientList.get(position).City


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


//                    Country country = new Country(0,"Select Country",0,"",0,"","");
//                    countries.add(0,country);


                }catch(Exception e){e.printStackTrace();}


                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                CountryAdapter countryAdapter = new CountryAdapter(getActivity(),R.layout.spinner_country, countries);
                spinnerCountryP2P.setAdapter(countryAdapter);

            }
        }.execute();

    }

    private void fetchCountryCodeAndDisplay() {


        new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                db_wrapper = new DatabaseWrapper(getActivity());
                try {
                    db_wrapper.openDataBase();

                    countryCodes= db_wrapper.getCountryData();
                    db_wrapper.close();


//                    Country country = new Country(0,"",0,"",0,"","Country Code");
//                    countryCodes.add(0,country);


                }catch(Exception e){e.printStackTrace();}


                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                CountryCodeAdapter countryCodeAdapter = new CountryCodeAdapter(getActivity(),R.layout.spinner_country, countryCodes);
                spinnerCountryCodeP2P.setAdapter(countryCodeAdapter);

            }
        }.execute();

    }

    private void fetchStateAndDisplay(final int CountryID) {

        statelist = new ArrayList<State>();


        new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                db_wrapper = new DatabaseWrapper(getActivity());
                try {
                    db_wrapper.openDataBase();
                    statelist= db_wrapper.getStateData(CountryID);
                    db_wrapper.close();

                }catch(Exception e){e.printStackTrace();}

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                StateAdapter stateAdapter = new StateAdapter(getActivity(),R.layout.spinner_state, statelist);
                spinnerStateP2P.setAdapter(stateAdapter);

                spinnerStateP2P.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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
        db_wrapper = new DatabaseWrapper(getActivity());
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
                    db_wrapper = new DatabaseWrapper(getActivity());
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

                    CityAdapter cityAdapter = new CityAdapter(getActivity(),R.layout.spinner_country, cityList);
                    spinnerCityP2P.setAdapter(cityAdapter);

                }
            }.execute();


        }else{

            final CircleDialog circleDialog=new CircleDialog(getActivity(),0);
            circleDialog.setCancelable(true);
            circleDialog.show();


            System.out.println("Cities are not there");
            new CallWebService(AppConstants.GETCITIES +stateID,CallWebService.TYPE_JSONARRAY) {

                @Override
                public void response(String response) {

                    circleDialog.dismiss();
                    Type listType=new TypeToken<List<City>>(){
                    }.getType();
                    cityList =  new GsonBuilder().create().fromJson(response, listType);

                    CityAdapter cityAdapter = new CityAdapter(getActivity(),R.layout.spinner_country, cityList);
                    spinnerCityP2P.setAdapter(cityAdapter);

                    db_wrapper = new DatabaseWrapper(getActivity());
                    try {
                        db_wrapper.openDataBase();
                        db_wrapper.insertCities(cityList);
                        db_wrapper.close();
                    }catch(Exception e){e.printStackTrace();}

                }

                @Override
                public void error(VolleyError error) {

                    circleDialog.dismiss();
                }
            }.start();

        }

    }


    private void fetchReceipientsAndDisplay() {

        complexPreferences= ComplexPreferences.getComplexPreferences(getActivity(), "send_to_p2p_user_pref", 0);
        sendMoneyToPaylabasUser=complexPreferences.getObject("p2p_user", SendMoneyToPaylabasUser.class);
        receipients=sendMoneyToPaylabasUser.PayLabasRecipientList;
        P2PReceipient receipient=new P2PReceipient();
        receipient.FirstName = "Select";
        receipient.LastName = "Receipient";
        receipients.add(0,receipient);
        ReceipientAdapter countryAdapter = new ReceipientAdapter(getActivity(),R.layout.spinner_country, receipients);
        spinnerRecipientP2P.setAdapter(countryAdapter);

    }



    public class ReceipientAdapter extends ArrayAdapter<P2PReceipient> {
        Context context;
        int layoutResourceId;
        ArrayList<P2PReceipient> values;
        // int android.R.Layout.
        public ReceipientAdapter(Context context, int resource, ArrayList<P2PReceipient> objects) {
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

    public class CountryCodeAdapter extends ArrayAdapter<Country> {
        Context context;
        int layoutResourceId;
        ArrayList<Country> values;
        // int android.R.Layout.
        public CountryCodeAdapter(Context context, int resource, ArrayList<Country> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values=objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(getActivity());
            txt.setPadding(16,16,16,16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).CountryShortName + "(+" + values.get(position).CountryCode + ")");

            return  txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(getActivity());
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setText(values.get(position).CountryShortName + "(+" + values.get(position).CountryCode + ")");

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

            TextView txt = new TextView(getActivity());
            txt.setPadding(16,16,16,16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).StateName);

            return  txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(getActivity());
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setText(values.get(position).StateName);
            return  txt;
        }
    }

    public class CityAdapter extends ArrayAdapter<City>{

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

            TextView txt = new TextView(getActivity());
            txt.setPadding(16,16,16,16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).CityName);
            return  txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(getActivity());
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setText(values.get(position).CityName);
            return  txt;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnBackPtoPSecondScreen:

                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.popBackStack();

                break;

            case R.id.btnNextPtoPSecondScreen:


                //TODO

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.parent_moneytransfer_ptop,new PtoPThirdScreen(),"ptop_third");
                ft.addToBackStack("");
                ft.commit();

                break;


        }


    }
}
