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
import com.cikarastudio.cikarajantungdesafix.ssl.HttpsTrustManager;
import com.cikarastudio.cikarajantungdesafix.ui.loadingdialog.LoadingDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditLapakActivity extends AppCompatActivity {

    ImageView img_back, img_editLapak, img_editPhotoLapak;
    LoadingDialog loadingDialog;
    CardView cr_simpanEditLapak;
    EditText et_namaLapakEdit, et_alamatLapakEdit, et_tentangLapakEdit, et_telpLapakEdit;
    String id_lapak, nama_lapak, alamat_lapak, tentang_lapak, telp_lapak, logo_lapak, status_lapak,
            token, link, linkGambar;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_lapak);

        loadingDialog = new LoadingDialog(EditLapakActivity.this);

        HttpsTrustManager.allowAllSSL();

        //inisiasi link
        link = getString(R.string.link);
        linkGambar = getString(R.string.linkGambar);

        token = getString(R.string.token);

        Intent intent = getIntent();
        id_lapak = intent.getStringExtra("id_lapak");
        nama_lapak = intent.getStringExtra("nama_lapak");
        alamat_lapak = intent.getStringExtra("alamat_lapak");
        tentang_lapak = intent.getStringExtra("tentang_lapak");
        telp_lapak = intent.getStringExtra("telp_lapak");
        logo_lapak = intent.getStringExtra("logo_lapak");
        status_lapak = intent.getStringExtra("status_lapak");

        et_namaLapakEdit = findViewById(R.id.et_namaLapakEdit);
        et_alamatLapakEdit = findViewById(R.id.et_alamatLapakEdit);
        et_tentangLapakEdit = findViewById(R.id.et_tentangLapakEdit);
        et_telpLapakEdit = findViewById(R.id.et_telpLapakEdit);
        img_editLapak = findViewById(R.id.img_editLapak);

        et_namaLapakEdit.setText(nama_lapak);
        et_alamatLapakEdit.setText(alamat_lapak);
        et_tentangLapakEdit.setText(tentang_lapak);
        et_telpLapakEdit.setText(telp_lapak);

        String imageUrl = linkGambar + "penduduk/lapak/" + logo_lapak;
        Picasso.with(EditLapakActivity.this).load(imageUrl).fit().centerCrop().into(img_editLapak);

        cr_simpanEditLapak = findViewById(R.id.cr_simpanEditLapak);
        cr_simpanEditLapak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cekInputanEditLapak();
            }
        });

        img_editPhotoLapak = findViewById(R.id.img_editPhotoLapak);
        img_editPhotoLapak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                img_editLapak.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bitmap) {

        if (bitmap == null) {
            bitmap = ((BitmapDrawable) img_editLapak.getDrawable()).getBitmap();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return encodedImage;
    }

    private void cekInputanEditLapak() {
        if (et_namaLapakEdit.isShown() && et_namaLapakEdit.getText().toString().equals("")) {
            et_namaLapakEdit.setError("Form ini harus diisi!");
            et_namaLapakEdit.requestFocus();
        } else if (et_alamatLapakEdit.isShown() && et_alamatLapakEdit.getText().toString().equals("")) {
            et_alamatLapakEdit.setError("Form ini harus diisi!");
            et_alamatLapakEdit.requestFocus();
        } else if (et_tentangLapakEdit.isShown() && et_tentangLapakEdit.getText().toString().equals("")) {
            et_tentangLapakEdit.setError("Form ini harus diisi!");
            et_tentangLapakEdit.requestFocus();
        } else if (et_telpLapakEdit.isShown() && et_telpLapakEdit.getText().toString().length() < 10) {
            et_telpLapakEdit.setError("Masukkan No Telepon/HP Dengan Benar!");
            et_telpLapakEdit.requestFocus();
        } else {
            editLapak();
        }
    }

    private void editLapak() {
        loadingDialog.startLoading();
        final String inp_lapak_id = id_lapak;
        final String inp_namaLapak = et_namaLapakEdit.getText().toString().trim();
        final String inp_alamatLapak = et_alamatLapakEdit.getText().toString().trim();
        final String inp_tentangLapak = et_tentangLapakEdit.getText().toString().trim();
        final String inp_telpLapak = et_telpLapakEdit.getText().toString().trim();
        final String inp_statusLapak = "menunggu";

        final String inp_logoLapak = "data:image/png;base64," + getStringImage(bitmap);

        Log.d("calpalnx", String.valueOf(inp_lapak_id));
        Log.d("calpalnx", String.valueOf(inp_namaLapak));
        Log.d("calpalnx", String.valueOf(inp_alamatLapak));
        Log.d("calpalnx", String.valueOf(inp_tentangLapak));
        Log.d("calpalnx", String.valueOf(inp_telpLapak));
        Log.d("calpalnx", String.valueOf(inp_logoLapak));
        Log.d("calpalnx", String.valueOf(inp_statusLapak));

        String URL_EDITLAPAK = link + "lapak/" + inp_lapak_id;
        StringRequest stringRequest = new StringRequest(Request.Method.PATCH, URL_EDITLAPAK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(EditLapakActivity.this, "Edit Lapak Sukses", Toast.LENGTH_LONG).show();
                                loadingDialog.dissmissDialog();
                                finish();
                            } else {
                                Toast.makeText(EditLapakActivity.this, "Edit Lapak Gagal!", Toast.LENGTH_LONG).show();
                                loadingDialog.dissmissDialog();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EditLapakActivity.this, "Edit Lapak Gagal! : " + e.toString(), Toast.LENGTH_LONG).show();
                            loadingDialog.dissmissDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditLapakActivity.this, "Tambah Lapak Gagal! : Cek Koneksi Anda, " + error, Toast.LENGTH_LONG).show();
                        loadingDialog.dissmissDialog();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama_lapak", inp_namaLapak);
                params.put("tentang", inp_tentangLapak);
                params.put("alamat", inp_alamatLapak);
                params.put("status_lapak", inp_statusLapak);
                params.put("telp", inp_telpLapak);
                params.put("image", inp_logoLapak);
                params.put("token", token);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}