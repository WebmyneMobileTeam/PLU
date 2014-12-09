package com.webmyne.paylabas.userapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Android on 08-12-2014.
 */
public class GiftCode {

    @SerializedName("AffiliateID")
    public double AffiliateID;

    @SerializedName("Affiliete")
    public String Affiliete;

    @SerializedName("CombineGCId")
    public double CombineGCId;

    @SerializedName("CombineGCList")
    public ArrayList<CombineGiftCode> CombineGCList;

    @SerializedName("CountryCode")
    public String CountryCode;

    @SerializedName("GCAmount")
    public double GCAmount;

    @SerializedName("GCFor")
    public double GCFor;

    @SerializedName("GCGeneratedBy")
    public double GCGeneratedBy;

  //  @SerializedName("GCGeneratedDate")
  //  public long GCGeneratedDate;

    @SerializedName("IsCombine")
    public boolean IsCombine;

    @SerializedName("IsExpired")
    public boolean IsExpired;

    @SerializedName("ReceiverMob")
    public String ReceiverMob;

    @SerializedName("SendBy")
    public String SendBy;

    @SerializedName("SendTo")
    public String SendTo;

    @SerializedName("SenderMob")
    public String SenderMob;

    @SerializedName("Status")
    public boolean Status;

   // @SerializedName("WithdrawalDate")
  //  public long WithdrawalDate;

    @SerializedName("id")
    public double id;

    @SerializedName("isUsed")
    public boolean isUsed;

}
