package com.webmyne.paylabas.userapp.money_transfer;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.webmyne.paylabas_user.R;


public class MoneyTransferFinalActivity extends ActionBarActivity {

    Toolbar toolbar_actionbar;
    TextView txtSelectRecipient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_transfer_final);

        toolbar_actionbar = (Toolbar)findViewById(R.id.toolbar);
        /* setting up the toolbar starts*/
        if (toolbar_actionbar != null) {
            toolbar_actionbar.setTitle("Money Transfer");
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

    txtSelectRecipient = (TextView)findViewById(R.id.txtSelectRecipient);

    txtSelectRecipient.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(MoneyTransferFinalActivity.this,MoneyTransferRecipientActivity.class);
            startActivity(i);
        }
    });
}

//end of main class
}
