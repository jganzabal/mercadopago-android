package com.mercadopago.example;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mercadopago.Mercadopago;
import com.mercadopago.model.Card;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.Token;
import com.mercadopago.util.InstallmentAdapter;
import com.mercadopago.util.RetrofitUtil;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ExampleActivity extends Activity {

    private EditText cardNumber;
    private EditText month;
    private EditText year;
    private EditText securityCode;
    private EditText name;
    private EditText docType;
    private EditText docNumber;
    private Mercadopago mp;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        setInputs();
        //Init mercadopago object with public key
        mp = new Mercadopago("841d020b-1077-4742-ad55-7888a0f5aefa", ExampleActivity.this);

        //Get payment methods and show installments spinner
        handleInstallments();
    }

    public void submitForm(View view){
        //Create card object with card data
        Card card = new Card(getCardNumber(), getMonth(), getYear(), getSecurityCode(), getName(), getDocType(), getDocNumber());

        //Callback handler
        Callback callback = new Callback<Token>() {
            @Override
            public void success(Token o, Response response) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), o.getId(), Toast.LENGTH_LONG).show();
            }
            @Override
            public void failure(RetrofitError error) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), RetrofitUtil.parseErrorBody(error).toString(), Toast.LENGTH_LONG).show();
            }
        };

        //Check valid card data
        if(card.validateCard()) {
            //Send card data to get token id
            dialog = ProgressDialog.show(ExampleActivity.this, "", "Loading. Please wait...", true);
            mp.createToken(card, callback);
        }else{
            Toast.makeText(getApplicationContext(), "Invalid data", Toast.LENGTH_LONG).show();
        }
    }

    void handleInstallments(){
        cardNumber.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 4) {
                    cardNumber.append(" ");
                }

                    if(s.length() == 6) {
                    Callback cb = new Callback<List<PaymentMethod>>() {
                        @Override
                        public void success(List<PaymentMethod> o, Response response) {
                            InstallmentAdapter adapter = new InstallmentAdapter(ExampleActivity.this, o.get(0).getPayerCosts(), new Double("100"));
                            ((Spinner)findViewById(R.id.spinner)).setAdapter(adapter);
                        }
                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(getApplicationContext(), RetrofitUtil.parseErrorBody(error).toString(), Toast.LENGTH_LONG).show();
                        }
                    };
                    mp.getPaymentMethodByBin(getCardNumber().substring(0,6), cb);
                }
            }
        });
    }

    String getCardNumber() { return this.cardNumber.getText().toString();}

    Integer getMonth(){ return getInteger(this.month);}

    Integer getYear(){ return getInteger(this.year);}

    String getSecurityCode(){ return this.securityCode.getText().toString();}

    String getName(){ return this.name.getText().toString();}

    String getDocType(){ return this.docType.getText().toString().toUpperCase();}

    String getDocNumber(){ return this.docNumber.getText().toString();}

    private Integer getInteger(EditText text) {
        try {
            return Integer.parseInt(text.getText().toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    //Handle inputs
    void setInputs(){
        cardNumber = (EditText) findViewById(R.id.add_card_form_card_number);
        month = (EditText) findViewById(R.id.add_card_form_month);
        year = (EditText) findViewById(R.id.add_card_form_year);
        securityCode = (EditText) findViewById(R.id.add_card_form_security_code);
        name = (EditText) findViewById(R.id.add_card_form_full_name);
        docType = (EditText) findViewById(R.id.add_card_form_document_type);
        docNumber = (EditText) findViewById(R.id.add_card_form_document_number);
    }

}