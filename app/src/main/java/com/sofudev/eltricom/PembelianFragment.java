package com.sofudev.eltricom;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sofudev.eltricom.adapter.Adapter_pembelian;
import com.sofudev.eltricom.app.AppController;
import com.sofudev.eltricom.data.Data_pembelian;

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
public class PembelianFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    //Inisialisasi Objek
    ListView listView;
    SwipeRefreshLayout swipe;
    RelativeLayout rl;

    //Inisialisasi List dan Adapter
    List<Data_pembelian> data_pembelianList = new ArrayList<>();
    Adapter_pembelian adapter_pembelian;

    //CONFIG URL, USERNAME & PASSWORD ke WEB API (DATABASE)
    //private static final String TAMPILKAN_URL   = "http://articdecoration.com/lti/android/tampilkan_pembelian.php";
    //private static final String PILIH_URL       = "http://articdecoration.com/lti/android/pilih_pembelian.php";
    private static final String TAMPILKAN_URL   = "http://192.168.43.36/lti/android/tampilkan_pembelian.php";
    private static final String PILIH_URL       = "http://192.168.43.36/lti/android/pilih_pembelian.php";

    private static final String ID_PESANAN      = "id_pesanan";
    private static final String BARANG_PESANAN  = "nama_produk";
    private static final String JUMLAH_PESANAN  = "jumlah";
    private static final String EXPEDISI_PESANAN= "ekspedisi";
    private static final String TANGGAL_PESANAN = "tanggal";
    //private static final String BIAYA_PESANAN   = "total_biaya";
    private static final String BIAYA_PESANAN   = "harga";
    private static final String USERNAME        = "username";

    //Konfigurasi TAG SUCCESS dan TAG MESSAGE
    private static final String TAG_SUCCESS         = "success";
    private static final String TAG_MESSAGE         = "message";

    //Inisialisasi Success value
    int success;

    private String userOnline;

    //Inisialisasi Dialog Builder
    AlertDialog.Builder dialog;

    public PembelianFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View thisView = inflater.inflate(R.layout.fragment_pembelian, container, false);
        getActivity().setTitle("Pembelian");

        //Hubungkan XML Dengan JAVA
        listView    = (ListView) thisView.findViewById(R.id.listview_pembelian);
        swipe       = (SwipeRefreshLayout) thisView.findViewById(R.id.swipeRefresh_pembelian);
        rl          = (RelativeLayout) thisView.findViewById(R.id.rl_pembelian);

        //Tampung objek ke dalam listview
        adapter_pembelian = new Adapter_pembelian(getActivity(), data_pembelianList);
        listView.setAdapter(adapter_pembelian);

        userOnline = getArguments().getString("userAktif");

        refreshSwipe();
        chooseAction();
        return thisView;
    }

    @Override
    public void onRefresh() {
        data_pembelianList.clear();
        adapter_pembelian.notifyDataSetChanged();
        showData();
    }

    //Refresh data ketika activity pertama dijalankan
    private void refreshSwipe() {
        swipe.setOnRefreshListener(this);
        swipe.post(new Runnable() {
            @Override
            public void run() {
                data_pembelianList.clear();
                swipe.setRefreshing(true);
                adapter_pembelian.notifyDataSetChanged();
                showData();
            }
        });
    }

    private void showData() {
        data_pembelianList.clear();
        swipe.setRefreshing(true);
        adapter_pembelian.notifyDataSetChanged();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, TAMPILKAN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        //Panggil objek Data Riwayat
                        Data_pembelian data_pembelian = new Data_pembelian();

                        //Simpan data dari json ke objek
                        data_pembelian.setId_pesanan(jsonObject.getString(ID_PESANAN));
                        data_pembelian.setBarang_pesanan(jsonObject.getString(BARANG_PESANAN));
                        data_pembelian.setJumlah_pesanan(jsonObject.getString(JUMLAH_PESANAN));
                        data_pembelian.setExpedisi_pesanan(jsonObject.getString(EXPEDISI_PESANAN));
                        data_pembelian.setTanggal_pesanan(jsonObject.getString(TANGGAL_PESANAN));
                        data_pembelian.setTotal_biaya(jsonObject.getString(BIAYA_PESANAN));

                        //Masukkan data ke dalam list
                        data_pembelianList.add(data_pembelian);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    //Apabila data tidak ditemukan tampilkan gambar
                    ImageView imageView = new ImageView(getContext());
                    imageView.setImageResource(R.drawable.notfound);
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                    imageView.setLayoutParams(lp);
                    rl.addView(imageView);
                }

                //Notifikasi perubahan data
                adapter_pembelian.notifyDataSetChanged();
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

    //Fungsi untuk menampilkan POPUP KONFIRMASI
    private void chooseAction() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Inisialisasi Kode Barang Yang dipilih
                final String id_pesanan = data_pembelianList.get(position).getId_pesanan();

                //Pilihan ketika POPUP Tampil
                final CharSequence [] dialogitem = {"Konfirmasi Pembayaran"};

                //Buat dialog
                dialog = new AlertDialog.Builder(getActivity());
                dialog.setCancelable(true);

                //Pilihan ketika edit atau delete ditekan
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Link ketika pilihan ditekan
                        switch (which) {
                            case 0:
                                konfirmasi(id_pesanan);
                                break;
                        }
                    }
                });

                //Tampilan dialognya
                dialog.show();

                return false;
            }
        });
    }

    private void konfirmasi (final String id_pesanan) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PILIH_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //Buat objek JSON
                    JSONObject jsonObject = new JSONObject(response);
                    //Ambil nilai success
                    success = jsonObject.getInt(TAG_SUCCESS);

                    //Pilihan jika sukses dan gagal
                    if (success == 1) {
                        //Tampung seluruh record dari json
                        String id_pesanan      = jsonObject.getString(ID_PESANAN);

                        //Buat intent dan kirimkan record ke FormBarangMasukActivity
                        Intent intent = new Intent(getContext(), KonfirmasiBayarActivity.class);
                        intent.putExtra("id_pesanan", id_pesanan);

                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getContext(), jsonObject.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap <String, String> hashMap = new HashMap<>();
                hashMap.put(ID_PESANAN, id_pesanan);
                return hashMap;
            }
        };

        //Tambahkan stringRequest ke RequestQueue di AppController
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
