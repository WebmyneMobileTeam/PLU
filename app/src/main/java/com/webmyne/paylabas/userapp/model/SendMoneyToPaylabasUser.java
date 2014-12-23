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
    public ArrayList<P2PUses> PayLabasUsesList;

    @SerializedName("RecipientList")
    public ArrayList<P2PReceipient> PayLabasRecipientList;

    public String ExchangeCost;

    public String WithdrawAmount;

}
