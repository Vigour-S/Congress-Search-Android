package com.xiezhuohan.csci571_hw9.fragment;


import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xiezhuohan.csci571_hw9.R;
import com.xiezhuohan.csci571_hw9.Utils.HttpUtils;
import com.xiezhuohan.csci571_hw9.adapter.JsonAdapter;
import com.xiezhuohan.csci571_hw9.model.legislators.Legislator;
import com.xiezhuohan.csci571_hw9.model.legislators.Legislators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.xiezhuohan.csci571_hw9.R.layout.fragment_legislators;


public class LegislatorsFragment extends Fragment implements AdapterView.OnItemClickListener, TabHost.TabContentFactory, TabHost.OnTabChangeListener, View.OnClickListener{
    private View legisView;
    private ListView lstView ;
    private ListView lstHouse;
    private ListView lstSenate;
    private int tabIndex;

    private String jsonUrl="http://sample-env.5p7uahjtiv.us-west-2.elasticbeanstalk.com/csci571hw8/LoadPHP.php?key=legis&cham=all";
    private String jsonUrlHouse="http://sample-env.5p7uahjtiv.us-west-2.elasticbeanstalk.com/csci571hw8/LoadPHP.php?key=legis&chamber=senate";
    private List<Legislator> legislatorList;
    private List<Legislator> itemHouseList;
    private List<Legislator> itemSenateList;
    TabHost tabHost;
    Map<String, Integer> mapIndex;
    Map<String, Integer> mapIndex2;
    Map<String, Integer> mapIndex3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        legisView = inflater.inflate(fragment_legislators, container,false);
        lstView = (ListView)legisView.findViewById(R.id.legisList);
        lstHouse = (ListView)legisView.findViewById(R.id.houseLegisList);
        lstSenate = (ListView)legisView.findViewById(R.id.legisSenateList);

        tabIndex=1;
        new LegisAsyncTask().execute(jsonUrl);
        tabHost =(TabHost) legisView.findViewById(R.id.tabhost);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("BY STATES").setContent(R.id.tab1));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("HOUSE").setContent(R.id.tab2));
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("SENATE").setContent(R.id.tab3));

        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(this);

        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Legislator data = (Legislator)parent.getItemAtPosition(position);
                Intent intent=new Intent("com.xiezhuohan.csci571_hw9.viewlegis");
                intent.putExtra("bioguide_id", data.bioguide_id.toString());
                startActivity(intent);
            }
        };

        lstView.setOnItemClickListener(onItemClickListener);
        lstHouse.setOnItemClickListener(onItemClickListener);
        lstSenate.setOnItemClickListener(onItemClickListener);

        return legisView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onTabChanged(String tabId) {
        if(tabId.equals("tab1")){
            tabIndex=1;
        }
        else if(tabId.equals("tab2")){
            tabIndex=2;
        }
        else if(tabId.equals("tab3")){
            tabIndex=3;
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
        if(tabIndex==1)
            lstView.setSelection(mapIndex.get(selectedIndex.getText()));
        if(tabIndex==2)
            lstHouse.setSelection(mapIndex2.get(selectedIndex.getText()));
        if(tabIndex==3)
            lstSenate.setSelection(mapIndex3.get(selectedIndex.getText()));
    }

    class LegisAsyncTask extends AsyncTask<String, Void, List<Legislator>> {
        @Override
        protected List<Legislator> doInBackground(String... params) {
            return getJsonData(jsonUrl);
        }

        @Override
        protected void onPostExecute(List<Legislator> result) {

            //解析完毕后，进行适配器的数据设置填充
            JsonAdapter adapter = new JsonAdapter(getActivity(), legislatorList);
            lstView.setAdapter(adapter);
            JsonAdapter adapter2 = new JsonAdapter(getActivity(), itemHouseList);
            lstHouse.setAdapter(adapter2);
            JsonAdapter adapter3 = new JsonAdapter(getActivity(), itemSenateList);
            lstSenate.setAdapter(adapter3);
            getIndexList(legislatorList);
            displayIndex("All", mapIndex);
            getIndexListHouse(itemHouseList);
            displayIndex("House", mapIndex2);
            getIndexListSenate(itemSenateList);
            displayIndex("Senate", mapIndex3);
        }

    }

    public List<Legislator> getJsonData(String jsonUrl) {
        String results = HttpUtils.getJSONfromHTTP(jsonUrl);

        legislatorList = new ArrayList<Legislator>();
        itemHouseList = new ArrayList<Legislator>();
        itemSenateList = new ArrayList<Legislator>();

        Gson gson=new Gson();
        legislatorList = gson.fromJson(results, Legislators.class).results;


            for (int i = 0; i < legislatorList.size(); i++) {
                Legislator legislator = legislatorList.get(i);

                legislator.name = legislator.last_name + ", " + legislator.first_name;

                if(legislator.chamber.equals("house")){
                    itemHouseList.add(legislator);
                }
                if(legislator.chamber.equals("senate")){
                    itemSenateList.add(legislator);
                }
            }

            Collections.sort(legislatorList, new Comparator<Legislator>(){
                public int compare(Legislator o1, Legislator o2){
                    if(o1.state_name.equals(o2.state_name))
                        return 0;
                    return o1.state_name.compareTo(o2.state_name);
                }
            });
            Collections.sort(itemHouseList, new Comparator<Legislator>(){
                public int compare(Legislator o1, Legislator o2){
                    if(o1.name.equals(o2.name))
                        return 0;
                    return o1.name.compareTo(o2.name);
                }
            });
            Collections.sort(itemSenateList, new Comparator<Legislator>(){
                public int compare(Legislator o1, Legislator o2){
                    if(o1.name.equals(o2.name))
                        return 0;
                    return o1.name.compareTo(o2.name);
                }
            });

        return legislatorList;
    }

    private void getIndexList(List<Legislator> list) {
        mapIndex = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < list.size(); i++) {
            String stateName = list.get(i).state;
            String index = stateName.substring(0, 1);

            if (mapIndex.get(index) == null)
                mapIndex.put(index, i);
        }
    }
    private void getIndexListHouse(List<Legislator> list) {
        mapIndex2 = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < list.size(); i++) {
            String stateName = list.get(i).name;
            String index = stateName.substring(0, 1);

            if (mapIndex2.get(index) == null)
                mapIndex2.put(index, i);
        }
    }
    private void getIndexListSenate(List<Legislator> list) {
        mapIndex3 = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < list.size(); i++) {
            String stateName = list.get(i).name;
            String index = stateName.substring(0, 1);

            if (mapIndex3.get(index) == null)
                mapIndex3.put(index, i);
        }
    }

    private void displayIndex(String sideId, Map<String, Integer> mapIndex) {
        LinearLayout indexLayout;
        if (sideId.equals("All")) {
            indexLayout = (LinearLayout) getView().findViewById(R.id.side_index);
        }
        else if(sideId.equals("House")) {
            indexLayout = (LinearLayout) getView().findViewById(R.id.side_index2);
        }
        else {
            indexLayout = (LinearLayout) getView().findViewById(R.id.side_index3);
        }

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
}
