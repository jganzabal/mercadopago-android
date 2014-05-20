package com.mercadopago;

import com.mercadopago.model.Token;

import retrofit.Callback;
import retrofit.client.Response;

public abstract class TokenCallback<T extends Token> implements Callback<T> {
    Mercadopago mercadopago;
    public TokenCallback(Mercadopago mp) {
        mercadopago = mp;
    }

    @Override public final void success(T data, Response response) {
        data.setId(data.getId() + "-" + mercadopago.getSessionId());
        success(data);
    }

    public abstract void success(T data);

}
