package com.mercadopago.example;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mercadopago.Mercadopago;
import com.mercadopago.model.Card;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.Token;
import com.mercadopago.util.InstallmentAdapter;
import com.mercadopago.util.IssuerAdapter;
import com.mercadopago.util.RetrofitUtil;

import junit.framework.Assert;

import org.json.JSONException;

import java.util.List;


import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class AdvancedExampleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_example);


        Mercadopago mp = new Mercadopago("841d020b-1077-4742-ad55-7888a0f5aefa");
        Callback cb = new Callback<List<PaymentMethod>>() {
            @Override
            public void success(List<PaymentMethod> o, Response response) {
                Spinner spinner = (Spinner) findViewById(R.id.spinner);

                InstallmentAdapter adapter = new InstallmentAdapter(AdvancedExampleActivity.this, o.get(0).getPayerCosts(), new Double("100"));
                spinner.setAdapter(adapter);

                Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
                IssuerAdapter adapter2 = new IssuerAdapter(AdvancedExampleActivity.this, o.get(0).getExceptionsByCardIssuer(), new Double("100"));
                spinner2.setAdapter(adapter2);
            }
            @Override
            public void failure(RetrofitError error) {
                try {
                    Assert.fail(String.valueOf(RetrofitUtil.parseErrorBody(error).getString("cause")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        mp.getPaymentMethodById("visa", cb);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.example, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void submitForm(View view){
        Mercadopago mp = new Mercadopago("841d020b-1077-4742-ad55-7888a0f5aefa");

        EditText cardNumber = (EditText) findViewById(R.id.add_card_form_card_number);
        EditText month = (EditText) findViewById(R.id.add_card_form_month);
        EditText year = (EditText) findViewById(R.id.add_card_form_year);
        EditText securityCode = (EditText) findViewById(R.id.add_card_form_security_code);
        EditText name = (EditText) findViewById(R.id.add_card_form_full_name);
        EditText docType = (EditText) findViewById(R.id.add_card_form_document_type);
        EditText docNumber = (EditText) findViewById(R.id.add_card_form_document_number);

//TODO: manejar años

//"4170 0688 1010 8020"
        Card card = new Card(cardNumber.getText().toString(),new Integer(month.getText().toString()),
                new Integer(year.getText().toString()), new Integer(securityCode.getText().toString()),name.getText().toString(),
                docType.getText().toString().toUpperCase(),docNumber.getText().toString());
        Callback callback = new Callback<Token>() {
            @Override
            public void success(Token o, Response response) {
                Context context = getApplicationContext();
                CharSequence text = "Hello toast!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, o.getId() + o.getPublicKey(), duration);
                toast.show();

            }
            @Override
            public void failure(RetrofitError error) {
                try {
                    Assert.fail(String.valueOf(RetrofitUtil.parseErrorBody(error).getString("cause")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        mp.createToken(card, callback);

    }


}
