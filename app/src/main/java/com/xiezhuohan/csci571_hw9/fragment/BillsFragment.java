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
import android.widget.ListView;
import android.widget.TabHost;

import com.google.gson.Gson;
import com.xiezhuohan.csci571_hw9.R;
import com.xiezhuohan.csci571_hw9.adapter.BillJsonAdapter;
import com.xiezhuohan.csci571_hw9.model.bills.BillItem;
import com.xiezhuohan.csci571_hw9.model.bills.BillItemList;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class BillsFragment extends Fragment implements AdapterView.OnItemClickListener, TabHost.TabContentFactory, TabHost.OnTabChangeListener{
    private View billView;
    TabHost tabHost;
    private ListView lstView ;
    private ListView lstNewBills;
    private String jsonUrl="http://sample-env.5p7uahjtiv.us-west-2.elasticbeanstalk.com/csci571hw8/LoadPHP.php?key=activeBill";
    private String jsonUrl2="http://sample-env.5p7uahjtiv.us-west-2.elasticbeanstalk.com/csci571hw8/LoadPHP.php?key=newBill";
    private List<BillItem> itemBeanList;
    private List<BillItem> newBillList;
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
        billView = inflater.inflate(R.layout.fragment_bills, container,false);
        lstView = (ListView) billView.findViewById(R.id.activeBillList);
        lstNewBills=(ListView) billView.findViewById((R.id.newBillList));

        new LegisAsyncTask().execute(jsonUrl,"1");
        new LegisAsyncTask().execute(jsonUrl2,"2");
        tabHost =(TabHost) billView.findViewById(R.id.tabhost);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("Active Bills").setContent(R.id.tab1));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("New Bills").setContent(R.id.tab2));
        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(this);
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent3 = new Intent("com.xiezhuohan.csci571_hw9.bills");
                BillItem data=(BillItem) parent.getItemAtPosition(position);
                Gson gson=new Gson();
                intent3.putExtra("bill_detail", gson.toJson(data, BillItem.class));
                startActivity(intent3);
            }};
        lstView.setOnItemClickListener(onItemClickListener);
        lstNewBills.setOnItemClickListener(onItemClickListener);

        return billView;
    }

    class LegisAsyncTask extends AsyncTask<String, Void, List<BillItem>> {
        private String key;
        @Override
        protected List<BillItem> doInBackground(String... params) {
            key=params[1];
            return getJsonData(params[0],params[1]);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<BillItem> result) {
            super.onPostExecute(result);
            //解析完毕后，进行适配器的数据设置填充
            if(key.equals("1")) {
                BillJsonAdapter adapter = new BillJsonAdapter(getActivity(), itemBeanList);
                lstView.setAdapter(adapter);
            }
            else{
                BillJsonAdapter adapter1 = new BillJsonAdapter(getActivity(), newBillList);
                lstNewBills.setAdapter(adapter1);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }
    public List<BillItem> getJsonData(String jsonUrl, String key) {
        try {
            //创建url http地址
            URL httpUrl = new URL(jsonUrl);
            //打开http 链接
            HttpURLConnection connection = (HttpURLConnection) httpUrl
                    .openConnection();
            //设置参数  请求为get请求
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");

            //connection.getInputStream()得到字节输入流，InputStreamReader从字节到字符的桥梁，外加包装字符流
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            //创建字符串容器
            StringBuffer sb = new StringBuffer();
            String str = "";
            //行读取
            while ((str = bufferedReader.readLine()) != null) {
                // 当读取完毕，就添加到容器中
                sb.append(str);
            }

            Gson gson=new Gson();
            if(key.equals("1")) {
                itemBeanList = gson.fromJson(sb.toString(), BillItemList.class).results;

                Collections.sort(itemBeanList, new Comparator<BillItem>() {
                    public int compare(BillItem o1, BillItem o2) {
                        if (o1.bill_id == o2.bill_id)
                            return 0;
                        return o1.bill_id.compareTo(o2.bill_id);
                    }
                });
            }
            else{
                newBillList= gson.fromJson(sb.toString(), BillItemList.class).results;
                Collections.sort(newBillList, new Comparator<BillItem>() {
                    public int compare(BillItem o1, BillItem o2) {
                        if (o1.bill_id == o2.bill_id)
                            return 0;
                        return o1.bill_id.compareTo(o2.bill_id);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(key.equals("1")) {
            return itemBeanList;
        }
        else{
            return newBillList;
        }
    }
}
