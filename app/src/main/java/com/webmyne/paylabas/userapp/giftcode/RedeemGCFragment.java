package com.webmyne.paylabas.userapp.giftcode;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.gc.materialdesign.widgets.SnackBar;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.webmyne.paylabas.userapp.custom_components.CircleDialog;
import com.webmyne.paylabas.userapp.helpers.AppConstants;
import com.webmyne.paylabas.userapp.helpers.CallWebService;
import com.webmyne.paylabas.userapp.helpers.ComplexPreferences;
import com.webmyne.paylabas.userapp.model.GiftCode;
import com.webmyne.paylabas.userapp.model.User;
import com.webmyne.paylabas_user.R;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RedeemGCFragment extends Fragment {

    private ListView listRedeemGC;
    private ArrayList<GiftCode> giftCodes;
    private ArrayList<GiftCode> redeemGiftCodesList;
    private CircleDialog circleDialog;
    private User user;
    private GCAdapter gcAdapter;

    public static RedeemGCFragment newInstance(String param1, String param2) {
        RedeemGCFragment fragment = new RedeemGCFragment();
        return fragment;
    }

    public RedeemGCFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView = inflater.inflate(R.layout.fragment_redeem_gc, container, false);
        listRedeemGC = (ListView) convertView.findViewById(R.id.listRedemGC);
        listRedeemGC.setEmptyView(convertView.findViewById(R.id.redeemEmptyView));
        return convertView;
    }

    @Override
    public void onResume() {
        super.onResume();

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
        user = complexPreferences.getObject("current_user", User.class);
        fetchRedeemGCList();
    }

    private void fetchRedeemGCList() {
//        Log.e("fetchRedeemGCList","fetchRedeemGCList");
        circleDialog = new CircleDialog(getActivity(), 0);
        circleDialog.setCancelable(true);
        circleDialog.show();

        new CallWebService(AppConstants.GIFTCODE_LIST + user.UserID, CallWebService.TYPE_JSONARRAY) {

            @Override
            public void response(String response) {

                circleDialog.dismiss();
//                Log.e("Response GC List ", response);
                Type listType = new TypeToken<List<GiftCode>>() {
                }.getType();
                giftCodes = new GsonBuilder().create().fromJson(response, listType);
                fillUpRedeemGCs(giftCodes);

            }

            @Override
            public void error(VolleyError error) {

                circleDialog.dismiss();
                SnackBar bar = new SnackBar(getActivity(), "Sync Error. Please Try again");
                bar.show();

            }
        }.start();

    }

    private void fillUpRedeemGCs(ArrayList<GiftCode> giftCodes) {

        redeemGiftCodesList = new ArrayList<GiftCode>();

        for (GiftCode giftCode : giftCodes) {
            if (giftCode.isUsed == true) {
                redeemGiftCodesList.add(giftCode);
            }
        }

        setRedeemGc(redeemGiftCodesList);
    }

    private void setRedeemGc(final ArrayList<GiftCode> redeemGiftCodesList) {

        if(redeemGiftCodesList !=null) {
            gcAdapter = new GCAdapter(getActivity(), redeemGiftCodesList);
            listRedeemGC.setAdapter(gcAdapter);
            gcAdapter.notifyDataSetChanged();
        }
    }

    private class GCAdapter extends BaseAdapter {

        private ArrayList<GiftCode> redeemList;
        private Context context;
        private LayoutInflater mInflater;
        private ViewHolder holder;

        public GCAdapter(FragmentActivity activity, ArrayList<GiftCode> redeemGiftCodesList) {
            this.context = activity;
            this.redeemList = redeemGiftCodesList;
        }

        @Override
        public int getCount() {

            return redeemList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            TextView txtGcItemTitleName, txtGcItemAmount, txtGcItemMobile, txtGcItemDate;
            ImageView imgCombine;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {

                convertView = mInflater.inflate(R.layout.item_mygc_list, parent, false);

                holder = new ViewHolder();
                holder.txtGcItemTitleName = (TextView) convertView.findViewById(R.id.txtGcItemTitleName);
                holder.txtGcItemAmount = (TextView) convertView.findViewById(R.id.txtGcItemAmount);
                holder.txtGcItemMobile = (TextView) convertView.findViewById(R.id.txtGcItemMobile);
                holder.txtGcItemDate = (TextView) convertView.findViewById(R.id.txtGcItemDate);
                holder.imgCombine = (ImageView)convertView.findViewById(R.id.imgCombine);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.imgCombine.setVisibility(View.GONE);
            holder.txtGcItemAmount.setText(getResources().getString(R.string.euro)+" "+redeemList.get(position).GCAmount);
            holder.txtGcItemDate.setText(redeemList.get(position).GCGeneratedDateString);
            holder.txtGcItemTitleName.setText(redeemList.get(position).SendBy.substring(0, 1).toUpperCase() + redeemList.get(position).SendBy.substring(1));
            holder.txtGcItemMobile.setText("+"+redeemList.get(position).CountryCode+" "+redeemList.get(position).SenderMob);

            return convertView;
        }

    }

}