package com.example.todolist.Controller;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.example.todolist.R;

import java.util.ArrayList;

public class DialogHandler {
    public Runnable ans_true = null;
    public Runnable ans_false = null;

    public boolean Confirm(Context context, String title, String confirmText, String strCancel, String strOk,
                           Runnable cancelProcedure, Runnable okProcedure) {
        ans_true = okProcedure;
        ans_false = cancelProcedure;
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog_Alert);
        builder.setTitle(title);
        builder.setMessage(confirmText);
        builder.setPositiveButton(strOk, (dialog, which) -> ans_true.run());
        builder.setNegativeButton(strCancel, (dialog, which) -> ans_false.run());
        builder.create().show();
        return true;
    }

    public void Input(Context context, ArrayList<String> listSubName, String title, String message, String strCancel, String strOk, OnDialogClick onDialogClick) {
        EditText edtInput = new EditText(context);
        edtInput.setHint("input name sublist");
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setView(edtInput);
        builder.setPositiveButton(strOk, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onDialogClick.onPositiveClick(edtInput.getText().toString());
            }
        });
        builder.setNegativeButton(strCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.create().show();
    }

    public void notify(Context context, String title, String message, String strOk) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog_Alert);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(strOk, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    public interface OnDialogClick {
        void onNegativeClick();

        void onPositiveClick(String text);
    }
}
