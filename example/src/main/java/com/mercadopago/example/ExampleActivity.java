package com.mercadopago.example;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mercadopago.Mercadopago;
import com.mercadopago.model.Card;
import com.mercadopago.model.Token;
import com.mercadopago.util.RetrofitUtil;

import junit.framework.Assert;

import org.json.JSONException;

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

    public void submitForm(View view){
        setInputs();
        //Init mercadopago object with public key
        Mercadopago mp = new Mercadopago("841d020b-1077-4742-ad55-7888a0f5aefa");
        //Create card object with card data
        Card card = new Card(getCardNumber(), getMonth(), getYear(), getSecurityCode(), getName(), getDocType(), getDocNumber());

        Callback callback = new Callback<Token>() {
            @Override
            public void success(Token o, Response response) {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, o.getId(), duration);
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
        //Check valid card data
        if(card.validateCard()) {
            //Send card data to get token id
            mp.createToken(card, callback);
        }
    }

    void setInputs(){
        cardNumber = (EditText) findViewById(R.id.add_card_form_card_number);
        month = (EditText) findViewById(R.id.add_card_form_month);
        year = (EditText) findViewById(R.id.add_card_form_year);
        securityCode = (EditText) findViewById(R.id.add_card_form_security_code);
        name = (EditText) findViewById(R.id.add_card_form_full_name);
        docType = (EditText) findViewById(R.id.add_card_form_document_type);
        docNumber = (EditText) findViewById(R.id.add_card_form_document_number);
    }

    String getCardNumber() {
        return this.cardNumber.getText().toString();
    }

    Integer getMonth(){ return getInteger(this.month);}

    Integer getYear(){ return getInteger(this.year);}

    Integer getSecurityCode(){ return getInteger(this.securityCode);}

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.example, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
