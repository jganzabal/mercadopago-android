package com.mercadopago.model;

import android.content.Context;

/**
 * Created by gbringas on 17/06/14.
 */
public class Device {

    Fingerprint fingerprint;

    public Device(Context context) {
        this.fingerprint = new Fingerprint(context);
    }
}
