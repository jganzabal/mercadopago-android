package com.mercadopago.util;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import retrofit.RetrofitError;
import retrofit.mime.TypedInput;

/**
 * Created by ngiagnoni on 10/4/13.
 */
public class RetrofitUtil {

    private static String getErrorBody(TypedInput error) {
        try {
            InputStream stream = error.in();
            byte[] bytes = new byte[(int)error.length()];
            stream.read(bytes);

            return new String(bytes);
        } catch (IOException ignored) {}

        return null;
    }

    public static JSONObject parseErrorBody(RetrofitError e) {
        try {
            if (e.getResponse() != null) {
                return new JSONObject(getErrorBody(e.getResponse().getBody()));
            }
        } catch (Exception ignored) { }
        return null;
    }

}