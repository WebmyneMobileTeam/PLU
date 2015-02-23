package com.webmyne.paylabas.userapp.user_navigation;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.widgets.SnackBar;
import com.google.gson.GsonBuilder;
import com.webmyne.paylabas.userapp.base.DatabaseWrapper;
import com.webmyne.paylabas.userapp.base.MyApplication;
import com.webmyne.paylabas.userapp.base.MyDrawerActivity;
import com.webmyne.paylabas.userapp.custom_components.CircleDialog;
import com.webmyne.paylabas.userapp.helpers.AppConstants;
import com.webmyne.paylabas.userapp.helpers.CallWebService;
import com.webmyne.paylabas.userapp.helpers.ComplexPreferences;
import com.webmyne.paylabas.userapp.model.Country;
import com.webmyne.paylabas.userapp.model.User;
import com.webmyne.paylabas_user.R;

import org.json.JSONObject;

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
    private EditText edUpdateEmail,edVerificationCode;
    private EditText edUpdateMobile;
    private LinearLayout verfiyLayout;


    private EditText edOldPassword,edNewpassword,edNewConfirmpassword;

    private ButtonRectangle btnVerify;

    private TextView msg;

    String toupdateEmail,toupdateMobile,toupdateMobileCountryCode;

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
        edVerificationCode= (EditText)convertView.findViewById(R.id.edVerificationCode);
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

                    if (isEmptyField(edVerificationCode)) {
                        SnackBar bar = new SnackBar(getActivity(),getString(R.string.code_R1));
                        bar.show();
                    }
                    else if(checkVerificationcodeEmail(edVerificationCode)){
                        SnackBar bar = new SnackBar(getActivity(),getString(R.string.code_R3));
                        bar.show();
                    }
                    else{
                        process_Update_Email(toupdateEmail,edVerificationCode.getText().toString().trim());
                    }

                }
                else {
                    if (isEmptyField(edVerificationCode)) {
                        SnackBar bar = new SnackBar(getActivity(),getString(R.string.code_R2));
                        bar.show();
                    }
                    else if(checkVerificationcodeMobile(edVerificationCode)){
                        SnackBar bar = new SnackBar(getActivity(),getString(R.string.code_R4));
                        bar.show();
                    }
                    else{
                        process_update_Mobile(toupdateMobile,toupdateMobileCountryCode,edVerificationCode.getText().toString().trim());
                    }

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

    public boolean checkVerificationcodeMobile(EditText param1){

        SharedPreferences preferences = getActivity().getSharedPreferences("verificationCode", getActivity().MODE_PRIVATE);

        boolean isWrong = false;
        if(!param1.getText().toString().equals(preferences.getString("VCMobile","vfcode"))){
            isWrong = true;
        }
        return isWrong;
    }
    public boolean checkVerificationcodeEmail(EditText param1){

        SharedPreferences preferences = getActivity().getSharedPreferences("verificationCode", getActivity().MODE_PRIVATE);

        boolean isWrong = false;
        if(!param1.getText().toString().equals(preferences.getString("VCEmail","vfcode"))){
            isWrong = true;
        }
        return isWrong;
    }


private void process_SendVC_Email(String UpdateEmail){
    toupdateEmail = UpdateEmail;
        try{
            JSONObject userObject = new JSONObject();


            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
            User user = complexPreferences.getObject("current_user", User.class);


            userObject.put("EmailID",user.EmailID);
            userObject.put("MobileCountryCode",user.MobileCountryCode);
            userObject.put("MobileNo",user.MobileNo);
            userObject.put("UserID",String.valueOf(user.UserID));


            Log.e("json obj send vc email", userObject.toString());

            final CircleDialog circleDialog = new CircleDialog(getActivity(), 0);
            circleDialog.setCancelable(true);
            circleDialog.show();

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.SEND_VC_TO_UPDATE_EMAIL, userObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jobj) {
                    circleDialog.dismiss();
                    String response = jobj.toString();
                    Log.e("send vc email Response", "" + response);


                    try{
                        JSONObject obj = new JSONObject(response);
                        if(obj.getString("ResponseCode").equalsIgnoreCase("1")){


                            SnackBar bar = new SnackBar(getActivity(),getString(R.string.code_VERIFCAIOYOCODESENT));
                            bar.show();

                            SharedPreferences preferences = getActivity().getSharedPreferences("verificationCode", getActivity().MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("VCEmail", obj.getString("VerificationCode").trim());
                            editor.commit();

                        }

                        else {
                            SnackBar bar112 = new SnackBar(getActivity(), obj.getString("ResponseMsg"));
                            bar112.show();

                        }

                    } catch (Exception e) {
                        Log.e("error response send vc email: ", e.toString() + "");
                    }


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    circleDialog.dismiss();
                    Log.e("error response forgot password2: ", error + "");
                    SnackBar bar = new SnackBar(getActivity(),getString(R.string.code_NEWE));
                    bar.show();

                }
            });


            req.setRetryPolicy(  new DefaultRetryPolicy(0,0,0));
            MyApplication.getInstance().addToRequestQueue(req);

            // end of main try block
        } catch(Exception e){
            Log.e("error in forgot password2",e.toString());
        }
    }


private void process_Update_Email(String Email,String vc){
    try{
        JSONObject userObject = new JSONObject();


        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
        User user = complexPreferences.getObject("current_user", User.class);


        userObject.put("EmailID",Email);
        userObject.put("MobileCountryCode",user.MobileCountryCode);
        userObject.put("MobileNo",user.MobileNo);
        userObject.put("UserID",String.valueOf(user.UserID));
        userObject.put("VerificationCode",vc);

        Log.e("json obj update email", userObject.toString());

        final CircleDialog circleDialog = new CircleDialog(getActivity(), 0);
        circleDialog.setCancelable(true);
        circleDialog.show();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.UPDATE_EMAIL, userObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jobj) {
                circleDialog.dismiss();
                String response = jobj.toString();
                Log.e("update emailResponse", "" + response);


                try{
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("ResponseCode").equalsIgnoreCase("1")){

                        SnackBar bar = new SnackBar(getActivity(),getString(R.string.code_EAMILUPDATES));
                        bar.show();

                      /*  SharedPreferences preferences = getActivity().getSharedPreferences("verificationCode", getActivity().MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.remove("VCEmail").commit();
*/
                        //      clearResetCode();

                        CountDownTimer countDownTimer;
                        countDownTimer = new MyCountDownTimer(3000, 1000); // 1000 = 1s
                        countDownTimer.start();


                    }

                    else {
                        SnackBar bar112 = new SnackBar(getActivity(), obj.getString("ResponseMsg"));
                        bar112.show();

                    }

                } catch (Exception e) {
                    Log.e("error response update email: ", e.toString() + "");
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                circleDialog.dismiss();
                Log.e("error response forgot password2: ", error + "");
                SnackBar bar = new SnackBar(getActivity(),getString(R.string.code_NEWE));
                bar.show();

            }
        });


        req.setRetryPolicy(  new DefaultRetryPolicy(0,0,0));
        MyApplication.getInstance().addToRequestQueue(req);

        // end of main try block
    } catch(Exception e){
        Log.e("error in forgot password2",e.toString());
    }
}



private void process_SendVC_Mobile(String Mobile){
    toupdateMobile = Mobile;
        try{
            JSONObject userObject = new JSONObject();


            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
            User user = complexPreferences.getObject("current_user", User.class);


            userObject.put("EmailID",user.EmailID);
            userObject.put("MobileCountryCode",user.MobileCountryCode);
            userObject.put("MobileNo",user.MobileNo);
            userObject.put("UserID",String.valueOf(user.UserID));


            Log.e("json obj send vc  mobile", userObject.toString());

            final CircleDialog circleDialog = new CircleDialog(getActivity(), 0);
            circleDialog.setCancelable(true);
            circleDialog.show();

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.SEND_VC_TO_UPDATE_MOBILE, userObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jobj) {
                    circleDialog.dismiss();
                    String response = jobj.toString();
                    Log.e("send vc mobile Response", "" + response);


                    try{
                        JSONObject obj = new JSONObject(response);
                        if(obj.getString("ResponseCode").equalsIgnoreCase("1")){

                            SnackBar bar = new SnackBar(getActivity(),getString(R.string.code_EMAILSE));
                            bar.show();

                            SharedPreferences preferences = getActivity().getSharedPreferences("verificationCode", getActivity().MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("VCMobile", obj.getString("VerificationCode").trim());
                            editor.commit();

                        }

                        else {
                            SnackBar bar112 = new SnackBar(getActivity(), obj.getString("ResponseMsg"));
                            bar112.show();

                        }

                    } catch (Exception e) {
                        Log.e("error response send vc mobile: ", e.toString() + "");
                    }


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    circleDialog.dismiss();
                    Log.e("error response send vc mobile: ", error + "");
                    SnackBar bar = new SnackBar(getActivity(),getString(R.string.code_NEWE));
                    bar.show();

                }
            });


            req.setRetryPolicy(  new DefaultRetryPolicy(0,0,0));
            MyApplication.getInstance().addToRequestQueue(req);

            // end of main try block
        } catch(Exception e){
            Log.e("error in forgot password2",e.toString());
        }
    }


private void process_update_Mobile(String Mobile,String MobileCountryCode,String vc){
    try{
        JSONObject userObject = new JSONObject();


        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
        User user = complexPreferences.getObject("current_user", User.class);


        userObject.put("EmailID",user.EmailID);
        userObject.put("MobileCountryCode",MobileCountryCode);
        userObject.put("MobileNo",Mobile);
        userObject.put("UserID",String.valueOf(user.UserID));
        userObject.put("VerificationCode",vc);


        Log.e("json obj update mobile", userObject.toString());

        final CircleDialog circleDialog = new CircleDialog(getActivity(), 0);
        circleDialog.setCancelable(true);
        circleDialog.show();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.UPDATE_MOBILE, userObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jobj) {
                circleDialog.dismiss();
                String response = jobj.toString();
                Log.e("update mobile Response", "" + response);


                try{
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("ResponseCode").equalsIgnoreCase("1")){

                        SnackBar bar = new SnackBar(getActivity(),getString(R.string.code_MOBILEUPDATES));
                        bar.show();


                       /* SharedPreferences preferences = getActivity().getSharedPreferences("verificationCode", getActivity().MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.remove("VCMobile").commit();
*/
                        //      clearResetCode();

                        CountDownTimer countDownTimer;
                        countDownTimer = new MyCountDownTimer(3000, 1000); // 1000 = 1s
                        countDownTimer.start();


                    }

                    else {
                        SnackBar bar112 = new SnackBar(getActivity(), obj.getString("ResponseMsg"));
                        bar112.show();

                    }

                } catch (Exception e) {
                    Log.e("error response update mobile: ", e.toString() + "");
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                circleDialog.dismiss();
                Log.e("error response update mobile: ", error + "");
                SnackBar bar = new SnackBar(getActivity(),getString(R.string.code_NEWE));
                bar.show();

            }
        });


        req.setRetryPolicy(  new DefaultRetryPolicy(0,0,0));
        MyApplication.getInstance().addToRequestQueue(req);

        // end of main try block
    } catch(Exception e){
        Log.e("error in update mobile",e.toString());
    }
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


private  void processCreateDialogChangePassword(){

    View v = getActivity().getLayoutInflater().inflate(R.layout.changepassword_dialog, null);
    alertDialogBuilder = new AlertDialog.Builder(getActivity());
    edOldPassword = (EditText)v.findViewById(R.id.edOldPassword);
    edNewpassword = (EditText)v.findViewById(R.id.edNewpassword);
    edNewConfirmpassword = (EditText)v.findViewById(R.id.edNewConfirmpassword);
    // set title
    alertDialogBuilder.setTitle("Change PIN");
    alertDialogBuilder.setView(v);
    // set dialog message
    alertDialogBuilder
            .setCancelable(false)
            .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {

                    if (isEmptyField(edOldPassword)) {
                        SnackBar bar = new SnackBar(getActivity(), getString(R.string.code_PLZENTERPIN));
                        bar.show();
                    }
                    else if (isEmptyField(edNewpassword) || isEmptyField(edNewConfirmpassword)) {
                        SnackBar bar = new SnackBar(getActivity(), getString(R.string.code_ENTERPINANDCONFIRMPIN));
                        bar.show();
                    }
                    else if (!isPasswordMatch(edNewpassword, edNewConfirmpassword)) {
                        SnackBar bar = new SnackBar(getActivity(), getString(R.string.code_PINDONOTMATCH));
                        bar.show();
                    }
                    else if (isPINLength(edNewConfirmpassword)) {
                        SnackBar bar = new SnackBar(getActivity(), getString(R.string.code_LENGTHOFPIN6));
                        bar.show();
                    }
                    else{
                        process_UpdatePIN();
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

public void process_UpdatePIN(){
    try{
        JSONObject userObject = new JSONObject();


        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
        User user = complexPreferences.getObject("current_user", User.class);


        userObject.put("NewPassword",edNewConfirmpassword.getText().toString().trim());
        userObject.put("OldPassword",edOldPassword.getText().toString().trim());
        userObject.put("UserID",String.valueOf(user.UserID));

        Log.e("json obj forgot password 2", userObject.toString());

        final CircleDialog circleDialog = new CircleDialog(getActivity(), 0);
        circleDialog.setCancelable(true);
        circleDialog.show();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.UPDATE_PASSWORD, userObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jobj) {
                circleDialog.dismiss();
                String response = jobj.toString();
                Log.e("forgot password2 Response", "" + response);


                try{
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("ResponseCode").equalsIgnoreCase("1")){

                        SnackBar bar = new SnackBar(getActivity(),getString(R.string.code_PINCHANGESUCESS));
                        bar.show();

                        SharedPreferences preferences = getActivity().getSharedPreferences("login", getActivity().MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("cup",edNewConfirmpassword.getText().toString().trim());
                        editor.commit();

                  //      clearResetCode();

                        CountDownTimer countDownTimer;
                        countDownTimer = new MyCountDownTimer(3000, 1000); // 1000 = 1s
                        countDownTimer.start();


                    }

                    else {
                        SnackBar bar112 = new SnackBar(getActivity(), obj.getString("ResponseMsg"));
                        bar112.show();

                    }

                } catch (Exception e) {
                    Log.e("error response forgot password2: ", e.toString() + "");
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                circleDialog.dismiss();
                Log.e("error response forgot password2: ", error + "");
                SnackBar bar = new SnackBar(getActivity(),getString(R.string.code_NEWE));
                bar.show();

            }
        });


        req.setRetryPolicy(  new DefaultRetryPolicy(0,0,0));
        MyApplication.getInstance().addToRequestQueue(req);

        // end of main try block
    } catch(Exception e){
        Log.e("error in forgot password2",e.toString());
    }
}

public boolean isPINLength(EditText param1) {

        boolean isEmpty = false;
        if (param1.getText().toString().trim().length() != 6 ) {
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
private  void processCreateDialogUpdateMobile(){

        View v = getActivity().getLayoutInflater().inflate(R.layout.changemobile_dialog, null);

        spCountry = (Spinner)v.findViewById(R.id.spCountry);
        txtMobileCountryCode = (TextView)v.findViewById(R.id.txtMobileCountryCode);
        edUpdateMobile =(EditText)v.findViewById(R.id.edUpdateMobile);
        fetchCountryAndDisplay();

        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        // set title
        alertDialogBuilder.setTitle(getString(R.string.code_UPDATEMOBIL));
        alertDialogBuilder.setView(v);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        if (isEmptyField(edUpdateMobile)) {
                            Toast.makeText(getActivity().getBaseContext(),getString(R.string.code_EENTERMOB),Toast.LENGTH_SHORT).show();
                        } else {
                            FLAG =1;
                            edVerificationCode.setText("");
                            process_SendVC_Mobile(edUpdateMobile.getText().toString().trim());
                            msg.setText(getString(R.string.code_ENTERVERFICA));
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
        alertDialogBuilder.setTitle(getString(R.string.code_UPDATEYOUREMAIL));
        alertDialogBuilder.setView(v);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (isEmptyField(edUpdateEmail)) {
                            Toast.makeText(getActivity().getBaseContext(),getString(R.string.code_ENTEERVODE),Toast.LENGTH_SHORT).show();
                        } else if (!isEmailMatch(edUpdateEmail)) {
                            Toast.makeText(getActivity().getBaseContext(),getString(R.string.code_CORRECTEMAIL),Toast.LENGTH_SHORT).show();
                        } else {
                            FLAG = 0;

                            process_SendVC_Email(edUpdateEmail.getText().toString().trim());
                            edVerificationCode.setText("");
                            msg.setText(getString(R.string.code_ENTERVERIFICATION));
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
                    spCountry.setSelection(0);

                    spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            txtMobileCountryCode.setText(String.valueOf(countrylist.get(position).CountryCode));
                            toupdateMobileCountryCode= txtMobileCountryCode.getText().toString().trim();
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
    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }
        @Override
        public void onFinish() {
            Log.e("counter","Time's up!");
            Intent i = new Intent(getActivity(), MyDrawerActivity.class);
            startActivity(i);
            getActivity().finish();

        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

    }

//end os main class
}
