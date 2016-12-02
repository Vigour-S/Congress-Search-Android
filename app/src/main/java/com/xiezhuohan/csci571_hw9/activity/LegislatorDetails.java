package com.xiezhuohan.csci571_hw9.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.xiezhuohan.csci571_hw9.R;
import com.xiezhuohan.csci571_hw9.Utils.MyButtonOnClickListener;
import com.xiezhuohan.csci571_hw9.model.legislators.Legislator;
import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Date;


/**
 * Created by xiezhuohan on 11/26/16.
 */

public class LegislatorDetails extends AppCompatActivity {

    private Legislator legislator;
    private ImageSwitcher favoriteBtn;
    private boolean saved = false;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ImageView photo;
        TextView name;
        TextView party;
        ImageView party_img;
        TextView email;
        TextView chamber;
        TextView contact;
        TextView start_term;
        TextView end_term;
        ProgressBar termBar;
        TextView bar_percentage;
        TextView office;
        TextView state;
        ImageView facebookBtn;
        ImageView twitterBtn;
        ImageView websiteBtn;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.legislator_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setTitle("Legislator Info");

        preferences = getSharedPreferences("favorLegislators", MODE_APPEND);
        photo = (ImageView) findViewById(R.id.legis_photo);
        name = (TextView) findViewById(R.id.tv_name_data);
        party_img = (ImageView) findViewById(R.id.party_icon);
        party = (TextView) findViewById(R.id.party_name);
        email = (TextView) findViewById(R.id.tv_email_data);
        chamber = (TextView) findViewById(R.id.tv_chamber_data);
        contact = (TextView) findViewById(R.id.tv_contact_data);
        start_term = (TextView) findViewById(R.id.tv_startTerm_data);
        end_term = (TextView) findViewById(R.id.tv_endTerm_data);
        termBar = (ProgressBar) findViewById(R.id.termProgress);
        bar_percentage = (TextView) findViewById(R.id.tv_progress_horizontal);
        office = (TextView) findViewById(R.id.tv_office_data);
        state = (TextView) findViewById(R.id.tv_state_data);
        favoriteBtn = (ImageSwitcher) findViewById(R.id.favorite_btn);
        facebookBtn = (ImageView) findViewById(R.id.facebook_icon);
        twitterBtn = (ImageView) findViewById(R.id.twitter_icon);
        websiteBtn = (ImageView) findViewById(R.id.website_icon);

        Intent intent = getIntent();
        legislator = intent.getParcelableExtra("legislator");


        Picasso.with(this).load("https://theunitedstates.io/images/congress/original/"
                + legislator.bioguide_id + ".jpg").into(photo);
        name.setText(legislator.name);
        email.setText(legislator.oc_email);
        chamber.setText(legislator.chamber);
        contact.setText(legislator.phone);
        office.setText(legislator.office);
        state.setText(legislator.state);

        if (legislator.party.equals("R")) {
            party_img.setImageResource(R.drawable.r);
            party.setText("Republican");
        } else if (legislator.party.equals("D")) {
            party_img.setImageResource(R.drawable.d);
            party.setText("Democrat");
        }

        FastDateFormat parser = FastDateFormat.getInstance("yyyy-MM-dd");
        try {
            Date startDay = parser.parse(legislator.term_start);
            FastDateFormat printer = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
            legislator.startTerm = printer.format(startDay);
            Date endDay = parser.parse(legislator.term_end);
            legislator.endTerm = printer.format(endDay);
            Date now = new Date();
            legislator.termProgress = (double) (now.getTime() - startDay.getTime()) / (endDay.getTime() - startDay.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        start_term.setText(legislator.startTerm);
        end_term.setText(legislator.endTerm);
        termBar.setProgress((int) (legislator.termProgress * 100));
        String strProgress = String.valueOf((int) (legislator.termProgress * 100)) + "%";
        bar_percentage.setText(strProgress);

        favoriteBtn.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return new ImageView(LegislatorDetails.this);
            }
        });

        if (preferences.getString(legislator.bioguide_id, "0").equals("0")) {
            saved = false;
            favoriteBtn.setImageResource(R.drawable.star_empty);
        } else {
            saved = true;
            favoriteBtn.setImageResource(R.drawable.star_filled);
        }

        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!saved) {
                    favoriteBtn.setImageResource(R.drawable.star_filled);
                    saved = true;
                    SharedPreferences.Editor editor = preferences.edit();
                    Gson gson = new Gson();
                    editor.putString(legislator.bioguide_id, gson.toJson(legislator, Legislator.class));
                    editor.apply();
                } else {
                    favoriteBtn.setImageResource(R.drawable.star_empty);
                    saved = false;
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.remove(legislator.bioguide_id);
                    editor.apply();
                }
            }
        });

        facebookBtn.setOnClickListener(new MyButtonOnClickListener(legislator.facebook_id, "facebook", getApplicationContext()));
        twitterBtn.setOnClickListener(new MyButtonOnClickListener(legislator.twitter_id, "twitter", getApplicationContext()));
        websiteBtn.setOnClickListener(new MyButtonOnClickListener(legislator.website, "website", getApplicationContext()));
    }
}
