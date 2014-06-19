package com.mercadopago.util;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.mercadopago.model.PayerCost;

import java.util.List;

public class InstallmentAdapter extends BaseAdapter {

    private List<PayerCost> mData;
    private static LayoutInflater mInflater = null;
    private Double mAmount;

    public InstallmentAdapter(Activity activity,  List<PayerCost> data, Double amount) {
        mData = data;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mAmount = amount;
    }

    public int getCount() {
        return mData.size();
    }

    public Object getItem(int position) {
        return mData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi=convertView;
        if(convertView==null)
            vi = mInflater.inflate(android.R.layout.simple_list_item_1, null);

        TextView label = (TextView)vi.findViewById(android.R.id.text1); // label

        PayerCost payerCost = mData.get(position);

        label.setText(payerCost.getInstallments() + " cuotas de " +  payerCost.getShareAmount(mAmount).toString() + " (" + payerCost.getTotalAmount(mAmount).toString() + ")");
        return vi;
    }



}

