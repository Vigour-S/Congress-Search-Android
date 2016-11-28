package com.xiezhuohan.csci571_hw9.fragment;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.google.gson.Gson;

import java.util.*;

import com.xiezhuohan.csci571_hw9.R;
import com.xiezhuohan.csci571_hw9.adapter.BillJsonAdapter;
import com.xiezhuohan.csci571_hw9.adapter.ComJsonAdapter;
import com.xiezhuohan.csci571_hw9.adapter.LegislatorJsonAdapter;
import com.xiezhuohan.csci571_hw9.model.bills.Bill;
import com.xiezhuohan.csci571_hw9.model.committees.Committee;
import com.xiezhuohan.csci571_hw9.model.legislators.Legislator;


public class FavoritesFragment extends Fragment implements AdapterView.OnItemClickListener, TabHost.TabContentFactory, TabHost.OnTabChangeListener, View.OnClickListener {
    View favorView;
    private ListView lvFavorLegis;
    private ListView lvFavorBills;
    private ListView lvFavorComs;
    private List<Legislator> favorLegisList;
    private List<Bill> favorBillsList;
    private List<Committee> favorComList;
    TabHost tabHost;
    Map<String, Integer> mapIndex = new LinkedHashMap<String, Integer>();
    private SharedPreferences preLegis;
    private SharedPreferences preBills;
    private SharedPreferences preComs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        favorView= inflater.inflate(R.layout.favorite_layout, container,false);
        lvFavorLegis=(ListView)favorView.findViewById(R.id.favorlegisList);
        lvFavorBills=(ListView)favorView.findViewById(R.id.favoriteBillList);
        lvFavorComs=(ListView)favorView.findViewById(R.id.favoriteCommitteeList);

        tabHost=(TabHost)favorView.findViewById(R.id.tabhost);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("Legislators").setContent(R.id.tab1));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("Bills").setContent(R.id.tab2));
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("Committees").setContent(R.id.tab3));
        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(this);
        preLegis=this.getActivity().getSharedPreferences("favor_legis", Context.MODE_APPEND);
        preBills=this.getActivity().getSharedPreferences("favor_bills", Context.MODE_APPEND);
        preComs=this.getActivity().getSharedPreferences("favor_committees",Context.MODE_APPEND);
        Map<String, String> legisMap= (Map<String, String>) preLegis.getAll();
        Map<String, String> billsMap=(Map<String, String>)preBills.getAll();
        Map<String, String> comsMap=(Map<String, String>)preComs.getAll();
        final Gson gson = new Gson();
        favorLegisList=new ArrayList<Legislator>();
        favorBillsList=new ArrayList<Bill>();
        favorComList=new ArrayList<Committee>();
        for(Map.Entry<String, String> entry:legisMap.entrySet()){
            favorLegisList.add(gson.fromJson(entry.getValue(), Legislator.class));
        }
        for (int i = 0; i < favorLegisList.size(); i++) {
            String name = favorLegisList.get(i).name;
            String index = name.substring(0, 1);

            if (mapIndex.get(index) == null)
                mapIndex.put(index, i);
        }
        displayIndex(mapIndex);
        for(Map.Entry<String, String> entry:billsMap.entrySet()){
            favorBillsList.add(gson.fromJson(entry.getValue(), Bill.class));
        }
        for(Map.Entry<String, String> entry:comsMap.entrySet()){
            favorComList.add(gson.fromJson(entry.getValue(), Committee.class));
        }
        LegislatorJsonAdapter adapter1=new LegislatorJsonAdapter(getActivity(), favorLegisList);
        BillJsonAdapter adapter2=new BillJsonAdapter(getActivity(),favorBillsList);
        ComJsonAdapter adapter3=new ComJsonAdapter(getActivity(), favorComList);
        lvFavorBills.setAdapter(adapter2);
        lvFavorLegis.setAdapter(adapter1);
        lvFavorComs.setAdapter(adapter3);
        lvFavorLegis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Legislator data=(Legislator) parent.getItemAtPosition(position);

                Intent intent1=new Intent("com.xiezhuohan.csci571_hw9.legislators");
                intent1.putExtra("legislator", data);
                startActivity(intent1);
            }
        });
        lvFavorBills.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bill data=(Bill)parent.getItemAtPosition(position);
                Intent intent2=new Intent("com.xiezhuohan.csci571_hw9.bills");
                intent2.putExtra("bill_detail", gson.toJson(data, Bill.class));
                startActivity(intent2);
            }
        });
        lvFavorComs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent3=new Intent("com.xiezhuohan.csci571_hw9.committees");
                Committee item=(Committee)parent.getItemAtPosition(position);
                intent3.putExtra("committee_detail", gson.toJson(item, Committee.class));
                startActivity(intent3);
            }
        });
        return favorView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onResume() {
        super.onResume();
        preLegis=this.getActivity().getSharedPreferences("favor_legis", Context.MODE_APPEND);
        preBills=this.getActivity().getSharedPreferences("favor_bills", Context.MODE_APPEND);
        preComs=this.getActivity().getSharedPreferences("favor_committees",Context.MODE_APPEND);
        Map<String, String> legisMap= (Map<String, String>) preLegis.getAll();
        Map<String, String> billsMap=(Map<String, String>)preBills.getAll();
        Map<String, String> comsMap=(Map<String, String>)preComs.getAll();
        final Gson gson=new Gson();
        favorLegisList=new ArrayList<Legislator>();
        favorBillsList=new ArrayList<Bill>();
        favorComList=new ArrayList<Committee>();
        for(Map.Entry<String, String> entry:legisMap.entrySet()){
            favorLegisList.add(gson.fromJson(entry.getValue(), Legislator.class));
        }
        for(Map.Entry<String, String> entry:billsMap.entrySet()){
            favorBillsList.add(gson.fromJson(entry.getValue(), Bill.class));
        }
        for(Map.Entry<String, String> entry:comsMap.entrySet()){
            favorComList.add(gson.fromJson(entry.getValue(), Committee.class));
        }
        LegislatorJsonAdapter adapter1=new LegislatorJsonAdapter(getActivity(), favorLegisList);
        BillJsonAdapter adapter2=new BillJsonAdapter(getActivity(),favorBillsList);
        ComJsonAdapter adapter3=new ComJsonAdapter(getActivity(), favorComList);
        lvFavorBills.setAdapter(adapter2);
        lvFavorLegis.setAdapter(adapter1);
        lvFavorComs.setAdapter(adapter3);

    }

    @Override
    public void onTabChanged(String tabId) {

    }

    @Override
    public View createTabContent(String tag) {
        return null;
    }

    private void displayIndex(Map<String, Integer> mapIndex) {
        LinearLayout indexLayout;
        indexLayout = (LinearLayout)favorView.findViewById(R.id.side_index4);
        TextView textView;
        List<String> indexList = new ArrayList<String>(mapIndex.keySet());
        for (String index : indexList) {
            textView = (TextView)getActivity().getLayoutInflater().inflate(
                    R.layout.side_index_item, null);
            textView.setText(index);
            textView.setOnClickListener(this);
            indexLayout.addView(textView);
        }
    }

    @Override
    public void onClick(View view) {
        view.getLayoutParams();
        TextView selectedIndex = (TextView) view;
        lvFavorLegis.setSelection(mapIndex.get(selectedIndex.getText()));
    }
}
