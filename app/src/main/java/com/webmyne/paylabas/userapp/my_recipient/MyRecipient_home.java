package com.webmyne.paylabas.userapp.my_recipient;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.views.ButtonFloatSmall;
import com.gc.materialdesign.widgets.SnackBar;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.webmyne.paylabas.userapp.custom_components.CircleDialog;
import com.webmyne.paylabas.userapp.helpers.AppConstants;
import com.webmyne.paylabas.userapp.helpers.CallWebService;
import com.webmyne.paylabas.userapp.helpers.ComplexPreferences;
import com.webmyne.paylabas.userapp.model.GiftCode;
import com.webmyne.paylabas.userapp.model.Receipient;
import com.webmyne.paylabas.userapp.model.User;
import com.webmyne.paylabas_user.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyRecipient_home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyRecipient_home extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ButtonFloat buttonADdFloat;
    private ListView listMyRecipient;
    private String[] faq_que;
    private PtrFrameLayout frame;
    private View footerView;


    private ArrayList<Receipient> receipients;
    private User user;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyRecipient_home.
     */
    // TODO: Rename and change types and number of parameters
    public static MyRecipient_home newInstance(String param1, String param2) {
        MyRecipient_home fragment = new MyRecipient_home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MyRecipient_home() {
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
        // Inflate the layout for this fragment

        View convertView = inflater.inflate(R.layout.fragment_my_recipient_home, container, false);
        buttonADdFloat = (ButtonFloat) convertView.findViewById(R.id.buttonADdFloat);
        buttonADdFloat.setDrawableIcon(getResources().getDrawable(R.drawable.ic_action_new));

        frame = (PtrFrameLayout) convertView.findViewById(R.id.material_style_ptr_frame);

        receipients = new ArrayList<Receipient>();

        final MaterialHeader header = new MaterialHeader(getActivity());
        int[] colors = getResources().getIntArray(R.array.google_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, 16, 0, 16);
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
            public void onRefreshBegin(final PtrFrameLayout frame) {
                fetchReceipientsAndDisplay();
            }
        });

        fetchReceipientsAndDisplay();

        listMyRecipient = (ListView) convertView.findViewById(R.id.listMyRecipient);
        listMyRecipient.setEmptyView(convertView.findViewById(R.id.redeemEmptyView));

        // setting the footer view
        footerView = ((LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footerview_recipient, null, false);
        listMyRecipient.addFooterView(footerView);


        listMyRecipient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FragmentManager manager22 = getActivity().getSupportFragmentManager();
                FragmentTransaction ft22 = manager22.beginTransaction();


                Bundle arg = new Bundle();

                arg.putInt("pos", position);

                arg.putInt("RecipientID", (int) receipients.get(position).RecipientID);

                arg.putInt("CountryID", (int) receipients.get(position).Country);
                arg.putInt("StateID", (int) receipients.get(position).State);
                arg.putInt("CityID", (int) receipients.get(position).City);

                arg.putString("FirstName", receipients.get(position).FirstName);
                arg.putString("LastName", receipients.get(position).LastName);
                arg.putString("Email", receipients.get(position).EmailId);
                arg.putString("Mobileno", receipients.get(position).MobileNo);

                Fragment fm = new MyRecipient_add_edit();
                fm.setArguments(arg);

                ft22.replace(R.id.main_container, fm);
                ft22.addToBackStack("");
                ft22.commit();
            }
        });


// click to open MYRecipient add_edit fragment
        buttonADdFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager manager22 = getActivity().getSupportFragmentManager();
                FragmentTransaction ft22 = manager22.beginTransaction();

                Bundle arg = new Bundle();
                arg.putInt("pos", -1);
                Fragment fm = new MyRecipient_add_edit();
                fm.setArguments(arg);

                ft22.replace(R.id.main_container, fm);
                ft22.addToBackStack("");
                ft22.commit();
            }
        });


// to delete the Recipient
        listMyRecipient.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                final com.gc.materialdesign.widgets.Dialog alert = new com.gc.materialdesign.widgets.Dialog(getActivity(), "Delete Recipient ?", "Are you sure want to delete this recipient");
                alert.show();

                alert.setOnAcceptButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                        processDeleteRecipient(position);
                    }
                });

                return false;
            }
        });


        return convertView;
    }

    private void processDeleteRecipient(int pos) {
        SnackBar bar = new SnackBar(getActivity(), "delete done:- " + String.valueOf(pos));
        bar.show();
    }

    private void fetchReceipientsAndDisplay() {

        final CircleDialog circleDialog = new CircleDialog(getActivity(), 0);
        circleDialog.setCancelable(true);
        circleDialog.show();

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
        user = complexPreferences.getObject("current_user", User.class);

        new CallWebService(AppConstants.GETRECEIPIENTS + user.UserID, CallWebService.TYPE_JSONARRAY) {

            @Override
            public void response(String response) {

                circleDialog.dismiss();

                frame.refreshComplete();

                Log.e("Receipients List", response);
                if (response == null) {

                } else {

                    Type listType = new TypeToken<List<Receipient>>() {
                    }.getType();

                    receipients = new GsonBuilder().create().fromJson(response, listType);
                    listMyRecipient.setAdapter(new list_MyRecipient(receipients));

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


    public class list_MyRecipient extends BaseAdapter {
        list_MyRecipient(ArrayList<Receipient> receipients) {
            Log.e("in consrt", String.valueOf(receipients.size()));
        }

        @Override
        public int getCount() {
            return receipients.size();
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

            txt_Fname.setText(receipients.get(position).FirstName + " " + receipients.get(position).LastName);
            txt_Email.setText(receipients.get(position).EmailId);
            txt_Mobile.setText("+" + receipients.get(position).CountryCode + " " + receipients.get(position).MobileNo);

            return row;
        }
    }

// end of main class
}
