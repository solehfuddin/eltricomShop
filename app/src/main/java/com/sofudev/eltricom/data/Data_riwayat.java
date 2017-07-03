package com.sofudev.eltricom.data;

/**
 * Created by Fuddins on 6/16/2017.
 */

public class Data_riwayat {
    private String id_pesanan, barang_pesanan, jumlah_pesanan, expedisi_pesanan, tanggal_pesanan, status_pesanan;

    public Data_riwayat() {

    }

    public Data_riwayat(String id_pesanan, String barang_pesanan, String jumlah_pesanan, String expedisi_pesanan,
                        String tanggal_pesanan, String status_pesanan) {
        this.id_pesanan = id_pesanan;
        this.barang_pesanan = barang_pesanan;
        this.jumlah_pesanan = jumlah_pesanan;
        this.expedisi_pesanan = expedisi_pesanan;
        this.tanggal_pesanan = tanggal_pesanan;
        this.status_pesanan = status_pesanan;
    }

    public String getId_pesanan() {
        return id_pesanan;
    }

    public void setId_pesanan(String id_pesanan) {
        this.id_pesanan = id_pesanan;
    }

    public String getBarang_pesanan() {
        return barang_pesanan;
    }

    public void setBarang_pesanan(String barang_pesanan) {
        this.barang_pesanan = barang_pesanan;
    }

    public String getJumlah_pesanan() {
        return jumlah_pesanan;
    }

    public void setJumlah_pesanan(String jumlah_pesanan) {
        this.jumlah_pesanan = jumlah_pesanan;
    }

    public String getExpedisi_pesanan() {
        return expedisi_pesanan;
    }

    public void setExpedisi_pesanan(String expedisi_pesanan) {
        this.expedisi_pesanan = expedisi_pesanan;
    }

    public String getTanggal_pesanan() {
        return tanggal_pesanan;
    }

    public void setTanggal_pesanan(String tanggal_pesanan) {
        this.tanggal_pesanan = tanggal_pesanan;
    }

    public String getStatus_pesanan() {
        return status_pesanan;
    }

    public void setStatus_pesanan(String status_pesanan) {
        this.status_pesanan = status_pesanan;
    }
}
