package com.xiezhuohan.csci571_hw9.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiezhuohan.csci571_hw9.R;
import com.xiezhuohan.csci571_hw9.model.legislators.Legislator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiezhuohan on 11/26/16.
 */

public class LegislatorJsonAdapter extends BaseAdapter {
    private List<Legislator> data = new ArrayList<Legislator>();
    private LayoutInflater inflater;


    public LegislatorJsonAdapter(Context context, List<Legislator> data) {
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
            convertView = inflater.inflate(R.layout.legislator_layout, null);
            viewHolder.name = (TextView) convertView
                    .findViewById(R.id.tv_name);
            viewHolder.content = (TextView) convertView
                    .findViewById(R.id.tv_content);
            viewHolder.imageView = (ImageView) convertView
                    .findViewById(R.id.legisimage);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(data.get(position).name);
        viewHolder.content.setText("("+data.get(position).party+") "+data.get(position).state_name+" - District "+data.get(position).district);
        Picasso.with(convertView.getContext()).load("https://theunitedstates.io/images/congress/original/"+data.get(position).bioguide_id+".jpg").into(viewHolder.imageView);

        return convertView;
    }
    private class ViewHolder{
        public TextView name,content;
        public ImageView imageView;
    }
}

