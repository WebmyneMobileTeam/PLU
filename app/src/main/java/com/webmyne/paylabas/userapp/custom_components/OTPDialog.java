package com.webmyne.paylabas.userapp.custom_components;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.webmyne.paylabas_user.R;

/**
 * Created by Android on 27-01-2015.
 */
public class OTPDialog extends Dialog{

    private View convertView;
    private OnConfirmListner listner;
    private EditText edOTP;
    private Button btnConfirm;

    public OTPDialog(final Context context, int theme) {
        super(context, android.R.style.Theme_Black_NoTitleBar);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        convertView = inflater.inflate(R.layout.item_otp_dialog,null);
        setContentView(convertView);

        edOTP = (EditText)convertView.findViewById(R.id.edOTP);
        btnConfirm = (Button)convertView.findViewById(R.id.btnFinishOTP);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edOTP.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(context, "Please enter One Time Password", Toast.LENGTH_SHORT).show();
                }else{
                    listner.onComplete(edOTP.getText().toString().trim());
                }
            }
        });


        this.show();

    }

    public void setConfirmListner(OnConfirmListner listner){
        this.listner = listner;
    }

    public static interface OnConfirmListner{
        public void onComplete(String enteredOTP);
    }



}
