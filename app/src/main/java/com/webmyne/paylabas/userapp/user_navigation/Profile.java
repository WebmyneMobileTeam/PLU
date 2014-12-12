package com.webmyne.paylabas.userapp.user_navigation;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.gc.materialdesign.views.ButtonRectangle;

import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.widgets.SnackBar;
import com.webmyne.paylabas.userapp.base.MyDrawerActivity;
import com.webmyne.paylabas.userapp.helpers.ComplexPreferences;
import com.webmyne.paylabas.userapp.home.MyAccountFragment;
import com.webmyne.paylabas.userapp.model.User;
import com.webmyne.paylabas_user.R;

import java.util.Calendar;

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


        imgprofile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    SnackBar bar = new SnackBar(getActivity(),"Camera is Cliked");
                    bar.show();
                }
                return false;
            }
        });

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
                    processUpdateProfile();
                }
            }
        });

        return convertView;
    }

    public void intView(){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
        User user = complexPreferences.getObject("current_user", User.class);
        edFirstName.setText(user.FName.toString());
        edLastName.setText(user.LName.toString());
//    edBirthdate.setText(user.DOBString.toString());
        edEmail.setText(user.EmailID.toString());
        //  Log.e("erro-add", user.Address.toString());
        //edAddress.setText(user.Address.toString());
//    edZipcode.setText(user.Zip.toString());
        //edCountryCode.setText(user.MobileCountryCode);
        edMobileno.setText(user.MobileNo.toString());

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

    public void processUpdateProfile(){

        SnackBar bar = new SnackBar(getActivity(),"Profile Update Sucessfully");
        bar.show();

        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.main_container,new MyAccountFragment());
        ft.commit();


    }

}
