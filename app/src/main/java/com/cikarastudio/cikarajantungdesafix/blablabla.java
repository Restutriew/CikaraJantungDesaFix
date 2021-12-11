////package com.cikarastudio.cikarajantungdesafix;
////
////import android.app.ProgressDialog;
////
////import com.android.volley.Response;
////import com.android.volley.toolbox.JsonArrayRequest;
////
////public class blablabla {
////
////    private void getData() {
////        final ProgressDialog progressDialog = new ProgressDialog(this);
////        progressDialog.setMessage("Loading...");
////        progressDialog.show();
////
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                for (int i = 0; i < response.length(); i++) {
//                    try {
//                        JSONObject jsonObject = response.getJSONObject(i);
//
//                        Movie movie = new Movie();
//                        movie.setTitle(jsonObject.getString("title"));
//                        movie.setRating(jsonObject.getInt("rating"));
//                        movie.setYear(jsonObject.getInt("releaseYear"));
//
//                        movieList.add(movie);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        progressDialog.dismiss();
//                    }
//                }
//                adapter.notifyDataSetChanged();
//                progressDialog.dismiss();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("Volley", error.toString());
//                progressDialog.dismiss();
//            }
//        })
////        {
////            @Override
////            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
////                try {
////                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
////                    if (cacheEntry == null) {
////                        cacheEntry = new Cache.Entry();
////                    }
////                    final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
////                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
////                    long now = System.currentTimeMillis();
////                    final long softExpire = now + cacheHitButRefreshed;
////                    final long ttl = now + cacheExpired;
////                    cacheEntry.data = response.data;
////                    cacheEntry.softTtl = softExpire;
////                    cacheEntry.ttl = ttl;
////                    String headerValue;
////                    headerValue = response.headers.get("Date");
////                    if (headerValue != null) {
////                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
////                    }
////                    headerValue = response.headers.get("Last-Modified");
////                    if (headerValue != null) {
////                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
////                    }
////                    cacheEntry.responseHeaders = response.headers;
////                    final String jsonString = new String(response.data,
////                            HttpHeaderParser.parseCharset(response.headers));
////                    return Response.success(new JSONArray(jsonString), cacheEntry);
////                } catch (UnsupportedEncodingException | JSONException e) {
////                    return Response.error(new ParseError(e));
////                }
////            }
////
////            @Override
////            protected void deliverResponse(JSONArray response) {
////                super.deliverResponse(response);
////            }
////
////            @Override
////            public void deliverError(VolleyError error) {
////                super.deliverError(error);
////            }
////
////            @Override
////            protected VolleyError parseNetworkError(VolleyError volleyError) {
////                return super.parseNetworkError(volleyError);
////            }
////        };
////        RequestQueue requestQueue = Volley.newRequestQueue(this);
////        requestQueue.add(jsonArrayRequest);
////    }
////
////}
