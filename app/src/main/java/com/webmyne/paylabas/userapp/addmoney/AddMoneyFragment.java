package com.webmyne.paylabas.userapp.addmoney;


import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.DefaultRetryPolicy;
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
import com.webmyne.paylabas.userapp.giftcode.GiftCodeFragment;
import com.webmyne.paylabas.userapp.helpers.AppConstants;
import com.webmyne.paylabas.userapp.helpers.ComplexPreferences;
import com.webmyne.paylabas.userapp.home.MyAccountFragment;
import com.webmyne.paylabas.userapp.model.User;
import com.webmyne.paylabas.userapp.registration.LoginActivity;
import com.webmyne.paylabas_user.R;

import org.json.JSONObject;

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
    private User user;
    private String web_url;
    private String token;
    private String transactionID;

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
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
        user = complexPreferences.getObject("current_user", User.class);
        ((MyDrawerActivity)getActivity()).setToolColor(getResources().getColor(R.color.paylabas_blue));
    }

    public void setupWebView(){

        webviewAddmoney.setVisibility(View.VISIBLE);
        linearMainAddMoney.setVisibility(View.GONE);
        btnNextAddMoney.setVisibility(View.INVISIBLE);
        btnResetAddMoney.setText("Back");
        showWebContents();
    }

    public void setupMainView(){

        webviewAddmoney.setVisibility(View.GONE);
        linearMainAddMoney.setVisibility(View.VISIBLE);
        btnNextAddMoney.setVisibility(View.VISIBLE);
        btnResetAddMoney.setText("Reset");
        webviewAddmoney.clearHistory();
        webviewAddmoney.clearFormData();

    }
    public void showWebContents(){


        WebSettings settings = webviewAddmoney.getSettings();
        settings.setJavaScriptEnabled(true);
        MyWebViewClient webViewClient = new MyWebViewClient();
        MyWebChromeClient webChromeClient = new MyWebChromeClient();
        webviewAddmoney.setWebViewClient(webViewClient);
        webviewAddmoney.setWebChromeClient(webChromeClient);
     //  webviewAddmoney.getSettings().setLoadWithOverviewMode(true);
      //  webviewAddmoney.getSettings().setUseWideViewPort(true);
      //  webviewAddmoney.getSettings().setSupportZoom(true);
     //   webviewAddmoney.getSettings().setBuiltInZoomControls(true);
         webviewAddmoney.loadUrl(web_url);
      //  webviewAddmoney.loadUrl("http://ws-srv-net.in.webmyne.com/Applications/paylabas_v02");

        webviewAddmoney.requestFocus();



    }


    private class MyWebChromeClient extends WebChromeClient {

        //display alert message in Web View
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {

            new AlertDialog.Builder(view.getContext())
                    .setMessage(message).setCancelable(true).show();
            result.confirm();
            return true;
        }



    }




    private class MyWebViewClient extends WebViewClient {

        boolean isLoaded = false;

        private MyWebViewClient() {

        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed(); // Ignore SSL certificate errors
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {

            if(isLoaded == true){
                //if(url.equalsIgnoreCase("http://ws-srv-net.in.webmyne.com/Applications/paylabas/Loading.html")){
                    if(url.contains("Loading.html")){
                    processAfterPaymentDone();
                    return true;
                }
            }
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

          //  isLoaded = false;

            try {
                ((MyDrawerActivity) getActivity()).showToolLoading();
            }catch(Exception e){

            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            isLoaded = true;
            try {
                ((MyDrawerActivity)getActivity()).hideToolLoading();

            }catch(Exception e){

            }

        }
    }

    private void processAfterPaymentDone() {

        setupMainView();

        try{

            JSONObject userObject = new JSONObject();
            userObject.put("CreditAmount","");
            userObject.put("ResponseCode","");
            userObject.put("ResponseMsg","");
            userObject.put("Token",token);
            userObject.put("TransID",transactionID);
            userObject.put("UserID",user.UserID);
            userObject.put("WebURL","");

            final CircleDialog circleDialog=new CircleDialog(getActivity(),0);
            circleDialog.setCancelable(true);
            circleDialog.show();

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.GET_PAYMENT_STATUS, userObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jobj) {

                    circleDialog.dismiss();

                    try{
                        String response = jobj.toString();
                        Log.e("Response Get Payment : ", "" + response);


                        if(jobj.getString("ResponseCode").equalsIgnoreCase("1") || jobj.getString("ResponseCode").equalsIgnoreCase("2")){

                            SnackBar bar = new SnackBar(getActivity(),"Credit added to your account");
                            bar.show();

                            FragmentManager manager = getActivity().getSupportFragmentManager();
                            FragmentTransaction ft = manager.beginTransaction();
                            ft.replace(R.id.main_container,new MyAccountFragment());
                            ft.commit();

                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                                fm.popBackStack();
                            }

                        }
                    }catch(Exception we){

                    }




                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    circleDialog.dismiss();
                    Log.e("error responsegg: ",error+"");
                    SnackBar bar = new SnackBar(getActivity(),error.getMessage());
                    bar.show();

                }
            });
            req.setRetryPolicy(
                    new DefaultRetryPolicy(0,0,0));
            MyApplication.getInstance().addToRequestQueue(req);

        }catch (Exception e){

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

                processNext();


                break;
        }

    }

    private void processNext() {


        try{

            JSONObject userObject = new JSONObject();
            userObject.put("CreditAmount",edAmountAddMoney.getText().toString().trim());
            userObject.put("ResponseCode","");
            userObject.put("ResponseMsg","");
            userObject.put("UserID",user.UserID);
            userObject.put("WebURL","");

            final CircleDialog circleDialog=new CircleDialog(getActivity(),0);
            circleDialog.setCancelable(true);
            circleDialog.show();

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.CREDIT_WALLET, userObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jobj) {

                    circleDialog.dismiss();
                    String response = jobj.toString();
                    Log.e("Response : ", "" + response);
                    try{

                        JSONObject obj = new JSONObject(response);
                        web_url = new String();

                        if(obj.getString("ResponseCode").equalsIgnoreCase("1")){
                            web_url = obj.getString("WebURL");
                            token = obj.getString("Token");
                            transactionID = obj.getString("TransID");
                            setupWebView();
                        }

                    }catch(Exception e){

                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    circleDialog.dismiss();
                    Log.e("error responsegg: ",error+"");
                    SnackBar bar = new SnackBar(getActivity(),error.getMessage());
                    bar.show();

                }
            });
            req.setRetryPolicy(
                    new DefaultRetryPolicy(0,0,0));
            MyApplication.getInstance().addToRequestQueue(req);

        }catch (Exception e){

        }









    }
}
