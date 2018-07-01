package jv.cadastrodelivros.utils;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import jv.cadastrodelivros.R;

public class Dialog {

    public static void alert(String title, String content, Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.setNeutralButton(R.string.dialog_alert_button_ok, null);

        builder.show();
    }
}
