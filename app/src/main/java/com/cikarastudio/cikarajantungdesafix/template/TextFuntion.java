package com.cikarastudio.cikarajantungdesafix.template;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class TextFuntion {

    //fungsi hidekeyboard
    public void hideEdittextKeyboard(EditText target) {
        target.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v, v.getContext());
                }
            }
        });
    }

    public void hideKeyboard(View view, Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //fungsi cek inputan edittext
    public void cekKosongEdittext(EditText edit, String pesan) {
        edit.setError(pesan + " tidak boleh kosong!");
        edit.requestFocus();
    }

    public void settextdannulldata(TextView textView, String string) {
        if (convertUpperCase(string).equals("Null")) {
            textView.setText("Tidak Ada Data");
        } else {
            textView.setText(convertUpperCase(string));
        }

    }

    private String convertUpperCase(String text) {
        String[] splits = text.toLowerCase().split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < splits.length; i++) {
            String eachWord = splits[i];
            if (i > 0 && eachWord.length() > 0) {
                sb.append(" ");
            }
            String cap = eachWord.substring(0, 1).toUpperCase()
                    + eachWord.substring(1);
            sb.append(cap);
        }
        return sb.toString();
    }
}
