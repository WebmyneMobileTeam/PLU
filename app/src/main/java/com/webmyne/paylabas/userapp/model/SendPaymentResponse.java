package com.webmyne.paylabas.userapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 24-12-2014.
 */
public class SendPaymentResponse {

    @SerializedName("LemonwayBal")
    public String LemonwayBal;

    @SerializedName("ResponseCode")
    public String ResponseCode;

    @SerializedName("ResponseMsg")
    public String ResponseMsg;
}
