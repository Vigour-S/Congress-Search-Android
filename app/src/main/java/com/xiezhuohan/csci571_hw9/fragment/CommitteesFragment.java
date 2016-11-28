package com.xiezhuohan.csci571_hw9.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import com.google.gson.Gson;
import com.xiezhuohan.csci571_hw9.R;
import com.xiezhuohan.csci571_hw9.Utils.HttpUtils;
import com.xiezhuohan.csci571_hw9.adapter.ComJsonAdapter;
import com.xiezhuohan.csci571_hw9.model.committees.Committee;
import com.xiezhuohan.csci571_hw9.model.committees.Committees;

import java.util.ArrayList;
import java.util.List;


public class CommitteesFragment extends Fragment implements AdapterView.OnItemClickListener, TabHost.TabContentFactory, TabHost.OnTabChangeListener {
    private View committeeView;
    private TabHost tabHost;
    private ListView lstHouse;
    private ListView lstSenate;
    private ListView lstJoint;
    private List<Committee> comHouseList;
    private List<Committee> comSenateList;
    private List<Committee> comJointList;
    private List<Committee> comAllList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        committeeView = inflater.inflate(R.layout.fragment_committees, container, false);

        tabHost = (TabHost) committeeView.findViewById(R.id.tabhost);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("House").setContent(R.id.tab1));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("Senate").setContent(R.id.tab2));
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("Joint").setContent(R.id.tab3));
        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(this);
        lstHouse = (ListView) committeeView.findViewById(R.id.committee_house);
        lstSenate = (ListView) committeeView.findViewById(R.id.committee_senate);
        lstJoint = (ListView) committeeView.findViewById(R.id.committee_joint);
        new CommitteeAsyncTask().execute(HttpUtils.getAllCommittees);
        final Gson gson = new Gson();
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent("com.xiezhuohan.csci571_hw9.committees");
                Committee item = (Committee) parent.getItemAtPosition(position);
                intent1.putExtra("committee_detail", gson.toJson(item, Committee.class));
                startActivity(intent1);
            }
        };

        lstHouse.setOnItemClickListener(onItemClickListener);
        lstSenate.setOnItemClickListener(onItemClickListener);
        lstJoint.setOnItemClickListener(onItemClickListener);

        return committeeView;
    }

    private class CommitteeAsyncTask extends AsyncTask<String, Void, List<Committee>> {

        @Override
        protected List<Committee> doInBackground(String... params) {
            return getJsonData(HttpUtils.getAllCommittees);
        }

        @Override
        protected void onPostExecute(List<Committee> committees) {
            super.onPostExecute(committees);

            ComJsonAdapter adapter1 = new ComJsonAdapter(getActivity(), comHouseList);
            lstHouse.setAdapter(adapter1);
            ComJsonAdapter adapter2 = new ComJsonAdapter(getActivity(), comSenateList);
            lstSenate.setAdapter(adapter2);
            ComJsonAdapter adapter3 = new ComJsonAdapter(getActivity(), comJointList);
            lstJoint.setAdapter(adapter3);
        }
    }

    private List<Committee> getJsonData(String jsonUrl) {

        String results = HttpUtils.getJSONFromHTTP(jsonUrl);
        Gson gson = new Gson();
        comAllList = gson.fromJson(results, Committees.class).results;
        comHouseList = new ArrayList<Committee>();
        comSenateList = new ArrayList<Committee>();
        comJointList = new ArrayList<Committee>();
        for (Committee entry : comAllList) {
            if (entry.chamber.equals("house")) {
                comHouseList.add(entry);
            } else if (entry.chamber.equals("senate")) {
                comSenateList.add(entry);
            } else {
                comJointList.add(entry);
            }
        }

        return comAllList;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onTabChanged(String tabId) {

    }

    @Override
    public View createTabContent(String tag) {
        return null;
    }
}
