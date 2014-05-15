package com.mercadopago;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.mercadopago.model.Card;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.Token;
import com.mercadopago.util.MercadopagoUtil;
import com.mercadopago.util.RetrofitUtil;

import junit.framework.Assert;

import org.json.JSONException;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MercadopagoTest  extends InstrumentationTestCase {

    public void testToken() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        Mercadopago mp = new Mercadopago("841d020b-1077-4742-ad55-7888a0f5aefa");
        Card card = new Card("4170068810108020",12,2020,123,"adf","DNI","12123123");

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
                    Assert.fail(String.valueOf(RetrofitUtil.parseErrorBody(error)));

                signal.countDown();
            }
        };
        mp.getPaymentMethodByBin("444444", cb);
        signal.await();
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

    public void testTokenSync() throws Exception {
        Mercadopago mp = new Mercadopago("841d020b-1077-4742-ad55-7888a0f5aefa");
        Card card = new Card("4170068810108020",12,2020,123,"adf","DNI","12123123");
        assertNotNull(mp.createToken(card).getId());
    }

    public void testPaymentMethodByBinSync() throws Exception{
        Mercadopago mp = new Mercadopago("841d020b-1077-4742-ad55-7888a0f5aefa");
        assertTrue(mp.getPaymentMethodByBin("444444").get(0).getName().contains("Visa"));
    }

    public void testPaymentMethodByIdSync() throws Exception{
        Mercadopago mp = new Mercadopago("841d020b-1077-4742-ad55-7888a0f5aefa");
        assertTrue(mp.getPaymentMethodById("visa").get(0).getName().contains("Visa"));
    }

    public void testInstallments(){
        Mercadopago mp = new Mercadopago("841d020b-1077-4742-ad55-7888a0f5aefa");
        PaymentMethod paymentMethod = mp.getPaymentMethodById("visa").get(0);

        Log.wtf("----", MercadopagoUtil.getInstallments(paymentMethod, new Double("100")).toString());

    }
}