package com.webmyne.paylabas.userapp.user_navigation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.webmyne.paylabas_user.R;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Use the {@link com.webmyne.paylabas.userapp.user_navigation.FAQ_Answer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FAQ_Answer extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView txtAnswer;
    private TextView txtQuestion_Title;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Aboutus.
     */
    // TODO: Rename and change types and number of parameters
    public static FAQ_Answer newInstance(String param1, String param2) {
        FAQ_Answer fragment = new FAQ_Answer();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FAQ_Answer() {
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
        View convertView = inflater.inflate(R.layout.fragment_faq_answer_layout, container, false);
        txtAnswer = (TextView)convertView.findViewById(R.id.txtAnswer);
        txtQuestion_Title = (TextView)convertView.findViewById(R.id.txtQuestion_Title);

        int Question_pos = getArguments().getInt("Position");

        String[] Question = getActivity().getResources().getStringArray(R.array.faq_questions);
        String[] Answer = getActivity().getResources().getStringArray(R.array.faq_answer);

        txtQuestion_Title.setText(Question[Question_pos]);
        txtAnswer.setText(Answer[Question_pos]);

        return convertView;
     }

//end of main class
}
