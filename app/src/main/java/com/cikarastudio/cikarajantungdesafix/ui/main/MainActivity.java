package com.cikarastudio.cikarajantungdesafix.ui.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.cikarastudio.cikarajantungdesafix.R;
import com.cikarastudio.cikarajantungdesafix.session.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

//        showDialogComingSoon();

    }

//    private void showDialogComingSoon() {
//        Dialog dialogKeProfil = new Dialog(this);
//        dialogKeProfil.setContentView(R.layout.dialog_coming_soon);
//        dialogKeProfil.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog_aduan);
//        dialogKeProfil.setCancelable(false);
//        dialogKeProfil.setCanceledOnTouchOutside(false);
//
//        CardView cr_dialogKeProfil = dialogKeProfil.findViewById(R.id.cr_dialogKeProfil);
//        cr_dialogKeProfil.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent keProfil = new Intent(MainActivity.this, ProfilActivity.class);
//                startActivity(keProfil);
//            }
//        });
//        dialogKeProfil.show();
//    }

}