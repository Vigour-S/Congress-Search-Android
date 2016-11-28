package com.xiezhuohan.csci571_hw9.model.bills;

/**
 * Created by xiezhuohan on 11/27/16.
 */
public class Bill {
    public String bill_id;
    public String bill_type;
    public String official_title;
    public String introduced_on;
    public String short_title;
    public String chamber;
    public String long_title;
    public Sponsor sponsor = new Sponsor();
    public Version last_version = new Version();
    public Urls urls = new Urls();
    public History history = new History();
}

