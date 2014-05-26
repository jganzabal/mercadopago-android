package com.mercadopago;

import com.mercadopago.model.PaymentMethod;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by gbringas on 06/05/14.
 */
public interface PaymentService {

    @GET("/checkout/custom/payment_methods/search")
    List<PaymentMethod> getPaymentMethodByBin(@Query("public_key") String public_key, @Query("bin") String bin);

    @GET("/checkout/custom/payment_methods/search")
    void getPaymentMethodByBin(@Query("public_key") String public_key, @Query("bin") String bin, Callback<List<PaymentMethod>> callback);

    @GET("/checkout/custom/payment_methods/search")
    void getPaymentMethodById(@Query("public_key") String public_key, @Query("payment_method") String paymentMethod, Callback<List<PaymentMethod>> callback);

    @GET("/checkout/custom/payment_methods/search")
    List<PaymentMethod> getPaymentMethodById(@Query("public_key") String public_key, @Query("payment_method") String paymentMethod);

    @GET("/contacts?access_token=APP_USR-1311377052931992-052015-f96957603815fe2f3bbf7f4d4e2f2435__M_K__-84696660")
    String getContacts();

}