package com.sofudev.eltricom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sofudev.eltricom.R;
import com.sofudev.eltricom.data.Data_produk;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Fuddins on 6/30/2017.
 */

public class Adapter_produk extends RecyclerView.Adapter<Adapter_produk.ViewHolder> {

    private List<Data_produk> list;
    private Context context;

    public Adapter_produk(List<Data_produk> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_produk, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Data_produk data_produk = list.get(position);

        holder.txtNamaproduk.setText(data_produk.getNamaproduk());
        holder.txtHarga.setText(data_produk.getHarga());
        holder.txtSpesifikasi.setText(data_produk.getSpesifikasi());
        holder.txtQuantity.setText(data_produk.getQuantity() + " unit");

        Picasso.with(context).load(data_produk.getGambar()).into(holder.img_gambar);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtNamaproduk, txtHarga, txtSpesifikasi, txtQuantity;
        private ImageView img_gambar;

        private ViewHolder(View view) {
            super(view);

            txtNamaproduk   = (TextView) view.findViewById(R.id.produk_txt_namaproduk);
            txtHarga        = (TextView) view.findViewById(R.id.produk_txt_harga);
            txtSpesifikasi  = (TextView) view.findViewById(R.id.produk_txt_spesifikasi);
            txtQuantity     = (TextView) view.findViewById(R.id.produk_txt_quantity);
            img_gambar      = (ImageView) view.findViewById(R.id.produk_img_gambar);
        }
    }
}
