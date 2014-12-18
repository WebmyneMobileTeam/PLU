package com.webmyne.paylabas.userapp.helpers;

import com.webmyne.paylabas.userapp.model.User;

/**
 * Created by Android on 05-12-2014.
 */
public class AppConstants {




    // Base url for the webservice
    public static String BASE_URL = "http://ws-srv-net.in.webmyne.com/Applications/PaylabasWS/";

    public static String USER_LOGIN = BASE_URL + "User.svc/json/UserLogin";

    public static String USER_DETAILS = BASE_URL + "User.svc/json/GetUserDetails/";

    public static String GIFTCODE_LIST = BASE_URL + "GiftCode.svc/json/GiftCodeList/";

    public static String GETCITIES = BASE_URL + "User.svc/json/GetCityList/";

    public static String GETRECEIPIENTS = BASE_URL + "GiftCode.svc/json/GetRecipientList/";

    public static String GENERATE_GC = BASE_URL+"GiftCode.svc/json/GenerateGC";

    public static String USER_REGISTRATION = BASE_URL + "User.svc/json/Registration";

    public static String VERIFY_USER = BASE_URL + "User.svc/json/VerifyUser";

    public static String GETGCDETAIL = BASE_URL + "GiftCode.svc/json/GetGCDetail";

    public static String COMBINE_GC = BASE_URL + "GiftCode.svc/json/CombineGC";

    public static String GET_USER_PROFILE = BASE_URL + "User.svc/json/GetUserProfile/";

    public static String SERVICE_CHARGE = BASE_URL + "GiftCode.svc/json/ServiceCharge/";

    public static String CREDIT_WALLET = BASE_URL + "MoneyTransfer.svc/json/CreditWallet";

   // http://ws-srv-net.in.webmyne.com/Applications/PayLabasWS/MoneyTransfer.svc/json/GetPaymentStatus
    public static String GET_PAYMENT_STATUS = BASE_URL + "MoneyTransfer.svc/json/GetPaymentStatus";







    /*********  FTP IP ***********/
    public static final String ftpPath="192.168.1.4";

    /*********  FTP USERNAME ***********/
    public static final String ftpUsername="androidftp";

    /*********  FTP PASSWORD ***********/
    public static final String ftpPassword="1234567890";

    /*********  FTP image download ***********/
    public static final String fileDownloadPath="http://ws-srv-net.in.webmyne.com/applications/Android/RiteWayServices/Images/";


}


