package com.webmyne.paylabas.userapp.giftcode;


import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.gc.materialdesign.views.ButtonRectangle;
import com.webmyne.paylabas_user.R;

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

        return convertView;

    }

    @Override
    public void onResume() {
        super.onResume();

        setMyGc();
        listGC.setAdapter(new GCAdapter());

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

    }

    private void setMyGc() {

        btnMyGc.setBackgroundColor(getResources().getColor(R.color.paylabas_dkgrey));
        btnSentGc.setBackgroundColor(getResources().getColor(R.color.paylabas_white));

        btnMyGc.setTextColor(Color.WHITE);
        btnSentGc.setTextColor(Color.BLACK);

    }


    private class GCAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return 10;
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

            return convertView;
        }
    }

}
