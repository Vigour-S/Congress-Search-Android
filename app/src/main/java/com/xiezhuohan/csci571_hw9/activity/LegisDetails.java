package com.xiezhuohan.csci571_hw9.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import com.xiezhuohan.csci571_hw9.R;
import com.xiezhuohan.csci571_hw9.model.legislators.Legislator;
import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Date;


/**
 * Created by xiezhuohan on 11/26/16.
 */

public class LegisDetails extends AppCompatActivity {

    private Legislator legislator;
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
        legislator = intent.getParcelableExtra("legislator");


        Picasso.with(this).load("https://theunitedstates.io/images/congress/original/"
                +legislator.bioguide_id+".jpg").into(photo);
        name.setText(legislator.name);
        email.setText(legislator.oc_email);
        chamber.setText(legislator.chamber);
        contact.setText(legislator.phone);
        office.setText(legislator.office);
        state.setText(legislator.state);

        if(legislator.party.equals("R")){
            party_img.setImageResource(R.drawable.r);
            party.setText("Republican");
        }
        else if(legislator.party.equals("D")){
            party_img.setImageResource(R.drawable.d);
            party.setText("Democrat");
        }

        FastDateFormat parser=FastDateFormat.getInstance("yyyy-MM-dd");
        try {
            Date startDay=parser.parse(legislator.term_start);
            FastDateFormat printer=FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
            legislator.startTerm=printer.format(startDay);
            Date endDay=parser.parse(legislator.term_end);
            legislator.endTerm=printer.format(endDay);
            Date now=new Date();
            legislator.termProgress=(double) (now.getTime()-startDay.getTime())/(endDay.getTime()-startDay.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        start_term.setText(legislator.startTerm);
        end_term.setText(legislator.endTerm);
        termBar.setProgress((int)(legislator.termProgress*100));
        String strProgress = String.valueOf((int)(legislator.termProgress*100)) + "%";
        bar_percentage.setText(strProgress);

        favoriteBtn.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return new ImageView(LegisDetails.this);
            }
        });

        if(preferences.getString(legislator.bioguide_id, "0").equals("0")){
            saved=false;
            favoriteBtn.setImageResource(R.drawable.star_empty);
        }
        else {
            saved=true;
            favoriteBtn.setImageResource(R.drawable.star_filled);
        }

        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(saved==false){
                    favoriteBtn.setImageResource(R.drawable.star_filled);
                    saved=true;
                    SharedPreferences.Editor editor=preferences.edit();
                    Gson gson=new Gson();
                    editor.putString(legislator.bioguide_id,gson.toJson(legislator, Legislator.class));
                    editor.commit();
                }
                else{
                    favoriteBtn.setImageResource(R.drawable.star_empty);
                    saved=false;
                    SharedPreferences.Editor editor=preferences.edit();
                    Gson gson=new Gson();
                    editor.remove(legislator.bioguide_id);
                    editor.commit();
                }
            }
        });

    }
}
