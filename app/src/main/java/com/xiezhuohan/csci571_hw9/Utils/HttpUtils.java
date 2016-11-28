package com.xiezhuohan.csci571_hw9.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by xiezhuohan on 11/27/16.
 */

public class HttpUtils {

    public static String getAllLegislators = "http://sample-env.5p7uahjtiv.us-west-2.elasticbeanstalk.com/csci571hw8/LoadPHP.php?key=legis&cham=all";
    public static String getAllActiveBills = "http://sample-env.5p7uahjtiv.us-west-2.elasticbeanstalk.com/csci571hw8/LoadPHP.php?key=activeBill";
    public static String getAllNewBills = "http://sample-env.5p7uahjtiv.us-west-2.elasticbeanstalk.com/csci571hw8/LoadPHP.php?key=newBill";
    public static String getAllCommittees = "http://sample-env.5p7uahjtiv.us-west-2.elasticbeanstalk.com/csci571hw8/LoadPHP.php?key=allCommittee";

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