package com.cikarastudio.cikarajantungdesafix.ui.lapak;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.Build;
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
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cikarastudio.cikarajantungdesafix.R;
import com.cikarastudio.cikarajantungdesafix.model.ProdukModel;
import com.cikarastudio.cikarajantungdesafix.ssl.HttpsTrustManager;
import com.cikarastudio.cikarajantungdesafix.template.kima.deletereq.CustomHurlStack;
import com.cikarastudio.cikarajantungdesafix.ui.loadingdialog.LoadingDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditProdukActivity extends AppCompatActivity {

    LoadingDialog loadingDialog;
    public static final String DATA_PRODUK = "extra_data";
    String id, id_lapak, nama, keterangan, gambar,
            token, link, linkGambar;
    Integer harga, dilihat;
    EditText et_namaProduk, et_hargaProduk, et_keteranganProduk;
    ImageView img_back, img_editProduk, img_editPhotoProduk;
    CardView cr_simpanProduk, cr_hapusProduk;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_produk);

        //allow ssl
        HttpsTrustManager.allowAllSSL();

        //inisiasi link
        link = getString(R.string.link);
        linkGambar = getString(R.string.linkGambar);

        //inisiasi loadingDialog
        loadingDialog = new LoadingDialog(EditProdukActivity.this);

        //string token
        token = getString(R.string.token);

        //get data produk dari list
        ProdukModel dataProduk = getIntent().getParcelableExtra(DATA_PRODUK);
        id = dataProduk.getId();
        id_lapak = dataProduk.getLapak_id();
        nama = dataProduk.getNama();
        keterangan = dataProduk.getKeterangan();
        gambar = dataProduk.getGambar();
        harga = dataProduk.getHarga();
        dilihat = dataProduk.getDilihat();

        et_namaProduk = findViewById(R.id.et_namaProduk);
        et_hargaProduk = findViewById(R.id.et_hargaProduk);
        et_keteranganProduk = findViewById(R.id.et_keteranganProduk);
        img_editProduk = findViewById(R.id.img_editProduk);

        et_namaProduk.setText(nama);
        et_hargaProduk.setText(String.valueOf(harga));
        et_keteranganProduk.setText(keterangan);

        String imageUrl = linkGambar + "penduduk/produk/" + gambar;
        Picasso.with(EditProdukActivity.this).load(imageUrl).fit().centerCrop().into(img_editProduk);

        img_editPhotoProduk = findViewById(R.id.img_editPhotoProduk);
        img_editPhotoProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

        cr_simpanProduk = findViewById(R.id.cr_simpanProduk);
        cr_simpanProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.startLoading();
                simpanEditProduk();
            }
        });

        cr_hapusProduk = findViewById(R.id.cr_hapusProduk);
        cr_hapusProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.startLoading();
                dialogDelete();
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
                img_editProduk.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return encodedImage;
    }

    private void dialogDelete() {
        AlertDialog.Builder alertdialogBuilder = new AlertDialog.Builder(EditProdukActivity.this);
        alertdialogBuilder.setTitle("Konfismasi Delete");
        alertdialogBuilder.setMessage("Apakah Anda Yakin Menghapus Data Ini?");
        alertdialogBuilder.setCancelable(false);
        alertdialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                loadingDialog.startLoading();
                hapusData();
            }
        });
        alertdialogBuilder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alertDialog = alertdialogBuilder.create();
        alertDialog.show();
    }

    private void hapusData() {
        Log.d("calpalnx", String.valueOf(id));
        String URL_DELETEPRODUK = link + "produk/" + id;
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, URL_DELETEPRODUK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                Toast.makeText(EditProdukActivity.this, "Hapus Produk Sukses", Toast.LENGTH_LONG).show();
//                                loadingDialog.dissmissDialog();
//                                finish();
                            } else {
                                Toast.makeText(EditProdukActivity.this, "Hapus Produk Gagal!", Toast.LENGTH_LONG).show();
                                loadingDialog.dissmissDialog();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EditProdukActivity.this, "Hapus Produk Gagal! : " + e.toString(), Toast.LENGTH_LONG).show();
                            loadingDialog.dissmissDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditProdukActivity.this, "Hapus Produk Gagal! : Cek Koneksi Anda, " + error, Toast.LENGTH_LONG).show();
                        loadingDialog.dissmissDialog();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                return params;
            }
        };

        HttpStack httpStack;
        if (Build.VERSION.SDK_INT > 19) {
            httpStack = new CustomHurlStack();
            //disable okhttphurlstack
//        } else if (Build.VERSION.SDK_INT >= 9 && Build.VERSION.SDK_INT <= 19)
//        {
//            httpStack = new OkHttpHurlStack();
        } else {
            httpStack = new HttpClientStack(AndroidHttpClient.newInstance("Android"));
        }
        RequestQueue requestQueue = Volley.newRequestQueue(EditProdukActivity.this, httpStack);
        requestQueue.add(stringRequest);

    }

    private void simpanEditProduk() {
        final String inp_lapak_id = id_lapak;
        final String inp_namaProduk = et_namaProduk.getText().toString().trim();
        final String inp_keteranganProduk = et_keteranganProduk.getText().toString().trim();
//        final String inp_gambarProduk = gambar;
        final String inp_hargaProduk = et_hargaProduk.getText().toString().trim();
        final String inp_dilihatProduk = String.valueOf(dilihat);

        final String inp_gambarProduk = "data:image/png;base64," + getStringImage(bitmap);

        Log.d("calpalnx", String.valueOf(id));
        Log.d("calpalnx", String.valueOf(inp_lapak_id));
        Log.d("calpalnx", String.valueOf(inp_namaProduk));
        Log.d("calpalnx", String.valueOf(inp_keteranganProduk));
        Log.d("calpalnx", String.valueOf(inp_gambarProduk));
        Log.d("calpalnx", String.valueOf(inp_hargaProduk));
        Log.d("calpalnx", String.valueOf(inp_dilihatProduk));

        String URL_EDITPRODUK = link + "produk/" + id;
        StringRequest stringRequest = new StringRequest(Request.Method.PATCH, URL_EDITPRODUK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(EditProdukActivity.this, "Edit Produk Sukses", Toast.LENGTH_LONG).show();
                                loadingDialog.dissmissDialog();
                                finish();
                            } else {
                                Toast.makeText(EditProdukActivity.this, "Edit Produk Gagal!", Toast.LENGTH_LONG).show();
                                loadingDialog.dissmissDialog();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EditProdukActivity.this, "Edit Produk Gagal! : " + e.toString(), Toast.LENGTH_LONG).show();
                            loadingDialog.dissmissDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditProdukActivity.this, "Tambah Produk Gagal! : Cek Koneksi Anda, " + error, Toast.LENGTH_LONG).show();
                        loadingDialog.dissmissDialog();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama", inp_namaProduk);
                params.put("harga", inp_hargaProduk);
                params.put("keterangan", inp_keteranganProduk);
                params.put("image", inp_gambarProduk);
                params.put("token", token);
//                params.put("dilihat", dilihat);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}
