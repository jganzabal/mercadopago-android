package com.mercadopago.util;

import com.mercadopago.model.PayerCost;
import com.mercadopago.model.PaymentMethod;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by gbringas on 12/05/14.
 */
public class MercadopagoUtil {

    public static Map<Integer, Object> getInstallments(PaymentMethod paymentMethod, Double amount) {
        Map<Integer, Object> installments = new TreeMap<Integer, Object>();

        for (PayerCost p : paymentMethod.getPayerCosts()) {
            if (p.getMinAllowedAmount() < amount && amount < p.getMaxAllowedAmount()) {
                Map<String, Object> installment = new HashMap<String, Object>();
                BigDecimal shareAmount = getShareAmount(amount, p.getInstallmentRate(), p.getInstallments());
                installment.put("installment_rate", p.getInstallmentRate());
                installment.put("share_amount", shareAmount);
                installment.put("total_amount", shareAmount.multiply(new BigDecimal(p.getInstallments())).setScale(2, RoundingMode.HALF_UP));

                installments.put(p.getInstallments(), installment);
            }
        }
        return installments;
    }

    private static BigDecimal getShareAmount(Double amount, Double rate, Integer installments) {
        return new BigDecimal(amount * ((1 + rate / 100) / installments)).setScale(2, RoundingMode.HALF_UP);
    }
}
