package com.webmyne.paylabas.userapp.user_navigation;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gc.materialdesign.views.ButtonRectangle;

import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.widgets.SnackBar;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
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
import com.webmyne.paylabas.userapp.model.State;
import com.webmyne.paylabas.userapp.model.User;
import com.webmyne.paylabas.userapp.registration.ConfirmationActivity;
import com.webmyne.paylabas_user.R;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_CAMERA =0;
    private static final int RESULT_OK=0;
    private Spinner spQuestion;
    private CircleDialog circleDialog;
    private static int FLAG_PROFILE_IMAGE=0;
    private String mParam1;
    private String mParam2;
    private ButtonRectangle btnupdateProfile;
    private EditText edFirstName;
    private EditText edLastName;
    private EditText edCountryCode;
    private EditText edEmail;
    private EditText edAddress;
    private EditText edZipcode;
    private EditText edMobileno;
    private EditText edBirthdate;
    private EditText edAnswer;
    private Spinner spCountry;
    private Spinner spState;
    private Spinner spCity;
    private static final int CAMERA_REQUEST = 500;
    private static final int GALLERY_REQUEST = 300;
    final CharSequence[] items = { "Take Photo", "Choose from Gallery" };
    private ImageView imgprofile;
    ArrayList<Country> countrylist;
    ArrayList<State> statelist;
    ArrayList<City> cityList;
    int temp_CountryID;
    int temp_CountryID1;
    int temp_StateID;
    int temp_CityID;
    private DatabaseWrapper db_wrapper;
    private User user_prof;
    static int QuestionID;

    int FLAG_STATE=0;
    int FLAG_CITY=0;
     /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Profile() {
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
        View convertView = inflater.inflate(R.layout.fragment_profile, container, false);
        edFirstName = (EditText)convertView.findViewById(R.id.edFirstname);
        edLastName = (EditText)convertView.findViewById(R.id.edLastname);
        edEmail  = (EditText)convertView.findViewById(R.id.edEmail);
        edAddress = (EditText)convertView.findViewById(R.id.edAddress);
        edZipcode = (EditText)convertView.findViewById(R.id.edZipcode);
        edMobileno = (EditText)convertView.findViewById(R.id.edMobileno);
        edBirthdate = (EditText)convertView.findViewById(R.id.dgBirthdate);
        btnupdateProfile=(ButtonRectangle)convertView.findViewById(R.id.btnupdateProfile);
        imgprofile = (ImageView)convertView.findViewById(R.id.imgProfile);
        edCountryCode = (EditText)convertView.findViewById(R.id.edCountryCode);
        spQuestion = (Spinner)convertView.findViewById(R.id.spQuestion);
        spCountry = (Spinner)convertView.findViewById(R.id.spCountry);
        spState = (Spinner)convertView.findViewById(R.id.spState);
        spCity = (Spinner)convertView.findViewById(R.id.spCity);
        edAnswer = (EditText)convertView.findViewById(R.id.edanswer);
        intView();


        imgprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Update Profile Photo");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Take Photo")) {
                            Log.e("Camera ","called");
                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, CAMERA_REQUEST);
                            Log.e("Camera ","exit");

                        } else if (items[item].equals("Choose from Gallery")) {
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                            startActivityForResult(pickPhoto , GALLERY_REQUEST);
                        }
                    }
                });
                builder.show();
            }
        });

// Update profile button is Clicked.....
       btnupdateProfile.setOnClickListener(new View.OnClickListener() {
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

                else if(isEmptyField(edBirthdate)){
                    SnackBar bar = new SnackBar(getActivity(),"Please Enter Birthdate");
                    bar.show();
                }
                else if(isEmptyField(edAddress)){

                    SnackBar bar = new SnackBar(getActivity(),"Please Enter Street Address");
                    bar.show();
                }
                else if(isEmptyField(edZipcode)){

                    SnackBar bar = new SnackBar(getActivity(),"Please Enter Zipcode");
                    bar.show();

                }
                else if(!isZipcodeMatch(edZipcode)){

                    SnackBar bar = new SnackBar(getActivity(),"Please Enter Valid Zipcode");
                    bar.show();
                }
                else if(isEmptyField(edAnswer)){
                    SnackBar bar = new SnackBar(getActivity(),"Please Enter Zipcode");
                    bar.show();
                }
                else if (!checkvalidquestion()) {
                    SnackBar bar = new SnackBar(getActivity(),"Please Select any Question");
                    bar.show();
                }
                else {
                    if (FLAG_PROFILE_IMAGE == 0) {
                        SnackBar bar = new SnackBar(getActivity(),"Profile Image is not Changed !!!");
                        bar.show();
                        process_UpdateProfile();
                    } else if (FLAG_PROFILE_IMAGE == 1) {
                        SnackBar bar = new SnackBar(getActivity(),"New Profile Image!!!");
                        bar.show();
                        process_UpdateProfile();
                    }
                }
            }
        });

        return convertView;
    }

    // User ProfileUpdate process
    private void process_UpdateProfile() {
        try {

            JSONObject userObject = new JSONObject();
            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
            User user = complexPreferences.getObject("current_user", User.class);

            userObject.put("FName", edFirstName.getText().toString().trim());
            userObject.put("LName", edLastName.getText().toString().trim());
            userObject.put("DOBString", edBirthdate.getText().toString().trim());
            userObject.put("EmailID", edEmail.getText().toString().trim());
            userObject.put("Address", edAddress.getText().toString().trim());

            userObject.put("Country",countrylist.get(spCountry.getSelectedItemPosition()).CountryID);
            userObject.put("State", statelist.get(spState.getSelectedItemPosition()).StateID);
            userObject.put("City", cityList.get(spCountry.getSelectedItemPosition()).CityID);

            userObject.put("Zip", edZipcode.getText().toString().trim());
            userObject.put("MobileNo", edMobileno.getText().toString().trim());

            userObject.put("Answer",edAnswer.getText().toString().trim());

            //   userObject.put("DeviceType", "Android");
            //   userObject.put("Gender", "Male");
                userObject.put("Image", " ");
            // userObject.put("IsDeleted", false);
              userObject.put("IsRegistered", true);
            //  userObject.put("IsSuperAdmin", false);
            //    userObject.put("LastTryDate", null);
            //    userObject.put("LastTryDateLogin", null);
            //   userObject.put("LemonwayBal", "lemon way amount");
            userObject.put("MobileCountryCode", edCountryCode.getText().toString().trim());
            //    userObject.put("NotificationID", "notification");
            //    userObject.put("PassportNo", "paspport");
            //    userObject.put("PaylabasMerchantID", "palabs merchant id");
             userObject.put("QuestionId", spQuestion.getSelectedItemPosition());
            //userObject.put("ResponseCode", "response code");
            // userObject.put("ResponseMsg", "response msg");
            //   userObject.put("RoleId", 2147483647);
            //   userObject.put("Status", true);
            //   userObject.put("StatusMsg", "status msg");
            //   userObject.put("TryCount", 2147483647);
            //    userObject.put("TryCountLogin", 2147483647);
            //  userObject.put("UpdateDate", null);
            //   userObject.put("UpdateDateInt", 2147483647);
            userObject.put("UserID",user.UserID );
            // userObject.put("UserName", "user1");
            //   userObject.put("VerificationCode", "verficatino code");
           userObject.put("isVerified", true);


            Log.e("json obj",userObject.toString());
            final CircleDialog circleDialog = new CircleDialog(getActivity(), 0);
            circleDialog.setCancelable(true);
            circleDialog.show();

                    JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.USER_REGISTRATION, userObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jobj) {
                    circleDialog.dismiss();
                    String response = jobj.toString();
                    Log.e("Profile Response : ", "" + response);

                    try{

                        JSONObject obj = new JSONObject(response);
                        if(obj.getString("ResponseCode").equalsIgnoreCase("1")){


                            User currentUser = new GsonBuilder().create().fromJson(response,User.class);
                            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref",0);
                            complexPreferences.putObject("current_user", currentUser);
                            complexPreferences.commit();


                            SnackBar bar112 = new SnackBar(getActivity(), "Profile Updating ok!");
                            bar112.show();

                            Intent iCOnfirmSignUp = new Intent( getActivity() ,MyDrawerActivity.class );
                            startActivity(iCOnfirmSignUp);
                            getActivity().finish();



                        }

                        else {

                                SnackBar bar112 = new SnackBar(getActivity(), "Error Occur While Updating Profile !!!");
                                bar112.show();
                        }

                    } catch (Exception e) {

                    }


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    circleDialog.dismiss();
                    Log.e("error responsPROFILE: ", error + "");
                    SnackBar bar = new SnackBar(getActivity(), error.getMessage());
                    bar.show();

                }
            });
            MyApplication.getInstance().addToRequestQueue(req);
        } catch (Exception e) {
            Log.e("error responsPROFILE: ", e.toString() + "");
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("inside on acitivyty","inside on activity");
        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == getActivity().RESULT_OK) {

                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                // Convert ByteArray to Bitmap::

                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
                        byteArray.length);
                imgprofile.setBackground(null);
                imgprofile.setImageBitmap(bitmap);
                FLAG_PROFILE_IMAGE=1;


            }
            else{
                SnackBar bar = new SnackBar(getActivity(),"Error to load Image from Camera");
                bar.show();
            }

        }
        else  if (requestCode == GALLERY_REQUEST) {
            if (resultCode == getActivity().RESULT_OK) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getActivity().getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));

                Log.e("path of image from gallery......******.........", picturePath+"");

                imgprofile.setBackground(null);
                imgprofile.setImageBitmap(thumbnail);
                FLAG_PROFILE_IMAGE=1;

            }
            else{
                SnackBar bar = new SnackBar(getActivity(),"Error to load Image from Gallery");
                bar.show();
            }
        }
    }
    public void intView(){

           fetchCountryAndDisplay();

           spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fetchStateAndDisplay(spCountry.getSelectedItemPosition()+1);
                temp_CountryID1=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FLAG_CITY=1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spQuestion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                QuestionID=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public boolean checkvalidquestion()
    {
        boolean isquestionvalid = false;
        if(QuestionID==0){

            isquestionvalid=false;
        }
        else
        {
            isquestionvalid=true;
        }

        return isquestionvalid;
    }

public boolean isZipcodeMatch(EditText param1){
        boolean isMatch = false;
        if(param1.getText().toString().matches("[a-zA-Z0-9]*")){
            isMatch = true;
        }
        return isMatch;
    }
public boolean isEmptyField(EditText param1){

        boolean isEmpty = false;
        if(param1.getText() == null || param1.getText().toString().equalsIgnoreCase("")){
            isEmpty = true;
        }
        return isEmpty;
    }




public void  fetchCountryAndDisplay(){
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


            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
            User user = complexPreferences.getObject("current_user", User.class);

            CountryAdapter countryAdapter = new CountryAdapter(getActivity(),R.layout.spinner_country, countrylist);
            spCountry.setAdapter(countryAdapter);




            circleDialog =new CircleDialog(getActivity(),0);
            circleDialog.setCancelable(true);
            circleDialog.show();

            new CallWebService(AppConstants.GET_USER_PROFILE +user.UserID,CallWebService.TYPE_JSONOBJECT) {
                @Override
                public void response(String response) {

                    Log.e("Profile Response",response);
                    User currentUser_Profile = new GsonBuilder().create().fromJson(response,User.class);

                    ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
                    complexPreferences.putObject("current_user", currentUser_Profile);
                    complexPreferences.commit();

                    ComplexPreferences complexPreferences2 = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
                    user_prof = complexPreferences2.getObject("current_user", User.class);

                    String dateTime = user_prof.DOBString.toString();
                    String date = dateTime.split(" ")[0];

                    edFirstName.setText(user_prof.FName.toString());
                    edLastName.setText(user_prof.LName.toString());
                    edBirthdate.setText(date);
                    edEmail.setText(user_prof.EmailID.toString());
                    edAddress.setText(user_prof.Address.toString());
                    edZipcode.setText(user_prof.Zip.toString());
                    edCountryCode.setText(user_prof.MobileCountryCode.toString());
                    edMobileno.setText(user_prof.MobileNo.toString());
                    spQuestion.setSelection((int) user_prof.QuestionId);
                    edAnswer.setText(user_prof.Answer);

                    fetchStateAndDisplay((int)user_prof.Country+1);
                    spCountry.setSelection((int)user_prof.Country-1);

                    circleDialog.dismiss();
                }
                @Override
                public void error(VolleyError error) {
                    Log.e("volly er",error.toString());
                    circleDialog.dismiss();
                }
            }.start();
         }

    }.execute();


}

private void fetchStateAndDisplay(int CountryID) {

        statelist = new ArrayList<State>();
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
                Log.e("state lsit size",String.valueOf(statelist.size()));

                if(FLAG_STATE==0){
                    for(int i=0;i<statelist.size();i++)
                    {
                        if((int)statelist.get(i).StateID==(int)user_prof.State){
                            spState.setSelection(i);
                            FLAG_STATE=1;
                        }
                    }
                }
                spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        temp_StateID=position;
                        FLAG_STATE=1;
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
                    Log.e("city lsit size1",String.valueOf(cityList.size()));
                    if(FLAG_CITY==0){
                        for(int i=0;i<cityList.size();i++)
                        {
                            if((int)cityList.get(i).CityID==(int)user_prof.City){
                                spCity.setSelection(i);
                                FLAG_CITY=1;
                            }
                        }
                    }
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
                    Log.e("city lsit size2",String.valueOf(cityList.size()));

                    if(FLAG_CITY==0){
                        for(int i=0;i<cityList.size();i++)
                        {
                            if((int)cityList.get(i).CityID==(int)user_prof.City){
                                spCity.setSelection(i);
                                FLAG_CITY=1;
                            }
                        }
                    }
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



}
