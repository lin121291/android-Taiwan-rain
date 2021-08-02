package com.example.listpop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class sportlistAdpter extends BaseAdapter {
    private List<CostBean> mList;
    private Context mcontext;
    private LayoutInflater mLayoutInflater;

    public sportlistAdpter(Context context, List<CostBean> mList){
        this.mcontext = context;
        this.mList = mList;
        // 儲存數據
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.mList.size();
    }

    @Override
    public Object getItem(int i) {
        return this.mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        sportlistAdpter.ViewHolder holder = null;
        if(view == null){
            view = mLayoutInflater.inflate(R.layout.list_item,null);
            holder = new sportlistAdpter.ViewHolder();
            holder.mTvConstTitle = (TextView) view.findViewById(R.id.tv_title);
            holder.mTvConstDate = (TextView) view.findViewById(R.id.tv_date);
            holder.mTvConstMoney = (TextView) view.findViewById(R.id.tv_costMoney);
            view.setTag(holder);
        }else{
            // 如果已經有，就直接取出來
            holder = (sportlistAdpter.ViewHolder) view.getTag();
        }
        CostBean bean = mList.get(i);
        holder.mTvConstTitle.setText(bean.costTitle);
        holder.mTvConstDate.setText(bean.costDate);
        holder.mTvConstMoney.setText(bean.time);
        return view;
    }

    private static class ViewHolder {
        TextView mTvConstTitle;
        TextView mTvConstDate;
        TextView mTvConstMoney;
    }
}
