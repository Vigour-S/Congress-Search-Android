package com.xiezhuohan.csci571_hw9.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.gson.Gson;
import com.xiezhuohan.csci571_hw9.R;
import com.xiezhuohan.csci571_hw9.Utils.HttpUtils;
import com.xiezhuohan.csci571_hw9.Utils.SidebarUtils;
import com.xiezhuohan.csci571_hw9.adapter.LegislatorListAdapter;
import com.xiezhuohan.csci571_hw9.model.legislators.Legislator;
import com.xiezhuohan.csci571_hw9.model.legislators.Legislators;

import java.util.*;

import static com.xiezhuohan.csci571_hw9.R.layout.fragment_legislators;

public class LegislatorsFragment extends Fragment implements AdapterView.OnItemClickListener, TabHost.TabContentFactory, TabHost.OnTabChangeListener, View.OnClickListener {
    private View legislatorView;
    private ListView lstView;
    private ListView lstHouse;
    private ListView lstSenate;
    private int tabIndex;

    private List<Legislator> AllLegislators;
    private List<Legislator> houseLegislators;
    private List<Legislator> senateLegislators;
    private TabHost tabHost;
    private Map<String, Integer> mapIndex = new LinkedHashMap<String, Integer>();
    private Map<String, Integer> mapIndex2 = new LinkedHashMap<String, Integer>();
    private Map<String, Integer> mapIndex3 = new LinkedHashMap<String, Integer>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        legislatorView = inflater.inflate(fragment_legislators, container, false);
        lstView = (ListView) legislatorView.findViewById(R.id.legisList);
        lstHouse = (ListView) legislatorView.findViewById(R.id.houseLegisList);
        lstSenate = (ListView) legislatorView.findViewById(R.id.legisSenateList);


        new LegislatorAsyncTask().execute(HttpUtils.getAllLegislators);
        tabHost = (TabHost) legislatorView.findViewById(R.id.tabhost);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("BY STATES").setContent(R.id.tab1));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("HOUSE").setContent(R.id.tab2));
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("SENATE").setContent(R.id.tab3));

        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(this);

        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Legislator data = (Legislator) parent.getItemAtPosition(position);
                Intent intent = new Intent("com.xiezhuohan.csci571_hw9.legislators");
                intent.putExtra("legislator", data);
                startActivity(intent);
            }
        };

        lstView.setOnItemClickListener(onItemClickListener);
        lstHouse.setOnItemClickListener(onItemClickListener);
        lstSenate.setOnItemClickListener(onItemClickListener);

        return legislatorView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onTabChanged(String tabId) {
        if (tabId.equals("tab1")) {
            tabIndex = 1;
        } else if (tabId.equals("tab2")) {
            tabIndex = 2;
        } else if (tabId.equals("tab3")) {
            tabIndex = 3;
        }
    }

    @Override
    public View createTabContent(String tag) {
        return null;
    }

    @Override
    public void onClick(View v) {
        v.getLayoutParams();
        TextView selectedIndex = (TextView) v;
        if (tabIndex == 1)
            lstView.setSelection(mapIndex.get(selectedIndex.getText()));
        if (tabIndex == 2)
            lstHouse.setSelection(mapIndex2.get(selectedIndex.getText()));
        if (tabIndex == 3)
            lstSenate.setSelection(mapIndex3.get(selectedIndex.getText()));
    }

    class LegislatorAsyncTask extends AsyncTask<String, Void, List<Legislator>> {
        @Override
        protected List<Legislator> doInBackground(String... params) {
            return getJsonData(HttpUtils.getAllLegislators);
        }

        @Override
        protected void onPostExecute(List<Legislator> result) {

            LegislatorListAdapter adapter = new LegislatorListAdapter(getActivity(), AllLegislators);
            lstView.setAdapter(adapter);
            LegislatorListAdapter adapter2 = new LegislatorListAdapter(getActivity(), houseLegislators);
            lstHouse.setAdapter(adapter2);
            LegislatorListAdapter adapter3 = new LegislatorListAdapter(getActivity(), senateLegislators);
            lstSenate.setAdapter(adapter3);
            SidebarUtils.getIndexList(AllLegislators, mapIndex, "state");
            displayIndex("All", mapIndex);
            SidebarUtils.getIndexList(houseLegislators, mapIndex2, "name");
            displayIndex("House", mapIndex2);
            SidebarUtils.getIndexList(senateLegislators, mapIndex3, "name");
            displayIndex("Senate", mapIndex3);
        }

    }

    public List<Legislator> getJsonData(String jsonUrl) {
        String results = HttpUtils.getJSONFromHTTP(jsonUrl);

        AllLegislators = new ArrayList<Legislator>();
        houseLegislators = new ArrayList<Legislator>();
        senateLegislators = new ArrayList<Legislator>();
        Gson gson = new Gson();
        AllLegislators = gson.fromJson(results, Legislators.class).results;


        for (int i = 0; i < AllLegislators.size(); i++) {
            Legislator legislator = AllLegislators.get(i);

            legislator.name = legislator.last_name + ", " + legislator.first_name;

            if (legislator.chamber.equals("house")) {
                houseLegislators.add(legislator);
            }
            if (legislator.chamber.equals("senate")) {
                senateLegislators.add(legislator);
            }
        }

        Comparator<Legislator> allLegislatorComparator = new Comparator<Legislator>() {
            public int compare(Legislator o1, Legislator o2) {
                if (o1.state_name.equals(o2.state_name))
                    return 0;
                return o1.state_name.compareTo(o2.state_name);
            }
        };
        Comparator<Legislator> legislatorComparator = new Comparator<Legislator>() {
            public int compare(Legislator o1, Legislator o2) {
                if (o1.name.equals(o2.name))
                    return 0;
                return o1.name.compareTo(o2.name);
            }
        };

        Collections.sort(AllLegislators, allLegislatorComparator);
        Collections.sort(houseLegislators, legislatorComparator);
        Collections.sort(senateLegislators, legislatorComparator);

        return AllLegislators;
    }

    private void displayIndex(String sideId, Map<String, Integer> mapIndex) {
        LinearLayout indexLayout;
        if (sideId.equals("All")) {
            indexLayout = (LinearLayout) legislatorView.findViewById(R.id.side_index);
        } else if (sideId.equals("House")) {
            indexLayout = (LinearLayout) legislatorView.findViewById(R.id.side_index2);
        } else {
            indexLayout = (LinearLayout) legislatorView.findViewById(R.id.side_index3);
        }

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
}
