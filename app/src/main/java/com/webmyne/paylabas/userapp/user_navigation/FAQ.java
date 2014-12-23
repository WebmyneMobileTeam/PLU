package com.webmyne.paylabas.userapp.user_navigation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.webmyne.paylabas.userapp.profile.Profile;
import com.webmyne.paylabas_user.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FAQ#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FAQ extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private String[] faq_que;
    private ListView list_faq;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FAQ.
     */
    // TODO: Rename and change types and number of parameters
    public static FAQ newInstance(String param1, String param2) {
        FAQ fragment = new FAQ();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FAQ() {
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
        View convertView = inflater.inflate(R.layout.fragment_faq, container, false);

        list_faq = (ListView)convertView.findViewById(R.id.list_faq);



        // setting the list view adapter
        list_faq.setAdapter(new list_faq_adapter());


        list_faq.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        FragmentManager manager1 = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft1 = manager1.beginTransaction();
                        Bundle args = new Bundle();
                        args.putInt("Position",position );
                        Fragment fm= new FAQ_Answer();
                        fm.setArguments(args);
                        ft1.replace(R.id.main_container,fm);
                        ft1.addToBackStack("");
                        ft1.commit();

            } // end of onItemCLick
        });

        return  convertView;
    }


public class list_faq_adapter extends BaseAdapter{
    list_faq_adapter(){
       faq_que=getActivity().getResources().getStringArray(R.array.faq_questions);
    }
    @Override
    public int getCount() {

        return faq_que.length;
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
        View row = inflater.inflate(R.layout.faq_listview_layout, parent, false);

        TextView txt_que=(TextView)row.findViewById(R.id.txtQuestion);
        txt_que.setText(faq_que[position]);
        txt_que.setTextSize(16);
        return row;
    }
}

// end of main class
}
