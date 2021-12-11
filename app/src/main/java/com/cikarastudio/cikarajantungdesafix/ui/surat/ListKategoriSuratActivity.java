package com.cikarastudio.cikarajantungdesafix.ui.surat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cikarastudio.cikarajantungdesafix.R;
import com.cikarastudio.cikarajantungdesafix.ui.loadingdialog.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListKategoriSuratActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView img_back;
    CardView cr_suratKeterangan, cr_suratPengantar, cr_suratRekomendasi, cr_suratLainnya;
    LoadingDialog loadingDialog;
    String link, token, jumlahSuratKeterangan, jumlahSuratPengantar, jumlahSuratRekomendasi, jumlahSuratLainnya;
    int intSuratKeterangan, intSuratPengantar, intSuratRekomendasi, intSuratLainnya;
    TextView tv_jumlahKeterangan, tv_jumlahPengantar, tv_jumlahRekomendasi, tv_jumlahLainnya;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_kategori_surat);

        loadingDialog = new LoadingDialog(ListKategoriSuratActivity.this);
        loadingDialog.startLoading();

        //inisiasi link
        link = getString(R.string.link);

        //inisiasi token
        token = getString(R.string.token);

        tv_jumlahKeterangan = findViewById(R.id.tv_jumlahKeterangan);
        tv_jumlahPengantar = findViewById(R.id.tv_jumlahPengantar);
        tv_jumlahRekomendasi = findViewById(R.id.tv_jumlahRekomendasi);
        tv_jumlahLainnya = findViewById(R.id.tv_jumlahLainnya);

        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(this);

        cr_suratKeterangan = findViewById(R.id.cr_suratKeterangan);
        cr_suratKeterangan.setOnClickListener(this);

        cr_suratPengantar = findViewById(R.id.cr_suratPengantar);
        cr_suratPengantar.setOnClickListener(this);

        cr_suratRekomendasi = findViewById(R.id.cr_suratRekomendasi);
        cr_suratRekomendasi.setOnClickListener(this);

        cr_suratLainnya = findViewById(R.id.cr_suratLainnya);
        cr_suratLainnya.setOnClickListener(this);

        loadDataJumlahSurat();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                // do your code
                finish();
                break;
            case R.id.cr_suratKeterangan:
                // do your code
                Intent keListTambahSuratKeterangan = new Intent(ListKategoriSuratActivity.this, ListTambahSuratActivity.class);
                keListTambahSuratKeterangan.putExtra("kategori_surat", "surat keterangan");
                startActivity(keListTambahSuratKeterangan);
                break;
            case R.id.cr_suratPengantar:
                // do your code
                Intent keListTambahSuratPengantar = new Intent(ListKategoriSuratActivity.this, ListTambahSuratActivity.class);
                keListTambahSuratPengantar.putExtra("kategori_surat", "surat pengantar");
                startActivity(keListTambahSuratPengantar);
                break;
            case R.id.cr_suratRekomendasi:
                // do your code
                Intent keListTambahSuratRekomendasi = new Intent(ListKategoriSuratActivity.this, ListTambahSuratActivity.class);
                keListTambahSuratRekomendasi.putExtra("kategori_surat", "surat rekomendasi");
                startActivity(keListTambahSuratRekomendasi);
                break;
            case R.id.cr_suratLainnya:
                // do your code
                Intent keListTambahSuratLainnya = new Intent(ListKategoriSuratActivity.this, ListTambahSuratActivity.class);
                keListTambahSuratLainnya.putExtra("kategori_surat", "surat lainnya");
                startActivity(keListTambahSuratLainnya);
                break;
            default:
                break;
        }
    }

    private void loadDataJumlahSurat() {
        String URL_READ = link + "listformatsurat?token=" + token;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    //data format surat
                                    String res_id = jsonObject.getString("id").trim();
                                    String res_kode = jsonObject.getString("kode").trim();
                                    String res_klasifikasisuratId = jsonObject.getString("klasifikasisurat_id").trim();
                                    String res_namaSurat = jsonObject.getString("nama_surat").trim();
                                    String res_nilaiMasaBerlaku = jsonObject.getString("nilai_masaberlaku").trim();
                                    String res_statusMasaBerlaku = jsonObject.getString("status_masaberlaku").trim();
                                    String res_layananMandiri = jsonObject.getString("layanan_mandiri").trim();
                                    String res_fileSurat = jsonObject.getString("file_surat").trim();
                                    String res_kategori = jsonObject.getString("kategori").trim();
                                    String res_createdAt = jsonObject.getString("created_at").trim();
                                    String res_updatedAt = jsonObject.getString("updated_at").trim();

                                    switch (res_kategori) {
                                        case "surat keterangan":
                                            intSuratKeterangan++;
                                            break;
                                        case "surat pengantar":
                                            intSuratPengantar++;
                                            break;
                                        case "surat rekomendasi":
                                            intSuratRekomendasi++;
                                            break;
                                        case "surat lainnya":
                                            intSuratLainnya++;
                                            break;
                                    }

                                    //hilangkan loading
                                    loadingDialog.dissmissDialog();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Data List Surat Tidak Ada!", Toast.LENGTH_SHORT).show();
                                loadingDialog.dissmissDialog();
                            }

                            Log.d("calpalnx", "onResponse: " + String.valueOf(intSuratKeterangan));

                            jumlahSuratKeterangan = String.valueOf(intSuratKeterangan);
                            jumlahSuratPengantar = String.valueOf(intSuratPengantar);
                            jumlahSuratRekomendasi = String.valueOf(intSuratRekomendasi);
                            jumlahSuratLainnya = String.valueOf(intSuratLainnya);

                            tv_jumlahKeterangan.setText(jumlahSuratKeterangan + " jenis surat");
                            tv_jumlahPengantar.setText(jumlahSuratPengantar + " jenis surat");
                            tv_jumlahRekomendasi.setText(jumlahSuratRekomendasi + " jumlah surat");
                            tv_jumlahLainnya.setText(jumlahSuratLainnya + " jumlah surat");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadingDialog.dissmissDialog();
                            Toast.makeText(getApplicationContext(), "Data List Surat Tidak Ada!", Toast.LENGTH_LONG).show();
                        }

                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dissmissDialog();
                Toast.makeText(getApplicationContext(), "Tidak Ada Koneksi Internet!" + error, Toast.LENGTH_LONG).show();
            }
        }) {
        };
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}