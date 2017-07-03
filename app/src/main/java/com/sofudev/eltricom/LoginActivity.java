package com.sofudev.eltricom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
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

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {

    //Hubungkan objek XML dengan JAVA
    @InjectView(R.id.login_input_username)  TextInputLayout inject_input_username;
    @InjectView(R.id.login_input_password)  TextInputLayout inject_input_password;
    @InjectView(R.id.login_txt_username)    EditText        inject_txt_username;
    @InjectView(R.id.login_txt_password)    EditText        inject_txt_password;
    @InjectView(R.id.login_btn_login)       Button          inject_btn_login;
    @InjectView(R.id.login_btn_register)    Button          inject_btn_register;

    //Objek RequestQueue dan StringRequest
    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    //CONFIG URL, USERNAME & PASSWORD ke WEB API (DATABASE)
    //private static final String LOGIN_URL = "http://articdecoration.com/lti/android/login.php";
    private static final String LOGIN_URL = "http://192.168.43.36/lti/android/login.php";
    private static final String USERNAME  = "username";
    private static final String PASSWORD  = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Inject Plugin Butterknife ke dalam Activity ketika program pertama kali dijalankan
        ButterKnife.inject(this);

        //Panggil fungsi openRegister
        openRegister();

        //Panggil fungsi loginApp
        loginApp();
        changeTitle();
    }

    private void changeTitle() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Login Aplikasi");
        }
    }

    private void openRegister() {
        //ketika button register di tekan
        inject_btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Link ke Register Activity
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
    }

    private void loginApp() {
        //Panggil objek requestQueue pada login activity
        requestQueue = Volley.newRequestQueue(this);

        //ketika button login di tekan
        inject_btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Simpan value username & password dari XML
                final String username = inject_input_username.getEditText().getText().toString();
                String password = inject_input_password.getEditText().getText().toString();

                //Jika nilai username kosong, tampilkan pesan error
                if (username.isEmpty()) {
                    inject_txt_username.setError("Username belum diisi");
                }
                //Jika nilai password kosong, tampilkan pesan error
                else if (password.isEmpty()) {
                    inject_txt_password.setError("Password belum diisi");
                }

                //Panggi objek stringRequest
                stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Pemilihan jika login sukses dan gagal
                        try {
                            //Buat JSONObject berdasarkan json yang di encode oleh WEB API
                            JSONObject jsonObject = new JSONObject(response);
                            //Jika output json (sukses)
                            if (jsonObject.names().get(0).equals("success")) {
                                //Buat objek progress dialog
                                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
                                progressDialog.setIndeterminate(true);
                                //Tampilkan pesan pada progress dialog
                                progressDialog.setMessage("Loading ...");
                                progressDialog.show();

                                //Buat handler OS
                                new android.os.Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Link ke home Activity dan simpan nilai username
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        intent.putExtra("username", username);

                                        startActivity(intent);

                                        //Tutup progress dialog setelah loading selesai
                                        progressDialog.dismiss();
                                    }
                                }, 3000);
                            }
                            //Jika output json (failed)
                            else if (jsonObject.names().get(0).equals("failed")) {
                                //Keterangan gagal login
                                Toast.makeText(getApplicationContext(), "Cek " + jsonObject.getString("failed"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Username & password salah", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Kesalahan koneksi
                        Toast.makeText(getApplicationContext(), "Cek koneksi anda ...", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        //Buat objek Hashmap untuk data XML yang di POST dengan Volley
                        HashMap<String, String> hashMap = new HashMap<>();
                        //Masukkan data username dan password
                        hashMap.put(USERNAME, inject_txt_username.getText().toString());
                        hashMap.put(PASSWORD, inject_txt_password.getText().toString());
                        return hashMap;
                    }
                };
                //Masukkan stringrequest ke dalam requestqueue
                requestQueue.add(stringRequest);
            }
        });
    }
}
