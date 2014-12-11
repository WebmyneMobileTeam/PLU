package com.webmyne.paylabas.userapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 09-12-2014.
 */
public class CombineGiftCode {

    @SerializedName("GCAmount")
    public String GCAmount;
  //  @SerializedName("GCGeneratedDate")
  //  public long GCGeneratedDate;

    @SerializedName("GCGeneratedDateString")
    public String GCGeneratedDateString;

    @SerializedName("SendBy")
    public String SendBy;
    @SerializedName("SenderMob")
    public String SenderMob;
    @SerializedName("id")
    public double id;

}
