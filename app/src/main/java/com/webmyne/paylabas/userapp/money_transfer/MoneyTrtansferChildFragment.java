package com.webmyne.paylabas.userapp.money_transfer;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.widgets.SnackBar;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Request;
import com.webmyne.paylabas.userapp.base.AddRecipientActivity;
import com.webmyne.paylabas.userapp.base.MyApplication;
import com.webmyne.paylabas.userapp.base.MyDrawerActivity;
import com.webmyne.paylabas.userapp.custom_components.CircleDialog;
import com.webmyne.paylabas.userapp.helpers.AppConstants;
import com.webmyne.paylabas.userapp.helpers.ComplexPreferences;
import com.webmyne.paylabas.userapp.model.MobileTopup_TopUpProducts;
import com.webmyne.paylabas.userapp.model.PickUpPoint;
import com.webmyne.paylabas.userapp.model.User;
import com.webmyne.paylabas_user.R;

import org.apache.http.impl.client.RequestWrapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoneyTrtansferChildFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoneyTrtansferChildFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private Spinner spinner_country;
    private Spinner spinner_city;
    // private Spinner spinner_pickup_points;
    // private ListView list_pickup_points;
    public int selected_cash_pickup = -1;
    private TextView btnSelectCashPickUp;

    private CheckedTextView txtTitlePickUp;
    private TextView txtTitlePickUpSubTitle,txtTitleWeekend;
    private TextView txtWeekend;
    private View include_item_pickup;
    private ButtonRectangle btnNextMoneyTransfer;

    private ArrayList<MONEYPOLO_COUNTRY> countries;
    private ArrayList<CITY_LIST> cities;
    public static ArrayList<BANK_LIST> bank;

    private boolean isCityLoad = false;
    private boolean isBankLoad = false;

    private EditText edAmountTransfer;
    private int BankID,SelectBankPosition;

    public static BANK_WEB_SERVICE bankobj;
    public static MONEYPOLO_BANK obj;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoneyTrtansferChildFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoneyTrtansferChildFragment newInstance(String param1, String param2) {
        MoneyTrtansferChildFragment fragment = new MoneyTrtansferChildFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MoneyTrtansferChildFragment() {
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

        View convertView = inflater.inflate(R.layout.fragment_money_trtansfer_child, container, false);

        edAmountTransfer = (EditText)convertView.findViewById(R.id.edAmountTransfer);
        spinner_country = (Spinner)convertView.findViewById(R.id.spinner_country);
        spinner_city = (Spinner)convertView.findViewById(R.id.spinner_city);
        btnSelectCashPickUp = (TextView)convertView.findViewById(R.id.btnSelectCashPickUp);
        btnSelectCashPickUp.setOnClickListener(mySelectListner);

        txtTitleWeekend= (TextView)convertView.findViewById(R.id.txtTitleWeekend);
        txtTitlePickUp = (CheckedTextView)convertView.findViewById(R.id.txtTitlePickUp);
        txtTitlePickUpSubTitle = (TextView)convertView.findViewById(R.id.txtTitlePickUpSubTitle);
        txtWeekend = (TextView)convertView.findViewById(R.id.txtWeekend);

        include_item_pickup = (View)convertView.findViewById(R.id.include_item_pickup);
        include_item_pickup.setVisibility(View.GONE);

        btnNextMoneyTransfer = (ButtonRectangle)convertView.findViewById(R.id.btnNextMoneyTransfer);
        btnNextMoneyTransfer.setOnClickListener(nextClickLisnter);



        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){

                }else{
                    fetchCityAndDisplay(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return convertView;
    }


    @Override
    public void onResume() {
        super.onResume();
        clearAll();
        isCityLoad = false;
        isBankLoad = false;
        fetchCountryAndDisplay();

    }

  private void clearAll(){
    //    spinner_country.s
      txtTitlePickUp.setText("");
      txtTitlePickUpSubTitle.setText("");
      txtWeekend.setText("");
      txtTitleWeekend.setText("");

    }

private View.OnClickListener mySelectListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

         if(!isCityLoad && spinner_city.getSelectedItemPosition()==0&& spinner_country.getSelectedItemPosition()==0) {
             SnackBar bar = new SnackBar(getActivity(), "Please Select City and State !!!");
             bar.show();
         }
         else if(edAmountTransfer.getText().length()==0){
             SnackBar bar = new SnackBar(getActivity(), "Please enter amount for money transfer !!!");
             bar.show();
         }
         else if (Integer.valueOf(edAmountTransfer.getText().toString())<10){
             edAmountTransfer.setError("Minimum Amount is € 10 For This Service");
         }
            else {
             fetchBankdetailsandDisplay(0);
         }

        }
    };

    private View.OnClickListener nextClickLisnter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(isBankLoad && spinner_city.getSelectedItemPosition()!=0&& spinner_country.getSelectedItemPosition()!=0) {
                fetchBankdetailsandDisplay(BankID);
                bankobj = new BANK_WEB_SERVICE();

                bankobj.BankID = BankID;
                bankobj.Amount = Float.valueOf(edAmountTransfer.getText().toString());

                bankobj.ApproxComm = bank.get(SelectBankPosition).ApproxComm;
                bankobj.Currencies = obj.ToCurrencyCode;

                bankobj.Fixedcharge = obj.Fixedcharge;
                bankobj.Perccharge = obj.Perccharge;

                bankobj.RecipientGet = obj.RecipientGet;
                bankobj.ConvRate = obj.ConvRate;

                Intent i = new Intent(getActivity(),MoneyTransferFinalActivity.class);
                startActivity(i);

            }
            else {
                SnackBar bar = new SnackBar(getActivity(), "Please Select Bank Details , Country and State !!!");
                bar.show();
            }


        }
    };

    private void fetchPickUpPointsAndDisplay() {

        final Dialog dialog = new Dialog(getActivity(),android.R.style.Theme_Holo_Light_DarkActionBar);
        dialog.setTitle("SELECT PICKUP POINT");
        dialog.setCancelable(true);


        ArrayList points = new ArrayList();
        PickUpPoint point = new PickUpPoint();
        point.name = "ICICI BANK LIMITED, INDIA";
        point.address = "ICICI BANK TOWERS, BANDRA KURLA COMPLEX, BANDRA(E)";
        point.weekend = "10.00 - 23.00";
        points.add(point);

        point = new PickUpPoint();
        point.name = "ICICI BANK LIMITED, INDIA - CASH PAYMENTS";
        point.address = "ICICI BANK TOWERS, BANDRA KURLA COMPLEX, BANDRA(E)";
        point.weekend = "SA: 07:00-15:00";
        points.add(point);

        point = new PickUpPoint();
        point.name = "MUTHFOOT FINANCE - MAHIM WEST";
        point.address = "61, RAM HALL, OPP. MAHIM RAILWAY STATION (WEST) MAHIM, MUMBAI - 400016";
        point.weekend = "SA: 07:00-15:00";
        points.add(point);

        MobilePickUpPointsAdapter adapter = new MobilePickUpPointsAdapter(getActivity(),android.R.layout.simple_list_item_single_choice,points);
        View vDialog = getActivity().getLayoutInflater().inflate(R.layout.item_dialog_pickup,null);
        ListView list_pickup_points = (ListView)vDialog.findViewById(R.id.list_pickup_points);
        list_pickup_points.setAdapter(adapter);
        dialog.setContentView(vDialog);
        dialog.show();

        list_pickup_points.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();

                fillSelectedPoint(position);

            }
        });


    }

    private void fillSelectedPoint(int pos) {

        if(!include_item_pickup.isShown()){
            include_item_pickup.setVisibility(View.VISIBLE);
        }
        SelectBankPosition = pos;
        BankID = bank.get(pos).BankID;
        txtTitlePickUp.setText(bank.get(pos).BankName.toString());
        txtTitlePickUpSubTitle.setText(bank.get(pos).BankAddress.toString());
        txtWeekend.setText(bank.get(pos).WorkingHours.toString());

    }


private void fetchBankdetailsandDisplay(final int bankID){

    final CircleDialog circleDialog=new CircleDialog(getActivity(),0);
    circleDialog.setCancelable(true);
    circleDialog.show();

    try{
        JSONObject userObject = new JSONObject();

        userObject.put("Amount",edAmountTransfer.getText().toString());
        userObject.put("BankID", bankID);
        userObject.put("CityID", cities.get(spinner_city.getSelectedItemPosition()).CityID);
        userObject.put("Description",cities.get(spinner_city.getSelectedItemPosition()).Description);
        userObject.put("ShortCode",countries.get(spinner_country.getSelectedItemPosition()).ShortCode);
        userObject.put("FrmCurrencyCode","EUR");


        Log.e("obj of bank",userObject.toString());
        JsonObjectRequest req = new JsonObjectRequest(com.android.volley.Request.Method.POST, AppConstants.GET_MONERPOLO_BANKLIST, userObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jobj) {

                circleDialog.dismiss();
                String response = jobj.toString();
                Log.e("Response bank : ", "" + response);
                try {

                    final Dialog dialog = new Dialog(getActivity(),android.R.style.Theme_Black_NoTitleBar);
                    dialog.setTitle("SELECT PICKUP POINT");
                    dialog.setCancelable(true);

                     obj =  new GsonBuilder().create().fromJson(response.toString(),MONEYPOLO_BANK.class);

                    bank = obj.BankList;

                    ArrayList points = new ArrayList();
                    PickUpPoint point = new PickUpPoint();

                    for(int i=0;i<bank.size();i++){
                        point = new PickUpPoint();

                        point.name = bank.get(i).BankName.toString();
                        point.address = bank.get(i).BankAddress.toString();
                        point.weekend = bank.get(i).WorkingHours.toString();
                        points.add(point);
                    }

                    MobilePickUpPointsAdapter adapter = new MobilePickUpPointsAdapter(getActivity(),android.R.layout.simple_list_item_single_choice,points);
                    View vDialog = getActivity().getLayoutInflater().inflate(R.layout.item_dialog_pickup,null);

                    ImageView imgBack=(ImageView)vDialog.findViewById(R.id.btnBack);
                    imgBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    ListView list_pickup_points = (ListView)vDialog.findViewById(R.id.list_pickup_points);
                    list_pickup_points.setAdapter(adapter);

                    dialog.setContentView(vDialog);
                    dialog.show();


                    list_pickup_points.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            dialog.dismiss();

                            fillSelectedPoint(position);
                            isBankLoad=true;

                        }
                    });


                } catch (Exception e) {
                    Log.e("error responsegg1: ", e.toString() + "");
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                circleDialog.dismiss();
                Log.e("error responsegg: ", error + "");
                SnackBar bar = new SnackBar(getActivity(), error.getMessage());
                bar.show();

            }
        });
        MyApplication.getInstance().addToRequestQueue(req);




    }catch (Exception e){
        Log.e("Exception in bank",e.toString());
    }
}



    private void fetchCityAndDisplay(int countrycode){

        Log.e("Selected Country ",countries.get(countrycode).CountryCodeName);

        Log.e("Money polo city list ", "................in fetch " + AppConstants.GET_MONERPOLO_CITYLIST);
        final CircleDialog circleDialog=new CircleDialog(getActivity(),0);
        circleDialog.setCancelable(true);
        circleDialog.show();


        try{
            JSONObject userObject = new JSONObject();
            userObject.put("CountryCodeName", countries.get(countrycode).CountryCodeName);
            userObject.put("ShortCode", countries.get(countrycode).ShortCode);
            userObject.put("FrmCurrencyCode", "EUR");
            userObject.put("CountryID", countries.get(countrycode).CountryID);


            JsonObjectRequest req = new JsonObjectRequest(com.android.volley.Request.Method.POST, AppConstants.GET_MONERPOLO_CITYLIST, userObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jobj) {

                    circleDialog.dismiss();
                    String response = jobj.toString();
                    Log.e("Response city : ", "" + response);
                    try {


                        MONEYPOLO_CITY obj =  new GsonBuilder().create().fromJson(response.toString(),MONEYPOLO_CITY.class);

                        cities = obj.CityList;
                        CITY_LIST c1 = new CITY_LIST();
                        c1.Description = "Select City";
                        cities.add(0,c1);

                        MobileCityAdapter adapter = new MobileCityAdapter(getActivity(),
                                android.R.layout.simple_spinner_item,cities);

                        spinner_city.setAdapter(adapter);




                    } catch (Exception e) {
                        Log.e("error responsegg1: ", e.toString() + "");
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    circleDialog.dismiss();
                    Log.e("error responsegg: ", error + "");
                    SnackBar bar = new SnackBar(getActivity(), error.getMessage());
                    bar.show();

                }
            });
            MyApplication.getInstance().addToRequestQueue(req);




        }catch (Exception e){
            Log.e("Exception in city",e.toString());
        }

   }






    private void fetchCountryAndDisplay() {

        Log.e("Money polo country list ", "................in fetch " + AppConstants.GET_MONEYPOLO_COUNTRYLIST);
        final CircleDialog circleDialog=new CircleDialog(getActivity(),0);
        circleDialog.setCancelable(true);
        circleDialog.show();


        JsonArrayRequest request2 = new JsonArrayRequest(AppConstants.GET_MONEYPOLO_COUNTRYLIST,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray jArray) {

                        circleDialog.dismiss();
                        Log.e("Response Country ",jArray.toString());

                        countries = new ArrayList<>();
                        Type listType=new TypeToken<List<MONEYPOLO_COUNTRY>>(){
                        }.getType();
                        countries =  new GsonBuilder().create().fromJson(jArray.toString(),listType);

                        MONEYPOLO_COUNTRY c1 = new MONEYPOLO_COUNTRY();
                        c1.CountryCodeName = "Select Country";
                        countries.add(0,c1);

                        MobileCountryAdapter adapter = new MobileCountryAdapter(getActivity(),
                                android.R.layout.simple_spinner_item,countries);

                        spinner_country.setAdapter(adapter);
                        isCityLoad=true;


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {

                Log.e("Response Country ",""+arg0);
                circleDialog.dismiss();

            }
        });

        int socketTimeout2 = 60000;//60 seconds - change to what you want
        RetryPolicy policy2 = new DefaultRetryPolicy(socketTimeout2, 0, 0);
        request2.setRetryPolicy(policy2);

        MyApplication.getInstance().addToRequestQueue(request2);



    }

    public class MobilePickUpPointsAdapter extends ArrayAdapter<PickUpPoint> {

        Context context;
        int layoutResourceId;
        ArrayList<PickUpPoint> values;
        // int android.R.Layout.

        public MobilePickUpPointsAdapter(Context context, int resource, ArrayList<PickUpPoint> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values=objects;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.item_pickup_points,null);
            }

            CheckedTextView txtTitle = (CheckedTextView) convertView.findViewById(R.id.txtTitlePickUp);
            TextView txtSubTitle = (TextView) convertView.findViewById(R.id.txtTitlePickUpSubTitle);
            TextView txtWeekend = (TextView) convertView.findViewById(R.id.txtWeekend);

            txtSubTitle.setText(values.get(position).address);
            txtTitle.setText(values.get(position).name);
            txtWeekend.setText(values.get(position).weekend);





            return  convertView;

        }
    }

    public class MobileCityAdapter extends ArrayAdapter<CITY_LIST> {

        Context context;
        int layoutResourceId;
        ArrayList<CITY_LIST> values;
        // int android.R.Layout.

        public MobileCityAdapter(Context context, int resource, ArrayList<CITY_LIST> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values=objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(getActivity());
            txt.setPadding(16,16,16,16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).Description);
            return  txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(getActivity());
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setText(values.get(position).Description);
            return  txt;
        }
    }


    public class MobileCountryAdapter extends ArrayAdapter<MONEYPOLO_COUNTRY> {

        Context context;
        int layoutResourceId;
        ArrayList<MONEYPOLO_COUNTRY> values;
        // int android.R.Layout.

        public MobileCountryAdapter(Context context, int resource, ArrayList<MONEYPOLO_COUNTRY> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values=objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(getActivity());
            txt.setPadding(16,16,16,16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).CountryCodeName);
            return  txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(getActivity());
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setText(values.get(position).CountryCodeName);


            return  txt;
        }
    }


    public static class MONEYPOLO_COUNTRY{

        @SerializedName("ConvRate")
        public String ConvRate;
        @SerializedName("CountryCodeName")
        public String CountryCodeName;
        @SerializedName("CountryID")
        public int CountryID;
        @SerializedName("Description")
        public String Description;
        @SerializedName("FrmCurrencyCode")
        public String FrmCurrencyCode;
        @SerializedName("RegionsCount")
        public int RegionsCount;
        @SerializedName("ShortCode")
        public String ShortCode;
        @SerializedName("ToCurrencyCode")
        public String ToCurrencyCode;
        public MONEYPOLO_COUNTRY() {
        }
    }

    public static class MONEYPOLO_CITY{

//        "Amount":"String content",
//                "CityID":4294967295,
//                "Description":"String content"

        @SerializedName("CityList")
        public ArrayList<CITY_LIST> CityList;

    }
    public static class CITY_LIST{
        @SerializedName("Amount")
        public String Amount;
        @SerializedName("CityID")
        public int CityID;
        @SerializedName("Description")
        public String Description;

    }
    public static class BANK_LIST{


        @SerializedName("ApproxComm")
        public float ApproxComm;

        @SerializedName("Amount")
        public float Amount;
        @SerializedName("BankID")
        public int BankID;
        @SerializedName("BankName")
        public String BankName;

        @SerializedName("BankAddress")
        public String BankAddress;

        @SerializedName("BankPSCODE")
        public String BankPSCODE;
        @SerializedName("BankPhone")
        public String BankPhone;

        @SerializedName("Currencies")
        public String Currencies;
        @SerializedName("ReceiverHand")
        public String ReceiverHand;

        @SerializedName("WorkingDays")
        public String WorkingDays;
        @SerializedName("WorkingHours")
        public String WorkingHours;


    }



    public static class MONEYPOLO_BANK{


        @SerializedName("BankList")
        public ArrayList<BANK_LIST> BankList;

        @SerializedName("ConvRate")
        public String ConvRate;

        @SerializedName("Fixedcharge")
        public String Fixedcharge;

        @SerializedName("PayableAmt")
        public String PayableAmt;

        @SerializedName("Perccharge")
        public String Perccharge;

        @SerializedName("RecipientGet")
        public String RecipientGet;

        @SerializedName("ToCurrencyCode")
        public String ToCurrencyCode;

    }

    public static class BANK_WEB_SERVICE{

        @SerializedName("ApproxComm")
        public float ApproxComm;

        @SerializedName("Amount")
        public float Amount;
        @SerializedName("BankID")
        public int BankID;
        @SerializedName("BankName")
        public String BankName;

        @SerializedName("BankAddress")
        public String BankAddress;

        @SerializedName("BankPSCODE")
        public String BankPSCODE;
        @SerializedName("BankPhone")
        public String BankPhone;

        @SerializedName("Currencies")
        public String Currencies;
        @SerializedName("ReceiverHand")
        public String ReceiverHand;

        @SerializedName("WorkingDays")
        public String WorkingDays;
        @SerializedName("WorkingHours")
        public String WorkingHours;

        @SerializedName("ConvRate")
        public String ConvRate;

        @SerializedName("Fixedcharge")
        public String Fixedcharge;

        @SerializedName("PayableAmt")
        public String PayableAmt;

        @SerializedName("Perccharge")
        public String Perccharge;

        @SerializedName("RecipientGet")
        public String RecipientGet;

        @SerializedName("ToCurrencyCode")
        public String ToCurrencyCode;

    }
}
