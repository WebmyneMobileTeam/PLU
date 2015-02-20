package com.webmyne.paylabas.userapp.registration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.widgets.SnackBar;
import com.google.gson.GsonBuilder;
import com.webmyne.paylabas.userapp.base.MyApplication;
import com.webmyne.paylabas.userapp.base.MyDrawerActivity;
import com.webmyne.paylabas.userapp.custom_components.CircleDialog;
import com.webmyne.paylabas.userapp.helpers.AppConstants;
import com.webmyne.paylabas.userapp.helpers.ComplexPreferences;
import com.webmyne.paylabas.userapp.model.User;
import com.webmyne.paylabas_user.R;

import org.json.JSONObject;

public class ConfirmationActivity extends ActionBarActivity implements View.OnClickListener{

    ButtonRectangle btnFinishSetup;
    private EditText edAnswer;
    private EditText edVerificationCode;
    private Spinner spQuestion;
    static int QuestionID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        btnFinishSetup = (ButtonRectangle)findViewById(R.id.btnFinishSetup);
        btnFinishSetup.setOnClickListener(this);
        edAnswer = (EditText)findViewById(R.id.edanswer);
        edVerificationCode = (EditText)findViewById(R.id.edVerificationCode);
        spQuestion = (Spinner)findViewById(R.id.spQuestion);

         // fetching the verification code
       /* SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        SnackBar bar = new SnackBar(ConfirmationActivity.this, "Your PayLabAS Verification Code is "+ preferences.getString("VerificationCode","vfcode"));
        bar.show();*/

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

    public boolean isVerified(){

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_confirmation, menu);
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

   public boolean isEmptyFiled(EditText param1){

        boolean isEmpty = false;
        if(param1.getText() == null || param1.getText().toString().equalsIgnoreCase("")){
            isEmpty = true;
        }
        return isEmpty;
    }
    public boolean checkVerificationcode(EditText param1){
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        boolean isWrong = false;
        if(!param1.getText().toString().equals(preferences.getString("VerificationCode","vfcode"))){
            isWrong = true;
        }
        return isWrong;
    }
public boolean checkvalidquestion(){
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
    @Override
    public void onClick(View v) {

       switch (v.getId()) {

           case R.id.btnFinishSetup:

               if (isEmptyFiled(edVerificationCode)) {
                   SnackBar bar = new SnackBar(ConfirmationActivity.this,"Please Enter Verification Code");
                   bar.show();
               }
             else if (!checkvalidquestion()) {
                   SnackBar bar = new SnackBar(ConfirmationActivity.this,"Please Select any Question");
                   bar.show();
                    }
              else if (isEmptyFiled(edAnswer)) {
                   SnackBar bar = new SnackBar(ConfirmationActivity.this,"Please Enter Answer");
                   bar.show();
               }
               else if (checkVerificationcode(edVerificationCode)) {
                   SnackBar bar = new SnackBar(ConfirmationActivity.this,"Please Enter Correct Verification Code");
                   bar.show();
               }
               else{
                   try {

                       ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ConfirmationActivity.this, "user_pref", 0);
                       User user = complexPreferences.getObject("current_user", User.class);

                       JSONObject userObject = new JSONObject();

                       userObject.put("Answer", edAnswer.getText().toString());
                       userObject.put("DeviceType", "Android");
                       userObject.put("EmailID", user.EmailID.toString());
                       userObject.put("MobileCountryCode", user.MobileCountryCode.toString());
                       userObject.put("MobileNo", user.MobileNo.toString());
                       userObject.put("UserID", user.UserID);
                       userObject.put("VerificationCode", user.VerificationCode.toString());

                       userObject.put("isVerified", false);

                       userObject.put("QuestionId", spQuestion.getSelectedItemPosition());


                       //    "NotificationID":"String content",
                       //   "QuestionId":9223372036854775807,
                       //     "ResponseCode":"String content",
                       //    "ResponseMsg":"String content",

                       Log.e("json obj2",userObject.toString());
                       final CircleDialog circleDialog = new CircleDialog(ConfirmationActivity.this, 0);
                       circleDialog.setCancelable(true);
                       circleDialog.show();

                       JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.VERIFY_USER, userObject, new Response.Listener<JSONObject>() {

                           @Override
                           public void onResponse(JSONObject jobj) {

                               circleDialog.dismiss();
                               String response = jobj.toString();
                               Log.e("Response : ", "" + response);
                               try {

                                   JSONObject obj = new JSONObject(response);

                                   if (obj.getString("ResponseCode").equalsIgnoreCase("1")) {

                                       User currentUser = new GsonBuilder().create().fromJson(response, User.class);
                                       currentUser.isVerified = true;
                                       //store current user and domain in shared preferences
                                       ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ConfirmationActivity.this, "user_pref", 0);
                                       complexPreferences.putObject("current_user", currentUser);
                                       complexPreferences.commit();

                                       // set login true

                                       SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                                       SharedPreferences.Editor editor = preferences.edit();
                                       editor.putBoolean("isUserLogin", true);
                                       editor.commit();

                                       Intent i = new Intent(ConfirmationActivity.this, MyDrawerActivity.class);
                                       startActivity(i);
                                       finish();

                                   }
                                   else if(obj.getString("ResponseCode").equalsIgnoreCase("-2")) {
                                           SnackBar bar112 = new SnackBar(ConfirmationActivity.this, "Error occur while updating Profile");
                                           bar112.show();
                                       }
                                       else if(obj.getString("ResponseCode").equalsIgnoreCase("-1")) {
                                           SnackBar bar112 = new SnackBar(ConfirmationActivity.this, "Error");
                                           bar112.show();

                                       }
                                       else if(obj.getString("ResponseCode").equalsIgnoreCase("2")) {
                                           SnackBar bar112 = new SnackBar(ConfirmationActivity.this, "Invalid Mobile no. or Password");
                                           bar112.show();
                                       }
                                       else if(obj.getString("ResponseCode").equalsIgnoreCase("3")) {
                                           SnackBar bar112 = new SnackBar(ConfirmationActivity.this, "Email Id already Exist");
                                           bar112.show();
                                       }
                                       else if(obj.getString("ResponseCode").equalsIgnoreCase("4")) {
                                           SnackBar bar112 = new SnackBar(ConfirmationActivity.this, "Mobile No. & Email Id already Exist");
                                           bar112.show();
                                       }
                                       else{
                                           SnackBar bar112 = new SnackBar(ConfirmationActivity.this, "Invalid Error");
                                           bar112.show();
                                       }

                               } catch (Exception e) {

                               }

                           }
                       }, new Response.ErrorListener() {

                           @Override
                           public void onErrorResponse(VolleyError error) {

                               circleDialog.dismiss();
                               Log.e("error responsegg: ", error + "");
                               SnackBar bar = new SnackBar(ConfirmationActivity.this, error.getMessage());
                               bar.show();

                           }
                       });
                       MyApplication.getInstance().addToRequestQueue(req);


                   } catch (Exception e) {

                   }
               }

       }// end of if

    }// end os switch
}
