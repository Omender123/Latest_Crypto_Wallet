package com.crypto.croytowallet.database;

import com.crypto.croytowallet.Extra_Class.ApiResponse.ActiveDeviceResponse;
import com.crypto.croytowallet.Extra_Class.ApiResponse.CurrencyDetailsModelResponse;
import com.crypto.croytowallet.Extra_Class.ApiResponse.FilterBody;
import com.crypto.croytowallet.Extra_Class.ApiResponse.GetNewCoinRespinse;
import com.crypto.croytowallet.Extra_Class.ApiResponse.LoginResponse;
import com.crypto.croytowallet.Extra_Class.ApiResponse.PublicKeyResponse;
import com.crypto.croytowallet.Extra_Class.ApiResponse.PearToPearResponse;
import com.crypto.croytowallet.Extra_Class.ApiResponse.ReceviedCoinHistoryResponse;
import com.crypto.croytowallet.Extra_Class.ApiResponse.ResponseBankDetails;
import com.crypto.croytowallet.Extra_Class.ApiResponse.RewardHistoryResponse;
import com.crypto.croytowallet.Extra_Class.ApiResponse.SendAddAmountRequestResponse;
import com.crypto.croytowallet.Extra_Class.ApiResponse.SendCoinHistoryResponse;
import com.crypto.croytowallet.Extra_Class.ApiResponse.TCResponse;
import com.crypto.croytowallet.Extra_Class.ApiResponse.TopUp_HistoryResponse;
import com.crypto.croytowallet.Extra_Class.ApiResponse.TransactionHistoryResponse;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @FormUrlEncoded
    @POST("user/register")
    Call<ResponseBody> register(
            @Field("name") String name,
            @Field("email") String email,
            @Field("username") String username,
            @Field("password") String password,
            @Field("referalCode") String referalCode,
            @Field("phone") String phone);

    @FormUrlEncoded
    @POST("user/login")
    Call<LoginResponse> Login(
            @Field("username") String username,
            @Field("password") String password,
            @Field("otp") String otp,
            @Field("location") String location,
            @Field("os") String OsName,
            @Field("ip") String IpAddress
            //  @Field("fcmToken") String FcmToken
    );

    @FormUrlEncoded
    @POST("user/transactionPin")
    Call<ResponseBody> Transaction(

            @Field("mnemonic") String mnemonic,
            @Field("transactionPin") String TransactionPin,
            @Field("username") String username);


    @FormUrlEncoded
    @POST("user/sendOTP")
    Call<ResponseBody> sendOtp(
            @Field("username") String username
    );


    @FormUrlEncoded
    @POST("user/otpExpiry")
    Call<ResponseBody> expireOtp(
            @Field("username") String username
    );

    @FormUrlEncoded
    @POST("user/emailVerify")
    Call<ResponseBody> OtpVerify(
            @Field("username") String username,
            @Field("otp") String otp

    );

    @FormUrlEncoded
    @POST("user/checkGoogleAuthentication")
    Call<ResponseBody> GoogleAuthVerify(
            @Field("username") String username,
            @Field("token") String token

    );

    @FormUrlEncoded
    @PUT("user/forgot-password")
    Call<ResponseBody> ChanagePassword(
            @Field("username") String username,
            @Field("mnemonic") String mnemonic,
            @Field("password") String password,
            @Field("otp") String Otp
    );

    @FormUrlEncoded
    @POST("transaction/peerToPeer")
    Call<PearToPearResponse> P2P(
            @Header("Authorization") String token,
            @Field("amount") String amount,
            @Field("transactionPin") String transactionPin,
            @Field("toUserId") String userId,
            @Field("toUsername") String username
    );

    @FormUrlEncoded
    @POST("transaction/transfer")
    Call<ResponseBody> coinTransfer(
            @Header("Authorization") String Authtoken,
            @Field("cryptoCurrency") String CoinType,
            @Field("name") String Symbols,
            @Field("deliveryRate") String deliveryRate,
            @Field("token") String token,
            @Field("otp") String otp,
            @Field("cryptoAmt") String amount,
            @Field("transactionPin") String transactionPin,
            /*  @Field("userAddress") String userAddress,*/
            @Field("receiverAddress") String receiverAddress
    );

   /* @FormUrlEncoded
    @POST("user/balance")
    Call<ResponseBody> Balance(
            @Header("Authorization") String Authtoken,
            @Field("type") String type
    );*/

    @FormUrlEncoded
    @POST("user/balance")
    Call<ResponseBody> Balance(
            @Header("Authorization") String Authtoken,
            @Field("type") String type,
            @Field("name") String name,
            @Field("currency") String currency
    );

    @FormUrlEncoded
    @POST("transaction/swapCurrency")
    Call<ResponseBody> IMT_SWAP(
            @Header("Authorization") String Authtoken,
            @Field("name") String sendData,
            @Field("sendPrice") String sendPrice,
            @Field("sendCurrency") String CoinToken,
            @Field("receiveCurrency") String receiveCurrency,
            @Field("deliveryRate") int rate,
            @Field("sendAmount") String sendAmount,
            @Field("transactionPin") String transactionPin
    );

    @FormUrlEncoded
    @POST("user/appCrashed/transactionPin")
    Call<ResponseBody> setTransactionPin(
            @Field("username") String username,
            @Field("transactionPin") String transactionPin,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("user/removeCurrentlyActiveDevices")
    Call<ResponseBody> remove_JWT(
            @Header("Authorization") String Authtoken,
            @Field("username") String username,
            @Field("jwt") String jwt
    );

    @FormUrlEncoded
    @POST("user/sendEmailAfterLogin")
    Call<ResponseBody> Send_Email(
            @Field("userId") String userId
    );

    @FormUrlEncoded
    @POST("user/viewMnemonic")
    Call<ResponseBody> get_Mnenonic(
            @Header("Authorization") String Authtoken,
            @Field("userId") String userId,
            @Field("transactionPin") String transactionPin,
            @Field("otp") String otp
    );

    @FormUrlEncoded
    @POST("transaction/send/user")
    Call<SendCoinHistoryResponse> get_SendHistory(
            @Header("Authorization") String Authtoken,
            @Field("crypto") String type
    );

    @FormUrlEncoded
    @POST("transaction/receive/user")
    Call<List<ReceviedCoinHistoryResponse>> get_ReceivedHistory(
            @Header("Authorization") String Authtoken,
            @Field("crypto") String type
    );

    @GET("banner")
    Call<ResponseBody> getStory(
            @Header("Authorization") String token
    );

    // @FormUrlEncoded
    // @Multipart
    @POST("chat/chatToAdmin")
    Call<ResponseBody> SendMessageApi(@Header("Authorization") String auth_token,
                                      @Body JsonObject object);


    //  @FormUrlEncoded
    @POST("chat/insertUser")
    Call<ResponseBody> ChatActive(
            @Header("Authorization") String Authtoken
    );

    @POST("chat/newChatDocument")
    Call<ResponseBody> newChatSeason(
            @Header("Authorization") String Authtoken
    );

    @POST("chat/ejectUser")
    Call<ResponseBody> Chat_Un_Active(
            @Header("Authorization") String Authtoken
    );

    @GET("chat/getMessages")
    Call<ResponseBody> getChat(
            @Header("Authorization") String Authorization
    );

    @DELETE("chat/deletAllMessage")
    Call<ResponseBody> deleteAllMessage(@Header("Authorization") String Authorization);

    @DELETE("api/chat/removeMessage/{id}")
    Call<ResponseBody> deleteMessage(@Header("Authorization") String Authorization, @Path("id") String messageId);


    @FormUrlEncoded
    @POST("user/threatMode")
    Call<ResponseBody> Threat_mode_Api(
            @Header("Authorization") String Authtoken,
            @Field("password") String password,
            @Field("otp") String otp,
            @Field("transactionPin") String transactionPin
    );

    @FormUrlEncoded
    @POST("user/unlockAccount")
    Call<ResponseBody> Unlock_Account_Api(
            @Field("username") String username,
            @Field("transactionPin") String transactionPin,
            @Field("otp") String otp,
            @Field("password") String new_password,
            @Field("mnemonic") String Mnemonic

    );

    @GET("user/countryCodeList")
    Call<ResponseBody> getCountryCode();

    @GET("chat/oldChats")
    Call<ResponseBody> getChatHistory(
            @Header("Authorization") String Authorization
    );

    @FormUrlEncoded
    @POST("FAQ/getFAQ")
    Call<ResponseBody> GetFAQ(
            @Field("subject") String subject
    );


    @GET("chat/chatDocument/{id}")
    Call<ResponseBody> getALLChat(
            @Header("Authorization") String Authorization,
            @Path("id") String id
    );


    @GET("coins/{id}/market_chart")
    Call<ResponseBody> getGraphData1(
            @Path("id") String id,
            @Query("vs_currency") String currency,
            @Query("days") String days,
            @Query("interval") String interval
    );


    @GET("notification/user/allNotification")
    Call<ResponseBody> getNotification(
            @Header("Authorization") String Authorization
    );

    @GET("histohour")
    Call<ResponseBody> ImtGraph1d(
            @Query("fsym") String coinName,
            @Query("tsym") String currency,
            @Query("limit") int hour
    );



    @FormUrlEncoded
    @POST("currency/newIMT")
    Call<ResponseBody> getIMTDetails(
            @Header("Authorization") String Authtoken,
            @Field("currency") String Currency
    );

    @GET("all")
    Call<ResponseBody> getCountryname();

    @FormUrlEncoded
    @POST("currency/countryState")
    Call<ResponseBody> GET_State(
            @Field("code") String code
    );


    @PUT("user")
    Call<ResponseBody> SetAddress(
            @Header("Authorization") String token,
            @Body JsonObject object
    );

    @Multipart
    @POST("user/kyc")
    Call<ResponseBody> Kyc(@Header("Authorization") String authorization,
                           @Part MultipartBody.Part image,
                           @Part("number") RequestBody doc_no,
                           @Part("type") RequestBody doc_type
    );

    @FormUrlEncoded
    @POST("user/twoFaEnableOrDisable")
    Call<ResponseBody> TwoFA(
            @Field("userId") String userId,
            @Field("google2fa") Boolean google2fa,
            @Field("email2fa") Boolean email2fa
    );


    @FormUrlEncoded
    @POST("user/google2fa")
    Call<ResponseBody> Google_Obtain(
            @Header("Authorization") String Authtoken,
            @Field("password") String password,
            @Field("otp") String otp,
            @Field("transactionPin") String transactionPin
    );

    @FormUrlEncoded
    @POST("user/resetTransactionPin")
    Call<ResponseBody> resetTransactionPin(
            @Header("Authorization") String Authtoken,
            @Field("otp") String otp,
            @Field("transactionPin") String OldtransactionPin,
            @Field("password") String password,
            @Field("newtransactionPin") String NewtransactionPin
    );

    @FormUrlEncoded
    @POST("user/forgetTransactionPin")
    Call<ResponseBody> forgetTransactionPin(
            @Header("Authorization") String Authtoken,
            @Field("otp") String otp,
            @Field("mnemonic") String mnemonic,
            @Field("password") String password,
            @Field("newtransactionPin") String NewtransactionPin
    );

    @FormUrlEncoded
    @POST("user/removeCurrentlyActiveDevices")
    Call<ResponseBody> LogOut(
            @Header("Authorization") String Authtoken,
            @Field("username") String username,
            @Field("jwt") String jwtToken
    );

    @PUT("user/emailCorrection")
    Call<ResponseBody> EmailCorrection(
            @Header("Authorization") String Authtoken,
            @Body JsonObject object
    );

    @PUT("user/updateFcmToken")
    Call<ResponseBody> PutDevice(
            @Header("Authorization") String Authtoken,
            @Body JsonObject object
    );

    @GET("coins/markets")
    Call<ResponseBody> getAllCoin(
            @Query("ids") String coinId,
            @Query("vs_currency") String currency

    );

    @GET("EC")
    Call<ResponseBody> getAllCoinDataBase(
            @Header("Authorization") String Authtoken
            );

    @GET("bankD")
    Call<ResponseBankDetails> getBankDetails(
            @Header("Authorization") String Authtoken
    );

    @GET("user/allCurrencyData")
    Call<ResponseBody> getAllCurrency(
            @Header("Authorization") String Authtoken
    );

    @FormUrlEncoded
    @POST("user/cryptoKey")
    Call<PublicKeyResponse>GetPublicKey(
            @Header("Authorization") String Authtoken,
            @Field("crypto") String CoinId
    );

    @Multipart
    @POST("pending/pendingtransfer")
    Call<SendAddAmountRequestResponse> SendAddAmountRequest(
            @Header("Authorization") String authorization,
            @Part("accountName") RequestBody Bankname,
            @Part("accountNo") RequestBody Acc_no,
            @Part("customerName") RequestBody Holder_name,
            @Part("transactionId") RequestBody TransId,
            @Part("amount") RequestBody Amount,
            @Part("currency") RequestBody Currency,
            @Part("utility") RequestBody utility,
            @Part MultipartBody.Part image

           );

    @FormUrlEncoded
    @POST("EC/editEC")
    Call<ResponseBody>EditCoin(
            @Header("Authorization") String Authtoken,
            @Field("crypto") String symbol,
            @Field("enabled") String  aBoolean

            );

    @GET("EC/modifyEC")
    Call<ResponseBody>UpdateCoin(
            @Header("Authorization") String Authtoken
    );

    @GET("currency/getNewCurrency")
    Call<ResponseBody> getNewCoin(
            @Header("Authorization") String Authtoken
    );

    @GET("coins/markets")
    Call<List<GetNewCoinRespinse>> getNewCoin(
            @Query("ids") String coinId,
            @Query("vs_currency") String currency
    );

    @FormUrlEncoded
    @POST("currency/addNewCurrency")
    Call<ResponseBody>AddCoinInProfile(
            @Header("Authorization") String Authtoken,
            @Field("name") String CoinName,
            @Field("enabled") Boolean  aBoolean,
            @Field("symbol") String symbol,
            @Field("image") String Image
     );
    @FormUrlEncoded
    @POST("user/findCurrentlyActiveDevices")
    Call<ActiveDeviceResponse>ActiveDevice(
            @Field("username") String username
            );

    @FormUrlEncoded
    @POST(" ERC20/getToken")
    Call<ResponseBody>getToken(
            @Header("Authorization") String Authtoken,
            @Field("name") String coinSymbols
    );
    @POST("transaction/allPeerTransactiones1")
    Call<TransactionHistoryResponse>GetAllTransactionHistory(
            @Header("Authorization") String Authtoken
    );
     @POST("transaction/allPeerTransactiones2")
    Call<TransactionHistoryResponse>GetAllTransactionHistory1(
            @Header("Authorization") String Authtoken,
            @Body FilterBody filterBody
            );

    @FormUrlEncoded
    @POST("transaction/swapRewards")
    Call<ResponseBody>SwapRewards(
            @Header("Authorization") String Authtoken,
            @Field("sendAmount") String amount,
            @Field("transactionPin") String transactionPin
    );

    @GET("user/calculateReward")
    Call<ResponseBody>GetRewards(
            @Header("Authorization") String Authtoken
    );

    @GET("transaction/rewardHistory")
    Call<RewardHistoryResponse>GetHistoryRewards(
            @Header("Authorization") String Authtoken
    );

    @GET("TC")
    Call<TCResponse>getTCs(
            @Header("Authorization") String Authtoken
    );

    @POST("pending/userPendingTrans")
    Call<List<TopUp_HistoryResponse>>getRewardHistory(
            @Header("Authorization") String Authtoken,
            @Body FilterBody filterBody
    );

    @FormUrlEncoded
    @POST("about/CurrencyDetails")
    Call<CurrencyDetailsModelResponse>getCurrencyDetails(
            @Field("currencyName") String symbols
    );
}
