package com.mercadopago.model;

import java.util.List;

/**
 * Created by seba on 10/25/13.
 */
public class CardIssuer {
    String id;
    String name;
    List<String> labels;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    @Override
    public String toString() {
        return name;
    }
}
