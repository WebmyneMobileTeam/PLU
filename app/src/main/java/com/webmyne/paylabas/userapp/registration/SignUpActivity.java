package com.webmyne.paylabas.userapp.registration;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.webmyne.paylabas_user.R;

public class SignUpActivity extends ActionBarActivity implements View.OnClickListener{

    private ButtonRectangle btnCreateAccount;
    private ButtonRectangle btnLoginFromRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
    }

    private void init() {

        btnCreateAccount = (ButtonRectangle)findViewById(R.id.btnConfirmSignUp);
        btnLoginFromRegister = (ButtonRectangle)findViewById(R.id.btnLoginFromRegister);
        btnCreateAccount.setOnClickListener(this);
        btnLoginFromRegister.setOnClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnConfirmSignUp:

                Intent iCOnfirmSignUp = new Intent( SignUpActivity.this ,ConfirmationActivity.class );
                startActivity(iCOnfirmSignUp);
                finish();

                break;

            case R.id.btnLoginFromRegister:

                Intent i = new Intent( SignUpActivity.this ,LoginActivity.class );
                startActivity(i);
                finish();

                break;


        }


    }
}
