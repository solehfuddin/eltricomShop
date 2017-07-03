package com.sofudev.eltricom;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sofudev.eltricom.adapter.Adapter_riwayat;
import com.sofudev.eltricom.app.AppController;
import com.sofudev.eltricom.data.Data_riwayat;

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
public class RiwayatFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    //Inisialisasi Objek
    ListView listView;
    SwipeRefreshLayout swipe;
    RelativeLayout rl;

    //Inisialisasi List dan Adapter
    List<Data_riwayat> data_riwayatList = new ArrayList<>();
    Adapter_riwayat adapter_riwayat;

    //CONFIG URL, USERNAME & PASSWORD ke WEB API (DATABASE)
    //private static final String TAMPILKAN_URL   = "http://articdecoration.com/lti/android/tampilkan_riwayat.php";
    private static final String TAMPILKAN_URL   = "http://192.168.43.36/lti/android/tampilkan_riwayat.php";

    private static final String ID_PESANAN      = "id_pesanan";
    private static final String BARANG_PESANAN  = "nama_produk";
    private static final String JUMLAH_PESANAN  = "jumlah";
    private static final String EXPEDISI_PESANAN= "ekspedisi";
    private static final String STATUS_PESANAN  = "status";
    private static final String TANGGAL_PESANAN = "tanggal";
    private static final String USERNAME        = "username";

    private String userOnline;

    public RiwayatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View thisView = inflater.inflate(R.layout.fragment_riwayat, container, false);
        getActivity().setTitle("Riwayat Pembelian");

        //Hubungkan XML Dengan JAVA
        listView    = (ListView) thisView.findViewById(R.id.listview_riwayat);
        swipe       = (SwipeRefreshLayout) thisView.findViewById(R.id.swipeRefresh_riwayat);
        rl          = (RelativeLayout) thisView.findViewById(R.id.rl_riwayat);

        //Tampung objek ke dalam listview
        adapter_riwayat = new Adapter_riwayat(getActivity(), data_riwayatList);
        listView.setAdapter(adapter_riwayat);

        userOnline = getArguments().getString("userAktif");

        refreshSwipe();
        return thisView;
    }

    @Override
    public void onRefresh() {
        data_riwayatList.clear();
        adapter_riwayat.notifyDataSetChanged();
        showData();
    }

    //Refresh data ketika activity pertama dijalankan
    private void refreshSwipe() {
        swipe.setOnRefreshListener(this);
        swipe.post(new Runnable() {
            @Override
            public void run() {
                data_riwayatList.clear();
                swipe.setRefreshing(true);
                adapter_riwayat.notifyDataSetChanged();
                showData();
            }
        });
    }

    private void showData() {
        //Inisialisasi refresh swipe
        data_riwayatList.clear();
        adapter_riwayat.notifyDataSetChanged();
        swipe.setRefreshing(true);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, TAMPILKAN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    try {

                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            Integer status = Integer.parseInt(jsonObject.getString(STATUS_PESANAN));
                            String hasil;

                            if (status == 2) {
                                hasil = "Verifikasi";
                            }
                            else {
                                hasil = "Belum Verifikasi";
                            }

                            //Panggil objek Data Riwayat
                            Data_riwayat data_riwayat = new Data_riwayat();

                            //Simpan data dari json ke objek
                            data_riwayat.setId_pesanan(jsonObject.getString(ID_PESANAN));
                            data_riwayat.setBarang_pesanan(jsonObject.getString(BARANG_PESANAN));
                            data_riwayat.setJumlah_pesanan(jsonObject.getString(JUMLAH_PESANAN));
                            data_riwayat.setExpedisi_pesanan(jsonObject.getString(EXPEDISI_PESANAN));
                            data_riwayat.setStatus_pesanan(hasil);
                            data_riwayat.setTanggal_pesanan(jsonObject.getString(TANGGAL_PESANAN));

                            //Masukkan data ke dalam list
                            data_riwayatList.add(data_riwayat);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //Apabila data tidak ditemukan tampilkan gambar
                        ImageView imageView = new ImageView(getContext());
                        imageView.setImageResource(R.drawable.notfound);
                        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                        imageView.setLayoutParams(lp);
                        rl.addView(imageView);
                    }

                    //Notifikasi perubahan data
                    adapter_riwayat.notifyDataSetChanged();
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
                hashMap.put(USERNAME, userOnline);
                //Eksekusi hashmap
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
