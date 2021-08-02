package com.example.listpop;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{

    // 存儲資訊清單
    private List<CostBean> mCostBeanList;
    // 顯示資訊適配層
    private sportlistAdpter sportlistAdpter;
    // 資料庫操作控制碼
    private DataBaseHelper mDataBaseHelper;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 設置主樣式
        setContentView(R.layout.activity_main);
        // 初始化頂部工具
        initToolbar();
        // 初始化數據
        initCostData();
        // 初始化按鍵
        initFloatBtn();
        //
    }


    // 初始頂部工具
    private void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    // 初始化按鍵
    private void initFloatBtn(){
        // 設置浮動按鍵
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 注意這裡產生實體 builder this
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                View viewDialog = inflater.inflate(R.layout.new_sport,null);
                // 獲取佈局元素
                final EditText title = (EditText) viewDialog.findViewById(R.id.sport);
                final EditText m = (EditText) viewDialog.findViewById(R.id.m);
                final DatePicker date = (DatePicker) viewDialog.findViewById(R.id.dp_cost_date);

                // 設置標題
                builder.setTitle("New sport");
                // 顯示視窗
                builder.setView(viewDialog);

                // OK 按鍵事件
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        CostBean costBean = new CostBean();
                        costBean.costTitle = title.getText().toString();
                        costBean.time = m.getText().toString();
                        // 注意這裡的日期拼接
                        costBean.costDate = date.getYear()+"-"+(date.getMonth()+1)+"-"+date.getDayOfMonth();
                        // 插入資料庫
                        mDataBaseHelper.insert(costBean);
                        // 顯示清單中添加
                        mCostBeanList.add(costBean);
                        // 通知 adapter 更新資料
                        sportlistAdpter.notifyDataSetChanged();
                    }
                });

                // 取消
                builder.setNegativeButton("Cancel",null);
                // 對話方塊
                builder.create().show();
            }
        });
    }


    // 初始化數據
    private void initCostData() {
        mCostBeanList = new ArrayList<>();
        mDataBaseHelper = new DataBaseHelper(this);

        // 從資料庫中取出資料
        Cursor cursor = mDataBaseHelper.getAllCursorDate();
        if(cursor != null){
            while(cursor.moveToNext()){
                CostBean costBean = new CostBean();
                costBean.costTitle = cursor.getString(cursor.getColumnIndex("cost_title"));
                costBean.costDate = cursor.getString(cursor.getColumnIndex("cost_date"));
                costBean.time = cursor.getString(cursor.getColumnIndex("cost_money"));
                mCostBeanList.add(costBean);
            }
            cursor.close();
        }

        ListView costList = findViewById(R.id.lv_main);
        sportlistAdpter = new sportlistAdpter(this, mCostBeanList);
        costList.setAdapter(sportlistAdpter);
    }

    // 頂部功能表項目設置
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // 設置工具條右邊功能表項目
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    // 頂部菜單事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.weather){
            // 調用另一個 active
            Intent intent = new Intent(MainActivity.this, weather.class);
            startActivity(intent);
        }
        if(id==R.id.delete){
            mDataBaseHelper.deleteAllData();//刪除所有資料
        }
        return super.onOptionsItemSelected(item);
    }

}
