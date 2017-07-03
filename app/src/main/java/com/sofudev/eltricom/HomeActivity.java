package com.sofudev.eltricom;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String ambil_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        HomeFragment homeFragment = new HomeFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, homeFragment);
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Tampilkan username yang login
        View header = navigationView.getHeaderView(0);
        TextView txtusername = (TextView) header.findViewById(R.id.navigasi_username);
        Bundle data = getIntent().getExtras();
        if (data != null) {
            ambil_username = data.getString("username");
            txtusername.setText(ambil_username);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logoutApp();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            HomeFragment homeFragment = new HomeFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, homeFragment);
            fragmentTransaction.commit();
        }
        else if (id == R.id.nav_pembelian) {
            Bundle bundle = new Bundle();
            bundle.putString("userAktif", ambil_username);
            PembelianFragment pembelianFragment = new PembelianFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, pembelianFragment);
            pembelianFragment.setArguments(bundle);
            fragmentTransaction.commit();
        }
        else if (id == R.id.nav_riwayat) {
            Bundle bundle = new Bundle();
            bundle.putString("userAktif", ambil_username);
            RiwayatFragment riwayatFragment = new RiwayatFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, riwayatFragment);
            riwayatFragment.setArguments(bundle);
            fragmentTransaction.commit();
        }
        else if (id == R.id.nav_testimonial) {
            TestimonialFragment testimonialFragment = new TestimonialFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, testimonialFragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //OnKeydown (Event) apabila tombol back ditekan
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Objek Alerrt Dialog {PopUp Dialog)
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        //Setting Title PopUp
        dialog.setTitle("Peringatan");
        //Setting Pesan PopUp
        dialog.setMessage("Apakah anda yakin akan keluar aplikasi ?");

        //Apabila User menekan tombol BACK pada android
        //Tampilkan PopUp dialog berikut
        if (keyCode==KeyEvent.KEYCODE_BACK) {
            dialog.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            //Apabila User menekan tombol no
            dialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }

        //Tampilkan PopUp
        dialog.show();
        return super.onKeyDown(keyCode, event);
    }

    private void logoutApp(){
        //Objek Alerrt Dialog {PopUp Dialog)
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        //Setting Title PopUp
        dialog.setTitle("Peringatan");
        //Setting Pesan PopUp
        dialog.setMessage("Apakah anda yakin akan keluar aplikasi ?");

        dialog.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        //Apabila User menekan tombol no
        dialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.show();
    }
}
