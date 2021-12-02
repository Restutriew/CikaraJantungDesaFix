package com.cikarastudio.cikarajantungdesafix.ui.laporan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.cikarastudio.cikarajantungdesafix.ui.lapak.TambahLapakActivity;
import com.cikarastudio.cikarajantungdesafix.ui.loadingdialog.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TambahLaporanActivity extends AppCompatActivity {

    SessionManager sessionManager;
    LoadingDialog loadingDialog;
    String id_user, link, linkGambar, token, uploadBase64;
    Spinner sp_jenisLaporan, sp_identitasLaporan, sp_postingLaporan;
    EditText et_isilaporan;
    ImageView img_back, img_chooseTambahLaporan, img_frameTambahLaporan;
    CardView cr_tambahLaporan, cr_fotoLaporan;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_laporan);

        sp_jenisLaporan = findViewById(R.id.sp_jenisLaporan);
        sp_identitasLaporan = findViewById(R.id.sp_identitasLaporan);
        sp_postingLaporan = findViewById(R.id.sp_postingLaporan);
        et_isilaporan = findViewById(R.id.et_isilaporan);
        cr_tambahLaporan = findViewById(R.id.cr_tambahLaporan);
        img_chooseTambahLaporan = findViewById(R.id.img_chooseTambahLaporan);
        cr_fotoLaporan = findViewById(R.id.cr_fotoLaporan);
        img_frameTambahLaporan = findViewById(R.id.img_frameTambahLaporan);

        sessionManager = new SessionManager(TambahLaporanActivity.this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        id_user = user.get(sessionManager.ID);

        HttpsTrustManager.allowAllSSL();

        //inisiasi link
        link = getString(R.string.link);
        linkGambar = getString(R.string.linkGambar);
        token = getString(R.string.token);

        loadingDialog = new LoadingDialog(TambahLaporanActivity.this);

        ArrayList<String> kategoriList = (ArrayList<String>) getIntent().getSerializableExtra("cateList");
        kategoriList.set(0, "Pilih Kategori");

        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ArrayAdapter<String> jenisLaporanAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, kategoriList);
        sp_jenisLaporan.setAdapter(jenisLaporanAdapter);

        ArrayAdapter<String> yaTidakAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.ya_tidak));
        sp_identitasLaporan.setAdapter(yaTidakAdapter);
        sp_postingLaporan.setAdapter(yaTidakAdapter);

        cr_tambahLaporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cekInputEdittext();
//                tambahDataLaporan();
            }
        });

        img_chooseTambahLaporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

    }

    private void cekInputEdittext() {
        if (sp_jenisLaporan.isShown() && sp_jenisLaporan.getSelectedItem().toString().equalsIgnoreCase("pilih kategori")) {
            ((TextView) sp_jenisLaporan.getSelectedView()).setError("Pilih Kategori!");
            sp_jenisLaporan.getSelectedView().requestFocus();
            Toast.makeText(getApplicationContext(), "Data Belum Lengkap: Pilih Kategori Laporan!", Toast.LENGTH_SHORT).show();
        } else if (et_isilaporan.isShown() && et_isilaporan.getText().toString().equals("")) {
            et_isilaporan.setError("Form ini harus diisi!");
            et_isilaporan.requestFocus();
        } else if (sp_identitasLaporan.isShown() && sp_identitasLaporan.getSelectedItem().toString().equalsIgnoreCase("pilih")) {
            ((TextView) sp_identitasLaporan.getSelectedView()).setError("Pilih Identitas Laporan!");
            sp_identitasLaporan.getSelectedView().requestFocus();
            Toast.makeText(getApplicationContext(), "Data Belum Lengkap: Pilih Identitas Laporan!", Toast.LENGTH_SHORT).show();
        } else if (sp_postingLaporan.isShown() && sp_postingLaporan.getSelectedItem().toString().equalsIgnoreCase("pilih")) {
            ((TextView) sp_postingLaporan.getSelectedView()).setError("Pilih Posting Laporan!");
            sp_postingLaporan.getSelectedView().requestFocus();
            Toast.makeText(getApplicationContext(), "Data Belum Lengkap: Pilih Posting Laporan!", Toast.LENGTH_SHORT).show();
        } else {
            tambahDataLaporan();
        }
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
                img_frameTambahLaporan.setImageBitmap(bitmap);
                cr_fotoLaporan.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bitmap) {

        if (bitmap == null) {
            bitmap = ((BitmapDrawable) img_frameTambahLaporan.getDrawable()).getBitmap();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return encodedImage;
    }

    private void tambahDataLaporan() {
        loadingDialog.startLoading();
        String URL_TAMBAH = link + "lapor";
        final String isiLaporan = et_isilaporan.getText().toString().trim();
        final String jenisLaporan = sp_jenisLaporan.getSelectedItem().toString().toLowerCase();
        final String statusLaporan = "menunggu";
        final String identitasLaporan = sp_identitasLaporan.getSelectedItem().toString().toLowerCase();
        final String postingLaporan = sp_postingLaporan.getSelectedItem().toString().toLowerCase();
        if (cr_fotoLaporan.isShown()){
            uploadBase64 = "data:image/png;base64," + getStringImage(bitmap);
        }else{
            uploadBase64 = "data:image/png;base64,";
        }

        Log.d("calpalnx", String.valueOf(id_user));
        Log.d("calpalnx", String.valueOf(isiLaporan));
        Log.d("calpalnx", String.valueOf(jenisLaporan));
        Log.d("calpalnx", String.valueOf(statusLaporan));
        Log.d("calpalnx", String.valueOf(token));
        Log.d("calpalnx", String.valueOf(identitasLaporan));
        Log.d("calpalnx", String.valueOf(postingLaporan));

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_TAMBAH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(TambahLaporanActivity.this, "Pengajuan Laporan Sukses", Toast.LENGTH_LONG).show();
                                loadingDialog.dissmissDialog();
                                finish();
                            } else {
                                Toast.makeText(TambahLaporanActivity.this, "Pengajuan Laporan Gagal!", Toast.LENGTH_LONG).show();
                                loadingDialog.dissmissDialog();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TambahLaporanActivity.this, "Pengajuan Laporan Gagal!", Toast.LENGTH_LONG).show();
                            loadingDialog.dissmissDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TambahLaporanActivity.this, "Pengajuan Laporan Gagal! : Cek Koneksi Anda", Toast.LENGTH_LONG).show();
                        loadingDialog.dissmissDialog();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", id_user);
                params.put("isi", isiLaporan);
                params.put("kategori", jenisLaporan);
                params.put("status", statusLaporan);
                params.put("token", token);
                params.put("image", uploadBase64);
                params.put("identitas", identitasLaporan);
                params.put("posting", postingLaporan);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}