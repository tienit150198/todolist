package com.example.todolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.todolist.Constants.Constants;
import com.example.todolist.Controller.DialogHandler;
import com.example.todolist.Models.Note;
import com.example.todolist.Utils.Validate;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.ArrayList;

public class EditActivity extends AppCompatActivity implements DialogHandler.OnDialogClick {
    private Toolbar mToolbar;
    private TextInputEditText tedt_content_edit;
    private CheckBox cb_content_edit;
    private TextView tv_sublist_name_edit;
    private FrameLayout frm_sublist_edit;
    private FrameLayout frm_color_edit;
    private Note noteEdited;
    private ArrayList<String> mListItemName;
    private DialogHandler mDialogHander;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        initialization();
        config();
        getData();
        setClick();
    }

    private void initialization() {
        mapp();
        initDialog();
    }

    private void mapp() {
        mToolbar = findViewById(R.id.toolbar_edit);
        tedt_content_edit = findViewById(R.id.tedt_content_edit);
        cb_content_edit = findViewById(R.id.cb_content_edit);
        tv_sublist_name_edit = findViewById(R.id.tv_sublist_name_edit);
        frm_sublist_edit = findViewById(R.id.frm_sublist_edit);
        frm_color_edit = findViewById(R.id.frm_color_edit);
    }

    private void initDialog() {
        mDialogHander = new DialogHandler();
    }

    private void config() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void getData() {
        Intent it = getIntent();

        Bundle bundle = it.getExtras();

        Gson gson = new Gson();
        String text = bundle.getString(Constants.KEY_INTENT_SEND_ACTIVITY);
        mListItemName = bundle.getStringArrayList(Constants.KEY_BUNDLE_SEND_FRAGMENT_LIST_NAME);
        noteEdited = gson.fromJson(text, Note.class);

        if (noteEdited != null) {
            tedt_content_edit.setText(noteEdited.getContent());
            cb_content_edit.setChecked(noteEdited.isChecked());
            tv_sublist_name_edit.setText(noteEdited.getNameSublist());
        }
    }

    private void setClick() {
        frm_sublist_edit.setOnClickListener(v -> {
            int selectedPosition = Validate.getInstance(EditActivity.this).findIndexWithId(mListItemName, noteEdited.getNameSublist());
            mDialogHander.selectionRadioButton(EditActivity.this, mListItemName, selectedPosition, getString(R.string.dialog_title_chooseRadioButton), getString(R.string.dialog_button_cancel), getString(R.string.dialog_button_ok), new DialogHandler.OnDialogClick() {
                @Override
                public void onNegativeClick(String text) {

                }

                @Override
                public void onPositiveClick(String text) {
                    tv_sublist_name_edit.setText(text);
                }
            });
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_edit, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_edit_save:
                saveData();
                break;
            case R.id.menu_edit_delete:
                deleteData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteData() {
        Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
    }

    private void saveData() {
        Intent intent = new Intent(this, NoteFragment.class);
        noteEdited.setContent(tedt_content_edit.getText().toString());
        noteEdited.setChecked(cb_content_edit.isChecked());
        noteEdited.setNameSublist(tv_sublist_name_edit.getText().toString());

        Gson gson = new Gson();
        String textNote = gson.toJson(noteEdited);
        intent.putExtra(Constants.KEY_INTENT_SEND_ACTIVITY, textNote);
        setResult(Constants.KEY_RESULT, intent);

        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    public void onNegativeClick(String text) {

    }

    @Override
    public void onPositiveClick(String text) {
        tv_sublist_name_edit.setText(text);
    }
}
