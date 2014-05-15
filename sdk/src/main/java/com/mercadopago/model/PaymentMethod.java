package com.mercadopago.model;

import java.util.List;

public class PaymentMethod {

    String id;
    String name;
    String paymentTypeId;
    CardIssuer cardIssuer;
    String siteId;
    String secureThumbnail;
    String thumbnail;
    List<String> labels;
    Integer minAccreditationDays;
    Integer maxAccreditationDays;
    List<PayerCost> payerCosts;
    List<ExceptionByCardIssuer> exceptionsByCardIssuer;
    List<CardConfiguration> cardConfiguration;


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

    public String getPaymentTypeId() {
        return paymentTypeId;
    }

    public void setPaymentTypeId(String paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSecureThumbnail() {
        return secureThumbnail;
    }

    public void setSecureThumbnail(String secureThumbnail) {
        this.secureThumbnail = secureThumbnail;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public Integer getMinAccreditationDays() {
        return minAccreditationDays;
    }

    public void setMinAccreditationDays(Integer minAccreditationDays) {
        this.minAccreditationDays = minAccreditationDays;
    }

    public Integer getMaxAccreditationDays() {
        return maxAccreditationDays;
    }

    public void setMaxAccreditationDays(Integer maxAccreditationDays) {
        this.maxAccreditationDays = maxAccreditationDays;
    }

    public List<PayerCost> getPayerCosts() {
        return payerCosts;
    }

    public void setPayerCosts(List<PayerCost> payerCosts) {
        this.payerCosts = payerCosts;
    }

    public List<ExceptionByCardIssuer> getExceptionsByCardIssuer() {
        return exceptionsByCardIssuer;
    }

    public void setExceptionsByCardIssuer(List<ExceptionByCardIssuer> exceptionsByCardIssuer) {
        this.exceptionsByCardIssuer = exceptionsByCardIssuer;
    }

    public List<CardConfiguration> getCardConfiguration() {
        return cardConfiguration;
    }

    public void setCardConfiguration(List<CardConfiguration> cardConfiguration) {
        this.cardConfiguration = cardConfiguration;
    }

    public CardIssuer getCardIssuer() {
        return cardIssuer;
    }

    public void setCardIssuer(CardIssuer cardIssuer) {
        this.cardIssuer = cardIssuer;
    }

    @Override
    public String toString() {
        return name;
    }
}
