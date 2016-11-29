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
import com.xiezhuohan.csci571_hw9.adapter.BillJsonAdapter;
import com.xiezhuohan.csci571_hw9.model.bills.Bill;
import com.xiezhuohan.csci571_hw9.model.bills.Bills;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class BillsFragment extends Fragment implements AdapterView.OnItemClickListener, TabHost.TabContentFactory, TabHost.OnTabChangeListener {
    private View billView;
    TabHost tabHost;
    private ListView lstView;
    private ListView lstNewBills;
    private List<Bill> itemBeanList;
    private List<Bill> newBillList;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        billView = inflater.inflate(R.layout.fragment_bills, container, false);
        lstView = (ListView) billView.findViewById(R.id.activeBillList);
        lstNewBills = (ListView) billView.findViewById((R.id.newBillList));

        new LegisAsyncTask().execute(HttpUtils.getAllActiveBills, "1");
        new LegisAsyncTask().execute(HttpUtils.getAllNewBills, "2");
        tabHost = (TabHost) billView.findViewById(R.id.tabhost);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("Active Bills").setContent(R.id.tab1));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("New Bills").setContent(R.id.tab2));
        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(this);
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent3 = new Intent("com.xiezhuohan.csci571_hw9.bills");
                Bill data = (Bill) parent.getItemAtPosition(position);
                Gson gson = new Gson();
                intent3.putExtra("bill_detail", gson.toJson(data, Bill.class));
                startActivity(intent3);
            }
        };
        lstView.setOnItemClickListener(onItemClickListener);
        lstNewBills.setOnItemClickListener(onItemClickListener);

        return billView;
    }

    class LegisAsyncTask extends AsyncTask<String, Void, List<Bill>> {
        private String key;

        @Override
        protected List<Bill> doInBackground(String... params) {
            key = params[1];
            return getJsonData(params[0], params[1]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Bill> result) {
            super.onPostExecute(result);
            if (key.equals("1")) {
                BillJsonAdapter adapter = new BillJsonAdapter(getActivity(), itemBeanList);
                lstView.setAdapter(adapter);
            } else {
                BillJsonAdapter adapter1 = new BillJsonAdapter(getActivity(), newBillList);
                lstNewBills.setAdapter(adapter1);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

    public List<Bill> getJsonData(String jsonUrl, String key) {
        try {

            String results = HttpUtils.getJSONFromHTTP(jsonUrl);
            Gson gson = new Gson();
            if (key.equals("1")) {
                itemBeanList = gson.fromJson(results, Bills.class).results;

                Collections.sort(itemBeanList, new Comparator<Bill>() {
                    public int compare(Bill o1, Bill o2) {
                        if (o1.bill_id.equals(o2.bill_id))
                            return 0;
                        return o1.bill_id.compareTo(o2.bill_id);
                    }
                });
            } else {
                newBillList = gson.fromJson(results, Bills.class).results;
                Collections.sort(newBillList, new Comparator<Bill>() {
                    public int compare(Bill o1, Bill o2) {
                        if (o1.bill_id.equals(o2.bill_id))
                            return 0;
                        return o1.bill_id.compareTo(o2.bill_id);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (key.equals("1")) {
            return itemBeanList;
        } else {
            return newBillList;
        }
    }
}
