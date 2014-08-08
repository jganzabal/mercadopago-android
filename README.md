#MercadoPago - Android SDK

The MercadoPago Android SDK make it easy to collect your users' credit card details inside your android app. By creating [tokens](https://coming-soon), MercadoPago handles the bulk of PCI compliance by preventing sensitive card data from hitting your server.

It is developed for API Level 14 (Android 4.0)  or sooner.

#### Quick links: processing payments with Credit Card.
* [Argentina, Venezuela & Brasil (no installments)](README.md#argentina-venezuela--brasil-no-installments)
* [México (no installments)](README.md#m%C3%A9xico-no-installments)
* [Installments: Argentina, Venezuela & Brasil](README.md#installments-argentina-venezuela--brasil)
* [Installments: México](README.md#installments-m%C3%A9xico)

##Installation

1. Clone the repository.
2. Be sure you've installed the Android SDK with API Level 17 and _android-support-v4_
3. Import the _sdk_ folder into Android Studio
4. Add the folder as library.

## Example app

Coming soon. We are building it. See the 'example' folder under your own risk =)

## Integration

First, you need a publishable key to collect a token. Send an e-mail to developers@mercadopago.com with your user and your test users e-mails in order to request the public_key which will identify you when submitting credit card data to our system.

Once you have it:

```  
Mercadopago mp = new Mercadopago("public-key") 
```

### Processing payments with Credit Card

#### Argentina, Venezuela & Brasil (no installments)

* After showing your view, create and populate a 'MPCard' with the details you collected.

```  
Card card = new Card(getCardNumber(), getMonth(), getYear(), getSecurityCode(), getName(), getDocType(), getDocNumber());
```

* Then send it to MercadoPago.

```
 Callback callback = new Callback<Token>() {
            @Override
            public void success(Token o, Response response) {
                Toast.makeText(getApplicationContext(), o.getId(), Toast.LENGTH_LONG).show();
            }
            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        };

mp.createToken(card, callback);
```

#### México (no installments)

* After showing your view, create and populate a 'Card' with the details you collected.

```  
Card card = new Card(getCardNumber(), getMonth(), getYear(), getSecurityCode(), getName(), getDocType(), getDocNumber());
```

* Get 'payment_method_id' and 'issuer_id' from MercadoPago. You will later need this info to charge your customer from your server.
```
Callback cb = new Callback<List<PaymentMethod>>() {
                        @Override
                        public void success(List<PaymentMethod> o, Response response) {
                            for(PaymentMethod paymentMethod : o){
                                if(paymentMethod.getPaymentTypeId() == "credit_card"){
                                    //save paymentMethodId and issuerId
                                    //later you will have to send them together with the token to your server
                                }
                            }
                        }
                        @Override
                        public void failure(RetrofitError error) {
                                //Handle error
				//¿Ask again for the card number?
                        }
                    };
                    mp.getPaymentMethodByBin(getCardNumber().substring(0,6), cb);
```

* Then send it to MercadoPago.

```
 Callback callback = new Callback<Token>() {
            @Override
            public void success(Token o, Response response) {
                Toast.makeText(getApplicationContext(), o.getId(), Toast.LENGTH_LONG).show();
            }
            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        };

mp.createToken(card, callback);
```

#### Installments: Argentina, Venezuela & Brasil

* After showing your view, create and populate a 'Card' with the details you collected.

```  
Card card = new Card(getCardNumber(), getMonth(), getYear(), getSecurityCode(), getName(), getDocType(), getDocNumber());
```

* Retrieve payment method information from MercadoPago. You will get possible installments and the rate per installment.

```
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
```

```
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
```

* Then send it to MercadoPago.

```
 Callback callback = new Callback<Token>() {
            @Override
            public void success(Token o, Response response) {
                Toast.makeText(getApplicationContext(), o.getId(), Toast.LENGTH_LONG).show();
            }
            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        };

mp.createToken(card, callback);
```

* _NOTE_: if you are collecting payments in Argentina with Mastercard, then you need to read this link (coming soon).


#### Installments: México

* After showing your view, create and populate a 'Card' with the details you collected.

```  
Card card = new Card(getCardNumber(), getMonth(), getYear(), getSecurityCode(), getName(), getDocType(), getDocNumber());
```

* Retrieve payment method information from MercadoPago. You will get possible installments and the rate per installment.

```
 Callback cb = new Callback<List<PaymentMethod>>() {
                        @Override
                        public void success(List<PaymentMethod> o, Response response) {
                        
                       /*
					API could return more than one payment methods when we don't know if the bin 
						is from a credit card or debit card.
						Just keeping the credit_card method for this example.
					*/
				for(PaymentMethod paymentMethod : o){
                                	if(paymentMethod.getPaymentTypeId() == "credit_card"){
                            InstallmentAdapter adapter = new InstallmentAdapter(ExampleActivity.this, paymentMethod.getPayerCosts(), new Double("100"));
                            ((Spinner)findViewById(R.id.spinner)).setAdapter(adapter);
                            }
                        }
                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(getApplicationContext(), RetrofitUtil.parseErrorBody(error).toString(), Toast.LENGTH_LONG).show();
                        }
                    };
                    mp.getPaymentMethodByBin(getCardNumber().substring(0,6), cb);
```

```
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
```

* Then send it to MercadoPago.

```
 Callback callback = new Callback<Token>() {
            @Override
            public void success(Token o, Response response) {
                Toast.makeText(getApplicationContext(), o.getId(), Toast.LENGTH_LONG).show();
            }
            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        };

mp.createToken(card, callback);
```

### Processing payments with Debit Card

Coming soon (only in México)
								
## Using tokens

Once you've collected a token, you can send the tokenId to your server to charge immediately your customer.

From your server:

	curl -X POST \
		 -H 'accept: application/json' \
		 -H 'content-type: application/json' \
		 https://api.mercadolibre.com/checkout/custom/create_payment?access_token=your_access_token \
		 -d '{
	      "amount": 10,
	      "reason": "Item Title",
	      "installments": 1,
	      "card_token_id": "card_token",
	      "payer_email": "payer@email.com",
	      "external_reference": "1234_your_reference",
	      "payment_method_id" : "visa",                   //Just for México
              "card_issuer_id":166                            //Just for México
	      }'

### Handling errors

Coming soon.

### Validation

You have a few options for handling validation of credit card data on the client, depending on what your application does.  Client-side validation of credit card data is not required since our API will correctly reject invalid card information, but can be useful to validate information as soon as a user enters it, or simply to save a network request.

The simplest thing you can do is to populate your 'MPCard' object and, before sending the request, call 'validate()' on the card.  This validates the entire card object, but is not useful for validating card properties one at a time.

To validate 'Card' properties individually, you should use the following:

     - validateCardNumber()
     - validateSecurityCode()
     - validateExpirationMonth()
     - validateExpirationYear()
     - validateCardholderIDType() - Coming soon.
     - validateCardholderIDSubType() - Coming soon.

.....
