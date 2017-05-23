package com.example.dominik.bakalarka.LoginRegister;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dominik on 15.03.2017.
 * Pripojenie k databaze kvoli registracii
 */

public class RegisterRequest extends StringRequest {
    /* URL adresa databazy so scriptom pre registraciu */
    private static final String REGISTER_REQUEST_URL = "https://ienergy.000webhostapp.com/registracia.php";
    private Map<String, String> params;

    public RegisterRequest(String name, String surname, String username, String password, String house, float total_area, String distributor, String tariff, float usageNT, float usageVT, float payment, Response.Listener<String> listener) {
        /* Ulozenie parametrov potrebnych k registracii */
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("name", name);
        params.put("surname", surname);
        params.put("username", username);
        params.put("password", password);
        params.put("house", house);
        params.put("total_area", Float.toString(total_area));
        params.put("distributor", distributor);
        params.put("tariff", tariff);
        params.put("usageNT", Float.toString(usageNT));
        params.put("usageVT", Float.toString(usageVT));
        params.put("payment", Float.toString(payment));
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
