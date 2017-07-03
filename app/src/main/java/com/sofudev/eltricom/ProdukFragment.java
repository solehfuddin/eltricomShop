package com.sofudev.eltricom;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sofudev.eltricom.adapter.Adapter_produk;
import com.sofudev.eltricom.data.Data_produk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProdukFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final String URL_data = "http://192.168.43.36/lti/android/tampilkan_produk.php";

    public String kategori = null;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private SwipeRefreshLayout swipe;
    private RelativeLayout relativeLayout;

    private List<Data_produk> data_produks;

    public ProdukFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View thisView = inflater.inflate(R.layout.fragment_produk, container, false);
        getActivity().setTitle("List Produk");

        recyclerView    = (RecyclerView) thisView.findViewById(R.id.recyclerView_produk);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        swipe           = (SwipeRefreshLayout) thisView.findViewById(R.id.swipeRefresh_produk);
        data_produks    = new ArrayList<>();
        relativeLayout  = (RelativeLayout) thisView.findViewById(R.id.rl_produk);

        refreshSwipe();

        return thisView;
    }

    @Override
    public void onRefresh() {
        data_produks.clear();
        tampilkanProduk();
    }

    //Refresh data ketika activity pertama dijalankan
    private void refreshSwipe() {
        swipe.setOnRefreshListener(this);
        swipe.post(new Runnable() {
            @Override
            public void run() {
                data_produks.clear();
                swipe.setRefreshing(true);
                tampilkanProduk();
            }
        });
    }

    public void tampilkanProduk() {
        data_produks.clear();
        swipe.setRefreshing(true);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_data, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i =0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        Data_produk produk = new Data_produk(
                                jsonObject.getString("nama_produk"),
                                jsonObject.getString("harga"),
                                jsonObject.getString("spesifikasi"),
                                jsonObject.getString("gambar"),
                                jsonObject.getString("quantity")
                        );

                        data_produks.add(produk);
                    }

                    adapter = new Adapter_produk(data_produks, getContext());

                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Apabila data tidak ditemukan tampilkan gambar
                    ImageView imageView = new ImageView(getContext());
                    imageView.setImageResource(R.drawable.notfound);
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                    imageView.setLayoutParams(lp);
                    relativeLayout.addView(imageView);
                }
                swipe.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Inisialisasi Hashmap
                HashMap<String, String> hashMap = new HashMap<>();
                //Isi dengan kode_barang
                hashMap.put("namaproduk", kategori);
                //Eksekusi hashmap
                return hashMap;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}
