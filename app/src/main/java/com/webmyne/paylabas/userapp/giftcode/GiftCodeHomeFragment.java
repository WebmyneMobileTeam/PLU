package com.webmyne.paylabas.userapp.giftcode;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.widgets.Dialog;
import com.gc.materialdesign.widgets.SnackBar;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.webmyne.paylabas.userapp.custom_components.CircleDialog;
import com.webmyne.paylabas.userapp.helpers.AppConstants;
import com.webmyne.paylabas.userapp.helpers.CallWebService;
import com.webmyne.paylabas.userapp.helpers.ComplexPreferences;
import com.webmyne.paylabas.userapp.model.CombineGiftCode;
import com.webmyne.paylabas.userapp.model.GiftCode;
import com.webmyne.paylabas.userapp.model.User;
import com.webmyne.paylabas.userapp.registration.LoginActivity;
import com.webmyne.paylabas_user.R;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GiftCodeHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GiftCodeHomeFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private ButtonRectangle btnMyGc;
    private ButtonRectangle btnSentGc;
    private ListView listGC;
    private User user;
    private ArrayList<GiftCode> giftCodes;
    private ArrayList<GiftCode> myGiftCodes;
    private ArrayList<GiftCode> sentGiftCodes;
    private GCAdapter gcAdapter;
    private PtrFrameLayout frame;




    public static GiftCodeHomeFragment newInstance(String param1, String param2) {
        GiftCodeHomeFragment fragment = new GiftCodeHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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

        View convertView = inflater.inflate(R.layout.fragment_gift_code_home, container, false);

        listGC = (ListView)convertView.findViewById(R.id.listGC);
        btnMyGc = (ButtonRectangle)convertView.findViewById(R.id.btnGCHomeMyGc);
        btnSentGc = (ButtonRectangle)convertView.findViewById(R.id.btnGCHomeSentGc);
        btnMyGc.setOnClickListener(this);
        btnSentGc.setOnClickListener(this);
        frame = (PtrFrameLayout)convertView.findViewById(R.id.material_style_ptr_frame);

     //   final MaterialHeader header = new MaterialHeader(getActivity());
      /*  int[] colors = getResources().getIntArray(R.array.google_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0,16, 0,16);
        header.setPtrFrameLayout(frame);

        frame.setLoadingMinTime(1000);
        frame.setDurationToCloseHeader(1500);
        frame.setHeaderView(header);
        frame.addPtrUIHandler(header);
      *//*  frame.postDelayed(new Runnable() {
            @Override
            public void run() {
                frame.autoRefresh(true);
            }
        }, 100);*//*

        frame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return true;
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame)

                fetchGCList();


            }
        });*/

        return convertView;

    }

    @Override
    public void onResume() {
        super.onResume();

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
        user = complexPreferences.getObject("current_user", User.class);

        fetchGCList();


       // listGC.setAdapter(new GCAdapter());

    }

    private void fetchGCList() {


        final CircleDialog circleDialog=new CircleDialog(getActivity(),0);
        circleDialog.setCancelable(true);

            circleDialog.show();




        new CallWebService(AppConstants.GIFTCODE_LIST +user.UserID,CallWebService.TYPE_JSONARRAY) {

            @Override
            public void response(String response) {


                    circleDialog.dismiss();


                Log.e("Response GC List ",response);
                Type listType=new TypeToken<List<GiftCode>>(){
                }.getType();
                giftCodes =  new GsonBuilder().create().fromJson(response, listType);


                fillUpGCs(giftCodes);



            }

            @Override
            public void error(VolleyError error) {

                circleDialog.dismiss();
                SnackBar bar = new SnackBar(getActivity(),"Sync Error. Please Try again");
                bar.show();

            }
        }.start();


    }

    private void fillUpGCs(ArrayList<GiftCode> giftCodes) {

        myGiftCodes = new ArrayList<GiftCode>();
        sentGiftCodes = new ArrayList<GiftCode>();




        for(GiftCode giftCode : giftCodes){

            if(giftCode.GCFor == user.UserID && giftCode.isUsed == false){
                 myGiftCodes.add(giftCode);
            }else if(giftCode.GCGeneratedBy == user.UserID && giftCode.GCFor != user.UserID && giftCode.isUsed == false){
                 sentGiftCodes.add(giftCode);
            }
        }

        setMyGc();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnGCHomeMyGc:

                setMyGc();

                break;

            case R.id.btnGCHomeSentGc:
                setSentGc();
                break;
        }
    }

    private void setSentGc() {

        btnMyGc.setBackgroundColor(getResources().getColor(R.color.paylabas_white));
        btnSentGc.setBackgroundColor(getResources().getColor(R.color.paylabas_dkgrey));
        btnMyGc.setTextColor(Color.BLACK);
        btnSentGc.setTextColor(Color.WHITE);

        gcAdapter = new GCAdapter(false);
        listGC.setAdapter(gcAdapter);
        gcAdapter.notifyDataSetInvalidated();



    }

    private void setMyGc() {


        btnMyGc.setBackgroundColor(getResources().getColor(R.color.paylabas_dkgrey));
        btnSentGc.setBackgroundColor(getResources().getColor(R.color.paylabas_white));
        btnMyGc.setTextColor(Color.WHITE);
        btnSentGc.setTextColor(Color.BLACK);
        gcAdapter = new GCAdapter(true);
        listGC.setAdapter(gcAdapter);
        gcAdapter.notifyDataSetInvalidated();

    }


    private class GCAdapter extends BaseAdapter{

        private boolean isMyGCListEnabled;

        private GCAdapter(boolean isMyGCListEnabled) {
            this.isMyGCListEnabled = isMyGCListEnabled;
        }


        @Override
        public int getCount() {

            if(isMyGCListEnabled == true){

               return myGiftCodes.size();

            }else{

                return sentGiftCodes.size();

            }


        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                convertView = View.inflate(getActivity(),R.layout.item_mygc_list,null);
            }

            GiftCode code = null;

            if(isMyGCListEnabled == true){

                code = myGiftCodes.get(position);

            }else{

                code = sentGiftCodes.get(position);

            }

            final boolean isCombine = code.IsCombine;

            final ArrayList<CombineGiftCode> arrCombined = code.CombineGCList;

            TextView txtGcItemTitleName = (TextView)convertView.findViewById(R.id.txtGcItemTitleName);
            TextView txtGcItemAmount = (TextView)convertView.findViewById(R.id.txtGcItemAmount);
            TextView txtGcItemMobile = (TextView)convertView.findViewById(R.id.txtGcItemMobile);
            TextView txtGcItemDate = (TextView)convertView.findViewById(R.id.txtGcItemDate);
            ImageView imgCombine = (ImageView)convertView.findViewById(R.id.imgCombine);
            txtGcItemAmount.setText(getResources().getString(R.string.euro)+" "+code.GCAmount);
            txtGcItemDate.setText(code.GCGeneratedDateString);

            if(code.IsCombine == true){
                imgCombine.setVisibility(View.VISIBLE);
            }else{
                imgCombine.setVisibility(View.INVISIBLE);
            }

            if(isMyGCListEnabled == true){
                txtGcItemTitleName.setText(code.SendBy.substring(0, 1).toUpperCase()+code.SendBy.substring(1));
                txtGcItemMobile.setText("+"+code.CountryCode+" "+code.SenderMob);
            }else{
                txtGcItemTitleName.setText(code.SendTo.substring(0, 1).toUpperCase()+code.SendTo.substring(1));
                txtGcItemMobile.setText("+"+code.CountryCode+" "+code.ReceiverMob);
            }


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(isCombine == true){

                        final android.app.Dialog dialog = new android.app.Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
                        View viewDialog = getActivity().getLayoutInflater().inflate(R.layout.item_combine_giftcode_dialog,null);
                        dialog.setContentView(viewDialog);
                        dialog.show();

                        ListView listCombined = (ListView)viewDialog.findViewById(R.id.listCombinedCodes);
                        listCombined.setAdapter(new CombinedGCAdapter(arrCombined));

                        ButtonFlat btnOk = (ButtonFlat)viewDialog.findViewById(R.id.button_accept);
                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();
                            }
                        });

                    }



                }
            });

            return convertView;
        }
    }

    private class CombinedGCAdapter extends BaseAdapter{


        private ArrayList<CombineGiftCode> combined_array;

        private CombinedGCAdapter(ArrayList<CombineGiftCode> combined_array) {
            this.combined_array = combined_array;
        }

        @Override
        public int getCount() {
                return combined_array.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                convertView = View.inflate(getActivity(),R.layout.item_mygc_list,null);
            }

            CombineGiftCode code = combined_array.get(position);

           TextView txtGcItemTitleName = (TextView)convertView.findViewById(R.id.txtGcItemTitleName);
           TextView txtGcItemAmount = (TextView)convertView.findViewById(R.id.txtGcItemAmount);
           TextView txtGcItemMobile = (TextView)convertView.findViewById(R.id.txtGcItemMobile);
           TextView txtGcItemDate = (TextView)convertView.findViewById(R.id.txtGcItemDate);
           ImageView imgCombine = (ImageView)convertView.findViewById(R.id.imgCombine);
           txtGcItemAmount.setText(getResources().getString(R.string.euro)+" "+code.GCAmount);
           txtGcItemDate.setText(code.GCGeneratedDateString);
           imgCombine.setVisibility(View.INVISIBLE);

           txtGcItemTitleName.setText(code.SendBy.substring(0, 1).toUpperCase()+code.SendBy.substring(1));
           txtGcItemMobile.setText(code.SenderMob);


            return convertView;
        }
    }

}
