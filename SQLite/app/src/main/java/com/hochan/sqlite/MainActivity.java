package com.hochan.sqlite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hochan.sqlite.data.Worker;
import com.hochan.sqlite.fragment.EditDialogFragment;
import com.hochan.sqlite.fragment.SearchDialogFragment;
import com.hochan.sqlite.fragment.WorkersListFragment;
import com.hochan.sqlite.sql.DataHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, EditDialogFragment.OnDialogListener{

    private Button btnSearch, btnAdd;
    private WorkersListFragment mWorkersListFragment;
    private LinearLayout llButton;
    private TextView tvText;
    private EditDialogFragment mAddDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                DataHelper dataHelper = new DataHelper(getApplicationContext());
                dataHelper.clearTable(getApplicationContext());
                mWorkersListFragment.clear();
                Toast.makeText(getApplicationContext(), "已清空表的记录", Toast.LENGTH_LONG).show();
                return true;
            }
        });

        Log.i("test","success");

        btnSearch = (Button) findViewById(R.id.btn_search);
        btnAdd = (Button) findViewById(R.id.btn_add);

        btnSearch.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        llButton = (LinearLayout) findViewById(R.id.ll_button);
        tvText = (TextView) findViewById(R.id.tv_text);

        mWorkersListFragment = (WorkersListFragment) getSupportFragmentManager().findFragmentByTag(WorkersListFragment.TAG);
        if(mWorkersListFragment == null) {
            mWorkersListFragment = WorkersListFragment.newInstance();
            mWorkersListFragment.setmShowFragmentListener(new WorkersListFragment.ShowFragmentListener() {
                @Override
                public void chooseMore(boolean isChooseMore) {
                    if(isChooseMore){
                        tvText.setVisibility(View.GONE);
                        llButton.setVisibility(View.GONE);
                    }
                    else{
                        tvText.setVisibility(View.VISIBLE);
                        llButton.setVisibility(View.VISIBLE);
                    }
                }
            });
            getSupportFragmentManager().beginTransaction().add(R.id.rl_workers_list, mWorkersListFragment, WorkersListFragment.TAG).commit();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.btn_search:
                //intent.setClass(this, SearchActivity.class);
                SearchDialogFragment searchDialog = SearchDialogFragment.newInstance();
                searchDialog.setSearchListener(new SearchDialogFragment.OnSearchDialogListener() {
                    @Override
                    public void searchDialog(String key, int type) {
                        mWorkersListFragment.search(key, type);
                    }

                    @Override
                    public void showAll() {
                        mWorkersListFragment.showAll();
                    }
                });
                searchDialog.show(getSupportFragmentManager(), "search");
                return;
            case R.id.btn_add:
                mAddDialog = EditDialogFragment.newInstance(EditDialogFragment.ADD_DIALOG);
                mAddDialog.setOnDialogListener(this);
                mAddDialog.show(getSupportFragmentManager(), "adddialog");
                return;
        }
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAddDialog = (EditDialogFragment) getSupportFragmentManager().findFragmentByTag("adddialog");
        if(mAddDialog != null){
            mAddDialog.setOnDialogListener(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void clicked(int index, Worker worker, int position) {
        DataHelper tmpDataHelper = new DataHelper(getApplicationContext());
        Long result = tmpDataHelper.addData(worker);
        worker.setmID(String.valueOf(result));
        Toast.makeText(getApplicationContext(), "添加成功：" + result, Toast.LENGTH_SHORT).show();
        mWorkersListFragment.add(worker);
    }
}
