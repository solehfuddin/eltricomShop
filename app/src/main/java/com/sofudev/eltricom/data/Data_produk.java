package com.sofudev.eltricom.data;

/**
 * Created by Fuddins on 6/30/2017.
 */

public class Data_produk {
    private String namaproduk, harga, spesifikasi, gambar, quantity;

    public Data_produk(String namaproduk, String harga, String spesifikasi, String gambar, String quantity) {
        this.namaproduk = namaproduk;
        this.harga = harga;
        this.spesifikasi = spesifikasi;
        this.gambar = gambar;
        this.quantity = quantity;
    }

    public String getNamaproduk() {
        return namaproduk;
    }

    public String getHarga() {
        return harga;
    }

    public String getSpesifikasi() {
        return spesifikasi;
    }

    public String getGambar() {
        return gambar;
    }

    public String getQuantity() {
        return quantity;
    }
}
