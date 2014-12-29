package com.webmyne.paylabas.userapp.mobile_topup;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.widgets.SnackBar;
import com.webmyne.paylabas.userapp.custom_components.CircleDialog;
import com.webmyne.paylabas.userapp.model.Receipient;
import com.webmyne.paylabas_user.R;

import java.util.ArrayList;

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

    listMobileTopup.setAdapter(new list_MobileTopup());

    circleDialog.dismiss();
}


public class list_MobileTopup extends BaseAdapter {
    /*list_MobileTopup(ArrayList<Receipient> receipients){

        }*/
    private String[]  faq={};
    list_MobileTopup() {
        Log.e("",String.valueOf(faq.length));
      //  faq_que = getActivity().getResources().getStringArray(R.array.faq_questions);
    }
        @Override
        public int getCount() {
            return faq.length;
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
            View row = inflater.inflate(R.layout.item_my_recipient_list, parent, false);



                TextView txt_Fname = (TextView) row.findViewById(R.id.txtName);
                TextView txt_Email = (TextView) row.findViewById(R.id.txtEmail);
                TextView txt_Mobile = (TextView) row.findViewById(R.id.txtMobile);

                txt_Fname.setText(faq[position]);
           /* txt_Email.setText(receipients.get(position).EmailId);
            txt_Mobile.setText("+"+receipients.get(position).CountryCode+" "+receipients.get(position).MobileNo);
*/

            return row;
        }
    }

// end of main class
}
