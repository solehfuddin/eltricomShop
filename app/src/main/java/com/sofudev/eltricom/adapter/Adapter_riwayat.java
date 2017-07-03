package com.sofudev.eltricom.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sofudev.eltricom.R;
import com.sofudev.eltricom.data.Data_riwayat;

import java.util.List;

/**
 * Created by Fuddins on 6/16/2017.
 */

public class Adapter_riwayat extends BaseAdapter {

    //Inisialisasi Inflater dan Activity dan list_Datariwayat
    private LayoutInflater inflater;
    private Activity activity;
    private List<Data_riwayat> items;

    public Adapter_riwayat(Activity activity, List<Data_riwayat> items) {
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
            convertView = inflater.inflate(R.layout.list_riwayat, null);
        }

        //Hubungkan Adapter ke XML (List_barangmasuk)
        TextView idpesanan      = (TextView) convertView.findViewById(R.id.riwayat_id_pesanan);
        TextView barangpesanan  = (TextView) convertView.findViewById(R.id.riwayat_barang_pesanan);
        TextView jumlahpesanan  = (TextView) convertView.findViewById(R.id.riwayat_jumlah_pesanan);
        TextView expedisipesanan= (TextView) convertView.findViewById(R.id.riwayat_expedisi_pesanan);
        TextView statuspesanan  = (TextView) convertView.findViewById(R.id.riwayat_status_pesanan);
        TextView tanggalpesanan = (TextView) convertView.findViewById(R.id.riwayat_tanggal_pesanan);

        //Ambil seluruh data ke objek
        Data_riwayat data_riwayat = items.get(position);

        //Tampung data ke TextView
        idpesanan.setText(data_riwayat.getId_pesanan());
        barangpesanan.setText(data_riwayat.getBarang_pesanan());
        jumlahpesanan.setText(data_riwayat.getJumlah_pesanan());
        expedisipesanan.setText(data_riwayat.getExpedisi_pesanan());
        statuspesanan.setText(data_riwayat.getStatus_pesanan());
        tanggalpesanan.setText(data_riwayat.getTanggal_pesanan());

        return convertView;
    }
}
