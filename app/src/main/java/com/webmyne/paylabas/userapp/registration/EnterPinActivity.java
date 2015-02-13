package com.webmyne.paylabas.userapp.registration;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.webmyne.paylabas.userapp.base.MyDrawerActivity;
import com.webmyne.paylabas_user.R;

public class EnterPinActivity extends ActionBarActivity {

    EditText edPin;
    Button btnOkPin;
    private String cup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pin);

        edPin = (EditText)findViewById(R.id.edPin);
        btnOkPin = (Button)findViewById(R.id.btnFinishPin);
        btnOkPin.setOnClickListener(finishListner);
        cup = getIntent().getStringExtra("cup");

        if(cup == null || cup.equalsIgnoreCase("")){
            return;
        }


        TextView txtForgotPin = (TextView)findViewById(R.id.txtForgotEnterPin);
        txtForgotPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EnterPinActivity.this,ForgotPassword.class);
                startActivity(i);
                finish();

               /* Intent i = new Intent(Intent.ACTION_VIEW);
                Uri ForgotPinURL = Uri.parse("http://ws-srv-net.in.webmyne.com/Applications/PayLabas_V02/Login/ForgotPassword#");
                i.setData(ForgotPinURL);
                startActivity(i);*/
            }
        });





    }

    private View.OnClickListener finishListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(edPin.getText().toString().equalsIgnoreCase("")){
                Toast.makeText(EnterPinActivity.this, "Enter Pin", Toast.LENGTH_SHORT).show();
            }else{
                if(edPin.getText().toString().equalsIgnoreCase(cup)){
                    Intent i = new Intent(EnterPinActivity.this, MyDrawerActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(EnterPinActivity.this, "Wrong Pin.  Please Try Again", Toast.LENGTH_SHORT).show();
                }
            }




        }
    };


}
