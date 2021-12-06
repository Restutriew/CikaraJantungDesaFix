package com.cikarastudio.cikarajantungdesafix.ui.forum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cikarastudio.cikarajantungdesafix.R;
import com.cikarastudio.cikarajantungdesafix.adapter.ArtikelAdapter;
import com.cikarastudio.cikarajantungdesafix.adapter.ForumAdapter;
import com.cikarastudio.cikarajantungdesafix.adapter.ProdukAdapter;
import com.cikarastudio.cikarajantungdesafix.model.ArtikelModel;
import com.cikarastudio.cikarajantungdesafix.model.ForumModel;
import com.cikarastudio.cikarajantungdesafix.model.ProdukModel;
import com.cikarastudio.cikarajantungdesafix.session.SessionManager;
import com.cikarastudio.cikarajantungdesafix.ssl.HttpsTrustManager;
import com.cikarastudio.cikarajantungdesafix.ui.lapak.EditProdukActivity;
import com.cikarastudio.cikarajantungdesafix.ui.loadingdialog.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ForumFragment extends Fragment {

    LoadingDialog loadingDialog;
    RecyclerView rv_listForum;
    private ArrayList<ForumModel> forumList;
    private ForumAdapter forumAdapter;
    String link, linkGambar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_forum, container, false);

        HttpsTrustManager.allowAllSSL();

        //inisiasi link
        link = getString(R.string.link);
        linkGambar = getString(R.string.linkGambar);

        loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.startLoading();

        forumList = new ArrayList<>();
        rv_listForum = root.findViewById(R.id.rv_listForum);
        LinearLayoutManager linearLayoutManageraaa = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rv_listForum.setLayoutManager(linearLayoutManageraaa);
        rv_listForum.setHasFixedSize(true);

        loadForum();

        return root;
    }

    private void loadForum() {
        String URL_READ = link + "forum";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    //data produk
                                    String res_id = jsonObject.getString("id").trim();
                                    String res_nama = jsonObject.getString("nama").trim();
                                    String res_ketForum = jsonObject.getString("ket_forum").trim();
                                    String res_poto = jsonObject.getString("poto").trim();
                                    String res_status = jsonObject.getString("status").trim();
                                    String res_createdAt = jsonObject.getString("created_at").trim();
                                    String res_updatedAt = jsonObject.getString("updated_at").trim();

                                    String resi_poto = res_poto.replace(" ", "%20");
                                    Log.d("calpalnx", resi_poto);

                                    forumList.add(new ForumModel(res_id, res_nama, res_ketForum, resi_poto, res_status, res_createdAt, res_updatedAt));
                                    forumAdapter = new ForumAdapter(getContext(), forumList);
                                    rv_listForum.setAdapter(forumAdapter);
                                    //sort alfabet
//                                    sortAlfabet();
                                    forumAdapter.setOnItemClickCallback(new ForumAdapter.OnItemClickCallback() {
                                        @Override
                                        public void onItemClicked(ForumModel data) {
//                                            Intent transferDataProduk = new Intent(getActivity(), EditProdukActivity.class);
//                                            transferDataProduk.putExtra(EditProdukActivity.DATA_PRODUK, data);
//                                            startActivity(transferDataProduk);
                                        }
                                    });
                                    //hilangkan loading
//                                    loadingDialog.dissmissDialog();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Data Forum Tidak Ada!", Toast.LENGTH_SHORT).show();
//                                loadingDialog.dissmissDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            loadingDialog.dissmissDialog();
                            Toast.makeText(getActivity(), "Data Forum Tidak Ada!" +e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                loadingDialog.dissmissDialog();
                Toast.makeText(getActivity(), "Tidak Ada Koneksi Internet!" + error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
        };
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}