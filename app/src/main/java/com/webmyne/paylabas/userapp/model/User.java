package com.webmyne.paylabas.userapp.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 08-12-2014.
 */


public class User {

    public User() {
    }

    @SerializedName("UserID")

    public long UserID;

    @SerializedName("LemonwayBal")
    public String LemonwayAmmount;   //LemonwayBal

    @SerializedName("Password")
    public String Password;

    @SerializedName("MobileCountryCode")
    public String MobileCountryCode;

    @SerializedName("MobileNo")
    public String MobileNo;

    @SerializedName("FName")
    public String FName;

    @SerializedName("LName")
    public String LName;

    @SerializedName("DOBString")
    public String DOBString;

    @SerializedName("Gender")
    public String Gender;

    @SerializedName("Address")
    public String Address;

    @SerializedName("Country")
    public long Country;

    @SerializedName("State")
    public long State;

    @SerializedName("City")
    public long City;

    @SerializedName("Zip")
    public String Zip;

    public long DateTime;

    @SerializedName("isVerified")
    public boolean isVerified;

    @SerializedName("Status")
    public boolean Status;

    @SerializedName("IsDeleted")
    public boolean IsDeleted;

    @SerializedName("CreatedDate")
    public long CreatedDate;

    @SerializedName("CreatedDateInt")
    public int CreatedDateInt;

    @SerializedName("UpdateDate")
    public long UpdateDate;

    @SerializedName("UpdateDateInt")
    public int UpdateDateInt;

    @SerializedName("RoleId")
    public long RoleId;

    @SerializedName("EmailID")
    public String EmailID;

    @SerializedName("IsSuperAdmin")
    public boolean IsSuperAdmin;

    @SerializedName("IsRegistered")
    public boolean IsRegistered;

    @SerializedName("UserName")
    public String UserName;

    @SerializedName("CashOutPointName")
    public String CashOutPointName;

    @SerializedName("Image")
    public String Image;

    @SerializedName("QuestionId")
    public long QuestionId;

    @SerializedName("Answer")
    public String Answer;

    @SerializedName("TryCount")
    public int TryCount;

    @SerializedName("LastTryDate")
    public long LastTryDate;

    @SerializedName("TryCountLogin")
    public long TryCountLogin;

    @SerializedName("LastTryDateLogin")
    public long LastTryDateLogin;

    @SerializedName("PaylabasMerchantID")
    public String PaylabasMerchantID;

    @SerializedName("PassportNo")
    public String PassportNo;

    @SerializedName("NotificationID")
    public String NotificationID;

    @SerializedName("DeviceType")
    public String DeviceType;

    @SerializedName("ResponseCode")
    public String ResponseCode;

    @SerializedName("ResponseMsg")
    public String ResponseMsg;

    @SerializedName("StatusMsg")
    public String StatusMsg;

    @SerializedName("VerificationCode")
    public String VerificationCode;
}
