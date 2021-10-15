package com.cikarastudio.cikarajantungdesafix.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cikarastudio.cikarajantungdesafix.session.SessionManager;
import com.cikarastudio.cikarajantungdesafix.ssl.HttpsTrustManager;
import com.cikarastudio.cikarajantungdesafix.template.TextFuntion;
import com.cikarastudio.cikarajantungdesafix.ui.lapak.EditProdukActivity;
import com.cikarastudio.cikarajantungdesafix.ui.loadingdialog.LoadingDialog;
import com.cikarastudio.cikarajantungdesafix.ui.main.MainActivity;
import com.cikarastudio.cikarajantungdesafix.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.security.cert.X509Certificate;

public class LoginActivity extends AppCompatActivity {

    LoadingDialog loadingDialog;
    CardView cr_login;
    EditText et_loginEmail, et_loginPassword;
    private static String URL_LOGIN = "https://jantungdesa.bunefit.com/JantungDesaAndroid/login_user.php";
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);
        loadingDialog = new LoadingDialog(LoginActivity.this);

        TextFuntion textFuntion = new TextFuntion();

        cr_login = findViewById(R.id.cr_login);
        et_loginEmail = findViewById(R.id.et_loginEmail);
        et_loginPassword = findViewById(R.id.et_loginPassword);

        textFuntion.hideEdittextKeyboard(et_loginEmail);
        textFuntion.hideEdittextKeyboard(et_loginPassword);


        cr_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Semail = et_loginEmail.getText().toString();
                String Spassword = et_loginPassword.getText().toString();
                if (Semail.equals("")) {
                    textFuntion.cekKosongEdittext(et_loginEmail, "Email");
                } else if (Spassword.equals("")) {
                    textFuntion.cekKosongEdittext(et_loginPassword, "Password");
                } else {
                    login();
                }
            }
        });
    }

    private void login() {
        loadingDialog.startLoading();
        HttpsTrustManager.allowAllSSL();
        final String email = this.et_loginEmail.getText().toString().trim();
        final String password = this.et_loginPassword.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");
                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString("id").trim();
                                    String profile_photo_path = object.getString("profile_photo_path").trim();
                                    sessionManager.createSession(id, profile_photo_path);
                                    Toast.makeText(LoginActivity.this, "Login Berhasil! " , Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Login Gagal: Password Salah!"+ password, Toast.LENGTH_LONG).show();
                                loadingDialog.dissmissDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Login Gagal : Email Salah!" + e.toString(), Toast.LENGTH_LONG).show();
                            loadingDialog.dissmissDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Login Gagal : Terjadi Kesalahan Dengan Jaringan " + error.toString(), Toast.LENGTH_LONG).show();
                        loadingDialog.dissmissDialog();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}