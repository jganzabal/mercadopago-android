package com.mercadopago.model;

import java.util.Date;

public class Token {

    String id;
    String publicKey;
    String cardId;
    String luhnValidation;
    String status;
    String usedDate;
    Integer cardNumberLength;
    Date creationDate;
    String truncCardNumber;
    Integer securityCodeLength;
    Integer expirationMonth;
    Integer expirationYear;
    Date lastModifiedDate;
    Date dueDate;
    //TODO: cardholder

    public Integer getSecurityCodeLength() {
        return securityCodeLength;
    }

    public void setSecurityCodeLength(Integer securityCodeLength) {
        this.securityCodeLength = securityCodeLength;
    }

    public Integer getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(Integer expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    public Integer getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(Integer expirationYear) {
        this.expirationYear = expirationYear;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getLuhnValidation() {
        return luhnValidation;
    }

    public void setLuhnValidation(String luhnValidation) {
        this.luhnValidation = luhnValidation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsedDate() {
        return usedDate;
    }

    public void setUsedDate(String usedDate) {
        this.usedDate = usedDate;
    }

    public Integer getCardNumberLength() {
        return cardNumberLength;
    }

    public void setCardNumberLength(Integer cardNumberLength) {
        this.cardNumberLength = cardNumberLength;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getTruncCardNumber() {
        return truncCardNumber;
    }

    public void setTruncCardNumber(String truncCardNumber) {
        this.truncCardNumber = truncCardNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }


}