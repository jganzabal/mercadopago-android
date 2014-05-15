package com.mercadopago.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class PayerCost {

    Integer installments;
    Double installmentRate;
    List<String> labels;
    Double minAllowedAmount;
    Double maxAllowedAmount;

    public Integer getInstallments() {
        return installments;
    }

    public void setInstallments(Integer installments) {
        this.installments = installments;
    }

    public Double getInstallmentRate() {
        return installmentRate;
    }

    public void setInstallmentRate(Double installmentRate) {
        this.installmentRate = installmentRate;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public Double getMinAllowedAmount() {
        return minAllowedAmount;
    }

    public void setMinAllowedAmount(Double minAllowedAmount) {
        this.minAllowedAmount = minAllowedAmount;
    }

    public Double getMaxAllowedAmount() {
        return maxAllowedAmount;
    }

    public void setMaxAllowedAmount(Double maxAllowedAmount) {
        this.maxAllowedAmount = maxAllowedAmount;
    }

    public BigDecimal getShareAmount(Double amount) {
        return new BigDecimal(amount * ((1 + this.installmentRate / 100) / installments)).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotalAmount(Double amount) {
        return new BigDecimal(amount * ((1 + this.installmentRate / 100) / installments)).setScale(2, RoundingMode.HALF_UP).multiply(new BigDecimal(installments));
    }

    @Override
    public String toString() {
        return installments.toString();
    }
}
