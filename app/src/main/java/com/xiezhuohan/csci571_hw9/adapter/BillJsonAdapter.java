package com.xiezhuohan.csci571_hw9.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiezhuohan.csci571_hw9.R;
import com.xiezhuohan.csci571_hw9.model.bills.Bill;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiezhuohan on 11/27/16.
 */

public class BillJsonAdapter extends BaseAdapter {
    private List<Bill> data = new ArrayList<Bill>();
    private LayoutInflater inflater;


    public BillJsonAdapter(Context context, List<Bill> data) {
        super();
        this.data = data;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.bill_layout, null);
            viewHolder.bill_id = (TextView) convertView
                    .findViewById(R.id.tv_billid);
            viewHolder.bill_title = (TextView) convertView
                    .findViewById(R.id.tv_billtitle);
            viewHolder.bill_date = (TextView) convertView
                    .findViewById(R.id.tv_billdate);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.bill_id.setText(data.get(position).bill_id);
        if(data.get(position).short_title!=null) {
            viewHolder.bill_title.setText(data.get(position).short_title);
        }
        else{
            viewHolder.bill_title.setText(data.get(position).official_title);
        }
        viewHolder.bill_date.setText(data.get(position).introduced_on);

        return convertView;
    }
    private class ViewHolder{
        public TextView bill_id,bill_title, bill_date;
    }
}
