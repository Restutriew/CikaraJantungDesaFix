package com.cikarastudio.cikarajantungdesafix.ui.lapak;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TambahLapakActivity extends AppCompatActivity {

    ImageView img_back, img_tambahLapak, img_tambahPhotoLapak;
    SessionManager sessionManager;
    LoadingDialog loadingDialog;
    CardView cr_simpanTambahLapak;
    EditText et_namaLapakTambah, et_alamatLapakTambah, et_tentangLapakTambah, et_telpLapakTambah;
    String id_user, token, link;
    private Bitmap bitmap;

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
        img_tambahLapak = findViewById(R.id.img_tambahLapak);

        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        img_tambahPhotoLapak = findViewById(R.id.img_tambahPhotoLapak);
        img_tambahPhotoLapak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

        cr_simpanTambahLapak = findViewById(R.id.cr_simpanTambahLapak);
        cr_simpanTambahLapak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cekInputanTambahLapak();
            }
        });
    }


    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Foto"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filepath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                img_tambahLapak.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bitmap) {

        if (bitmap == null) {
            bitmap = ((BitmapDrawable) img_tambahLapak.getDrawable()).getBitmap();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return encodedImage;
    }

    private void cekInputanTambahLapak() {
        if (et_namaLapakTambah.isShown() && et_namaLapakTambah.getText().toString().equals("")) {
            et_namaLapakTambah.setError("Form ini harus diisi!");
            et_namaLapakTambah.requestFocus();
        } else if (et_alamatLapakTambah.isShown() && et_alamatLapakTambah.getText().toString().equals("")) {
            et_alamatLapakTambah.setError("Form ini harus diisi!");
            et_alamatLapakTambah.requestFocus();
        } else if (et_tentangLapakTambah.isShown() && et_tentangLapakTambah.getText().toString().equals("")) {
            et_tentangLapakTambah.setError("Form ini harus diisi!");
            et_tentangLapakTambah.requestFocus();
        } else if (et_telpLapakTambah.isShown() && et_telpLapakTambah.getText().toString().length() < 10) {
            et_telpLapakTambah.setError("Masukkan No Telepon/HP Dengan Benar!");
            et_telpLapakTambah.requestFocus();
        } else {
            tambahLapak();
        }
    }

    private void tambahLapak() {
        loadingDialog.startLoading();
        String URL_TAMBAHLAPAK = link + "lapak";
        final String namalapak = et_namaLapakTambah.getText().toString().trim();
        final String alamatLapak = et_alamatLapakTambah.getText().toString().trim();
        final String tentangLapak = et_tentangLapakTambah.getText().toString().trim();
        final String telp = et_telpLapakTambah.getText().toString().trim();
        final String status_lapak = "menunggu";

        final String gambar = "data:image/png;base64," + getStringImage(bitmap);

        Log.d("calpalnx", String.valueOf(id_user));
        Log.d("calpalnx", String.valueOf(namalapak));
        Log.d("calpalnx", String.valueOf(alamatLapak));
        Log.d("calpalnx", String.valueOf(tentangLapak));
        Log.d("calpalnx", String.valueOf(telp));
        Log.d("calpalnx", String.valueOf(gambar));
        Log.d("calpalnx", String.valueOf(token));
        Log.d("calpalnx", String.valueOf(status_lapak));

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
                        Toast.makeText(TambahLapakActivity.this, "Daftar Lapak Gagal! : Cek Koneksi Anda" + error.toString(), Toast.LENGTH_LONG).show();
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
                params.put("image", gambar);
                params.put("token", token);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}