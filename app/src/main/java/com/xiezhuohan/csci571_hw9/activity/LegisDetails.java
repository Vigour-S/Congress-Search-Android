package com.xiezhuohan.csci571_hw9.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import com.xiezhuohan.csci571_hw9.R;
import com.xiezhuohan.csci571_hw9.model.legislators.DetailBean;
import org.apache.commons.lang3.time.FastDateFormat;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;


/**
 * Created by xiezhuohan on 11/26/16.
 */

public class LegisDetails extends AppCompatActivity {
    private String jsonUrl;
    private DetailBean personInfo;
    private ImageView photo;
    private TextView name;
    private TextView party;
    private ImageView party_img;
    private TextView email;
    private TextView chamber;
    private TextView contact;
    private TextView start_term;
    private TextView end_term;
    private ProgressBar termBar;
    private TextView bar_percentage;
    private TextView office;
    private TextView state;
    private ImageSwitcher favoriteBtn;
    private boolean saved=false;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.legis_details);
        Toolbar mtoolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //setSupportActionBar(toolbar);
        preferences=getSharedPreferences("favor_legis", MODE_APPEND);
        photo=(ImageView)findViewById(R.id.legis_photo);
        name=(TextView)findViewById(R.id.tv_name_data);
        party_img=(ImageView)findViewById(R.id.party_icon);
        party=(TextView)findViewById(R.id.party_name);
        email=(TextView)findViewById(R.id.tv_email_data);
        chamber=(TextView)findViewById(R.id.tv_chamber_data);
        contact=(TextView)findViewById(R.id.tv_contact_data);
        start_term=(TextView)findViewById(R.id.tv_startTerm_data);
        end_term=(TextView)findViewById(R.id.tv_endTerm_data);
        termBar=(ProgressBar)findViewById(R.id.termProgress);
        bar_percentage = (TextView)findViewById(R.id.tv_progress_horizontal);
        office = (TextView)findViewById(R.id.tv_office_data);
        state = (TextView)findViewById(R.id.tv_state_data);
        favoriteBtn=(ImageSwitcher)findViewById(R.id.favorite_btn);


        Intent intent=getIntent();
        String bioguide_id=intent.getStringExtra("bioguide_id");
        jsonUrl="http://sample-env.5p7uahjtiv.us-west-2.elasticbeanstalk.com/csci571hw8/LoadPHP.php?key=legisDetail&bioguideId="+bioguide_id;
        new LegisAsyncTask(this).execute(jsonUrl);
        favoriteBtn.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return new ImageView(LegisDetails.this);
            }
        });



        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(saved==false){
                    favoriteBtn.setImageResource(R.drawable.star_filled);
                    saved=true;
                    SharedPreferences.Editor editor=preferences.edit();
                    Gson gson=new Gson();
                    editor.putString(personInfo.bioId,gson.toJson(personInfo, DetailBean.class));
                    editor.commit();
                }
                else{
                    favoriteBtn.setImageResource(R.drawable.star_empty);
                    saved=false;
                    SharedPreferences.Editor editor=preferences.edit();
                    Gson gson=new Gson();
                    editor.remove(personInfo.bioId);
                    editor.commit();
                }
            }
        });
    }

    class LegisAsyncTask extends AsyncTask<String, Void, DetailBean> {
        private Context mContext;
        public LegisAsyncTask(Context context){
            mContext=context;
        }
        @Override
        protected DetailBean doInBackground(String... params) {
            return getJsonData(jsonUrl);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(DetailBean result) {
            super.onPostExecute(result);
            //解析完毕后，进行适配器的数据设置填充
            Picasso.with(mContext).load("https://theunitedstates.io/images/congress/original/"+personInfo.bioId+".jpg").into(photo);
            name.setText(personInfo.name);
            email.setText(personInfo.email);
            chamber.setText(personInfo.chamber);
            contact.setText(personInfo.contact);
            office.setText(personInfo.office);
            state.setText(personInfo.state);

            if(personInfo.party.equals("R")){
                party_img.setImageResource(R.drawable.r);
                party.setText("Republican");
            }
            else if(personInfo.party.equals("D")){
                party_img.setImageResource(R.drawable.d);
                party.setText("Democrat");
            }

            start_term.setText(personInfo.startTerm);
            end_term.setText(personInfo.endTerm);
            termBar.setProgress((int)(personInfo.termProgress*100));
            String strProgress = String.valueOf((int)(personInfo.termProgress*100)) + "%";
            bar_percentage.setText(strProgress);
            if(preferences.getString(personInfo.bioId, "0").equals("0")){
                saved=false;
                favoriteBtn.setImageResource(R.drawable.star_empty);
            }
            else {
                saved=true;
                favoriteBtn.setImageResource(R.drawable.star_filled);
            }
        }
    }

    public DetailBean getJsonData(String jsonUrl) {
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
            //测试是否得到json字符串
            Log.e("TAG", ""+sb.toString());
            //创建本地对象的集合
            personInfo=new DetailBean();
            // 整体是一个jsonObject
            JSONObject jsonObject = new JSONObject(sb.toString());
            // 键是jsonArray数组
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            //获取jsonArray中的每个对象
            JSONObject jsonObject2 = jsonArray.getJSONObject(0);
            //创建本地的newsBean对象
//                DetailBean detailBean = new DetailBean();
            //为该对象进行属性值的设置操作
//				newsBean.imageViewUrl = jsonObject2
//						.getString("picSmall");
            personInfo.name = jsonObject2.getString("last_name")+", "+jsonObject2.getString("first_name");
            personInfo.state = jsonObject2.getString("state");
            personInfo.email=jsonObject2.getString("oc_email");
            personInfo.bioId=jsonObject2.getString("bioguide_id");
            personInfo.party=jsonObject2.getString("party");
            personInfo.chamber=jsonObject2.getString("chamber");
            personInfo.contact=jsonObject2.getString("phone");
            personInfo.startTerm=jsonObject2.getString("term_start");
            personInfo.office = jsonObject2.getString("office");

            FastDateFormat parser=FastDateFormat.getInstance("yyyy-MM-dd");
            Date startDay=parser.parse(personInfo.startTerm);
            FastDateFormat printer=FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
            personInfo.startTerm=printer.format(startDay);

            personInfo.endTerm=jsonObject2.getString("term_end");
            Date endDay=parser.parse(personInfo.endTerm);
            personInfo.endTerm=printer.format(endDay);
            Date now=new Date();
            personInfo.termProgress=(double) (now.getTime()-startDay.getTime())/(endDay.getTime()-startDay.getTime());
            //添加对象，组建集合


        } catch (Exception e) {
            e.printStackTrace();
        }
        return personInfo;
    }
}
