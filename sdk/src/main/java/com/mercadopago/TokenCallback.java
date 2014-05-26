package com.mercadopago;

import com.mercadopago.model.Token;

import retrofit.Callback;
import retrofit.client.Response;

public abstract class TokenCallback<T extends Token> implements Callback<T> {
    public abstract void success(T data);
}
