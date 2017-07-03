package com.sofudev.eltricom;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.sofudev.eltricom.adapter.Adapter_button;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    GridView androidGridView;

    String[] gridViewString = {
            "Casing", "DVD Drive", "Harddisk", "Mainboard", "Mouse & Keyboard", "Processor", "Power Supply", "RAM", "VGA"
    } ;
    int[] gridViewImageId = {
            R.drawable.casing, R.drawable.diskdrive, R.drawable.hardisk, R.drawable.mainboard, R.drawable.mandk, R.drawable.processor, R.drawable.psu, R.drawable.ram, R.drawable.vga,
    };

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View thisView = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setTitle("Home");

        Adapter_button adapter_button = new Adapter_button(getActivity(), gridViewString, gridViewImageId);
        androidGridView = (GridView) thisView.findViewById(R.id.gridview_icons);
        androidGridView.setAdapter(adapter_button);
        openCasing();

        return thisView;
    }

    private void openCasing() {
        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (gridViewString[+position]) {
                    case "Casing":
                        ProdukFragment produkFragmentCasing = new ProdukFragment();
                        bukaFragment(produkFragmentCasing);
                        produkFragmentCasing.kategori = "Casing";
                        break;
                    case "DVD Drive":
                        ProdukFragment produkFragmentDVD = new ProdukFragment();
                        bukaFragment(produkFragmentDVD);
                        produkFragmentDVD.kategori = "DVD Drive";
                        break;
                    case "Harddisk":
                        ProdukFragment produkFragmentHarddisk = new ProdukFragment();
                        bukaFragment(produkFragmentHarddisk);
                        produkFragmentHarddisk.kategori = "Harddisk";
                        break;
                    case "Mainboard":
                        ProdukFragment produkFragmentMainboard = new ProdukFragment();
                        bukaFragment(produkFragmentMainboard);
                        produkFragmentMainboard.kategori = "Mainboard";
                        break;
                    case "Mouse & Keyboard":
                        ProdukFragment produkFragmentMouseKeyboard = new ProdukFragment();
                        bukaFragment(produkFragmentMouseKeyboard);
                        produkFragmentMouseKeyboard.kategori = "Mouse & Keyboard";
                        break;
                    case "Processor":
                        ProdukFragment produkFragmentProcessor = new ProdukFragment();
                        bukaFragment(produkFragmentProcessor);
                        produkFragmentProcessor.kategori = "Processor";
                        break;
                    case "Power Supply":
                        ProdukFragment produkFragmentPowerSupply = new ProdukFragment();
                        bukaFragment(produkFragmentPowerSupply);
                        produkFragmentPowerSupply.kategori = "Power Supply";
                        break;
                    case "RAM":
                        ProdukFragment produkFragmentRAM = new ProdukFragment();
                        bukaFragment(produkFragmentRAM);
                        produkFragmentRAM.kategori = "RAM";
                        break;
                    case "VGA":
                        ProdukFragment produkFragmentVGA = new ProdukFragment();
                        bukaFragment(produkFragmentVGA);
                        produkFragmentVGA.kategori = "VGA";
                        break;
                }
            }
        });
    }

    private void bukaFragment (Fragment namafragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, namafragment);
        fragmentTransaction.commit();
    }
}
