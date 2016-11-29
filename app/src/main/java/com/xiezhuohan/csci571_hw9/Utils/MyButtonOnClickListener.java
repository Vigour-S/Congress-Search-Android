package com.xiezhuohan.csci571_hw9.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

/**
 * Created by xiezhuohan on 11/28/16.
 */
public class MyButtonOnClickListener implements View.OnClickListener {
    private String url;
    private String type;
    private Context context;

    public MyButtonOnClickListener(String url, String type, Context context) {
        this.url = url;
        this.type = type;
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        String fullUrl = "";
        switch (type) {
            case "facebook": fullUrl = "https://www.facebook.com/" + url; break;
            case "twitter" : fullUrl = "https://twitter.com/" + url; break;
            case "website" : fullUrl = url;
            default: //error;
        }
        if (url == null || fullUrl.equals("")) {
            Toast.makeText(context, type + " is not available!", Toast.LENGTH_SHORT).show();
        } else {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fullUrl));
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(browserIntent);
        }

    }
}
