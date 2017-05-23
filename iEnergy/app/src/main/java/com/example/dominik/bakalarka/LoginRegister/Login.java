package com.example.dominik.bakalarka.LoginRegister;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.dominik.bakalarka.Function.Faktura;
import com.example.dominik.bakalarka.Main.MainActivity;
import com.example.dominik.bakalarka.R;
import com.android.volley.Response;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dominik on 15.03.2017.
 * Prihlasenie do aplikacie
 */

public class Login extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button login, registration;
    ProgressDialog progressDialog;
    SharedPreferences preferences = null;// = PreferenceManager.getDefaultSharedPreferences(this);
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Nastavenie titulku */
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout_login);
        /* Zablokovanie pretacania obrazovky */
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        /* Prepojenie komponentov a ziskanie pristupu k nim a ich datam */
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        login = (Button) findViewById(R.id.login);
        registration = (Button) findViewById(R.id.register2);

        /* Vytvorenie dialogoveho okna kvoli odozve databazy */
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Prebieha prihlásenie");
        progressDialog.setMessage("Čakajte prosím...");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Ziskanie prihlasovacich udajov */
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                progressDialog.show();
                /* Pripojenie na server a ziskanie udajov z databazy */
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                /* Ziskanie jednotlivych udajov z databazy */
                                String name = jsonResponse.getString("name");
                                String surname = jsonResponse.getString("surname");
                                String house = jsonResponse.getString("house");
                                float total_area = (float) jsonResponse.getDouble("total_area");
                                String distributor = jsonResponse.getString("distributor");
                                String tariff = jsonResponse.getString("tariff");
                                float usageNT = (float) jsonResponse.getDouble("usageNT");
                                float usageVT = (float) jsonResponse.getDouble("usageVT");
                                float payment = (float) jsonResponse.getDouble("payment");

                                /* Ulozenie udajov do sharedpreferences */


                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("name", name);
                                editor.putString("surname", surname);
                                editor.putString("username", username);
                                editor.putString("house", house);
                                editor.putString("distributor", distributor);
                                editor.putString("tariff", tariff);
                                editor.putFloat("usageNT", usageNT);
                                editor.putFloat("usageVT", usageVT);
                                editor.putFloat("total_area", total_area);
                                editor.putFloat("payment", payment);
                                editor.apply();
                                progressDialog.dismiss();

                                /* V pripade uspechu prejst do hlavnej aktivity */
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                /* V pripade neuspechu info pre pouzivatela o neuspechu */
                                progressDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                builder.setMessage("Zle meno alebo heslo!!")
                                        .setNegativeButton("Ok", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                /* Pripojenie k databaze s prihlasovacimi udajmi */
                LoginRequest loginRequest = new LoginRequest(username, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Login.this);
                queue.add(loginRequest);
            }
        });

        /* Prejst do aktivity registracie v pripade ze este nemame prihlasovacie udaje */
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(Login.this, Register.class);
                Login.this.startActivity(registerIntent);
            }
        });
    }
}
