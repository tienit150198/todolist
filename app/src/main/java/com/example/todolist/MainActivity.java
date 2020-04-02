package com.example.todolist;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Adapter.ItemAdapter;
import com.example.todolist.Constants.Constants;
import com.example.todolist.Controller.AppDatabase;
import com.example.todolist.Controller.DialogHandler;
import com.example.todolist.Interface.OnItemListenerHelper;
import com.example.todolist.Models.Item;
import com.example.todolist.Utils.Validate;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnItemListenerHelper {
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;
    private ItemAdapter mItemAdapterListItem;
    private RecyclerView mRecyclerViewListTask;
    private Validate instanceValidate;   // manager sublist in navigation drawer
    private NoteFragment mNoteFragment;
    private DialogHandler mDialogHander;
    private AppDatabase instanceData;
    private ArrayList<Item> mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        config();
    }

    private void init() {
        mapp();
        initItem();
        initDialogHander();
    }

    private void mapp() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        mToolbar = findViewById(R.id.toolbar);
        mRecyclerViewListTask = mNavigationView.findViewById(R.id.recycler_listTask);
    }

    private void initItem() {
        mItems = new ArrayList<>();
        instanceData = AppDatabase.getInstance(this);
        mItems = (ArrayList) instanceData.itemDao().queryAll();
    }

    private void initDialogHander() {
        mDialogHander = new DialogHandler();
    }

    private void config() {
        instanceValidate = Validate.getInstance(this);

        configActionBar();
        configAnimation();
        configRecyclerView();
    }

    private void configActionBar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.dehaze);
        }
    }

    private void configAnimation() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void configRecyclerView() {
        mItemAdapterListItem = new ItemAdapter(mItems, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerViewListTask.setAdapter(mItemAdapterListItem);
        mRecyclerViewListTask.setLayoutManager(linearLayoutManager);
        mRecyclerViewListTask.setHasFixedSize(true);

        if (mItems.size() == 0) {
            mItems.add(makeItem(R.drawable.sublist, Constants.KEY_SUBLIST_DEFAULT));
            instanceData.itemDao().insertItem(makeItem(R.drawable.sublist, Constants.KEY_SUBLIST_DEFAULT));
        }

        selectedItem(0);
    }

    private void selectedItem(int position) {
        mToolbar.setTitle(mItems.get(position).getName());
        mNavigationView.setCheckedItem(position);
        clickFragment(position);
        mDrawerLayout.closeDrawers();
    }

    private void clickFragment(int position) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_BUNDLE_SEND_FRAGMENT_SUBLISTNAME, mItems.get(position).getName());
        bundle.putStringArrayList(Constants.KEY_BUNDLE_SEND_FRAGMENT_LIST_NAME, (ArrayList) instanceData.itemDao().queryAllName());
        mNoteFragment = new NoteFragment();
        mNoteFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.frm_main, mNoteFragment).commit();
    }

    private Item makeItem(int idIcon, String subName) {
        return new Item(idIcon, subName);
    }

    private void deleteSublist() {
        String nameSublistCurrent = mToolbar.getTitle().toString();
        int idSublistSelected = instanceValidate.findIndexWithId((ArrayList) instanceData.itemDao().queryAllName(), nameSublistCurrent);

        mNoteFragment.deleteSublist(nameSublistCurrent);
        // must always have the All sublist. Cannot delete sublist All
        if (idSublistSelected != -1 && !nameSublistCurrent.equals(Constants.KEY_SUBLIST_DEFAULT)) {
            instanceData.itemDao().deleteItem(mItems.get(idSublistSelected));
            mItems.remove(idSublistSelected);

            if (mItems.size() > 0) {
                selectedItem(0);
            }
        }
        mItemAdapterListItem.notifyItemRemoved(idSublistSelected);
    }

    private void addSublist() {
        mDialogHander.Input(this, getString(R.string.dialog_title_notify), "", getString(R.string.dialog_button_cancel), getString(R.string.dialog_button_ok), new DialogHandler.OnDialogClick() {
            @Override
            public void onNegativeClick(String text) {
            }

            @Override
            public void onPositiveClick(String text) {
                if (text.trim().isEmpty()) {
                    mDialogHander.notify(MainActivity.this, getString(R.string.dialog_title_notify), getString(R.string.dialog_messenger_empty), getString(R.string.dialog_button_ok));
                } else if (instanceValidate.checkContainsInArrayList(mItems, text.trim())) {
                    mDialogHander.notify(MainActivity.this, getString(R.string.dialog_title_notify), getString(R.string.dialog_messenger_existed), getString(R.string.dialog_button_ok));
                } else {
                    mItems.add(makeItem(R.drawable.sublist, text.trim()));
                    instanceData.itemDao().insertItem(makeItem(R.drawable.sublist, text.trim()));
                    selectedItem(instanceValidate.findIndexWithId((ArrayList) instanceData.itemDao().queryAllName(), text.trim()));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.it_add:
                Toast.makeText(this, getString(R.string.toast_menu_add_item), Toast.LENGTH_SHORT).show();
                break;
            case R.id.it_add_sublist:
                addSublist();
                break;
            case R.id.it_delete:
                Toast.makeText(this, getString(R.string.toast_menu_delete_item), Toast.LENGTH_SHORT).show();
                break;
            case R.id.it_delete_sublist:
                deleteSublist();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        return true;
    }

//    @Override
//    protected void onDestroy() {
//        // close database
//        if (!mRealm.isClosed()) {
//            mRealm.close();
//        }
//        mNoteFragment.closeRealm();
//        super.onDestroy();
//    }

    @Override
    public void onItemClick(View view, int position) {
        selectedItem(position);
    }

    @Override
    public void onChangeCheckedListener(View view, boolean isChecked, int position) {

    }
}
