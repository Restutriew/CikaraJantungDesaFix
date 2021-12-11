package com.cikarastudio.cikarajantungdesafix.ui.login;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cikarastudio.cikarajantungdesafix.R;
import com.cikarastudio.cikarajantungdesafix.session.SessionManager;
import com.cikarastudio.cikarajantungdesafix.ssl.HttpsTrustManager;
import com.cikarastudio.cikarajantungdesafix.template.kima.text.TextFuntion;
import com.cikarastudio.cikarajantungdesafix.ui.loadingdialog.LoadingDialog;
import com.cikarastudio.cikarajantungdesafix.ui.main.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    LoadingDialog loadingDialog;
    CardView cr_login;
    EditText et_loginEmail, et_loginPassword;
    TextView tv_lupaPassword;
    SessionManager sessionManager;
    TextFuntion textFuntion;
    String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);
        loadingDialog = new LoadingDialog(LoginActivity.this);

        textFuntion = new TextFuntion();

        //inisiasi link
        link = getString(R.string.link);

        et_loginEmail = findViewById(R.id.et_loginEmail);
        et_loginPassword = findViewById(R.id.et_loginPassword);

        textFuntion.hideEdittextKeyboard(et_loginEmail);
        textFuntion.hideEdittextKeyboard(et_loginPassword);

        cr_login = findViewById(R.id.cr_login);
        cr_login.setOnClickListener(this);

        tv_lupaPassword = findViewById(R.id.tv_lupaPassword);
        tv_lupaPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_lupaPassword:
                // do your code
                showDialogLupaPassword();
//                Intent keListArtikel = new Intent(getActivity(), ListArtikelActivity.class);
//                startActivity(keListArtikel);
                break;
            case R.id.cr_login:
                // do your code
                String Semail = et_loginEmail.getText().toString();
                String Spassword = et_loginPassword.getText().toString();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (Semail.equals("")) {
                    textFuntion.cekKosongEdittext(et_loginEmail, "Email");
                } else if (!Semail.matches(emailPattern)) {
                    Toast.makeText(getApplicationContext(), "Penulisan Email Salah, Masukkan Email Dengan Benar!", Toast.LENGTH_SHORT).show();
                } else if (Spassword.equals("")) {
                    textFuntion.cekKosongEdittext(et_loginPassword, "Password");
                } else {
                    login();
                }
                break;
            default:
                break;
        }
    }

    private void showDialogLupaPassword() {
        Dialog dialogAduan = new Dialog(this);
        dialogAduan.setContentView(R.layout.dialog_aduan_lupa_password);
        dialogAduan.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog_aduan);

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.80);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.50);
        dialogAduan.getWindow().setLayout(width, height);

        EditText et_emailAduanLupaPassword = dialogAduan.findViewById(R.id.et_emailAduanLupaPassword);
        et_emailAduanLupaPassword.requestFocus();

        EditText et_nikAduanLupaPassword = dialogAduan.findViewById(R.id.et_nikAduanLupaPassword);

        //showkeyboard
        InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        CardView cr_kirimAduanLupaPassword = dialogAduan.findViewById(R.id.cr_kirimAduanLupaPassword);
        cr_kirimAduanLupaPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide keyboard
                InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                String SemailLupaPassword = et_emailAduanLupaPassword.getText().toString();
                String SnikLupaPassword = et_nikAduanLupaPassword.getText().toString();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (SemailLupaPassword.equals("")) {
                    textFuntion.cekKosongEdittext(et_emailAduanLupaPassword, "Email");
                } else if (!SemailLupaPassword.matches(emailPattern)) {
                    Toast.makeText(getApplicationContext(), "Penulisan Email Salah, Masukkan Email Dengan Benar!", Toast.LENGTH_SHORT).show();
                } else if (SnikLupaPassword.length() < 16) {
                    et_nikAduanLupaPassword.setError("No KK Harus 16 Digit!");
                    et_nikAduanLupaPassword.requestFocus();
                } else {
                    aduanLupaPassword(SemailLupaPassword, SnikLupaPassword);
                }

            }
        });
        dialogAduan.show();
    }

    private void aduanLupaPassword(String email, String nik) {
        loadingDialog.startLoading();
        String ubahPassword = "TRUE";
        String URL_TAMBAHADUANLUPAPASSWORD = link + "user/id";
        StringRequest stringRequest = new StringRequest(Request.Method.PATCH, URL_TAMBAHADUANLUPAPASSWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(LoginActivity.this, "Pengajuan Lupa Password Sukses", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(LoginActivity.this, LoginActivity.class);
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(i);
                                overridePendingTransition(0, 0);
                                loadingDialog.dissmissDialog();

                            } else {
                                Toast.makeText(LoginActivity.this, "Pengajuan Gagal!", Toast.LENGTH_LONG).show();
                                loadingDialog.dissmissDialog();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Pengajuan Gagal!" + e.toString(), Toast.LENGTH_LONG).show();
                            loadingDialog.dissmissDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Pengajuan Gagal! : Cek Koneksi Anda" + error, Toast.LENGTH_LONG).show();
                        loadingDialog.dissmissDialog();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ubahpassword", ubahPassword);
                params.put("email", email);
                params.put("nik", nik);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void login() {
        String URL_LOGIN = "https://puteran.cikarastudio.com/jantungdesaandroid/login_user.php";
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
                                    sessionManager.createSession(id);
                                    Toast.makeText(LoginActivity.this, "Login Berhasil! ", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Login Gagal: Password Salah!", Toast.LENGTH_LONG).show();
                                loadingDialog.dissmissDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Login Gagal : Email Tidak Ditemukan, Mohon Periksa Kembali Email Anda!", Toast.LENGTH_LONG).show();
                            loadingDialog.dissmissDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Login Gagal : Terjadi Kesalahan Dengan Jaringan!" + error.toString(), Toast.LENGTH_LONG).show();
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