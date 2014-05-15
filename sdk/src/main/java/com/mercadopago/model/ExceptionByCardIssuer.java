package com.mercadopago.model;

import java.util.List;

/**
 * Created by seba on 10/25/13.
 */
public class ExceptionByCardIssuer {

    CardIssuer cardIssuer;
    List<String> labels;
    String thumbnail;
    String secureThumbnail;
    Double totalFinancialCost;
    List<Integer> acceptedBins;
    List<PayerCost> payerCosts;

    public CardIssuer getCardIssuer() {
        return cardIssuer;
    }

    public void setCardIssuer(CardIssuer cardIssuer) {
        this.cardIssuer = cardIssuer;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
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

    public Double getTotalFinancialCost() {
        return totalFinancialCost;
    }

    public void setTotalFinancialCost(Double totalFinancialCost) {
        this.totalFinancialCost = totalFinancialCost;
    }

    public List<Integer> getAcceptedBins() {
        return acceptedBins;
    }

    public void setAcceptedBins(List<Integer> acceptedBins) {
        this.acceptedBins = acceptedBins;
    }

    public List<PayerCost> getPayerCosts() {
        return payerCosts;
    }

    public void setPayerCosts(List<PayerCost> payerCosts) {
        this.payerCosts = payerCosts;
    }
}
