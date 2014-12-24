package com.webmyne.paylabas.userapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Android on 23-12-2014.
 */
public class SendMoneyToPaylabasUser {


    @SerializedName("FixCharge")
    public String FixCharge;

    @SerializedName("LemonwayBal")
    public String LemonwayBal;

    @SerializedName("MaxLimit")
    public String MaxLimit;

    @SerializedName("MinLimit")
    public String MinLimit;

    @SerializedName("PayableAmount")
    public String PayableAmount;

    @SerializedName("PercentageCharge")
    public String PercentageCharge;

    @SerializedName("ResponseCode")
    public String ResponseCode;

    @SerializedName("ResponseMsg")
    public String ResponseMsg;

    @SerializedName("PayLabasUses")
    public ArrayList<P2PUses> PayLabasUsesList=new ArrayList<P2PUses>();

    @SerializedName("RecipientList")
    public ArrayList<P2PReceipient> PayLabasRecipientList=new ArrayList<P2PReceipient>();

    public String tempExchangeCost;

    public String tempWithdrawAmount;

    public String temppayableAmount;

    public String tempCountryCodeId;

    public String tempCountryId;

    public String tempStateId;

    public String tempCityId;

    public String tempFirstName;

    public String tempLastName;

    public String tempMobileId;

    public String tempEmailId;

    public String tempCityName;

    public String tempCountryName;








}
