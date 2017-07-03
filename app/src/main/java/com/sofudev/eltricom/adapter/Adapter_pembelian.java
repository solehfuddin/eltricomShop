package com.sofudev.eltricom.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sofudev.eltricom.R;
import com.sofudev.eltricom.data.Data_pembelian;

import java.util.List;

/**
 * Created by Fuddins on 6/21/2017.
 */

public class Adapter_pembelian extends BaseAdapter {

    //Inisialisasi Inflater dan Activity dan list_Datapembelian
    private LayoutInflater inflater;
    private Activity activity;
    private List<Data_pembelian> items;

    public Adapter_pembelian(Activity activity, List<Data_pembelian> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Kondisi jika Inflater null
        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        //Arahkan convertview ke list_barangmasuk
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_pembelian, null);
        }

        //Hubungkan Adapter ke XML (List_barangmasuk)
        TextView idpesanan      = (TextView) convertView.findViewById(R.id.pembelian_id_pesanan);
        TextView barangpesanan  = (TextView) convertView.findViewById(R.id.pembelian_barang_pesanan);
        TextView jumlahpesanan  = (TextView) convertView.findViewById(R.id.pembelian_jumlah_pesanan);
        TextView expedisipesanan= (TextView) convertView.findViewById(R.id.pembelian_expedisi_pesanan);
        TextView tanggalpesanan = (TextView) convertView.findViewById(R.id.pembelian_tanggal_pesanan);
        TextView biayapesanan   = (TextView) convertView.findViewById(R.id.pembelian_biaya_pesanan);

        //Ambil seluruh data ke objek
        Data_pembelian data_pembelian = items.get(position);

        //Tampung data ke TextView
        idpesanan.setText(data_pembelian.getId_pesanan());
        barangpesanan.setText(data_pembelian.getBarang_pesanan());
        jumlahpesanan.setText(data_pembelian.getJumlah_pesanan());
        expedisipesanan.setText(data_pembelian.getExpedisi_pesanan());
        tanggalpesanan.setText(data_pembelian.getTanggal_pesanan());
        biayapesanan.setText(data_pembelian.getTotal_biaya());

        return convertView;
    }
}
