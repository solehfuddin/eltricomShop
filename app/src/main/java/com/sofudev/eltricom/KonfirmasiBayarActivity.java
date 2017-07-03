package com.sofudev.eltricom;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.net.Uri;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class KonfirmasiBayarActivity extends AppCompatActivity {

    @InjectView(R.id.konfirmasi_input_idpembelian)    TextInputLayout   konfirmasi_input_idpembelian;
    @InjectView(R.id.konfirmasi_input_jumlahtransfer) TextInputLayout   konfirmasi_input_jumlahtransfer;
    @InjectView(R.id.konfirmasi_txt_idpembelian)      EditText          konfirmasi_txt_idpembelian;
    @InjectView(R.id.konfirmasi_txt_jumlahtransfer)   EditText          konfirmasi_txt_jumlahtransfer;
    @InjectView(R.id.konfirmasi_img_buktitransfer)    ImageView         konfirmasi_img_buktitransfer;
    @InjectView(R.id.konfirmasi_btn_bukti)            Button            konfirmasi_btn_bukti;
    @InjectView(R.id.konfirmasi_btn_kirim)            Button            konfirmasi_btn_kirim;

    //PILIH GAMBAR
    Bitmap bitmap;
    String encodedImage;
    private int PICK_IMAGE_REQUEST = 1;

    //Objek RequestQueue dan StringRequest
    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    //CONFIG URL, USERNAME & PASSWORD ke WEB API (DATABASE)
    //private static final String TAMBAH_URL      = "http://articdecoration.com/lti/android/simpan_konfirmasibayar.php";
    private static final String TAMBAH_URL      = "http://192.168.43.36/lti/android/simpan_konfirmasibayar.php";
    private static final String IDPEMBELIAN     = "id_pembelian";
    private static final String JUMLAHTRANSFER  = "jumlah_transfer";
    private static final String BUKTITRANSFER   = "bukti_transfer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_bayar);

        ButterKnife.inject(this);

        changeTitle();
        getData();

        tampilkanPilihan();
        uploadData();
    }

    private void changeTitle() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Konfirmasi Pembayaran");
        }
    }

    private void getData() {
        Bundle extra = getIntent().getExtras();

        if (extra != null) {
            String id;

            id    = extra.getString("id_pesanan");

            konfirmasi_txt_idpembelian.setText(id);

            konfirmasi_txt_idpembelian.setEnabled(false);
            //form_txt_tanggalmasuk.setEnabled(false);
        }
    }

    private void tampilkanPilihan() {
        konfirmasi_btn_bukti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Pilih gambar"), PICK_IMAGE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                konfirmasi_img_buktitransfer.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadData() {
        //Panggil objek requestQueue pada login activity
        requestQueue = Volley.newRequestQueue(this);

        konfirmasi_btn_kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringRequest = new StringRequest(Request.Method.POST, TAMBAH_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.names().get(0).equals("success")) {
                                //Tampilkan pesan cek data
                                Toast.makeText(getApplicationContext(), jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                                konfirmasi_txt_jumlahtransfer.setText("");
                                konfirmasi_img_buktitransfer.setImageResource(R.drawable.not_available);
                            }
                            else if (jsonObject.names().get(0).equals("failed")){
                                //Tampilkan pesan cek data
                                Toast.makeText(getApplicationContext(), jsonObject.getString("failed"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            //Apabila objek tidak didapatkan tampilkan pesan berikut
                            Toast.makeText(getApplicationContext(), "Masukkan data dengan benar", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                         Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        //Converting Bitmap to String
                        String image = getStringImage(bitmap);

                        //Objek Hashmap
                        HashMap<String, String> hashMap = new HashMap<>();
                        //Tampung data username, password dan email ke dalam hashmap
                        hashMap.put(IDPEMBELIAN, konfirmasi_txt_idpembelian.getText().toString());
                        hashMap.put(JUMLAHTRANSFER, konfirmasi_txt_jumlahtransfer.getText().toString());
                        hashMap.put(BUKTITRANSFER, image);

                        return hashMap;
                    }
                };

                requestQueue.add(stringRequest);
            }
        });
    }
}
