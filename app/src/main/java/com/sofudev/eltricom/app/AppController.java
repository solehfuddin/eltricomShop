package com.sofudev.eltricom.app;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Fuddins on 6/16/2017.
 */

public class AppController extends Application {
    //Variable untuk menampung Nama TAG
    public static final String TAG = AppController.class.getSimpleName();

    //Inisialisasi RequestQueue
    private RequestQueue mRequestQueue;

    //Inisialisasi AppController
    private static AppController mInstance;


    //Aktifkan AppController ketika diload pertama kali
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    //Fungsi untuk sinkronisasi AppController
    public static synchronized AppController getInstance() {
        return mInstance;
    }

    //Fungsi untuk requestQueue pada Volley
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    //Fungsi menambahkan requestQueue dengan Parameter Tag
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    //Fungsi menambahkan requestQueue tanpa Tag
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    //Fungsi untuk membatalkan requestQueue
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
