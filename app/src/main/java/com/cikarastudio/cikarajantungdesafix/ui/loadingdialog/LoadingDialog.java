package com.cikarastudio.cikarajantungdesafix.ui.loadingdialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Handler;
import android.view.LayoutInflater;

import com.cikarastudio.cikarajantungdesafix.R;

public class LoadingDialog {

    private Activity activity;
    private AlertDialog dialog;

    public LoadingDialog(Activity myActivity) {
        activity = myActivity;
    }

    public void startLoading() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();

        Handler handler = null;
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.cancel();
                dialog.dismiss();
            }
        }, 5000);
    }

    public void dissmissDialog() {
        dialog.dismiss();
    }
}
