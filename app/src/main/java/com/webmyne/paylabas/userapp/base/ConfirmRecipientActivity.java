package com.webmyne.paylabas.userapp.base;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
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
import com.webmyne.paylabas.userapp.custom_components.CircleDialog;
import com.webmyne.paylabas.userapp.helpers.AppConstants;
import com.webmyne.paylabas.userapp.helpers.CallWebService;
import com.webmyne.paylabas.userapp.helpers.ComplexPreferences;
import com.webmyne.paylabas.userapp.model.City;
import com.webmyne.paylabas.userapp.model.Country;
import com.webmyne.paylabas.userapp.model.State;
import com.webmyne.paylabas.userapp.model.User;
import com.webmyne.paylabas_user.R;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ConfirmRecipientActivity extends ActionBarActivity {

    private ButtonRectangle btnLogout;
    private Toolbar toolbar;

    private ListView leftDrawerList;
    private ArrayAdapter<String> navigationDrawerAdapter;

    private ActionBarDrawerToggle drawerToggle;

    private EditText edVerificationCode;

    private ButtonRectangle btnVerifyRecipient;

    private DatabaseWrapper db_wrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_recipient);

        initView();
        if (toolbar != null) {
            toolbar.setTitle(getString(R.string.code_TITLEADDRECIPIENT));
            setSupportActionBar(toolbar);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmRecipientActivity.this.finish();
            }
        });

        // fetching the verification code
       /* SharedPreferences preferences = getSharedPreferences("Recipient", MODE_PRIVATE);
        SnackBar bar = new SnackBar(ConfirmRecipientActivity.this, "Your Add Recipient Verification Code is "+ preferences.getString("VerificationCode","vfcode"));
        bar.show();*/

        btnVerifyRecipient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isEmptyField(edVerificationCode)) {
                    SnackBar bar = new SnackBar(ConfirmRecipientActivity.this, getString(R.string.code_ENTERVERIFICATIONCODE));
                    bar.show();
                } else if (checkVerificationcode(edVerificationCode)) {
                    SnackBar bar = new SnackBar(ConfirmRecipientActivity.this, getString(R.string.code_ENTERCORECTVERIFICATIONCODE));
                    bar.show();
                } else {
                    processAddRecipient();
                }
            }
        });

        // end of main class
    }

    public void processAddRecipient() {


        try {
            ComplexPreferences complexPreferences2 = ComplexPreferences.getComplexPreferences(ConfirmRecipientActivity.this, "user_pref", 0);
            JSONObject userObject = complexPreferences2.getObject("new-recipient", JSONObject.class);

            Log.e("User id ", String.valueOf(userObject.toString()));

            Log.e("json obj", userObject.toString());
            final CircleDialog circleDialog = new CircleDialog(ConfirmRecipientActivity.this, 0);
            circleDialog.setCancelable(true);
            circleDialog.show();

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.ADD_RECIPIENT, userObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jobj) {
                    circleDialog.dismiss();
                    String response = jobj.toString();
                    Log.e("Response Addrecipient: ", "" + response);

                    try {

                        JSONObject obj = new JSONObject(response);

                        if (obj.getString("ResponseCode").equalsIgnoreCase("1")) {

                            // User currentUser = new GsonBuilder().create().fromJson(response,User.class);
                            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ConfirmRecipientActivity.this, "user_pref", 0);
                            complexPreferences.remove("new-recipient");
                            complexPreferences.commit();

                            SharedPreferences preferences = getSharedPreferences("Recipient", MODE_PRIVATE);
                            preferences.edit().remove("VerificationCode").commit();

                            SnackBar bar = new SnackBar(ConfirmRecipientActivity.this, getString(R.string.code_RECIPIENTADDSUCESFULL));
                            bar.show();

                            Intent verifyRecipient = new Intent(ConfirmRecipientActivity.this, MyDrawerActivity.class);
                            startActivity(verifyRecipient);
                            finish();
                        } else {
                            if (obj.getString("ResponseCode").equalsIgnoreCase("-2")) {
                                SnackBar bar112 = new SnackBar(ConfirmRecipientActivity.this, getString(R.string.code_EEROROOCURE));
                                bar112.show();
                            } else if (obj.getString("ResponseCode").equalsIgnoreCase("-1")) {
                                SnackBar bar112 = new SnackBar(ConfirmRecipientActivity.this,  getString(R.string.code_ERRORADDINGRECIPEITN));
                                bar112.show();
                            } else if (obj.getString("ResponseCode").equalsIgnoreCase("2")) {
                                SnackBar bar112 = new SnackBar(ConfirmRecipientActivity.this, getString(R.string.code_MOBALREADYEXISTS));
                                bar112.show();
                            } else if (obj.getString("ResponseCode").equalsIgnoreCase("3")) {
                                SnackBar bar112 = new SnackBar(ConfirmRecipientActivity.this, getString(R.string.code_EMAILIDALREADYEXISTS));
                                bar112.show();
                            } else if (obj.getString("ResponseCode").equalsIgnoreCase("4")) {
                                SnackBar bar112 = new SnackBar(ConfirmRecipientActivity.this, getString(R.string.code_MOBANDEMAILALREADYEXISTS));
                                bar112.show();
                            } else {
                                SnackBar bar112 = new SnackBar(ConfirmRecipientActivity.this, getString(R.string.code_TIMEOUTPLZTRYAGIN));
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
                    SnackBar bar = new SnackBar(ConfirmRecipientActivity.this, error.getMessage());
                    bar.show();

                }
            });
            MyApplication.getInstance().addToRequestQueue(req);


        } catch (Exception e) {

        }
    }

    public boolean checkVerificationcode(EditText param1) {
        SharedPreferences preferences = getSharedPreferences("Recipient", MODE_PRIVATE);
        boolean isWrong = false;
        if (!param1.getText().toString().equals(preferences.getString("VerificationCode", "vfcode"))) {
            isWrong = true;
        }
        return isWrong;
    }

    public boolean isEmptyField(EditText param1) {
        boolean isEmpty = false;
        if (param1.getText() == null || param1.getText().toString().equalsIgnoreCase("")) {
            isEmpty = true;
        }
        return isEmpty;
    }

    private void initView() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#494949"));
        toolbar.setNavigationIcon(R.drawable.icon_back);

        btnVerifyRecipient = (ButtonRectangle) findViewById(R.id.btnVerifyRecipient);
        edVerificationCode = (EditText) findViewById(R.id.edVerificationCode);

    }

    public void setToolColor(int color) {
        toolbar.setBackgroundColor(color);
    }

    public void setToolTitle(String title) {
        toolbar.setTitle(title);
    }

    public void setToolSubTitle(String subTitle) {

        toolbar.setSubtitle(subTitle);
    }
}