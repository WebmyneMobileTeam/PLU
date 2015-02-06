package com.webmyne.paylabas.userapp.my_recipient;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.widgets.SnackBar;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.webmyne.paylabas.userapp.base.ConfirmRecipientActivity;
import com.webmyne.paylabas.userapp.base.DatabaseWrapper;
import com.webmyne.paylabas.userapp.base.MyApplication;
import com.webmyne.paylabas.userapp.base.MyDrawerActivity;
import com.webmyne.paylabas.userapp.custom_components.CircleDialog;
import com.webmyne.paylabas.userapp.helpers.AppConstants;
import com.webmyne.paylabas.userapp.helpers.CallWebService;
import com.webmyne.paylabas.userapp.helpers.ComplexPreferences;
import com.webmyne.paylabas.userapp.home.MyAccountFragment;
import com.webmyne.paylabas.userapp.model.City;
import com.webmyne.paylabas.userapp.model.Country;
import com.webmyne.paylabas.userapp.model.Receipient;
import com.webmyne.paylabas.userapp.model.State;
import com.webmyne.paylabas.userapp.model.User;
import com.webmyne.paylabas_user.R;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyRecipient_add_edit#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyRecipient_add_edit extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private  String CountryName;
    private  String getMobileno;

    private EditText edFirstName;
    private EditText edLastName;
    private EditText edEmail;
    private EditText edMobileno;
    private EditText edCountryCode;
    private Spinner spCountry;
    private Spinner spState;
    private Spinner spCity;
    private ButtonRectangle btnAddRecipient;

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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyRecipient_add_edit.
     */
    // TODO: Rename and change types and number of parameters
    public static MyRecipient_add_edit newInstance(String param1, String param2) {
        MyRecipient_add_edit fragment = new MyRecipient_add_edit();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MyRecipient_add_edit() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView = inflater.inflate(R.layout.fragment_my_recipient_add_edit_del, container, false);

        btnAddRecipient = (ButtonRectangle)convertView.findViewById(R.id.btnAddRecipient);
        edFirstName = (EditText)convertView.findViewById(R.id.edFirstname);
        edLastName = (EditText)convertView.findViewById(R.id.edLastname);
        edEmail  = (EditText)convertView.findViewById(R.id.edEmail);
        edMobileno = (EditText)convertView.findViewById(R.id.edMobileno);
        edCountryCode = (EditText)convertView.findViewById(R.id.edCountryCode);

        spCountry = (Spinner)convertView.findViewById(R.id.spCountry);
        spState = (Spinner)convertView.findViewById(R.id.spState);
        spCity = (Spinner)convertView.findViewById(R.id.spCity);


        if(getArguments().getInt("pos")!=-1){

           getCountryID = getArguments().getInt("CountryID");

           RecipientId = getArguments().getInt("RecipientID");
           edFirstName.setText(getArguments().getString("FirstName"));
           edLastName.setText(getArguments().getString("LastName"));
           edEmail.setText(getArguments().getString("Email"));
           edMobileno.setText(getArguments().getString("Mobileno"));
           btnAddRecipient.setText("UPDATE RECIPIENT");
           btnAddRecipient.setBackgroundColor(getResources().getColor(R.color.paylabas_blue));

        }
        else{

            getCountryID=1;
            RecipientId=0;

        }

        fetchCountryAndDisplay();


        btnAddRecipient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isEmptyField(edFirstName)){
                    SnackBar bar = new SnackBar(getActivity(),"Please Enter First Name");
                    bar.show();
                }
                else if(isEmptyField(edLastName)){
                    SnackBar bar = new SnackBar(getActivity(),"Please Enter Last Name");
                    bar.show();
                }
                else if(isEmptyField(edEmail)){
                    SnackBar bar = new SnackBar(getActivity(),"Please Enter Email Address");
                    bar.show();
                }
                else if(!isEmailMatch(edEmail)){
                    SnackBar bar = new SnackBar(getActivity(),"Please Enter Valid Email Address");
                    bar.show();
                }
                else if(isMobileMatch(edMobileno)){

                    SnackBar bar = new SnackBar(getActivity(),"Please Enter Valid Mobile Number");
                    bar.show();

                }
                else {

                    if (btnAddRecipient.getText().toString().trim().equals("UPDATE RECIPIENT")){
                        processUpdateRecipient();
                    }
                    else
                    processVerifyRecipient();
                }
            }
        });


        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = countrylist.get(position).CountryID;
                fetchStateAndDisplay(pos);
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




        return convertView;
    }

    public void processUpdateRecipient(){

        try {

            ComplexPreferences complexPreferences1 = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
            User temp_user = complexPreferences1.getObject("current_user", User.class);

            JSONObject userObject = new JSONObject();
            userObject.put("FirstName", edFirstName.getText().toString().trim());
            userObject.put("LastName", edLastName.getText().toString().trim());
            userObject.put("EmailID", edEmail.getText().toString().trim());
            userObject.put("MobileCountryCode", edCountryCode.getText().toString().trim());
            userObject.put("CountryID",countrylist.get(spCountry.getSelectedItemPosition()).CountryID);
            userObject.put("StateID", statelist.get(temp_StateID).StateID);
            userObject.put("CityID", cityList.get(temp_CityID).CityID);
            userObject.put("MobileNo", edMobileno.getText().toString().trim());
            userObject.put("RecipientID", RecipientId);
            userObject.put("UserEmailID", temp_user.EmailID);
            userObject.put("UserID", temp_user.UserID);


            Log.e("json obj with rec",userObject.toString());
            final CircleDialog circleDialog = new CircleDialog(getActivity(), 0);
            circleDialog.setCancelable(true);
            circleDialog.show();

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.ADD_RECIPIENT, userObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jobj) {
                    circleDialog.dismiss();
                    String response = jobj.toString();
                    Log.e("Response Addrecipient: ", "" + response);

                    try{

                        JSONObject obj = new JSONObject(response);
                        if(obj.getString("ResponseCode").equalsIgnoreCase("1")){


                            SnackBar bar = new SnackBar(getActivity(),"Recipient Updated Sucessfully");
                            bar.show();

                            CountDownTimer countDownTimer;
                            countDownTimer = new MyCountDownTimer(3000, 1000); // 1000 = 1s
                            countDownTimer.start();


                        }

                        else {
                            if(obj.getString("ResponseCode").equalsIgnoreCase("-2")) {
                                SnackBar bar112 = new SnackBar(getActivity(), "Error occur ");
                                bar112.show();
                            }
                            else if(obj.getString("ResponseCode").equalsIgnoreCase("-1")) {
                                SnackBar bar112 = new SnackBar(getActivity(), "Error Occur While Updating Recipient details");
                                bar112.show();
                            }
                            else if(obj.getString("ResponseCode").equalsIgnoreCase("2")) {
                                SnackBar bar112 = new SnackBar(getActivity(), "Mobile No. already Exist");
                                bar112.show();
                            }
                            else if(obj.getString("ResponseCode").equalsIgnoreCase("3")) {
                                SnackBar bar112 = new SnackBar(getActivity(), "Email Id already Exist");
                                bar112.show();
                            }
                            else if(obj.getString("ResponseCode").equalsIgnoreCase("4")) {
                                SnackBar bar112 = new SnackBar(getActivity(), "Mobile No. & Email Id already Exist");
                                bar112.show();
                            }
                            else{
                                SnackBar bar112 = new SnackBar(getActivity(), "Time out, Please Try again.");
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
                    Log.e("error updaterecipeint: ", error + "");
                    SnackBar bar = new SnackBar(getActivity(), error.getMessage());
                    bar.show();

                }
            });
            MyApplication.getInstance().addToRequestQueue(req);


        } catch (Exception e) {
            Log.e("error updaterecipeint: ", e + "");

        }
    }

 public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }
        @Override
        public void onFinish() {
            Log.e("counter","Time's up!");

            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.main_container,new MyAccountFragment());
            ft.commit();
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

    }



public void processVerifyRecipient(){
        try {

            ComplexPreferences complexPreferences2 = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
            User user = complexPreferences2.getObject("current_user", User.class);

            final JSONObject userObject = new JSONObject();

            Log.e("User id ", String.valueOf(user.UserID));

            userObject.put("EmailID", edEmail.getText().toString().trim());
            userObject.put("MobileNo", edMobileno.getText().toString().trim());
            userObject.put("MobileCountryCode", edCountryCode.getText().toString().trim());
            userObject.put("UserID", user.UserID);

            final long tempUserID= user.UserID;
            final String tempUserEmailID=user.EmailID;

            Log.e("json obj",userObject.toString());
            final CircleDialog circleDialog = new CircleDialog(getActivity(), 0);
            circleDialog.setCancelable(true);
            circleDialog.show();

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.VERIFY_RECIPIENT, userObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jobj) {
                    circleDialog.dismiss();
                    String response = jobj.toString();
                    Log.e("Response Addrecipient: ", "" + response);
                    try{

                        JSONObject obj = new JSONObject(response);

                        if(obj.getString("ResponseCode").equalsIgnoreCase("1")){
                            User currentUser = new GsonBuilder().create().fromJson(response,User.class);
                            final JSONObject newRecipientobj = new JSONObject();

                            newRecipientobj.put("FirstName", edFirstName.getText().toString().trim());
                            newRecipientobj.put("LastName", edLastName.getText().toString().trim());

                            newRecipientobj.put("EmailID", edEmail.getText().toString().trim());
                            newRecipientobj.put("MobileNo", edMobileno.getText().toString().trim());
                            newRecipientobj.put("MobileCountryCode", edCountryCode.getText().toString().trim());

                            newRecipientobj.put("CityID", cityList.get(spCity.getSelectedItemPosition()).CityID);
                            newRecipientobj.put("CountryID", countrylist.get(spCountry.getSelectedItemPosition()).CountryID);
                            newRecipientobj.put("StateID", statelist.get(spState.getSelectedItemPosition()).StateID);

                            newRecipientobj.put("RecipientID",0);

                            newRecipientobj.put("UserEmailID",tempUserEmailID);
                            newRecipientobj.put("UserID",tempUserID);


                            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref",0);
                            complexPreferences.putObject("new-recipient", newRecipientobj);
                            complexPreferences.commit();

                            // set verification code true
                            SharedPreferences preferences = getActivity().getSharedPreferences("Recipient", getActivity().MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("VerificationCode", currentUser.VerificationCode.toString());
                            editor.commit();

                            Intent verfiyRecipient = new Intent( getActivity() ,ConfirmRecipientActivity.class );
                            startActivity(verfiyRecipient);
                            getActivity().finish();

                        }

                        else {
                            if(obj.getString("ResponseCode").equalsIgnoreCase("-2")) {
                                SnackBar bar112 = new SnackBar(getActivity(), "Error occur ");
                                bar112.show();
                            }
                            else if(obj.getString("ResponseCode").equalsIgnoreCase("-1")) {
                                SnackBar bar112 = new SnackBar(getActivity(), "Error Occur While adding New Recipient");
                                bar112.show();
                            }
                            else if(obj.getString("ResponseCode").equalsIgnoreCase("2")) {
                                SnackBar bar112 = new SnackBar(getActivity(), "Mobile No.   already Exist");
                                bar112.show();
                            }
                            else if(obj.getString("ResponseCode").equalsIgnoreCase("3")) {
                                SnackBar bar112 = new SnackBar(getActivity(), "Email Id already Exist");
                                bar112.show();
                            }
                            else if(obj.getString("ResponseCode").equalsIgnoreCase("4")) {
                                SnackBar bar112 = new SnackBar(getActivity(), "Mobile No. & Email Id already Exist");
                                bar112.show();
                            }
                            else{
                                SnackBar bar112 = new SnackBar(getActivity(), "Time out, Please Try again.");
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
                    Log.e("error Addrecipeint: ", error + "");
                    SnackBar bar = new SnackBar(getActivity(), error.getMessage());
                    bar.show();

                }
            });
            MyApplication.getInstance().addToRequestQueue(req);

        } catch (Exception e) {

        }
    }
    public boolean isEmptyField(EditText param1){
        boolean isEmpty = false;
        if(param1.getText() == null || param1.getText().toString().equalsIgnoreCase("")){
            isEmpty = true;
        }
        return isEmpty;
    }
    public boolean isEmailMatch(EditText param1){
        // boolean isMatch = false;
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(param1.getText().toString()).matches();
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


    private void fetchCountryAndDisplay() {

        new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                db_wrapper = new DatabaseWrapper(getActivity());
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

                CountryAdapter countryAdapter = new CountryAdapter(getActivity(),R.layout.spinner_country, countrylist);
                spCountry.setAdapter(countryAdapter);
                spCountry.setSelection(getCountryID-1);
            }
        }.execute();

    }
    private void fetchStateAndDisplay(int CountryID) {

        statelist = new ArrayList<State>();
        edCountryCode.setText(String.valueOf(countrylist.get(spCountry.getSelectedItemPosition()).CountryCode));
        temp_CountryID=CountryID;

        new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                db_wrapper = new DatabaseWrapper(getActivity());
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
                StateAdapter stateAdapter = new StateAdapter(getActivity(),R.layout.spinner_state, statelist);
                spState.setAdapter(stateAdapter);

                int posState = 0;
                try {
                    for (int i = 0; i < statelist.size(); i++) {
                        if (statelist.get(i).StateID == getArguments().getInt("StateID")) {
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
                    spCity.setAdapter(cityAdapter);

                    int posCity = 0;
                    for(int i=0;i<cityList.size();i++){
                        if(cityList.get(i).CityID == getArguments().getInt("CityID")){
                            posCity = i;
                            break;
                        }
                    }
                     spCity.setSelection(posCity);

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
                    spCity.setAdapter(cityAdapter);

                    db_wrapper = new DatabaseWrapper(getActivity());
                    try {
                        db_wrapper.openDataBase();
                        db_wrapper.insertCities(cityList);
                        db_wrapper.close();
                    }catch(Exception e){e.printStackTrace();}

                    int posCity = 0;
                    for(int i=0;i<cityList.size();i++){
                        if(cityList.get(i).CityID == getArguments().getInt("CityID")){
                            posCity = i;
                            break;
                        }
                    }

                    spCity.setSelection(posCity);
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



//end of main class
}
