package com.cikarastudio.cikarajantungdesafix.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cikarastudio.cikarajantungdesafix.R;
import com.cikarastudio.cikarajantungdesafix.ui.forum.ForumFragment;
import com.cikarastudio.cikarajantungdesafix.ui.laporan.LaporanUserActivity;
import com.cikarastudio.cikarajantungdesafix.ui.profil.ProfilActivity;


public class HomeFragment extends Fragment {

    CardView cr_fotoProfil,
            cr_dashboardLaporanDibuat, cr_dashboardForumDiikuti, cr_dashboardJumlahProduk, cr_dashboardSuratDibuat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        cr_dashboardLaporanDibuat = root.findViewById(R.id.cr_dashboardLaporanDibuat);
        cr_dashboardLaporanDibuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent keLaporanDibuat = new Intent(getActivity(), LaporanUserActivity.class);
                startActivity(keLaporanDibuat);
            }
        });

        cr_dashboardForumDiikuti = root.findViewById(R.id.cr_dashboardForumDiikuti);
        cr_dashboardForumDiikuti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                // Create new fragment and transaction
//                Fragment newFragment = new ForumFragment();
//                // consider using Java coding conventions (upper first char class names!!!)
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//
//                // Replace whatever is in the fragment_container view with this fragment,
//                // and add the transaction to the back stack
//                transaction.replace(R.id.container, newFragment);
//                transaction.addToBackStack(null);
//
//                // Commit the transaction
//                transaction.commit();
            }
        });

        cr_fotoProfil = root.findViewById(R.id.cr_fotoProfil);
        cr_fotoProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent keProfil = new Intent(getActivity(), ProfilActivity.class);
                startActivity(keProfil);
            }
        });

        return root;
    }

//    public void selectFragment(int position) {
//        mViewPager.setCurrentItem(position, true);
//// true is to animate the transaction
//    }
}