package com.xiezhuohan.csci571_hw9.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by xiezhuohan on 11/27/16.
 */

public class HttpUtils {

    public static String getAllLegislators = "http://csci571.zesvu2ttmu.us-west-1.elasticbeanstalk.com/index.php?legislators=true";
    public static String getAllActiveBills = "http://csci571.zesvu2ttmu.us-west-1.elasticbeanstalk.com/index.php?allActiveBills=true";
    public static String getAllNewBills = "http://csci571.zesvu2ttmu.us-west-1.elasticbeanstalk.com/index.php?allNewBills=true";
    public static String getAllCommittees = "http://csci571.zesvu2ttmu.us-west-1.elasticbeanstalk.com/index.php?allCommittees=true";

    public static String getJSONFromHTTP(String url) {
        StringBuffer sb = new StringBuffer();
        try {
            URL httpUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) httpUrl
                    .openConnection();
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            String str;

            while ((str = bufferedReader.readLine()) != null) {
                sb.append(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}