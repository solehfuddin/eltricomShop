package com.sofudev.eltricom;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class TestimonialFragment extends Fragment {

    TextInputLayout input_nama, input_pesan;
    EditText txt_nama, txt_pesan;
    Button btn_kirim;

    //Objek RequestQueue dan StringRequest
    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    //CONFIG URL, USERNAME & PASSWORD ke WEB API (DATABASE)
    //private static final String TAMBAH_URL = "http://articdecoration.com/lti/android/tambah_testimoni.php";
    private static final String TAMBAH_URL = "http://192.168.43.36/lti/android/tambah_testimoni.php";
    private static final String NAMA  = "nama";
    private static final String PESAN = "pesan";

    public TestimonialFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Mengubah judul fragment
        final View view = inflater.inflate(R.layout.fragment_testimonial, container, false);
        getActivity().setTitle("Konfirmasi Barang");

        input_nama = (TextInputLayout) view.findViewById(R.id.testimoni_input_nama);
        input_pesan = (TextInputLayout) view.findViewById(R.id.testimoni_input_pesan);
        txt_nama = (EditText) view.findViewById(R.id.testimoni_txt_nama);
        txt_pesan = (EditText) view.findViewById(R.id.testimoni_txt_pesan);
        btn_kirim = (Button) view.findViewById(R.id.testimoni_btn_kirim);

        simpanTestimoni();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void simpanTestimoni () {
        //Panggil objek requestQueue pada login activity
        requestQueue = Volley.newRequestQueue(getContext());

        btn_kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dt_nama = input_nama.getEditText().getText().toString();
                String dt_pesan = input_pesan.getEditText().getText().toString();

                if (dt_nama.isEmpty()) {
                    txt_nama.setError("Nama belum diisi");
                }
                else if (dt_pesan.isEmpty()) {
                    txt_pesan.setError("Pesan belum diisi");
                }
                else {
                    stringRequest = new StringRequest(Request.Method.POST, TAMBAH_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.names().get(0).equals("success")) {
                                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                    alert.setTitle("Informasi");
                                    alert.setMessage("Testimoni berhasil ditambahkan");
                                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            txt_nama.setText("");
                                            txt_pesan.setText("");
                                        }
                                    });
                                    alert.show();
                                }
                                else if (jsonObject.names().get(0).equals("failed")) {
                                    //Tampilkan pesan cek data
                                    Toast.makeText(getContext(), jsonObject.getString("failed"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                //Apabila objek tidak didapatkan tampilkan pesan berikut
                                Toast.makeText(getContext(), "Masukkan data dengan benar", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            //Objek Hashmap
                            HashMap<String, String> hashMap = new HashMap<>();
                            //Tampung data username, password dan email ke dalam hashmap
                            hashMap.put(NAMA, txt_nama.getText().toString());
                            hashMap.put(PESAN, txt_pesan.getText().toString());

                            return hashMap;
                        }
                    };

                    requestQueue.add(stringRequest);
                }
            }
        });
    }
}
