package com.example.dominik.bakalarka.LoginRegister;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.example.dominik.bakalarka.Function.Info;
import com.example.dominik.bakalarka.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * Created by Dominik on 18.03.2017.
 * Upravenie udajov zadanych pri registracii
 */

public class UpdateData  extends AppCompatActivity{
    Button uprav;
    EditText etName, etSurname, etPayment, etTotal_area, etNT, etVT;
    Spinner sHouse, sProgram, sTariff;
    String sname, ssurname, shouse, sprogram, sUsername, setTariff;
    float stotal_area, spayment,  sUsageNT, sUsageVT;
    ProgressDialog progressDialog;
    private Sheet s;
    private String[] houses = new String[] { "Single house", "Apartmen", "Town Home", "Single-Family Home", "Sales" };
    private String[] programs = new String[] { "Program 59", "Program 57", "Program 59", "Program 12" };
    SharedPreferences preferences;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        /* Nastavenie titulku */
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout_info);
        /* Zablokovanie pretacania obrazovky */
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        /* Prepojenie komponentov a ziskanie pristupu k nim a ich datam */
        uprav = (Button) findViewById(R.id.register);
        etName = (EditText) findViewById(R.id.etName);
        etSurname = (EditText) findViewById(R.id.etSurname);
        etPayment = (EditText) findViewById(R.id.etPayment);
        etTotal_area = (EditText) findViewById(R.id.etTotal_area);
        sHouse = (Spinner) findViewById(R.id.sHouse);
        sProgram = (Spinner) findViewById(R.id.sProgram);
        etNT = (EditText) findViewById(R.id.etNT);
        etVT = (EditText) findViewById(R.id.etVT);
        sTariff = (Spinner) findViewById(R.id.sTariff);
        /* Vytvorenie dialogoveho okna kvoli odozve databazy */
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Prebieha úprava");
        progressDialog.setMessage("Čakajte prosím...");
        /* Nastavenie jednotlivych udajov */
        setSpinnerDistribution();
        setString();
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

        /* Upravenie existujucich dat */
        uprav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* Ziskanie vsetkych potrebnych udajov na upravenie */
                String myname = etName.getText().toString();
                String mysurname = etSurname.getText().toString();
                Float mypayment = Float.parseFloat(etPayment.getText().toString());
                Float mytotal_area = Float.parseFloat(etTotal_area.getText().toString());
                String myhouse = sHouse.getSelectedItem().toString();
                String myprogram = sProgram.getSelectedItem().toString();
                Float usageNT = Float.parseFloat(etNT.getText().toString());
                Float usageVT = Float.parseFloat(etVT.getText().toString());
                String tariff = sTariff.getSelectedItem().toString();
                progressDialog.show();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                /* Ziskanie upravenych udajov z databazy */
                                String name = jsonResponse.getString("name");
                                String surname = jsonResponse.getString("surname");
                                String house = jsonResponse.getString("house");
                                float total_area = (float) jsonResponse.getDouble("total_area");
                                String program = jsonResponse.getString("distributor");
                                String tariff = jsonResponse.getString("tariff");
                                float usageNT = (float) jsonResponse.getDouble("usageNT");
                                float usageVT = (float) jsonResponse.getDouble("usageVT");
                                float payment = (float) jsonResponse.getDouble("payment");
                                /* Ulozenie novych udajov do pamati programu */
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("name", name);
                                editor.putString("surname", surname);
                                editor.putString("house", house);
                                editor.putString("program", program);
                                editor.putFloat("total_area", total_area);
                                editor.putFloat("payment", payment);
                                editor.putFloat("usageNT", usageNT);
                                editor.putFloat("usageVT", usageVT);
                                editor.putString("tariff", tariff);
                                editor.apply();
                                progressDialog.dismiss();
                                /* Zmena jednotlivych textov komponentov v triede info */
                                Info.name.setText(name + " " + surname);
                                Info.house.setText(house);
                                Info.program.setText(program);
                                Info.setTariff.setText(tariff);
                                Info.setUsageNT.setText(String.valueOf(usageNT));
                                Info.setUsageVT.setText(String.valueOf(usageVT));
                                Info.total_area.setText(String.valueOf(total_area));
                                Info.payment.setText(String.valueOf(payment));

                                finish();
                            } else {
                                /* Neuspesna uprava udajov info */
                                progressDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateData.this);
                                builder.setMessage("Uprava neúspešná")
                                        .setNegativeButton("Znova", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                /* Pripojenie k databaze a vykonanie potrebnych zmien */
                UpdateRequest loginRequest = new UpdateRequest(sUsername, myprogram, myhouse, mytotal_area, tariff, usageNT, usageVT, mypayment, myname, mysurname, responseListener);
                RequestQueue queue = Volley.newRequestQueue(UpdateData.this);
                queue.add(loginRequest);
            }
        });

    }

    public void setDefualtValue() {

        int row,col;
        String distributorName = "";
        sprogram = preferences.getString("distributor", "");
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
        int j = 0;
        for (int i= 0;i<row; i++) {
            Cell c =s.getCell(0,i);
            if (sprogram.equals(c.getContents())){
                sProgram.setSelection(i);
                j = i;
                break;
            }
        }
        int k = 0;
        for (int i= 0;i<houses.length; i++) {
            if (shouse.equals(houses[i])){
                k = i;
                break;
            }
        }

    }
    /* Nastavenie jednotlivych komponentov na udaje ktore su ulozene v programe */
    public void setString() {

        sname = preferences.getString("name", "");
        ssurname = preferences.getString("surname", "");
        shouse = preferences.getString("house", "");
        sUsername = preferences.getString("username", "");
        sprogram = preferences.getString("distributor", "");
        stotal_area = preferences.getFloat("total_area", 0);
        spayment = preferences.getFloat("payment", 0);
        sUsageNT = preferences.getFloat("usageNT", 0);
        sUsageVT = preferences.getFloat("usageVT", 0);
        setTariff = preferences.getString("tariff", "");
        int row,col;
        String distributorName = "";

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
        int j = 0;
        for (int i= 0;i<row; i++) {
            Cell c =s.getCell(0,i);
            if (sprogram.equals(c.getContents().toString())){
                Log.i("Riadok ", String.valueOf(i));
                sProgram.setSelection(i);
                j = i;
                break;
            }
        }
        int k = 0;
        for (int i= 0;i<houses.length; i++) {
            if (shouse.equals(houses[i])){
                k = i;
                break;
            }
        }
        etName.setText(sname);
        etSurname.setText(ssurname);
        sHouse.setSelection(k);
        setSpinnerData(j);

        etTotal_area.setText(String.valueOf(stotal_area));
        etPayment.setText(String.valueOf(spayment));
        etNT.setText(String.valueOf(sUsageNT));
        etVT.setText(String.valueOf(sUsageVT));


    }

    /* Nastavenie spinera distributora na defaultnu hodnotu a pridanie vsetkych ostatnych distributorov */
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

    /* Nastavenie jednotlivych tarif vybraneho distributora */
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
            if (setTariff.equals(c.getContents().toString())){
                sTariff.setSelection(i-1);
            }
        }

        spinnerAdapter.notifyDataSetChanged();

    }
    /* Ukoncenie aktivity v pripade stlacenia tlacidla spat */
    public void onBackPressed() {
        this.finish();
    }
}
