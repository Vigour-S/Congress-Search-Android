package com.xiezhuohan.csci571_hw9.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.gson.Gson;
import com.xiezhuohan.csci571_hw9.R;
import com.xiezhuohan.csci571_hw9.Utils.SidebarUtils;
import com.xiezhuohan.csci571_hw9.adapter.BillListAdapter;
import com.xiezhuohan.csci571_hw9.adapter.CommitteeListAdapter;
import com.xiezhuohan.csci571_hw9.adapter.LegislatorListAdapter;
import com.xiezhuohan.csci571_hw9.model.bills.Bill;
import com.xiezhuohan.csci571_hw9.model.committees.Committee;
import com.xiezhuohan.csci571_hw9.model.legislators.Legislator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class FavoritesFragment extends Fragment implements AdapterView.OnItemClickListener, TabHost.TabContentFactory, TabHost.OnTabChangeListener, View.OnClickListener {

    private View favorView;
    private ListView lvFavorLegislators;
    private ListView lvFavorBills;
    private ListView lvFavorCommittees;
    private List<Legislator> favorLegislators;
    private List<Bill> favorBills;
    private List<Committee> favorCommittees;
    private Map<String, Integer> mapIndex = new LinkedHashMap<String, Integer>();
    private SharedPreferences preLegislators;
    private SharedPreferences preBills;
    private SharedPreferences preCommittees;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        TabHost tabHost;

        favorView = inflater.inflate(R.layout.favorite_layout, container, false);
        lvFavorLegislators = (ListView) favorView.findViewById(R.id.favorlegisList);
        lvFavorBills = (ListView) favorView.findViewById(R.id.favoriteBillList);
        lvFavorCommittees = (ListView) favorView.findViewById(R.id.favoriteCommitteeList);

        tabHost = (TabHost) favorView.findViewById(R.id.tabhost);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("Legislators").setContent(R.id.tab1));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("Bills").setContent(R.id.tab2));
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("Committees").setContent(R.id.tab3));
        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(this);
        preLegislators = this.getActivity().getSharedPreferences("favorLegislators", Context.MODE_APPEND);
        preBills = this.getActivity().getSharedPreferences("favorBills", Context.MODE_APPEND);
        preCommittees = this.getActivity().getSharedPreferences("favorCommittees", Context.MODE_APPEND);

        Map<String, String> legislatorMap, billsMap, committeesMap;
        legislatorMap = (Map<String, String>) preLegislators.getAll();
        billsMap = (Map<String, String>) preBills.getAll();
        committeesMap = (Map<String, String>) preCommittees.getAll();

        final Gson gson = new Gson();
        favorLegislators = new ArrayList<>();
        favorBills = new ArrayList<>();
        favorCommittees = new ArrayList<>();
        for (Map.Entry<String, String> entry : legislatorMap.entrySet()) {
            favorLegislators.add(gson.fromJson(entry.getValue(), Legislator.class));
        }
        SidebarUtils.getIndexList(favorLegislators, mapIndex, "name");
        displayIndex(mapIndex);
        for (Map.Entry<String, String> entry : billsMap.entrySet()) {
            favorBills.add(gson.fromJson(entry.getValue(), Bill.class));
        }
        for (Map.Entry<String, String> entry : committeesMap.entrySet()) {
            favorCommittees.add(gson.fromJson(entry.getValue(), Committee.class));
        }
        LegislatorListAdapter adapter1 = new LegislatorListAdapter(getActivity(), favorLegislators);
        BillListAdapter adapter2 = new BillListAdapter(getActivity(), favorBills);
        CommitteeListAdapter adapter3 = new CommitteeListAdapter(getActivity(), favorCommittees);
        lvFavorBills.setAdapter(adapter2);
        lvFavorLegislators.setAdapter(adapter1);
        lvFavorCommittees.setAdapter(adapter3);

        lvFavorLegislators.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Legislator data = (Legislator) parent.getItemAtPosition(position);

                Intent intent1 = new Intent("com.xiezhuohan.csci571_hw9.legislators");
                intent1.putExtra("legislator", data);
                startActivity(intent1);
            }
        });
        lvFavorBills.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bill data = (Bill) parent.getItemAtPosition(position);
                Intent intent2 = new Intent("com.xiezhuohan.csci571_hw9.bills");
                intent2.putExtra("bill_detail", gson.toJson(data, Bill.class));
                startActivity(intent2);
            }
        });
        lvFavorCommittees.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent3 = new Intent("com.xiezhuohan.csci571_hw9.committees");
                Committee item = (Committee) parent.getItemAtPosition(position);
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
        preLegislators = this.getActivity().getSharedPreferences("favorLegislators", Context.MODE_APPEND);
        preBills = this.getActivity().getSharedPreferences("favorBills", Context.MODE_APPEND);
        preCommittees = this.getActivity().getSharedPreferences("favorCommittees", Context.MODE_APPEND);

        Map<String, String> legislatorMap, billsMap, committeesMap;
        legislatorMap = (Map<String, String>) preLegislators.getAll();
        billsMap = (Map<String, String>) preBills.getAll();
        committeesMap = (Map<String, String>) preCommittees.getAll();

        final Gson gson = new Gson();
        favorLegislators = new ArrayList<>();
        favorBills = new ArrayList<>();
        favorCommittees = new ArrayList<>();
        for (Map.Entry<String, String> entry : legislatorMap.entrySet()) {
            favorLegislators.add(gson.fromJson(entry.getValue(), Legislator.class));
        }
        for (Map.Entry<String, String> entry : billsMap.entrySet()) {
            favorBills.add(gson.fromJson(entry.getValue(), Bill.class));
        }
        for (Map.Entry<String, String> entry : committeesMap.entrySet()) {
            favorCommittees.add(gson.fromJson(entry.getValue(), Committee.class));
        }
        LegislatorListAdapter adapter1 = new LegislatorListAdapter(getActivity(), favorLegislators);
        BillListAdapter adapter2 = new BillListAdapter(getActivity(), favorBills);
        CommitteeListAdapter adapter3 = new CommitteeListAdapter(getActivity(), favorCommittees);
        lvFavorBills.setAdapter(adapter2);
        lvFavorLegislators.setAdapter(adapter1);
        lvFavorCommittees.setAdapter(adapter3);

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
        indexLayout = (LinearLayout) favorView.findViewById(R.id.fav_side_index);
        TextView textView;
        List<String> indexList = new ArrayList<String>(mapIndex.keySet());
        for (String index : indexList) {
            textView = (TextView) getActivity().getLayoutInflater().inflate(
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
        lvFavorLegislators.setSelection(mapIndex.get(selectedIndex.getText()));
    }
}
