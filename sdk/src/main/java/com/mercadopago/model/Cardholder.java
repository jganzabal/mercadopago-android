package com.mercadopago.model;

/**
 * Created by gbringas on 01/08/14.
 */
public class Cardholder {
    String name;
    Identification identification;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Identification getIdentification() {
        return identification;
    }

    public void setIdentification(Identification identification) {
        this.identification = identification;
    }

}
