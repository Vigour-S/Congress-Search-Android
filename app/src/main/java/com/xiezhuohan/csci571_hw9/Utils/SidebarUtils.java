package com.xiezhuohan.csci571_hw9.Utils;


import com.xiezhuohan.csci571_hw9.model.legislators.Legislator;

import java.util.List;
import java.util.Map;

/**
 * Created by xiezhuohan on 11/28/16.
 */
public class SidebarUtils {
    public static void getIndexList(List<Legislator> list, Map<String, Integer> mapIndex, String order) {
        for (int i = 0; i < list.size(); i++) {
            String fullIndex = "";
            if (order.equals("state")) {
                fullIndex = list.get(i).state;
            } else if (order.equals("name")) {
                fullIndex = list.get(i).name;
            } else {
                //error
            }

            String index = fullIndex.substring(0, 1);

            if (mapIndex.get(index) == null)
                mapIndex.put(index, i);
        }
    }

}
