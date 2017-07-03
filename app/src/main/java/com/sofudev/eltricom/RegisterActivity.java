package com.sofudev.eltricom;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RegisterActivity extends AppCompatActivity {

    //Hubungkan XML dengan Java
    @InjectView(R.id.register_input_username) TextInputLayout inject_input_username;
    @InjectView(R.id.register_input_password) TextInputLayout inject_input_password;
    @InjectView(R.id.register_input_email)    TextInputLayout inject_input_email;
    @InjectView(R.id.register_txt_username)   EditText        inject_username;
    @InjectView(R.id.register_txt_password)   EditText        inject_password;
    @InjectView(R.id.register_txt_email)      EditText        inject_email;
    @InjectView(R.id.register_btn_register)   Button          inject_btn_register;

    //Inisialisasi RequestQueue dan StringRequest
    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    //Konfigurasi REGISTER URL, USERNAME, PASSWORD DAN EMAIL
    //private static final String REGISTER_URL = "http://articdecoration.com/lti/android/register.php";
    //private static final String NOTIF_URL    = "http://articdecoration.com/lti/android/email_tes.php";
    private static final String REGISTER_URL = "http://192.168.43.36/lti/android/register.php";
    private static final String NOTIF_URL    = "http://192.168.43.36/lti/android/email_tes.php";
    private static final String USERNAME     = "username";
    private static final String PASSWORD     = "password";
    private static final String EMAIL        = "email";

    //Konfigurasi Validasi Email
    String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]"; //contoh soleh.fuddin123_asad@gmail.com
    Pattern pattern = Pattern.compile(EMAIL_PATTERN); //Compile Pattern Email
    Matcher matcher; //Cek kesesuaian Pattern

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Inisialisasi Butterknife
        ButterKnife.inject(this);

        //Run registerApp ketika aplikasi pertama dijalankan
        registerApp();
    }

    private void registerApp() {
        //Objek volley ke Request Queue
        requestQueue = Volley.newRequestQueue(this);

        //Fungsi tombol register ketika ditekan
        inject_btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Tampung seluruh control TextInputLayout kedalam Variable
                String input_username = inject_input_username.getEditText().getText().toString();
                String input_password = inject_input_password.getEditText().getText().toString();
                String input_email    = inject_input_email.getEditText().getText().toString();

                //Jika username bernilai kosong
                if (input_username.isEmpty()) {
                    inject_username.setError("Username harus diisi");
                }
                //Jika password bernilai kosong
                else if (input_password.isEmpty()) {
                    inject_password.setError("Password harus diisi");
                }
                //Jika email bernilai kosong
                else if (input_email.isEmpty()) {
                    inject_email.setError("Email harus diisi");
                }
                //Jika password kurang dari 6 karakter
                else if (!validasiPassword(input_password)){
                    inject_password.setError("Password kurang dari 6 karakter");
                }
                //Jika email tidak sesuai ketentuan
                else if(validasiEmail(input_email)) {
                    inject_email.setError("Email tidak sesuai");
                }
                else {
                    //Objek StringRequest
                    //Method POST disesuaikan dengan Method di Register.php
                    //Register URL merupakan alamat / link dari API Register
                    stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Apabila Volley merespon permintaan
                            //Buat JsonObject untuk menampung hasil json yang di encode oleh file register.php
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                //Cek hasil jsonObject sesuai file Register.php
                                //Apabila sukses
                                if (jsonObject.names().get(0).equals("success")) {
                                    //Aktifkan notifikasi Register
                                    notifRegister(NOTIF_URL);
                                    AlertDialog.Builder alert = new AlertDialog.Builder(RegisterActivity.this);
                                    alert.setTitle("User berhasil ditambahkan");
                                    alert.setMessage("Silahkan cek email anda untuk informasi akun");
                                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                        }
                                    });
                                    alert.show();
                                }
                                //Apabila failed
                                else if (jsonObject.names().get(0).equals("failed")) {
                                    //Tampilkan pesan cek data
                                    Toast.makeText(getApplicationContext(), "Cek : " + jsonObject.getString("failed"), Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                //Apabila objek tidak didapatkan tampilkan pesan berikut
                                Toast.makeText(getApplicationContext(), "Masukkan data dengan benar", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Apabila Volley tidak merespon permintaan
                            Toast.makeText(getApplicationContext(), "Cek koneksi anda", Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            //Objek Hashmap
                            HashMap<String, String> hashMap = new HashMap<>();
                            //Tampung data username, password dan email ke dalam hashmap
                            hashMap.put(USERNAME, inject_username.getText().toString());
                            hashMap.put(PASSWORD, inject_password.getText().toString());
                            hashMap.put(EMAIL, inject_email.getText().toString());
                            return hashMap;
                        }
                    };

                    //Tambahkan requestQueue
                    requestQueue.add(stringRequest);
                }
            }
        });
    }

    //Fungsi untuk mencocokan pattern untuk email
    private boolean validasiEmail(String email){
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //Fungsi untuk membatasi nilai password minimal 6 karakter
    private boolean validasiPassword(String password){
        return password.length() >= 6;
    }

    //Fungsi untuk menjalankan notifikasiRegister via Email
    private InputStream notifRegister(String url) {
        URL registerURL;
        InputStream inputStream = null;
        try {
            registerURL = new URL(url);
            inputStream = registerURL.openStream();
            Toast.makeText(getApplicationContext(), "Sukses", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStream;
    }
}
