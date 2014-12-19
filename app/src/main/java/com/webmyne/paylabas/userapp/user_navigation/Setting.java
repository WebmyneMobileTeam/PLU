package com.webmyne.paylabas.userapp.user_navigation;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.widgets.SnackBar;
import com.google.gson.GsonBuilder;
import com.webmyne.paylabas.userapp.base.DatabaseWrapper;
import com.webmyne.paylabas.userapp.base.MyDrawerActivity;
import com.webmyne.paylabas.userapp.custom_components.CircleDialog;
import com.webmyne.paylabas.userapp.helpers.AppConstants;
import com.webmyne.paylabas.userapp.helpers.CallWebService;
import com.webmyne.paylabas.userapp.helpers.ComplexPreferences;
import com.webmyne.paylabas.userapp.model.Country;
import com.webmyne.paylabas.userapp.model.User;
import com.webmyne.paylabas_user.R;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Setting#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Setting extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ButtonFlat btnChangePassword;
    private ButtonFlat btnUpdateEmail;
    private ButtonFlat btnUpdateMobile;

    ArrayList<Country> countrylist;
    private TextView txtMobile;
    private TextView txtEmail;
    private  AlertDialog.Builder alertDialogBuilder;
    private DatabaseWrapper db_wrapper;
    int temp_CountryID;
    private Spinner spCountry;
    private TextView txtMobileCountryCode;
    int temp_pos_country;
    private User user;
    private EditText edUpdateEmail;
    private EditText edUpdateMobile;
    private LinearLayout verfiyLayout;

    private ButtonRectangle btnVerify;

    private TextView msg;

    int FLAG=0; // 1 for Mobile & 0 for Email


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Setting.
     */
    // TODO: Rename and change types and number of parameters
    public static Setting newInstance(String param1, String param2) {
        Setting fragment = new Setting();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Setting() {
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
        View convertView = inflater.inflate(R.layout.fragment_setting, container, false);
        btnChangePassword = (ButtonFlat)convertView.findViewById(R.id.btnChangePassword);
        btnUpdateEmail = (ButtonFlat)convertView.findViewById(R.id.btnUpdateEmail);
        btnUpdateMobile = (ButtonFlat)convertView.findViewById(R.id.btnUpdateMobile);

        txtMobile = (TextView)convertView.findViewById(R.id.txtMobile);
        txtEmail = (TextView)convertView.findViewById(R.id.txtEmail);

        verfiyLayout = (LinearLayout)convertView.findViewById(R.id.verfiyLayout);
        msg = (TextView)convertView.findViewById(R.id.msg);
        btnVerify = (ButtonRectangle)convertView.findViewById(R.id.btnVerify);

        verfiyLayout.setVisibility(View.GONE);

        processDisplayEmail_Mobile();

       // processCreateDialogUpdateMobile();


// click to perform btnVerifyclick
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1 for Mobile and 0 for email
                if(FLAG==0){
                    process_Update_Email();
                }
                else {
                    process_update_Mobile();
                }
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener()

              {
                  @Override
                  public void onClick (View v){
                  // create alert dialog
                  processCreateDialogChangePassword();
                  AlertDialog alertDialog = alertDialogBuilder.create();
                  alertDialog.show();
              }
              }

              );
              // click to perform update mobile alert dialog
              btnUpdateMobile.setOnClickListener(new View.OnClickListener()

              {
                  @Override
                  public void onClick (View v){
                  processCreateDialogUpdateMobile();
                  AlertDialog alertDialog = alertDialogBuilder.create();
                  alertDialog.show();
              }
              }

              );

              // click to perform update email alert dialog
              btnUpdateEmail.setOnClickListener(new View.OnClickListener()

              {
                  @Override
                  public void onClick (View v){
                  processCreateDialogUpdateEmail();
                  AlertDialog alertDialog = alertDialogBuilder.create();
                  alertDialog.show();
              }
              }

              );


              return convertView;
          }


private void process_Update_Email(){
    SnackBar bar = new SnackBar(getActivity(),"Update email process");
    bar.show();
}

private void process_update_Mobile(){
    SnackBar bar = new SnackBar(getActivity(),"Update mobile process");
    bar.show();
}

    private void processDisplayEmail_Mobile() {
    ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
    user = complexPreferences.getObject("current_user", User.class);
    Log.e("user id", String.valueOf(user.UserID));
    txtEmail.setText(user.EmailID);
    txtMobile.setText("+"+user.MobileCountryCode+" "+user.MobileNo);

    }

public boolean isEmailMatch(EditText param1){
        // boolean isMatch = false;
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(param1.getText().toString()).matches();
    }
public boolean isEmptyField(EditText param1){

        boolean isEmpty = false;
        if(param1.getText() == null || param1.getText().toString().equalsIgnoreCase("")){
            isEmpty = true;
        }
        return isEmpty;
    }

private  void processCreateDialogChangePassword()
{

    View v = getActivity().getLayoutInflater().inflate(R.layout.changepassword_dialog, null);
    alertDialogBuilder = new AlertDialog.Builder(getActivity());
    // set title
    alertDialogBuilder.setTitle("Change Password");
    alertDialogBuilder.setView(v);
    // set dialog message
    alertDialogBuilder
            .setCancelable(false)
            .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {
                    // if this button is clicked, close
                    // current activity
                    SnackBar bar = new SnackBar(getActivity(),"Change Password");
                    bar.show();
                }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // if this button is clicked, just close
                    // the dialog box and do nothing
                    dialog.dismiss();
                }
            });

}

private  void processCreateDialogUpdateMobile(){

        View v = getActivity().getLayoutInflater().inflate(R.layout.changemobile_dialog, null);

        spCountry = (Spinner)v.findViewById(R.id.spCountry);
        txtMobileCountryCode = (TextView)v.findViewById(R.id.txtMobileCountryCode);
        edUpdateMobile =(EditText)v.findViewById(R.id.edUpdateMobile);

        fetchCountryAndDisplay();

        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        // set title
        alertDialogBuilder.setTitle("Update Your Mobile No");
        alertDialogBuilder.setView(v);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        if (isEmptyField(edUpdateMobile)) {
                            Toast.makeText(getActivity().getBaseContext(),"Please Enter Mobile No.",Toast.LENGTH_SHORT).show();
                        } else {
                            FLAG =1;
                            msg.setText("Enter verification code that sent to your mobile");
                            verfiyLayout.setVisibility(View.VISIBLE);
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.dismiss();
                    }
                });

    }

private  void processCreateDialogUpdateEmail(){

        View v = getActivity().getLayoutInflater().inflate(R.layout.changeemail_dialog, null);

        edUpdateEmail = (EditText)v.findViewById(R.id.edUpdateEmail);

        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        // set title
        alertDialogBuilder.setTitle("Update Your Email Address");
        alertDialogBuilder.setView(v);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (isEmptyField(edUpdateEmail)) {
                            Toast.makeText(getActivity().getBaseContext(),"Please Enter Email Address",Toast.LENGTH_SHORT).show();
                        } else if (!isEmailMatch(edUpdateEmail)) {
                            Toast.makeText(getActivity().getBaseContext(),"Please Enter Correct Email Address",Toast.LENGTH_SHORT).show();
                        } else {
                            FLAG = 0;
                            msg.setText("Enter verification code that sent to your Email");
                            verfiyLayout.setVisibility(View.VISIBLE);

                        }

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.dismiss();
                    }
                });

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

                try{

                    CountryAdapter countryAdapter = new CountryAdapter(getActivity(),R.layout.spinner_country, countrylist);
                    spCountry.setAdapter(countryAdapter);
                    spCountry.setSelection((int)user.CountryID-1);
                    spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            txtMobileCountryCode.setText(String.valueOf(countrylist.get(position).CountryCode));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                }catch(Exception e){
                }
            }

        }.execute();


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
//end os main class
}
