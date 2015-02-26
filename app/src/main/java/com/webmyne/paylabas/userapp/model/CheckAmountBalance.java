package com.webmyne.paylabas.userapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 26-02-2015.
 */
public class CheckAmountBalance {



    @SerializedName("LemonwayBal")
    public String LemonwayBal;
    @SerializedName("MaxLimit")
    public String MaxLimit;
    @SerializedName("MinLimit")
    public String MinLimit;
    @SerializedName("ResponseCode")
    public String ResponseCode;
    @SerializedName("ResponseMsg")
    public String ResponseMsg;
    @SerializedName("ServiceID")
    public String ServiceID;
    @SerializedName("UserID")
    public String UserID;
}
