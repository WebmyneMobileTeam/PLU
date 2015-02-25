package com.webmyne.paylabas.userapp.money_transfer;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.webmyne.paylabas.userapp.base.MyDrawerActivity;
import com.webmyne.paylabas.userapp.custom_components.PagerSlidingTabStrip;
import com.webmyne.paylabas.userapp.giftcode.CombineGCFragment;
import com.webmyne.paylabas.userapp.giftcode.GenerateGCFragment;
import com.webmyne.paylabas.userapp.giftcode.GiftCodeHomeFragment;
import com.webmyne.paylabas.userapp.giftcode.RedeemGCFragment;
import com.webmyne.paylabas.userapp.helpers.AppConstants;
import com.webmyne.paylabas.userapp.helpers.CallWebService;
import com.webmyne.paylabas.userapp.helpers.ComplexPreferences;
import com.webmyne.paylabas.userapp.model.LanguageStringUtil;
import com.webmyne.paylabas.userapp.model.User;
import com.webmyne.paylabas_user.R;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ParentMoneyTransferFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ParentMoneyTransferFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;


    PagerSlidingTabStrip tabs;
    public ViewPager pager;
    public MyPagerAdapter adapter;
    private User user;



    public static ParentMoneyTransferFragment newInstance(String param1, String param2) {
        ParentMoneyTransferFragment fragment = new ParentMoneyTransferFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ParentMoneyTransferFragment() {
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

        View convertView =inflater.inflate(R.layout.fragment_parent_money_transfer, container, false);
        tabs=(PagerSlidingTabStrip)convertView.findViewById(R.id.tabs_moneytransfer);
        pager=(ViewPager)convertView.findViewById(R.id.pager_moneytransfer);

        adapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);

        return convertView;

    }


    public void refreshBalance(){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
        user = complexPreferences.getObject("current_user", User.class);

        ((MyDrawerActivity)getActivity()).setToolTitle("Hi, "+user.FName);

        new CallWebService(AppConstants.USER_DETAILS+user.UserID,CallWebService.TYPE_JSONOBJECT) {

            @Override
            public void response(String response) {

                Log.e("Response User Details ", response);

                try{

                    JSONObject obj = new JSONObject(response);
                    try{
                        ((MyDrawerActivity)getActivity()).setToolSubTitle("Balance "+getResources().getString(R.string.euro)+" "+ LanguageStringUtil.languageString(getActivity(), String.valueOf(obj.getString("LemonwayBal"))));
                    }catch(Exception e){

                    }

                }catch(Exception e){

                }

            }

            @Override
            public void error(VolleyError error) {


            }
        }.start();


    }


    @Override
    public void onResume() {
        super.onResume();

        refreshBalance();

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
         user = complexPreferences.getObject("current_user", User.class);
        ((MyDrawerActivity)getActivity()).setToolTitle("Hi, "+user.FName);
        ((MyDrawerActivity)getActivity()).setToolSubTitle("Balance "+getResources().getString(R.string.euro)+" "+ LanguageStringUtil.languageString(getActivity(), String.valueOf(user.LemonwayAmmount)));
        ((MyDrawerActivity)getActivity()).setToolColor(getResources().getColor(R.color.color_moneytransfer));
    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        private final String[] TITLES = {getResources().getString(R.string.code_TITLEMONEYTRANSFE),getResources().getString(R.string.code_TITLEP2P),getResources().getString(R.string.code_TITLEHISTORY)};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }
        @Override
        public Fragment getItem(int position) {

            if(position == 0){
                return MoneyTrtansferChildFragment.newInstance("", "");
            }else if(position == 1){
                return MoneyTransferPtoPFragment.newInstance("", "");
            }else if(position == 2){
                return MoneyTransferHomeFragment.newInstance("", "");
            }else{
                return MoneyTrtansferChildFragment.newInstance("","");
            }

/*
            switch (position){

                case 0:
                    return GiftCodeHomeFragment.newInstance("","");
                case 1: return GenerateGCFragment.newInstance("","");
                case 2: return CombineGCFragment.newInstance("","");
                case 3: return RedeemGCFragment.newInstance("","");

                default: return null;


            }
*/
        }
    }
}
