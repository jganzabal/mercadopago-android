package com.mercadopago.model;

import java.util.List;

/**
 * Created by seba on 10/25/13.
 */
public class CardConfiguration {

    String binCardPattern;
    String binCardExclusionPattern;
    Integer cardNumberLength;
    Integer securityCodeLength;
    String luhnAlgorithm;
    List<String> additionalInfoNeeded;
    String exceptionsByCardValidations;

    public String getExceptionsByCardValidations() {
        return exceptionsByCardValidations;
    }

    public void setExceptionsByCardValidations(String exceptionsByCardValidations) {
        this.exceptionsByCardValidations = exceptionsByCardValidations;
    }

    public String getBinCardPattern() {
        return binCardPattern;
    }

    public void setBinCardPattern(String binCardPattern) {
        this.binCardPattern = binCardPattern;
    }

    public String getBinCardExclusionPattern() {
        return binCardExclusionPattern;
    }

    public void setBinCardExclusionPattern(String binCardExclusionPattern) {
        this.binCardExclusionPattern = binCardExclusionPattern;
    }

    public Integer getCardNumberLength() {
        return cardNumberLength;
    }

    public void setCardNumberLength(Integer cardNumberLength) {
        this.cardNumberLength = cardNumberLength;
    }

    public Integer getSecurityCodeLength() {
        return securityCodeLength;
    }

    public void setSecurityCodeLength(Integer securityCodeLength) {
        this.securityCodeLength = securityCodeLength;
    }

    public String getLuhnAlgorithm() {
        return luhnAlgorithm;
    }

    public void setLuhnAlgorithm(String luhnAlgorithm) {
        this.luhnAlgorithm = luhnAlgorithm;
    }

    public List<String> getAdditionalInfoNeeded() {
        return additionalInfoNeeded;
    }

    public void setAdditionalInfoNeeded(List<String> additionalInfoNeeded) {
        this.additionalInfoNeeded = additionalInfoNeeded;
    }
}
