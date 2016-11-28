package com.xiezhuohan.csci571_hw9.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.xiezhuohan.csci571_hw9.R;
import com.xiezhuohan.csci571_hw9.model.committees.Committee;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiezhuohan on 11/27/16.
 */
public class ComJsonAdapter extends BaseAdapter {
    private List<Committee> data = new ArrayList<Committee>();
    private LayoutInflater inflater;

    public ComJsonAdapter(Context context, List<Committee> data) {
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
            convertView = inflater.inflate(R.layout.listitem_committees, null);
            viewHolder.committee_id = (TextView) convertView
                    .findViewById(R.id.tv_com_id);
            viewHolder.name = (TextView) convertView
                    .findViewById(R.id.tv_com_name);
            viewHolder.chamber = (TextView) convertView
                    .findViewById(R.id.tv_com_chamber);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.committee_id.setText(data.get(position).committee_id);
        viewHolder.name.setText(data.get(position).name);
        viewHolder.chamber.setText(data.get(position).chamber);

        return convertView;
    }

    private class ViewHolder{
        public TextView committee_id,name, chamber;
    }
}
