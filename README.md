#MercadoPago - Android SDK

The MercadoPago Android SDK make it easy to collect your users' credit card details inside your android app. By creating [tokens](https://coming-soon), MercadoPago handles the bulk of PCI compliance by preventing sensitive card data from hitting your server.

It is developed for API Level 14 (Android 4.0)  or sooner.

##Installation

1. Clone the repository.
2. Be sure you've installed the Android SDK with API Level 17 and _android-support-v4_
3. Import the _sdk_ folder into Android Studio
4. Add the folder as library.

##Steps

###Initialization

First, you need a publishable key to collect a token. Send an email to developers@mercadopago.com to ask for your public key.

Once you have it:

```  
Mercadopago mp = new Mercadopago("public-key") 
```

###Create card

After showing your view, create and populate a 'Card' with the details you collected:


```  
Card card = new Card(getCardNumber(), getMonth(), getYear(), getSecurityCode(), getName(), getDocType(), getDocNumber());
```

###Create callback

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
```

###POST card

Then send it to MercadoPago:

```
mp.createToken(card, callback);
```

#### México

Coming soon.

### Processing payments with Credit Card (with installments)

Doc coming soon (the code is already there)

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
	      "payment_code": "tokenId",
	      "payer_email": "payer@email.com",
	      "external_reference": "1234_your_reference"
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
	    - validateCardholderIDType()
	    - validateCardholderIDSubType()

.....
