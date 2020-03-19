package com.example.todolist;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class DialogHandler {
    public Runnable ans_true = null;
    public Runnable ans_false = null;

    public boolean Confirm(Context context, String title, String confirmText, String strCancel, String strOk,
                                  Runnable cancelProcedure, Runnable okProcedure) {
        ans_true = okProcedure;
        ans_false = cancelProcedure;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(confirmText);
        builder.setPositiveButton(strOk, (dialog, which) -> ans_true.run());
        builder.setNegativeButton(strCancel, (dialog, which) -> ans_false.run());
        builder.create().show();
        return true;
    }

}
