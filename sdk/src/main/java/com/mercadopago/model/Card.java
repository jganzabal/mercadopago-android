package com.mercadopago.model;

import android.content.Context;
import android.text.TextUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Card {
    private final static Calendar now = Calendar.getInstance();
    public static final int MIN_LENGTH_NUMBER = 10;
    public static final int MAX_LENGTH_NUMBER = 19;

    String cardNumber;
    String securityCode;
    Integer expirationMonth;
    Integer expirationYear;
    Cardholder cardholder;
    Device device;

    public Device getDevice() {
        return device;
    }

    public void setDevice(Context context) {
        this.device = new Device(context);
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
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

    public Card(String cardNumber, Integer expirationMonth, Integer expirationYear,
                String securityCode, String cardholderName, String docType, String docNumber) {
        this.cardNumber = normalizeCardNumber(cardNumber);;
        this.expirationMonth = expirationMonth;
        this.expirationYear = normalizeYear(expirationYear);
        this.securityCode = securityCode;
        this.cardholder = new Cardholder();
        this.cardholder.setName(cardholderName);
        this.cardholder.identification = new Identification();
        this.cardholder.identification.setNumber(docNumber);
        this.cardholder.identification.setType(docType);
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
        return !TextUtils.isEmpty(cardNumber) && (cardNumber.length() > MIN_LENGTH_NUMBER) && (cardNumber.length() < MAX_LENGTH_NUMBER);
    }

    public boolean validateSecurityCode(){
        return securityCode == null || (!TextUtils.isEmpty(securityCode) && securityCode.length() >= 3 && securityCode.length() <= 4);
    }

    public boolean validateExpiryDate() {
        if (!validateExpMonth()) {
            return false;
        }
        if (!validateExpYear()) {
            return false;
        }
        return !hasMonthPassed(expirationYear, expirationMonth);
    }

    public boolean validateExpMonth() {
        if (expirationMonth == null) {
            return false;
        }
        return (expirationMonth >= 1 && expirationMonth <= 12);
    }

    public boolean validateExpYear() {
        if (expirationYear == null) {
            return false;
        }
        return !hasYearPassed(expirationYear);
    }

    public boolean validateDoc(){
        return validateDocType() && validateDocNumber();
    }

    public boolean validateDocType(){
        return !TextUtils.isEmpty(cardholder.getIdentification().getType());
    }

    public boolean validateDocNumber(){
        return !TextUtils.isEmpty(cardholder.getIdentification().getNumber());
    }

    public boolean validateCardholderName(){
        return !TextUtils.isEmpty(cardholder.getName());
    }

    private static boolean hasYearPassed(int year) {
        int normalized = normalizeYear(year);
        return normalized < now.get(Calendar.YEAR);
    }

    private static boolean hasMonthPassed(int year, int month) {
        return hasYearPassed(year) || normalizeYear(year) == now.get(Calendar.YEAR) && month < (now.get(Calendar.MONTH) + 1);
    }

    private static int normalizeYear(int year)  {
        if (year < 100 && year >= 0) {
            String currentYear = String.valueOf(now.get(Calendar.YEAR));
            String prefix = currentYear.substring(0, currentYear.length() - 2);
            year = Integer.parseInt(String.format(Locale.US, "%s%02d", prefix, year));
        }
        return year;
    }

}