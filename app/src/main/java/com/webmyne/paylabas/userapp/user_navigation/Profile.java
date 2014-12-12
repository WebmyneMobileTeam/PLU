package com.webmyne.paylabas.userapp.user_navigation;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.VolleyError;
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
import com.webmyne.paylabas.userapp.model.User;
import com.webmyne.paylabas_user.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Type;
import java.util.Calendar;
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

    // TODO: Rename and change types of parameters
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
    private Spinner spCountry;
    private Spinner spState;
    private Spinner spCity;
    private static final int CAMERA_REQUEST = 500;
    private static final int GALLERY_REQUEST = 300;
    final CharSequence[] items = { "Take Photo", "Choose from Gallery" };
    private ImageView imgprofile;
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

        intView();



//  calling date picker dialog box
        edBirthdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // displaying the date picker dialog box
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            edBirthdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
                    datePicker.show();
                }
                return false;
            }
        });


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
                else{
                  //  processUpdateProfile();
                }
            }
        });

        return convertView;
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

                imgprofile.setImageBitmap(bitmap);
               // imgprofile.getLayoutParams().width=150;
               // imgprofile.getLayoutParams().height=150;

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
              //  imgprofile.setBackground(andro);
                imgprofile.setBackground(null);
                imgprofile.setImageBitmap(thumbnail);

            }
            else{
                SnackBar bar = new SnackBar(getActivity(),"Error to load Image from Gallery");
                bar.show();
            }
        }
    }
    public void intView(){

        processUpdateProfile();
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

public void get_data(){
 /*   final CircleDialog circleDialog=new CircleDialog(getActivity().getApplicationContext(),0);
    circleDialog.setCancelable(true);
    circleDialog.show();
*/

    ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
    User user = complexPreferences.getObject("current_user", User.class);
    Log.e("in getdata","start");

    new CallWebService(AppConstants.GET_USER_PROFILE +user.UserID,CallWebService.TYPE_JSONARRAY) {

        @Override
        public void response(String response) {
           // circleDialog.dismiss();
            Log.e("res of profile",response);
            User currentUser = new GsonBuilder().create().fromJson(response,User.class);
            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
            complexPreferences.putObject("current_user", currentUser);
            complexPreferences.commit();
        }

        @Override
        public void error(VolleyError error) {

         //  circleDialog.dismiss();
        }
    }.start();

}
    public void processUpdateProfile(){

           new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                 get_data();


                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
                User user1 = complexPreferences.getObject("current_user", User.class);

                   edFirstName.setText(user1.FName.toString());
                   edLastName.setText(user1.LName.toString());
                  // edBirthdate.setText(user.DOBString.toString());
                   edEmail.setText(user1.EmailID.toString());
                  // edAddress.setText(user.Address.toString());
                  // edZipcode.setText(user.Zip.toString());
                  // edCountryCode.setText(user.MobileCountryCode);
                   edMobileno.setText(user1.MobileNo.toString());

            }
        }.execute();








        /*
        SnackBar bar = new SnackBar(getActivity(),"Profile Update Sucessfully");
        bar.show();

        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.main_container,new MyAccountFragment());
        ft.commit();
*/

        // end od processupdate profile
    }

}
