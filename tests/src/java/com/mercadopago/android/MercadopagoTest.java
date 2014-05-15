package com.mercadopago.android;

import android.test.InstrumentationTestCase;
import android.util.Log;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import com.mercadopago.android.model.Card;
import com.mercadopago.android.model.PaymentMethod;
import com.mercadopago.android.model.Token;
import com.mercadopago.android.util.RetrofitUtil;

import junit.framework.Assert;

import org.json.JSONException;

import java.util.List;
import java.util.concurrent.CountDownLatch;


public class MercadopagoTest  extends InstrumentationTestCase {

    public void testToken() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        Mercadopago mp = new Mercadopago("841d020b-1077-4742-ad55-7888a0f5aefa");
        Card card = new Card("4170068810108020",12,2020,123,"adf","DNI","12123123","credit_card");

        Callback callback = new Callback<Token>() {
            @Override
            public void success(Token o, Response response) {
                signal.countDown();
            }
            @Override
            public void failure(RetrofitError error) {
                try {
                    Assert.fail(String.valueOf(RetrofitUtil.parseErrorBody(error).getString("cause")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                signal.countDown();
            }
        };
        mp.createToken(card, callback);
        signal.await();
    }

    public void testPaymentMethodByBin() throws Exception{
        final CountDownLatch signal = new CountDownLatch(1);
        Mercadopago mp = new Mercadopago("841d020b-1077-4742-ad55-7888a0f5aefa");
        Callback cb = new Callback<List<PaymentMethod>>() {
            @Override
            public void success(List<PaymentMethod> o, Response response) {
                signal.countDown();
            }
            @Override
            public void failure(RetrofitError error) {
                try {
                    Assert.fail(String.valueOf(RetrofitUtil.parseErrorBody(error).getString("cause")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                signal.countDown();
            }
        };
        mp.getPaymentMethodByBin("444444", cb);
        signal.await();
    }

    public void testPaymentMethodByBinSync() throws Exception{
        Mercadopago mp = new Mercadopago("841d020b-1077-4742-ad55-7888a0f5aefa");
        assertTrue(mp.getPaymentMethodByBin("444444").get(0).getName().contains("Visa"));
    }

    public void testPaymentMethodById() throws Exception{
        final CountDownLatch signal = new CountDownLatch(1);
        Mercadopago mp = new Mercadopago("841d020b-1077-4742-ad55-7888a0f5aefa");
        Callback cb = new Callback<List<PaymentMethod>>() {
            @Override
            public void success(List<PaymentMethod> o, Response response) {
                signal.countDown();
            }
            @Override
            public void failure(RetrofitError error) {
                try {
                    Assert.fail(String.valueOf(RetrofitUtil.parseErrorBody(error).getString("cause")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                signal.countDown();
            }
        };
        mp.getPaymentMethodById("visa", cb);
        signal.await();
    }
}