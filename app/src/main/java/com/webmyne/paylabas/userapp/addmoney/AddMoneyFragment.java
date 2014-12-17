package com.webmyne.paylabas.userapp.addmoney;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.gc.materialdesign.views.ButtonRectangle;
import com.webmyne.paylabas.userapp.base.MyDrawerActivity;
import com.webmyne.paylabas_user.R;

public class AddMoneyFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private LinearLayout linearMainAddMoney;
    private ButtonRectangle btnNextAddMoney;
    private ButtonRectangle btnResetAddMoney;
    private RadioGroup rg;
    private RadioButton rbCreditCard;
    private RadioButton rbUkashVoucher;
    private EditText edAmountAddMoney;
    private WebView webviewAddmoney;


    public static AddMoneyFragment newInstance(String param1, String param2) {
        AddMoneyFragment fragment = new AddMoneyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AddMoneyFragment() {
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

        View convertView = inflater.inflate(R.layout.fragment_add_money, container, false);
        init(convertView);
        return convertView;
    }

    private void init(View convertView) {

        linearMainAddMoney = (LinearLayout)convertView.findViewById(R.id.linearMainAddMoney);
        edAmountAddMoney = (EditText)convertView.findViewById(R.id.edAmountAddMoney);
        btnNextAddMoney = (ButtonRectangle)convertView.findViewById(R.id.btnNextAddMoney);
        btnResetAddMoney = (ButtonRectangle)convertView.findViewById(R.id.btnResetAddMoney);
        rg = (RadioGroup)convertView.findViewById(R.id.rgAddMoney);
        btnResetAddMoney.setOnClickListener(this);
        btnNextAddMoney.setOnClickListener(this);
        webviewAddmoney = (WebView)convertView.findViewById(R.id.webviewAddMoney);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MyDrawerActivity)getActivity()).setToolColor(getResources().getColor(R.color.paylabas_blue));
    }

    public void setupWebView(){

        webviewAddmoney.setVisibility(View.VISIBLE);
        linearMainAddMoney.setVisibility(View.GONE);
        btnNextAddMoney.setVisibility(View.GONE);
        btnResetAddMoney.setText("Back");

    }

    public void setupMainView(){

        webviewAddmoney.setVisibility(View.GONE);
        linearMainAddMoney.setVisibility(View.VISIBLE);
        btnNextAddMoney.setVisibility(View.VISIBLE);
        btnResetAddMoney.setText("Reset");
    }

    public void showWebContents(){

        WebSettings settings = webviewAddmoney.getSettings();
        settings.setJavaScriptEnabled(true);
        MyWebViewClient webViewClient = new MyWebViewClient();
        webviewAddmoney.setWebViewClient(webViewClient);
        webviewAddmoney.loadUrl("http://www.facebook.com");



    }

    private class MyWebViewClient extends WebViewClient {

        private MyWebViewClient() {
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            return false;
        }
    }





    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnResetAddMoney:

                if(webviewAddmoney.isShown()){
                    setupMainView();
                }else{
                    edAmountAddMoney.setText("");
                }
                break;

            case R.id.btnNextAddMoney:

                setupWebView();
                showWebContents();


                break;


        }


    }
}
