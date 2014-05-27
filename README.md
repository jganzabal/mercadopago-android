#MercadoPago - Android SDK

##Installation

1. Clone the repository.
2. Be sure you've installed the Android SDK with API Level 17 and _android-support-v4_
3. Import the _sdk_ folder into Android Studio
4. Add the folder as library.

##Steps

###Initialization

```  
Mercadopago mp = new Mercadopago("public-key") 
```

###Create card

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

```
mp.createToken(card, callback);
```
