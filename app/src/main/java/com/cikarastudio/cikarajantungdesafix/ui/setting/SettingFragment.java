package com.cikarastudio.cikarajantungdesafix.ui.setting;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cikarastudio.cikarajantungdesafix.ui.login.LoginActivity;
import com.cikarastudio.cikarajantungdesafix.R;

public class SettingFragment extends Fragment {

    ImageView img_logout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_setting, container, false);

        img_logout = root.findViewById(R.id.img_logout);

        img_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent keLogout = new Intent(getActivity(), LoginActivity.class);
                startActivity(keLogout);
                getActivity().finishAffinity();
            }
        });
        return root;
    }
}