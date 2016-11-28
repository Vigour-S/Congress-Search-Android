package com.xiezhuohan.csci571_hw9.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.gson.Gson;
import com.xiezhuohan.csci571_hw9.R;
import com.xiezhuohan.csci571_hw9.model.bills.Bill;


/**
 * Created by xiezhuohan on 11/27/16.
 */
public class BillDetail extends AppCompatActivity {
    private TextView bill_id;
    private TextView bill_title;
    private TextView bill_type;
    private TextView bill_sponsor;
    private TextView bill_introduce;
    private TextView bill_status;
    private TextView bill_congress;
    private TextView bill_url;
    private TextView bill_version;
    private TextView bill_chamber;
    private ImageSwitcher favor_btn;
    private Bill bill;
    private boolean saved;
    SharedPreferences preferences;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_details);
        preferences=getSharedPreferences("favor_bills", MODE_APPEND);
        final SharedPreferences.Editor editor=preferences.edit();
        Toolbar mtoolbar = (Toolbar) findViewById(R.id.tool_bar_bill);
        setSupportActionBar(mtoolbar);
        //getSupportActionBar().setTitle("Bills");
        mtoolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        favor_btn = (ImageSwitcher)findViewById(R.id.favorite_button);
        bill_id = (TextView)findViewById(R.id.bill_id_data);
        bill_title = (TextView)findViewById(R.id.bill_title_data);
        bill_type = (TextView)findViewById(R.id.bill_type_data);
        bill_sponsor=(TextView)findViewById(R.id.bill_sponsor_data);
        bill_introduce=(TextView)findViewById(R.id.biil_introduce_data);
        bill_status=(TextView)findViewById(R.id.biil_status_data);
        bill_congress=(TextView)findViewById(R.id.bill_congress_data);
        bill_url=(TextView)findViewById(R.id.biil_url_data);
        bill_version=(TextView)findViewById(R.id.biil_version_data);
        bill_chamber=(TextView)findViewById(R.id.bill_chamber_data);


        Intent intent=getIntent();
        final Gson gson=new Gson();
        bill=gson.fromJson(intent.getStringExtra("bill_detail"), Bill.class);

        favor_btn.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return new ImageView(BillDetail.this);
            }
        });
        favor_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(saved==false){
                    favor_btn.setImageResource(R.drawable.star_filled);
                    saved=true;
                    editor.putString(bill.bill_id, gson.toJson(bill, Bill.class));
                    editor.commit();
                }
                else {
                    favor_btn.setImageResource(R.drawable.star_empty);
                    saved=false;
                    editor.remove(bill.bill_id);
                    editor.commit();
                }
            }
        });
        if(preferences.getString(bill.bill_id, "0").equals("0")){
            saved=false;
            favor_btn.setImageResource(R.drawable.star_empty);
        }
        else {
            saved=true;
            favor_btn.setImageResource(R.drawable.star_filled);
        }
        bill_sponsor.setText(bill.sponsor.title+". "+bill.sponsor.last_name+", "+bill.sponsor.first_name);
        bill_id.setText(bill.bill_id);
        bill_title.setText(bill.official_title);
        bill_type.setText(bill.bill_type);
        bill_introduce.setText(bill.introduced_on);
        if(bill.last_version.urls.pdf==null){
            bill_url.setText("None");
        }
        else {
            bill_url.setText(bill.last_version.urls.pdf);
        }
        bill_congress.setText(bill.urls.congress);
        if(bill.history.active.equals("true")){
            bill_status.setText("Active");
        }
        else{
            bill_status.setText("New");
        }
        bill_chamber.setText(bill.chamber);
        bill_version.setText(bill.last_version.version_name);
    }
}
