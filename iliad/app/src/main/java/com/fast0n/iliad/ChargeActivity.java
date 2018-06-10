package com.fast0n.iliad;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cooltechworks.creditcarddesign.CreditCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class ChargeActivity extends AppCompatActivity {

    EditText nCard, nExpiration, ncvv;
    CreditCardView creditCardView;
    Boolean touch = true;
    Button button;
    Spinner spinner;
    ActionBar actionBar;
    String typeCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.chargeNumber);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // set row icon in the toolbar
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);



        button = findViewById(R.id.button);
        creditCardView = findViewById(R.id.card);
        spinner = findViewById(R.id.spinner);



        final Bundle extras = getIntent().getExtras();
        assert extras != null;
        final String token = extras.getString("token");
        final String site_url = getString(R.string.site_url);






        RequestQueue queue = Volley.newRequestQueue(ChargeActivity.this);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, site_url + "?payinfoprice=true&&token="+token, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject json_raw = new JSONObject(response.toString());
                            String iliad = json_raw.getString("iliad");

                            JSONObject json = new JSONObject(iliad);
                            String price = json.getString("0");
                            JSONArray json_title = new JSONArray(price);

                            final List<String> list = new ArrayList<>();

                            for (int z = 0; z < json_title.length(); z++) {

                                String stringPrice = json_title.getString(z);
                                list.add(stringPrice + "€");
                            }
                            list.add("Importo");
                            final int listsize = list.size() - 1;
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ChargeActivity.this, R.layout.spinner_item, list) {
                                @Override
                                public int getCount() {
                                    return (listsize);
                                }
                            };
                            spinner.setAdapter(dataAdapter);
                            spinner.setSelection(list.size()-1);

                        } catch (JSONException ignored) {
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(getRequest);





        creditCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (touch){
                    creditCardView.showBack();
                    touch = false;
                }
                else{
                    creditCardView.showFront();
                    touch = true;
                }
            }
        });



        final ArrayList<String> listOfPattern= new ArrayList<>();

        String ptVisa = "^4[0-9]{6,}$";
        listOfPattern.add(ptVisa);
        String ptMasterCard = "^5[1-5][0-9]{5,}$";
        listOfPattern.add(ptMasterCard);

        nCard = findViewById(R.id.nCard);
        nExpiration = findViewById(R.id.nExpiration);
        ncvv = findViewById(R.id.ncvv);

        nCard.addTextChangedListener(new TextWatcher() {
            private final String space = " "; // you can change this to whatever you want
            private final Pattern pattern = Pattern.compile("^(\\d{4}" + space + "{1}){0,3}\\d{1,4}$"); // check whether we need to modify or not

            @Override
            public void onTextChanged(CharSequence s, int st, int be, int count) {
                creditCardView.setCardNumber(nCard.getText().toString().replaceAll("\\s+", ""));
                String currentText = nCard.getText().toString();
                if (currentText.isEmpty() || pattern.matcher(currentText).matches())
                    return; // no need to modify
                String numbersOnly = currentText.trim().replaceAll("[^\\d.]", "");
                ; // remove everything but numbers
                String formatted = "";
                for (int i = 0; i < numbersOnly.length(); i += 4)
                    if (i + 4 < numbersOnly.length())
                        formatted += numbersOnly.substring(i, i + 4) + space;
                    else
                        formatted += numbersOnly.substring(i);
                nCard.setText(formatted);
                nCard.setSelection(nCard.getText().toString().length());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String ccNum = s.toString().replaceAll("\\s+","");
                for(String p:listOfPattern){
                    if(ccNum.matches(p)){
                        if (p.equals("^4[0-9]{6,}$")){
                            p = "visa";
                            typeCard = p;
                        }
                        else if(p.equals("^5[1-5][0-9]{5,}$")){
                            p = "mastercard";
                            typeCard = p;
                        }
                        else{
                            typeCard = "";
                        }
                        break;
                    }
                }
            }
        });


        ncvv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (ncvv.getText().toString().length() == 3 || ncvv.getText().toString().length() == 4) {
                    creditCardView.showFront();
                    touch = true;
                } else {
                    if (touch) {
                        creditCardView.showBack();
                        touch = false;
                    }
                }
                creditCardView.setCVV(ncvv.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });



        nExpiration.addTextChangedListener(new TextWatcher() {
            private final String space = "/"; // you can change this to whatever you want
            private final Pattern pattern = Pattern.compile("^(\\d{2}" + space + "{1}){0,1}\\d{1,2}$"); // check whether we need to modify or not

            @Override
            public void onTextChanged(CharSequence s, int st, int be, int count) {
                creditCardView.setCardExpiry(nExpiration.getText().toString().replaceAll("\\s+", ""));
                String currentText = nExpiration.getText().toString();
                if (currentText.isEmpty() || pattern.matcher(currentText).matches())
                    return;
                String numbersOnly = currentText.trim().replaceAll("[^\\d.]", "");
                StringBuilder formatted = new StringBuilder();
                for (int i = 0; i < numbersOnly.length(); i += 2)
                    if (i + 2 < numbersOnly.length())
                        formatted.append(numbersOnly.substring(i, i + 2)).append(space);
                    else
                        formatted.append(numbersOnly.substring(i));
                nExpiration.setText(formatted.toString());
                nExpiration.setSelection(nExpiration.getText().toString().length());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable e) {
            }
        });




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nCard.getText().toString().length() == 0 && ncvv.getText().toString().length() == 0 && nExpiration.getText().toString().length() == 0 )
                    Toasty.warning(ChargeActivity.this, "Valore mancante controlla bene", Toast.LENGTH_SHORT).show();
                else{

                    RequestQueue queue = Volley.newRequestQueue(ChargeActivity.this);

                    JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, site_url + "?payinfocard=true&token="+token, null,
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    try {

                                        JSONObject json_raw = new JSONObject(response.toString());
                                        String iliad = json_raw.getString("iliad");
                                        JSONObject json = new JSONObject(iliad);

                                        String price = json.getString("0");
                                        JSONArray json_title = new JSONArray(price);

                                        System.out.println(json_title.length());

                                        for (int z = 0; z < json_title.length(); z++) {

                                            String stringPrice = json_title.getString(z);

                                            if (stringPrice.equals(typeCard)){



                                            }
                                            else {
                                                Toasty.warning(ChargeActivity.this, "Valore mancante controlla bene", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    } catch (JSONException ignored) {
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

                    queue.add(getRequest);

                }
            }
        });

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                super.onBackPressed();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
