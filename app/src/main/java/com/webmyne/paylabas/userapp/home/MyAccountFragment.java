package com.webmyne.paylabas.userapp.home;


import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.widgets.SnackBar;
import com.google.gson.GsonBuilder;
import com.webmyne.paylabas.userapp.base.MyDrawerActivity;
import com.webmyne.paylabas.userapp.giftcode.GiftCodeFragment;
import com.webmyne.paylabas.userapp.helpers.AppConstants;
import com.webmyne.paylabas.userapp.helpers.CallWebService;
import com.webmyne.paylabas.userapp.helpers.ComplexPreferences;
import com.webmyne.paylabas.userapp.model.User;
import com.webmyne.paylabas.userapp.registration.LoginActivity;
import com.webmyne.paylabas_user.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyAccountFragment extends Fragment implements View.OnClickListener{


    public static String LOG_TAG = "My Account Page";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private TextView txtGiftCode,txtSendMoney,txtMoneytransfer,txtMobileTopup;
    private ImageView imgGiftCode,imgSendMoney,imgMoneyTransfer,imgMObileTopup;
    private LinearLayout linearGiftCode;

    ButtonFloat btnFloatAddMoney;
    private User user;


    public static MyAccountFragment newInstance(String param1, String param2) {
        MyAccountFragment fragment = new MyAccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MyAccountFragment() {
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

        View convertView = inflater.inflate(R.layout.fragment_my_account, container, false);

        txtGiftCode = (TextView)convertView.findViewById(R.id.txtGiftCode);
        txtMobileTopup = (TextView)convertView.findViewById(R.id.txtMobileTopup);
        txtMoneytransfer = (TextView)convertView.findViewById(R.id.txtMoneyTransfer);
        txtSendMoney = (TextView)convertView.findViewById(R.id.txtSendMoney);
        imgGiftCode = (ImageView)convertView.findViewById(R.id.imgGiftCode);
        imgMObileTopup = (ImageView)convertView.findViewById(R.id.imgMobileTopup);
        imgMoneyTransfer = (ImageView)convertView.findViewById(R.id.imgMoneyTransfer);
        imgSendMoney = (ImageView)convertView.findViewById(R.id.imgSendMoney);

        linearGiftCode = (LinearLayout)convertView.findViewById(R.id.linearGiftCode);
        linearGiftCode.setOnClickListener(this);

        btnFloatAddMoney = (ButtonFloat)convertView.findViewById(R.id.buttonFloatAddMoney);
        btnFloatAddMoney.setDrawableIcon(getResources().getDrawable(R.drawable.ic_action_new));




        return convertView;
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i(LOG_TAG,"OnResume Clicked");
        setupColors();

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
        user = complexPreferences.getObject("current_user", User.class);


         getBalanceAndDisplay();


    }

    private void getBalanceAndDisplay() {

        ((MyDrawerActivity)getActivity()).setToolTitle("Hi, "+user.FName);

          new CallWebService(AppConstants.USER_DETAILS+user.UserID,CallWebService.TYPE_JSONOBJECT) {

            @Override
            public void response(String response) {

                Log.e("Response User Details ",response);

                User currentUser = new GsonBuilder().create().fromJson(response,User.class);
                //store current user and domain in shared preferences
                ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
                complexPreferences.putObject("current_user", currentUser);
                complexPreferences.commit();

                 user = complexPreferences.getObject("current_user", User.class);
                ((MyDrawerActivity)getActivity()).setToolSubTitle("Balance "+getResources().getString(R.string.euro)+" "+user.LemonwayAmmount);


            }

            @Override
            public void error(VolleyError error) {


            }
        }.start();




    }

    private void setupColors() {

        txtGiftCode.setTextColor(getResources().getColor(R.color.color_giftcode));
        txtMobileTopup.setTextColor(getResources().getColor(R.color.color_mobiletopup));
        txtMoneytransfer.setTextColor(getResources().getColor(R.color.color_moneytransfer));
        txtSendMoney.setTextColor(getResources().getColor(R.color.color_sendmoney));

        imgGiftCode.setColorFilter(getResources().getColor(R.color.color_giftcode));
        imgMObileTopup.setColorFilter(getResources().getColor(R.color.color_mobiletopup));
        imgMoneyTransfer.setColorFilter(getResources().getColor(R.color.color_moneytransfer));
        imgSendMoney.setColorFilter(getResources().getColor(R.color.color_sendmoney));
        ((MyDrawerActivity)getActivity()).setToolColor(Color.parseColor("#494949"));

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.linearGiftCode:

                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.main_container,new GiftCodeFragment());
                ft.addToBackStack("");
                ft.commit();


                break;

        }

    }
}
