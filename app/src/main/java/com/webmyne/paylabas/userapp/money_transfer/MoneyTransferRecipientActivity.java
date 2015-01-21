package com.webmyne.paylabas.userapp.money_transfer;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.gc.materialdesign.widgets.SnackBar;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.webmyne.paylabas.userapp.custom_components.CircleDialog;
import com.webmyne.paylabas.userapp.helpers.AppConstants;
import com.webmyne.paylabas.userapp.helpers.CallWebService;
import com.webmyne.paylabas.userapp.helpers.ComplexPreferences;
import com.webmyne.paylabas.userapp.model.Receipient;
import com.webmyne.paylabas.userapp.model.User;
import com.webmyne.paylabas_user.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MoneyTransferRecipientActivity extends ActionBarActivity {

    Toolbar toolbar_actionbar;
    TextView txtSelectRecipient;
    Spinner spinnerRecipientContact,spCountry,spState,spCity;
    EditText edFirstname,edLastname,edEmail,edAddress,edZipcode,edCountryCode,edMobileno;

    private ArrayList<Receipient> receipients;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_transfer_recipient);

        toolbar_actionbar = (Toolbar)findViewById(R.id.toolbar);
        /* setting up the toolbar starts*/
        if (toolbar_actionbar != null) {
            toolbar_actionbar.setTitle("Add Recipient");
            toolbar_actionbar.setNavigationIcon(R.drawable.icon_back);
            toolbar_actionbar.setBackgroundColor(getResources().getColor(R.color.paylabas_green));

            setSupportActionBar(toolbar_actionbar);

        }
        toolbar_actionbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        intView();


    }

private void intView(){
    spinnerRecipientContact = (Spinner)findViewById(R.id.spinnerRecipient);

}

    @Override
    protected void onResume() {
        super.onResume();
        fetchRecipientDisplay();
    }

private void fetchRecipientDisplay(){
    final CircleDialog circleDialog = new CircleDialog(MoneyTransferRecipientActivity.this, 0);
    circleDialog.setCancelable(true);
    circleDialog.show();

    ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(MoneyTransferRecipientActivity.this, "user_pref", 0);
    user = complexPreferences.getObject("current_user", User.class);

    new CallWebService(AppConstants.GETRECEIPIENTS + user.UserID, CallWebService.TYPE_JSONARRAY) {

        @Override
        public void response(String response) {

            circleDialog.dismiss();
            Log.e("Receipients List", response);
            if (response == null) {

            } else {

                Type listType = new TypeToken<List<Receipient>>() {
                }.getType();

                receipients = new GsonBuilder().create().fromJson(response, listType);

                Receipient r1 = new Receipient();
                r1.FirstName = "Select";
                r1.LastName = "Recipient";
                receipients.add(0,r1);



                RecipientAdapter adapter = new RecipientAdapter(MoneyTransferRecipientActivity.this,
                        android.R.layout.simple_spinner_item,receipients);

                spinnerRecipientContact.setAdapter(adapter);

            }

        }

        @Override
        public void error(VolleyError error) {
            circleDialog.dismiss();
            SnackBar bar = new SnackBar(MoneyTransferRecipientActivity.this, "Sync Error. Please Try again");
            bar.show();
        }
    }.start();

}

    public class RecipientAdapter extends ArrayAdapter<Receipient> {

        Context context;
        int layoutResourceId;
        ArrayList<Receipient> values;
        // int android.R.Layout.

        public RecipientAdapter(Context context, int resource, ArrayList<Receipient> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values=objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(MoneyTransferRecipientActivity.this);
            txt.setPadding(16,16,16,16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).FirstName+" "+values.get(position).LastName);
            return  txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(MoneyTransferRecipientActivity.this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setText(values.get(position).FirstName+" "+values.get(position).LastName);
            return  txt;
        }
    }



    //end of main class
}
