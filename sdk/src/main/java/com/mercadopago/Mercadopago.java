package com.mercadopago;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.mercadopago.model.Card;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.Token;

import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class Mercadopago {
    private static final String MERCADOPAGO_BASE_URL = "https://pagamento.mercadopago.com";
    private static final String BASE_URL = "https://api.mercadolibre.com";
    private final Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).serializeNulls().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();
    private final String defaultPublishableKey;
    public Mercadopago(String publishableKey) {
        this.defaultPublishableKey = publishableKey;
    }

    public void createToken(final Card card, final Callback<Token> callback){
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(MERCADOPAGO_BASE_URL).setConverter(new GsonConverter(gson)).build();
        GatewayService service = restAdapter.create(GatewayService.class);
        service.getToken(defaultPublishableKey, card.getAsMap(), callback);
    }

    public void getPaymentMethodByBin(String bin, Callback<List<PaymentMethod>> callback){
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(BASE_URL).setConverter(new GsonConverter(gson)).build();
        PaymentService service = restAdapter.create(PaymentService.class);
        service.getPaymentMethodByBin(defaultPublishableKey, bin, callback);
    }

    public void getPaymentMethodById(String paymentMethod, Callback<List<PaymentMethod>> callback){
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(BASE_URL).setConverter(new GsonConverter(gson)).build();
        PaymentService service = restAdapter.create(PaymentService.class);
        service.getPaymentMethodById(defaultPublishableKey, paymentMethod, callback);
    }

    public Token createToken(final Card card){
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(MERCADOPAGO_BASE_URL).setConverter(new GsonConverter(gson)).build();
        GatewayService service = restAdapter.create(GatewayService.class);
        return service.getToken(defaultPublishableKey, card.getAsMap());
    }

    public List<PaymentMethod> getPaymentMethodByBin(String bin){
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(BASE_URL).setConverter(new GsonConverter(gson)).build();
        PaymentService service = restAdapter.create(PaymentService.class);
        return service.getPaymentMethodByBin(defaultPublishableKey, bin);
    }

    public List<PaymentMethod> getPaymentMethodById(String paymentMethod){
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(BASE_URL).setConverter(new GsonConverter(gson)).build();
        PaymentService service = restAdapter.create(PaymentService.class);
        return service.getPaymentMethodById(defaultPublishableKey, paymentMethod);
    }

}