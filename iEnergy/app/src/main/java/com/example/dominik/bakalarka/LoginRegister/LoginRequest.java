package com.example.dominik.bakalarka.LoginRegister;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dominik on 15.03.2017.
 * Pripojenie k databaze kvoli prihlaseniu
 */

public class LoginRequest extends StringRequest {
    /* URL adresa databazy so scriptom pre prihlasenie */
    private static final String LOGIN_REQUEST_URL = "https://ienergy.000webhostapp.com/prihlasenie.php";
    private Map<String, String> params;

    public LoginRequest(String username, String password, Response.Listener<String> listener) {
        /* Ulozenie parametrov potrebnych k prihlaseniu */
        super(Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
