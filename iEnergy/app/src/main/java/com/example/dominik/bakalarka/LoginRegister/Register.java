package com.example.dominik.bakalarka.LoginRegister;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.dominik.bakalarka.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * Created by Dominik on 15.03.2017.
 * Registracia noveho pouzivatela
 */

public class Register extends AppCompatActivity{

    Button register;
    EditText etName, etSurname, etUsername, etPassword, etPayment, etTotal_area, etNT, etVT;
    Spinner sHouse, sProgram, sTariff;
    private Sheet s;
    private String[] houses = new String[] { "Byt", "Bungalov", "Dvojdom" };
    private String[] programs = new String[] { "Program 30", "Program 57", "Program 59", "Program 12" };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Nastavenie titulku */
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout_register);
        /* Zablokovanie pretacania obrazovky */
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_register);
        /* Prepojenie komponentov a ziskanie pristupu k nim a ich datam */
        register = (Button) findViewById(R.id.register);
        etName = (EditText) findViewById(R.id.etName);
        etSurname = (EditText) findViewById(R.id.etSurname);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPayment = (EditText) findViewById(R.id.etPayment);
        etTotal_area = (EditText) findViewById(R.id.etTotal_area);
        sHouse = (Spinner) findViewById(R.id.sHouse);
        sProgram = (Spinner) findViewById(R.id.sProgram);
        sTariff = (Spinner) findViewById(R.id.sTariff);
        etNT = (EditText) findViewById(R.id.etNT);
        etVT = (EditText) findViewById(R.id.etVT);
        setSpinnerDistribution();

        /* Posluchac na spiner adapter kvoli zmene udajov v druhom spinery */
        sProgram.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                setSpinnerData(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                setSpinnerData(0);
            }

        });

        /* Posluchac na druhy spiner kvoli zablokovaniu jednej tarify v pripade jednotarifneho programu */
        sTariff.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                /* Zistenie pozicie vybraneho udaja zo spinera */
                if(position < 2) {
                    etNT.setText("0");
                    etNT.setFocusable(false);
                }
                else {
                    etNT.setFocusableInTouchMode(true);
                    etNT.setFocusable(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                    etNT.setFocusable(false);
            }

        });

        /* Registracia noveho pouzivatela */
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* Ziskanie vsetkych potrebnych udajov k registracii */
                etNT.setFocusableInTouchMode(true);
                etNT.setFocusable(true);
                String name = etName.getText().toString();
                String surname = etSurname.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                Float payment = Float.parseFloat(etPayment.getText().toString());
                Float total_area = Float.parseFloat(etTotal_area.getText().toString());
                String house = sHouse.getSelectedItem().toString();
                String dodavatel = sProgram.getSelectedItem().toString();
                String tariff = sTariff.getSelectedItem().toString();
                Float usageNT;
                if (etNT.getText() == null)
                    usageNT = 0f;
                else
                    usageNT = Float.parseFloat(etNT.getText().toString());

                Float usageVT = Float.parseFloat(etVT.getText().toString());
                if(!checkData()){

                } else {

                    /* Nastavenie pomocou pre zadavanie */
                    etUsername.setHint("Prihlasovacie meno:");
                    etUsername.setHintTextColor(getResources().getColor(R.color.black));
                    etPassword.setHint("Heslo:");
                    etPassword.setHintTextColor(getResources().getColor(R.color.black));
                    /* Pripojenie na databazu a ziskanie udaja o uspesnosti registracie */
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    Intent intent = new Intent(Register.this, Login.class);
                                    Register.this.startActivity(intent);
                                    finish();
                                } else {
                                    /* Registracia prebehla neuspesne info */
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                    builder.setMessage("Registrácia neúspešna!")
                                            .setNegativeButton("Znova", null)
                                            .create()
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    /* Pripojenie k databaze a ulozenie udajov */
                    RegisterRequest registerRequest = new RegisterRequest(name, surname, username, password, house, total_area, dodavatel, tariff, usageNT, usageVT, payment, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Register.this);
                    queue.add(registerRequest);
                }
            }
        });
    }
    /* Ziskanie vsetkzch distributorov zo suboru */
    public void setSpinnerDistribution() {

        int row, col;
        Workbook wb = null;
        try {
            AssetManager am = getAssets();
            InputStream is = am.open("nazvy.xls");
            wb = Workbook.getWorkbook(is);
        } catch(Exception e) {
        }
        s = wb.getSheet(0);
        row = s.getRows();
        col = s.getColumns();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sProgram.setAdapter(spinnerAdapter);

        for (int i=0; i<row;i++) {
            Cell c =s.getCell(0,i);
            spinnerAdapter.add(c.getContents());
        }

        spinnerAdapter.notifyDataSetChanged();
    }
    /* Vybratie jednotlivych tarif k zvolenemu distributorovi zo suboru */
    public void setSpinnerData(int r) {

        int row, col;
        Workbook wb = null;
        try {
            AssetManager am = getAssets();
            InputStream is = am.open("nazvy.xls");
            wb = Workbook.getWorkbook(is);
        } catch(Exception e) {
        }
        s = wb.getSheet(0);
        row = s.getRows();
        col = s.getColumns();

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sTariff.setAdapter(spinnerAdapter);

        for (int i=1; i<col;i++) {
            Cell c =s.getCell(i,r);
            spinnerAdapter.add(c.getContents());
        }

        spinnerAdapter.notifyDataSetChanged();
    }
    /* Skontrolovanie dlzky prihlasovanieho mena a hesla */
    private boolean checkData(){

        if (etUsername.getText().length() < 8) {
            etUsername.setText("");
            etUsername.setHint("Minimalne 8 znakov!!");
            etUsername.setHintTextColor(getResources().getColor(R.color.red));
            return false;
        }
        if (etPassword.getText().length() < 8) {
            etPassword.setText("");
            etPassword.setHint("Minimalne 8 znakov!!");
            etPassword.setHintTextColor(getResources().getColor(R.color.red));
            return false;
        }
        return true;
    }
}
