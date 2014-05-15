package com.mercadopago.model;

import android.text.TextUtils;
import android.text.format.DateUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Card {

    String cardNumber;
    Integer securityCode;
    Integer cardExpirationMonth;
    Integer cardExpirationYear;
    String cardholderName;
    String docType;
    String docNumber;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Integer getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(Integer securityCode) {
        this.securityCode = securityCode;
    }

    public Integer getCardExpirationMonth() {
        return cardExpirationMonth;
    }

    public void setCardExpirationMonth(Integer cardExpirationMonth) {
        this.cardExpirationMonth = cardExpirationMonth;
    }

    public Integer getCardExpirationYear() {
        return cardExpirationYear;
    }

    public void setCardExpirationYear(Integer cardExpirationYear) {
        this.cardExpirationYear = cardExpirationYear;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    public Card(String cardNumber, Integer cardExpirationMonth, Integer cardExpirationYear,
                Integer securityCode, String cardholderName, String docType, String docNumber) {
        this.cardNumber = normalizeCardNumber(cardNumber);;
        this.cardExpirationMonth = cardExpirationMonth;
        this.cardExpirationYear = normalizeYear(cardExpirationYear);
        this.securityCode = securityCode;
        this.cardholderName = cardholderName;
        this.docType = docType;
        this.docNumber = docNumber;
    }

    public Map<String, Object> getAsMap() {
        Map<String, Object> cardParams = new HashMap<String, Object>();
        Map<String, Object> cardHolder = new HashMap<String, Object>();
        Map<String, Object> document = new HashMap<String, Object>();

        cardParams.put("card_number", cardNumber);
        cardParams.put("security_code", securityCode);
        cardParams.put("expiration_month", cardExpirationMonth);
        cardParams.put("expiration_year", cardExpirationYear);

        document.put("type", docType);
        document.put("number", docNumber);
        cardHolder.put("document", document);
        cardHolder.put("name", cardholderName);

        cardParams.put("cardholder", cardHolder);

        return cardParams;
    }

    private String normalizeCardNumber(String number) {
        if (number == null) {
            return null;
        }
        return number.trim().replaceAll("\\s+|-", "");
    }

    public boolean validateCard() {
        return validateCardNumber() && validateSecurityCode() && validateExpiryDate() && validateDoc() && validateCardholderName();
    }

    public boolean validateCardNumber(){
        return !TextUtils.isEmpty(cardNumber);
    }

    public boolean validateSecurityCode(){
        return securityCode > 0;
    }

    public boolean validateExpiryDate() {
        if (!validateExpMonth()) {
            return false;
        }
        if (!validateExpYear()) {
            return false;
        }
        return !hasMonthPassed(cardExpirationYear, cardExpirationMonth);
    }

    public boolean validateExpMonth() {
        if (cardExpirationMonth == null) {
            return false;
        }
        return (cardExpirationMonth >= 1 && cardExpirationMonth <= 12);
    }

    public boolean validateExpYear() {
        if (cardExpirationYear == null) {
            return false;
        }
        return !hasYearPassed(cardExpirationYear);
    }

    public boolean validateDoc(){
        return validateDocType() && validateDocNumber();
    }

    public boolean validateDocType(){
        return !TextUtils.isEmpty(docType);
    }

    public boolean validateDocNumber(){
        return !TextUtils.isEmpty(docNumber);
    }

    public boolean validateCardholderName(){
        return !TextUtils.isEmpty(cardholderName);
    }

    private static boolean hasYearPassed(int year) {
        int normalized = normalizeYear(year);
        Calendar now = Calendar.getInstance();
        return normalized < now.get(Calendar.YEAR);
    }

    private static boolean hasMonthPassed(int year, int month) {
        Calendar now = Calendar.getInstance();
        return hasYearPassed(year) || normalizeYear(year) == now.get(Calendar.YEAR) && month < (now.get(Calendar.MONTH) + 1);
    }

    private static int normalizeYear(int year)  {
        if (year < 100 && year >= 0) {
            Calendar now = Calendar.getInstance();
            String currentYear = String.valueOf(now.get(Calendar.YEAR));
            String prefix = currentYear.substring(0, currentYear.length() - 2);
            year = Integer.parseInt(String.format(Locale.US, "%s%02d", prefix, year));
        }
        return year;
    }

}