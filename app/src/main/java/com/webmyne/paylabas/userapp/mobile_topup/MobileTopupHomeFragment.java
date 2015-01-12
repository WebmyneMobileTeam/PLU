package com.webmyne.paylabas.userapp.mobile_topup;


import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import com.webmyne.paylabas.userapp.model.User;
import com.webmyne.paylabas_user.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;

public class MobileTopupHomeFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private ListView listMobileTopup;
    private String[] faq_que;
    private PtrFrameLayout frame;

    ArrayList<MobileTopupList> mobiletopuplist;


    public static MobileTopupHomeFragment newInstance(String param1, String param2) {
        MobileTopupHomeFragment fragment = new MobileTopupHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MobileTopupHomeFragment() {
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

        View convertView = inflater.inflate(R.layout.fragment_mobiletopup_home, container, false);

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
            public void onRefreshBegin(final PtrFrameLayout frame){ fetchMobileTopupAndDisplay();


            }
        });

        fetchMobileTopupAndDisplay();


        return convertView;
    }

private  void fetchMobileTopupAndDisplay(){

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


}


public class list_MobileTopup extends BaseAdapter {

        ArrayList<MobileTopupList> mobiletopuplist1;

        list_MobileTopup( ArrayList<MobileTopupList> mobiletopuplist_temp){
        Log.e("in consrt", String.valueOf(mobiletopuplist_temp.size()));
            mobiletopuplist1 = mobiletopuplist_temp;
        }

        @Override
        public int getCount() {
            return mobiletopuplist1.size();
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

                txt_MobileNo.setText(mobiletopuplist1.get(position).MobileNo);
                txt_AmountIndolla.setText("$"+mobiletopuplist1.get(position).AmountIndollar);
                txt_rechardedate.setText(mobiletopuplist1.get(position).createdOnString);


            return row;
        }
    }



    public class MobileTopupList {

        @SerializedName("AmountIndollar")
        public int AmountIndollar;

        @SerializedName("MobileNo")
        public String MobileNo;

        @SerializedName("createdOnString")
        public String createdOnString;




    }

// end of main class
}
