package com.xiezhuohan.csci571_hw9.model.legislators;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiezhuohan on 11/26/16.
 */
public class Legislator implements Parcelable{
    public String bioguide_id;
    public String first_name;
    public String last_name;
    public String name;
    public String state;
    public String state_name;
    public String title;
    public String chamber;
    public String district;
    public String party;
    public String imageViewUrl;
    public String term_start;
    public String term_end;
    public String oc_email;
    public String phone;
    public String office;
    public double termProgress;
    public String startTerm;
    public String endTerm;

    public Legislator(Parcel in) {
        bioguide_id = in.readString();
        first_name = in.readString();
        last_name = in.readString();
        name = in.readString();
        state = in.readString();
        state_name = in.readString();
        title = in.readString();
        chamber = in.readString();
        district = in.readString();
        party = in.readString();
        imageViewUrl = in.readString();
        term_start = in.readString();
        term_end = in.readString();
        oc_email = in.readString();
        phone = in.readString();
        office = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(bioguide_id);
        parcel.writeString(first_name);
        parcel.writeString(last_name);
        parcel.writeString(name);
        parcel.writeString(state);
        parcel.writeString(state_name);
        parcel.writeString(title);
        parcel.writeString(chamber);
        parcel.writeString(district);
        parcel.writeString(party);
        parcel.writeString(imageViewUrl);
        parcel.writeString(term_start);
        parcel.writeString(term_end);
        parcel.writeString(oc_email);
        parcel.writeString(phone);
        parcel.writeString(office);
    }

    public static final Parcelable.Creator<Legislator> CREATOR
            = new Parcelable.Creator<Legislator>() {
        public Legislator createFromParcel (Parcel in) {
            return new Legislator(in);
        }
        public Legislator[] newArray(int size) {
            return new Legislator[size];
        }
    };
}
