package com.mercadopago;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mercadopago.model.Card;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.Token;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class Mercadopago {
    private static final String MERCADOPAGO_BASE_URL = "https://pagamento.mercadopago.com";
    private static final String BASE_URL = "https://api.mercadolibre.com";
    private final String defaultPublishableKey;
    private final Context mContext;
    private final Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).serializeNulls().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();
    private final RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(MERCADOPAGO_BASE_URL).setLogLevel(RestAdapter.LogLevel.FULL).setConverter(new GsonConverter(gson)).build();
    private final RestAdapter restAdapterApi = new RestAdapter.Builder().setEndpoint(BASE_URL).setLogLevel(RestAdapter.LogLevel.FULL).setConverter(new GsonConverter(gson)).build();

    public Mercadopago(String publishableKey, Context context) {
        defaultPublishableKey = publishableKey;
        mContext = context;
    }

    public void createToken(final Card card, final Callback callback){
        card.setDevice(mContext);
        GatewayService service = restAdapter.create(GatewayService.class);
        service.getToken(defaultPublishableKey, card, callback);
    }

    public void getPaymentMethodByBin(String bin, Callback<List<PaymentMethod>> callback){
        PaymentService service = restAdapterApi.create(PaymentService.class);
        service.getPaymentMethodByBin(defaultPublishableKey, bin, callback);
    }

    public void getPaymentMethodById(String paymentMethod, Callback<List<PaymentMethod>> callback){
        PaymentService service = restAdapterApi.create(PaymentService.class);
        service.getPaymentMethodById(defaultPublishableKey, paymentMethod, callback);
    }

    public Token createToken(final Card card){
        GatewayService service = restAdapter.create(GatewayService.class);
        Token token = service.getToken(defaultPublishableKey, card);
        return token;
    }

    public List<PaymentMethod> getPaymentMethodByBin(String bin){
        PaymentService service = restAdapterApi.create(PaymentService.class);
        return service.getPaymentMethodByBin(defaultPublishableKey, bin);
    }

    public List<PaymentMethod> getPaymentMethodById(String paymentMethod){
        PaymentService service = restAdapterApi.create(PaymentService.class);
        return service.getPaymentMethodById(defaultPublishableKey, paymentMethod);
    }
}