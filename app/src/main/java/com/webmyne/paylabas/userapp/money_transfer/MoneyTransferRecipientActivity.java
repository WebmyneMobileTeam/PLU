package com.webmyne.paylabas.userapp.money_transfer;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.webmyne.paylabas_user.R;


public class MoneyTransferRecipientActivity extends ActionBarActivity {

    Toolbar toolbar_actionbar;
    TextView txtSelectRecipient;
    Spinner spinnerRecipientContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_transfer_recipient);

        toolbar_actionbar = (Toolbar)findViewById(R.id.toolbar);
        /* setting up the toolbar starts*/
        if (toolbar_actionbar != null) {
            toolbar_actionbar.setTitle("Add Recipient");
            toolbar_actionbar.setNavigationIcon(R.drawable.icon_back);
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

//end of main class
}
