package com.example.dominik.bakalarka.LoginRegister;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dominik on 18.03.2017.
 * Pripojenie k databaze kvoli upraveniu udajov
 */

public class UpdateRequest extends StringRequest {
    /* URL adresa databazy so scriptom pre upravenie udajov */
    private static final String LOGIN_REQUEST_URL = "https://ienergy.000webhostapp.com/update.php";
    private Map<String, String> params;

    public UpdateRequest(String username, String distributor, String house, float total_area, String tariff, float usageNT, float usageVT, float payment, String name, String surname, Response.Listener<String> listener) {
        /* Ulozenie parametrov potrebnych k upraveniu */
        super(Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("distributor", distributor);
        params.put("house", house);
        params.put("total_area", Float.toString(total_area));
        params.put("payment", Float.toString(payment));
        params.put("name", name);
        params.put("surname", surname);
        params.put("tariff", tariff);
        params.put("usageNT", Float.toString(usageNT));
        params.put("usageVT", Float.toString(usageVT));
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
