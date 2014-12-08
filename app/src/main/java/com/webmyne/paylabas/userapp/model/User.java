package com.webmyne.paylabas.userapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 08-12-2014.
 */


public class User {

    public User() {
    }

    @SerializedName("UserID")
    public long UserID;


    @SerializedName("LemonwayAmmount")
    public String LemonwayAmmount;

    public String Password;

    @SerializedName("MobileCountryCode")
    public String MobileCountryCode;

    @SerializedName("MobileNo")
    public String MobileNo;

    @SerializedName("FName")
    public String FName;

    @SerializedName("LName")
    public String LName;

    public String Gender;

    public String Address;

    public long Country;

    public long State;

    public long City;

    public String Zip;

    public long DateTime;

    public boolean isVerified;

    public boolean Status;

    public boolean IsDeleted;

    public long CreatedDate;

    public int CreatedDateInt;

    public long UpdateDate;

    public int UpdateDateInt;

    public long RoleId;

    @SerializedName("EmailID")
    public String EmailID;

    public boolean IsSuperAdmin;

    public boolean IsRegistered;

    public String UserName;

    public String CashOutPointName;

    public String Image;

    public long QuestionId;

    public String Answer;

    public int TryCount;

    public long LastTryDate;

    public long TryCountLogin;

    public long LastTryDateLogin;

    public String PaylabasMerchantID;

    public String PassportNo;

    public String NotificationID;

    @SerializedName("DeviceType")
    public String DeviceType;




}
