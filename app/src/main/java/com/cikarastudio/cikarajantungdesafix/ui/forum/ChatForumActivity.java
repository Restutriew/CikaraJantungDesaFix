package com.cikarastudio.cikarajantungdesafix.ui.forum;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cikarastudio.cikarajantungdesafix.R;
import com.cikarastudio.cikarajantungdesafix.adapter.ChatAdapter;
import com.cikarastudio.cikarajantungdesafix.model.ChatModel;
import com.cikarastudio.cikarajantungdesafix.model.ForumModel;
import com.cikarastudio.cikarajantungdesafix.session.SessionManager;
import com.cikarastudio.cikarajantungdesafix.ssl.HttpsTrustManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatForumActivity extends AppCompatActivity {

    public static final String DATA_FORUM = "extra_data";
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String TEXT = "text";
    private final Handler handler = new Handler();
    SessionManager sessionManager;
    ImageView img_back, img_kirimChat;
    String id_forum, nama_forum, link, id_user, token;
    RecyclerView rv_listChat;
    TextView tv_namaForum;
    EditText et_isiChat;
    String chat;
    private ArrayList<ChatModel> chatList;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_forum);

        HttpsTrustManager.allowAllSSL();

        sessionManager = new SessionManager(ChatForumActivity.this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        id_user = user.get(sessionManager.ID);

        chatList = new ArrayList<>();
        rv_listChat = findViewById(R.id.rv_listChat);
        LinearLayoutManager linearLayoutManageraaa = new LinearLayoutManager(ChatForumActivity.this);
        linearLayoutManageraaa.setStackFromEnd(true);
        rv_listChat.setLayoutManager(linearLayoutManageraaa);
        rv_listChat.setHasFixedSize(true);
        chatAdapter = new ChatAdapter(getApplicationContext(), chatList, id_user);
        rv_listChat.setAdapter(chatAdapter);

        ForumModel dataProduk = getIntent().getParcelableExtra(DATA_FORUM);
        id_forum = dataProduk.getId();
        nama_forum = dataProduk.getNama();
        Log.d("calpalnx", "onCreate: " + id_forum);

        et_isiChat = findViewById(R.id.et_isiChat);

        tv_namaForum = findViewById(R.id.tv_namaForum);
        tv_namaForum.setText(nama_forum);

        //inisiasi link
        link = getString(R.string.link);

        //inisiasi token
        token = getString(R.string.token);

        //refresh data
        doTheAutoRefresh();

        loadTextChat();
        updateTextChat();

        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        img_kirimChat = findViewById(R.id.img_kirimChat);
        img_kirimChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahchat();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveTextChat();
    }

    private void saveTextChat() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT, et_isiChat.getText().toString().trim());
        editor.apply();
    }

    private void loadTextChat() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        chat = sharedPreferences.getString(TEXT, "");
    }

    private void updateTextChat() {
        et_isiChat.setText(chat);
    }

    private void tambahchat() {
        final String isiChat = et_isiChat.getText().toString().trim();
        String URL_TAMBAHCHAT = link + "forum";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_TAMBAHCHAT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(ChatForumActivity.this, "Kirim Chat Sukses", Toast.LENGTH_LONG).show();
                                et_isiChat.setText("");
                                doTheAutoRefresh();
                                rv_listChat.smoothScrollToPosition(chatAdapter.getItemCount());
                            } else {
                                Toast.makeText(ChatForumActivity.this, "Kirim Chat Gagal!", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ChatForumActivity.this, "Kirim Chat Gagal!" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChatForumActivity.this, "Kirim Chat Gagal! : Cek Koneksi Anda" + error, Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", id_user);
                params.put("forum_id", id_forum);
                params.put("isi", isiChat);
                params.put("token", token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void doTheAutoRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadDataChat(); // this is where you put your refresh code
                doTheAutoRefresh();
            }
        }, 5000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataChat();
    }

    private void loadDataChat() {
        String URL_READ = link + "chatforum/" + id_forum + "?token=" + token;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (chatList.size() > 0) {
                            chatList.clear();
                        }
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    //data chat
                                    String res_id = jsonObject.getString("id").trim();
                                    String res_userId = jsonObject.getString("user_id").trim();
                                    String res_forumId = jsonObject.getString("forum_id").trim();
                                    String res_isi = jsonObject.getString("isi").trim();
                                    String res_createdAt = jsonObject.getString("created_at").trim();
                                    String res_updatedAt = jsonObject.getString("updated_at").trim();
                                    String res_namaPenduduk = jsonObject.getString("nama_penduduk").trim();

                                    chatList.add(new ChatModel(res_id, res_userId, res_forumId, res_isi, res_createdAt, res_updatedAt, res_namaPenduduk));
                                }
                                if (chatList.size() > 0) {
                                    chatAdapter.notifyDataSetChanged();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(ChatForumActivity.this, "Data Forum Tidak Ada!" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(ChatForumActivity.this, "Tidak Ada Koneksi Internet!" + error.toString(), Toast.LENGTH_LONG).show();
//                Log.d("calpalnx", "onErrorResponse: " + error.toString());
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 5 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(jsonString, cacheEntry);
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(String response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        };
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(ChatForumActivity.this);
        requestQueue.add(stringRequest);
    }
}