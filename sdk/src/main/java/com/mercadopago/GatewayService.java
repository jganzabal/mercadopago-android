package com.mercadopago;

import com.mercadopago.model.Card;
import com.mercadopago.model.Token;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by gbringas on 06/05/14.
 */
public interface GatewayService {
    @POST("/card_tokens")
    void getToken(@Query("public_key") String public_key, @Body Card card, Callback<Token> callback);

    @POST("/card_tokens")
    Token getToken(@Query("public_key") String public_key, @Body Card card);
}