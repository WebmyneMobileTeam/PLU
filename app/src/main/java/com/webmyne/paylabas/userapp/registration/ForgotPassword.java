package com.webmyne.paylabas.userapp.registration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.webmyne.paylabas.userapp.base.MyApplication;
import com.webmyne.paylabas.userapp.base.MyDrawerActivity;
import com.webmyne.paylabas.userapp.custom_components.CircleDialog;
import com.webmyne.paylabas.userapp.helpers.AppConstants;
import com.webmyne.paylabas.userapp.helpers.ComplexPreferences;
import com.webmyne.paylabas.userapp.home.MyAccountFragment;
import com.webmyne.paylabas.userapp.model.User;
import com.webmyne.paylabas_user.R;

import org.json.JSONObject;

import java.util.regex.Pattern;

public class ForgotPassword extends ActionBarActivity {
EditText etEmail,etResetCode,etAnswer,etNewpassword1,etNewpassword2;
    ButtonFlat sendPIN;
    ButtonRectangle ResetPassword;
    static int QuestionID;
    Spinner spQuestion;
    LinearLayout mainresetlayout;
    String tempResetCode,tempUserid;

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        initView();

        if (toolbar != null) {
            toolbar.setTitle(getString(R.string.code_FOROGTPIN));
            setSupportActionBar(toolbar);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        mainresetlayout.setVisibility(View.GONE);
        clearResetCode();
    }

    private void initView() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#494949"));
        toolbar.setNavigationIcon(R.drawable.icon_back);



        mainresetlayout = (LinearLayout)findViewById(R.id.changepasswordLayout);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etResetCode= (EditText) findViewById(R.id.etResetCode);
        etAnswer= (EditText) findViewById(R.id.etAnswer);
        etNewpassword1= (EditText) findViewById(R.id.etNewPassword);
        etNewpassword2= (EditText) findViewById(R.id.etNewPassword2);
        spQuestion = (Spinner)findViewById(R.id.spQuestion);

        sendPIN = (ButtonFlat) findViewById(R.id.btnsendpin);
        ResetPassword = (ButtonRectangle) findViewById(R.id.btnReset);

        spQuestion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                QuestionID=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        sendPIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmptyField(etEmail)) {
                    SnackBar bar = new SnackBar(ForgotPassword.this, getString(R.string.code_FE1));
                    bar.show();
                }
                else if (!isEmailMatch(etEmail)) {
                    SnackBar bar = new SnackBar(ForgotPassword.this, getString(R.string.code_FE2));
                    bar.show();
                }  else if (!checkvalidquestion()) {
                    SnackBar bar = new SnackBar(ForgotPassword.this,getString(R.string.code_FE3));
                    bar.show();
                }else if (isEmptyField(etAnswer)) {
                    SnackBar bar = new SnackBar(ForgotPassword.this, getString(R.string.code_FE4));
                    bar.show();
                }
                else{
                    process_ForgotPassword1();
                }

            }
        });


        ResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmptyField(etResetCode)) {
                    SnackBar bar = new SnackBar(ForgotPassword.this, getString(R.string.FE5));
                    bar.show();
                }
                else if (isEmptyField(etNewpassword1) || isEmptyField(etNewpassword2)) {
                    SnackBar bar = new SnackBar(ForgotPassword.this, getString(R.string.code_FE6));
                    bar.show();
                }
                else if (!isPasswordMatch(etNewpassword1, etNewpassword2)) {
                    SnackBar bar = new SnackBar(ForgotPassword.this, getString(R.string.code_FE7));
                    bar.show();
                }
                else if (isPINLength(etNewpassword2)) {
                    SnackBar bar = new SnackBar(ForgotPassword.this, getString(R.string.code_FE8));
                    bar.show();
                }
                else{
                    process_ForgotPassword2();
                }
            }
        });

    }



    public void  clearResetCode(){
        SharedPreferences preferences = getSharedPreferences("resetCode", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("code").commit();
        editor.remove("userid").commit();

    }
    public void  process_ForgotPassword2(){

        try{
            JSONObject userObject = new JSONObject();


            SharedPreferences preferences = getSharedPreferences("resetCode", MODE_PRIVATE);
            tempUserid=preferences.getString("userid","");
            tempResetCode=preferences.getString("code","");


            userObject.put("NewPassword",etNewpassword1.getText().toString().trim());
            userObject.put("ResetCode",etResetCode.getText().toString().trim());
            userObject.put("UserID",tempUserid);

            Log.e("json obj forgot password 2", userObject.toString());

            final CircleDialog circleDialog = new CircleDialog(ForgotPassword.this, 0);
            circleDialog.setCancelable(true);
            circleDialog.show();

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.FORGOT_PASSWORD2, userObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jobj) {
                    circleDialog.dismiss();
                    String response = jobj.toString();
                    Log.e("forgot password2 Response", "" + response);


                    try{
                        JSONObject obj = new JSONObject(response);
                        if(obj.getString("ResponseCode").equalsIgnoreCase("1")){

                            SnackBar bar = new SnackBar(ForgotPassword.this,getString(R.string.code_PINCHNAGESUCESFULLY));
                            bar.show();

                            SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("cup",etNewpassword1.getText().toString().trim());
                            editor.commit();

                            clearResetCode();

                            CountDownTimer countDownTimer;
                            countDownTimer = new MyCountDownTimer(3000, 1000); // 1000 = 1s
                            countDownTimer.start();


                        }

                        else {
                            SnackBar bar112 = new SnackBar(ForgotPassword.this, obj.getString("ResponseMsg"));
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
                    SnackBar bar = new SnackBar(ForgotPassword.this,getString(R.string.code_NNWERRO));
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
    public void  process_ForgotPassword1(){

        try{
            JSONObject userObject = new JSONObject();

            userObject.put("Answer",etAnswer.getText().toString());
            userObject.put("EmailID",etEmail.getText().toString());
            userObject.put("QuestionID",spQuestion.getSelectedItemPosition());

            Log.e("json obj forgot password 1", userObject.toString());

            final CircleDialog circleDialog = new CircleDialog(ForgotPassword.this, 0);
            circleDialog.setCancelable(true);
            circleDialog.show();

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.FORGOT_PASSWORD1, userObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jobj) {
                    circleDialog.dismiss();
                    String response = jobj.toString();
                    Log.e("forgot password1 Response", "" + response);


                    try{
                        JSONObject obj = new JSONObject(response);
                        if(obj.getString("ResponseCode").equalsIgnoreCase("1")){

                            SnackBar bar = new SnackBar(ForgotPassword.this,getString(R.string.code_RESETCIDESENTTOYOURMOBILE));
                            bar.show();

                            mainresetlayout.setVisibility(View.VISIBLE);
                            // setting the reset code in shared prefernces

                            SharedPreferences preferences = getSharedPreferences("resetCode", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("code",obj.getString("ResetCode"));
                            editor.putString("userid",obj.getString("UserID"));
                            editor.commit();


                        }

                        else {
                                SnackBar bar112 = new SnackBar(ForgotPassword.this, obj.getString("ResponseMsg"));
                                bar112.show();

                        }

                    } catch (Exception e) {
                        Log.e("error response forgot password: ", e.toString() + "");
                    }


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    circleDialog.dismiss();
                    Log.e("error response recharge2: ", error + "");
                    SnackBar bar = new SnackBar(ForgotPassword.this,getString(R.string.code_NEWEWN));
                    bar.show();

                }
            });


            req.setRetryPolicy(  new DefaultRetryPolicy(0,0,0));
            MyApplication.getInstance().addToRequestQueue(req);

            // end of main try block
        } catch(Exception e){
            Log.e("error in forgot password",e.toString());
        }
    }



    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }
        @Override
        public void onFinish() {
            Log.e("counter","Time's up!");
            Intent i = new Intent(ForgotPassword.this, MyDrawerActivity.class);
            startActivity(i);
            finish();

        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

    }

    public boolean isPINLength(EditText param1) {

        boolean isEmpty = false;
        if (param1.getText().toString().trim().length() != 6 ) {
            isEmpty = true;
        }
        return isEmpty;

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

   /* public boolean checkVerificationcode(EditText param1){
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        boolean isWrong = false;
        if(!param1.getText().toString().equals(preferences.getString("VerificationCode","vfcode"))){
            isWrong = true;
        }
        return isWrong;
    }*/


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




    //end of main class
}
