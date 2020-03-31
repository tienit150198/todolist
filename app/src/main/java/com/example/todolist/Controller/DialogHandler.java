package com.example.todolist.Controller;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;

import androidx.appcompat.app.AlertDialog;

import com.example.todolist.R;

import java.util.ArrayList;

public class DialogHandler {

    public boolean Confirm(Context context, String title, String confirmText, String strCancel, String strOk,
                           OnDialogClick onDialogClick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog_Alert);
        builder.setTitle(title);
        builder.setMessage(confirmText);
        builder.setPositiveButton(strOk, (dialog, which) -> onDialogClick.onPositiveClick(strOk));
        builder.setNegativeButton(strCancel, (dialog, which) -> onDialogClick.onNegativeClick(strCancel));
        builder.create().show();
        return true;
    }

    public void Input(Context context, String title, String message, String strCancel, String strOk, OnDialogClick onDialogClick) {
        EditText edtInput = new EditText(context);
        edtInput.setHint(context.getString(R.string.hint_sublist));
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setView(edtInput);
        builder.setPositiveButton(strOk, (dialog, which) -> onDialogClick.onPositiveClick(edtInput.getText().toString()));
        builder.setNegativeButton(strCancel, (dialog, which) -> dialog.cancel());

        builder.create().show();
    }

    public void notify(Context context, String title, String message, String strOk) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog_Alert);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(strOk, ((dialog, which) ->
                dialog.dismiss()
        ));

        builder.create().show();
    }

    public void selectionRadioButton(Context context, ArrayList<String> listChoice, int defaultChoiceIndex, String title, String strCancel, String strOk, OnDialogClick onDialogClick){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);

        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_singlechoice, listChoice);

        builder.setSingleChoiceItems(listAdapter, defaultChoiceIndex, null);
        builder.setPositiveButton(strOk, (dialog, which) -> {
            int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
            onDialogClick.onPositiveClick(listChoice.get(selectedPosition));
        });
        builder.create().show();
    }

    public interface OnDialogClick {
        void onNegativeClick(String text);

        void onPositiveClick(String text);
    }
}
