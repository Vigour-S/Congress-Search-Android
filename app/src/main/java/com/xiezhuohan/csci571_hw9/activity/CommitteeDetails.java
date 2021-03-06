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
import com.xiezhuohan.csci571_hw9.model.committees.Committee;

/**
 * Created by xiezhuohan on 11/27/16.
 */
public class CommitteeDetails extends AppCompatActivity {

    private ImageSwitcher favor_btn;
    private Committee committee;
    private boolean saved;

    @Override
    public void onCreate(Bundle savedInstanceState) {

         TextView committee_id;
         TextView committee_name;
         TextView committee_chamber;
         TextView committee_parent;
         TextView committee_contact;
         TextView committee_office;
         ImageView committee_chamber_icon;
         SharedPreferences preferences;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.committees_details);
        preferences = getSharedPreferences("favorCommittees", MODE_APPEND);
        final SharedPreferences.Editor editor = preferences.edit();
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_bill);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setTitle("Committee Info");
        favor_btn = (ImageSwitcher)findViewById(R.id.favorite_button);
        committee_id = (TextView)findViewById(R.id.com_id_data);
        committee_name = (TextView)findViewById(R.id.com_name_data);
        committee_chamber = (TextView)findViewById(R.id.com_chamber_data);
        committee_parent = (TextView)findViewById(R.id.com_parent_data);
        committee_office = (TextView)findViewById(R.id.com_office_data);
        committee_contact = (TextView)findViewById(R.id.com_contact_data);
        committee_chamber_icon = (ImageView)findViewById(R.id.chamber_icon);

        Intent intent = getIntent();
        final Gson gson = new Gson();
        committee = gson.fromJson(intent.getStringExtra("committee_detail"), Committee.class);

        favor_btn.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return new ImageView(CommitteeDetails.this);
            }
        });
        favor_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!saved) {
                    favor_btn.setImageResource(R.drawable.star_filled);
                    saved = true;
                    editor.putString(committee.committee_id, gson.toJson(committee, Committee.class));
                    editor.apply();
                } else {
                    favor_btn.setImageResource(R.drawable.star_empty);
                    saved = false;
                    editor.remove(committee.committee_id);
                    editor.apply();
                }
            }
        });
        if (preferences.getString(committee.committee_id, "0").equals("0")) {
            saved = false;
            favor_btn.setImageResource(R.drawable.star_empty);
        } else {
            saved = true;
            favor_btn.setImageResource(R.drawable.star_filled);
        }
        committee_id.setText(committee.committee_id);
        committee_name.setText(committee.name);
        committee_chamber.setText(committee.chamber);
        if (committee.chamber.equals("house"))
            committee_chamber_icon.setImageResource(R.drawable.h);
        if (committee.phone != null && !committee.phone.equals(""))
            committee_contact.setText(committee.phone);
        if (committee.parent_committee_id != null && !committee.parent_committee_id.equals(""))
            committee_parent.setText(committee.parent_committee_id);
        if (committee.office != null && !committee.office.equals(""))
            committee_office.setText(committee.office);
    }
}
