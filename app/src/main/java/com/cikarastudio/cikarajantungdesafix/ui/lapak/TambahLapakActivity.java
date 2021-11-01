package com.cikarastudio.cikarajantungdesafix.ui.lapak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cikarastudio.cikarajantungdesafix.R;
import com.cikarastudio.cikarajantungdesafix.session.SessionManager;
import com.cikarastudio.cikarajantungdesafix.ssl.HttpsTrustManager;
import com.cikarastudio.cikarajantungdesafix.ui.loadingdialog.LoadingDialog;
import com.cikarastudio.cikarajantungdesafix.ui.profil.ProfilActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TambahLapakActivity extends AppCompatActivity {

    ImageView img_back;
    SessionManager sessionManager;
    LoadingDialog loadingDialog;
    CardView cr_simpanTambahLapak;
    EditText et_namaLapakTambah, et_alamatLapakTambah, et_tentangLapakTambah, et_telpLapakTambah;

    String id_user, token , link;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_lapak);

        sessionManager = new SessionManager(TambahLapakActivity.this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        id_user = user.get(sessionManager.ID);

        loadingDialog = new LoadingDialog(TambahLapakActivity.this);

        HttpsTrustManager.allowAllSSL();

        //inisiasi link
        link = getString(R.string.link);

        token = getString(R.string.token);

        et_namaLapakTambah = findViewById(R.id.et_namaLapakTambah);
        et_alamatLapakTambah = findViewById(R.id.et_alamatLapakTambah);
        et_tentangLapakTambah = findViewById(R.id.et_tentangLapakTambah);
        et_telpLapakTambah = findViewById(R.id.et_telpLapakTambah);

        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cr_simpanTambahLapak = findViewById(R.id.cr_simpanTambahLapak);
        cr_simpanTambahLapak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.startLoading();
                tambahLapak();
            }
        });
    }

    private void tambahLapak() {
        String URL_TAMBAHLAPAK = link + "tambahlapak";
        final String namalapak = et_namaLapakTambah.getText().toString().trim();
        final String alamatLapak = et_alamatLapakTambah.getText().toString().trim();
        final String tentangLapak = et_tentangLapakTambah.getText().toString().trim();
        final String telp = et_telpLapakTambah.getText().toString().trim();
        final String gambar = "blablabla.jpg";
        final String status_lapak = "menunggu";

        Log.d("calpalnx", String.valueOf(id_user));
        Log.d("calpalnx", String.valueOf(namalapak));
        Log.d("calpalnx", String.valueOf(alamatLapak));
        Log.d("calpalnx", String.valueOf(tentangLapak));
        Log.d("calpalnx", String.valueOf(telp));
        Log.d("calpalnx", String.valueOf(gambar));
        Log.d("calpalnx", String.valueOf(token));
        Log.d("calpalnx", String.valueOf(status_lapak));


//        String tglLahir = et_tambahBumilTglLahir.getText().toString().trim();
//        String tanggal = tglLahir.substring(0, 2);
//        String bulan = tglLahir.substring(3, 5);
//        String tahun = tglLahir.substring(6, 10);
//        final String tglLahirFixBumil = tahun + "-" + bulan + "-" + tanggal;
//        final String goldarBumil = et_golDarah.getSelectedItem().toString();
//        final String agamaBumil = et_agama.getSelectedItem().toString();
//        final String pekerjaanBumil = et_tambahBumilPekerjaan.getText().toString().trim();
//        final String pendidikanBumil = et_pendidikan.getSelectedItem().toString();

//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Calendar cal = Calendar.getInstance();
//        final String tanggalSekarang = dateFormat.format(cal.getTime());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_TAMBAHLAPAK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(TambahLapakActivity.this, "Daftar Lapak Sukses", Toast.LENGTH_LONG).show();
                                loadingDialog.dissmissDialog();
                                finish();
                            } else {
                                Toast.makeText(TambahLapakActivity.this, "Daftar Lapak Gagal!", Toast.LENGTH_LONG).show();
                                loadingDialog.dissmissDialog();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TambahLapakActivity.this, "Daftar Lapak Gagal!", Toast.LENGTH_LONG).show();
                            loadingDialog.dissmissDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TambahLapakActivity.this, "Daftar Lapak Gagal! : Cek Koneksi Anda", Toast.LENGTH_LONG).show();
                        loadingDialog.dissmissDialog();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", id_user);
                params.put("nama_lapak", namalapak);
                params.put("tentang", tentangLapak);
                params.put("alamat", alamatLapak);
                params.put("status_lapak", status_lapak);
                params.put("telp", telp);
                params.put("logo", gambar);
                params.put("token", token);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}