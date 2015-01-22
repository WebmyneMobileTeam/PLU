package com.webmyne.paylabas.userapp.money_transfer;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.gc.materialdesign.widgets.SnackBar;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.webmyne.paylabas.userapp.custom_components.CircleDialog;
import com.webmyne.paylabas.userapp.helpers.AppConstants;
import com.webmyne.paylabas.userapp.helpers.CallWebService;
import com.webmyne.paylabas.userapp.helpers.ComplexPreferences;
import com.webmyne.paylabas.userapp.mobile_topup.MobileTopupHomeFragment;
import com.webmyne.paylabas.userapp.model.User;
import com.webmyne.paylabas_user.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;

public class MoneyTransferHomeFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private ListView listMobileTopup;
    private String[] faq_que;
    private PtrFrameLayout frame;

    ArrayList<MobileTopupList> mobiletopuplist;

    ArrayList<String> demo;
    public static MoneyTransferHomeFragment newInstance(String param1, String param2) {
        MoneyTransferHomeFragment fragment = new MoneyTransferHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MoneyTransferHomeFragment() {
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

        View convertView = inflater.inflate(R.layout.fragment_money_transfer_home, container, false);


        listMobileTopup = (ListView)convertView.findViewById(R.id.listMobileTopup);
        listMobileTopup.setEmptyView(convertView.findViewById(R.id.redeemEmptyView));

        frame = (PtrFrameLayout)convertView.findViewById(R.id.material_style_ptr_frame);

        final MaterialHeader header = new MaterialHeader(getActivity());
        int[] colors = getResources().getIntArray(R.array.google_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0,16, 0,16);
        header.setPtrFrameLayout(frame);

        frame.setLoadingMinTime(1000);
        frame.setDurationToCloseHeader(1000);
        frame.setHeaderView(header);
        frame.addPtrUIHandler(header);

        frame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return true;
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame){ //fetchMobileTopupAndDisplay();


            }
        });

      //  fetchMobileTopupAndDisplay();

        demo = new ArrayList<String>();
        listMobileTopup.setAdapter(new list_MobileTopup(demo));

        return convertView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


   /* private  void fetchMobileTopupAndDisplay(){

        final CircleDialog circleDialog=new CircleDialog(getActivity(),0);
        circleDialog.setCancelable(true);
        circleDialog.show();

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
        User user = complexPreferences.getObject("current_user", User.class);

        //GET_MY_MOBILE_TOPUPLIST
        new CallWebService(AppConstants.GET_MY_MOBILE_TOPUPLIST + user.UserID, CallWebService.TYPE_JSONARRAY) {

            @Override
            public void response(String response) {

                circleDialog.dismiss();

                frame.refreshComplete();

                Log.e("Mobile topup List", response);
                if (response == null) {

                } else {

                    Type listType = new TypeToken<List<MobileTopupList>>() {
                    }.getType();

                    mobiletopuplist = new GsonBuilder().create().fromJson(response, listType);
                    listMobileTopup.setAdapter(new list_MobileTopup(mobiletopuplist));

                }

            }

            @Override
            public void error(VolleyError error) {
                frame.refreshComplete();
                circleDialog.dismiss();
                SnackBar bar = new SnackBar(getActivity(), "Sync Error. Please Try again");
                bar.show();
            }
        }.start();


    }*/


    public class list_MobileTopup extends BaseAdapter {

      //  ArrayList<MobileTopupList> mobiletopuplist1;

        list_MobileTopup( ArrayList<String> mobiletopuplist_temp){
            Log.e("in consrt", String.valueOf(mobiletopuplist_temp.size()));
      //      mobiletopuplist1 = mobiletopuplist_temp;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View row = inflater.inflate(R.layout.item_my_mobiletopup_list, parent, false);


            TextView txt_MobileNo = (TextView) row.findViewById(R.id.txt_MobileNo);
            TextView txt_rechardedate = (TextView) row.findViewById(R.id.txt_rechardedate);
            TextView txt_AmountIndolla = (TextView) row.findViewById(R.id.txt_AmountIndolla);
            TextView txt_Status= (TextView)row.findViewById(R.id.txt_Status);

            txt_MobileNo.setText("Krishna Patel");
            txt_AmountIndolla.setText("€ 10");
            txt_rechardedate.setText("Jan 5 2015 12:03PM");
            txt_Status.setText("Status: Done");


            return row;
        }
    }



    public class MobileTopupList {

        @SerializedName("RechargeAmount")
        public String RechargeAmount;


        @SerializedName("MobileNo")
        public String MobileNo;


        @SerializedName("IDTTransactionId")
        public String IDTTransactionId;


        public String FirstName;

        public String LastName;

        public String Status;

        @SerializedName("createdOnString")
        public String createdOnString;




    }




}
