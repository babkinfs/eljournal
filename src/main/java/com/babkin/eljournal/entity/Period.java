package com.babkin.eljournal.entity;

import java.util.Arrays;
import java.util.List;

public class Period {
    //ONCE_A_WEEK,EVERY_WEEK;
    private static List<String> listWeek = Arrays.asList(
            "Один день (1)",
            "Раз в две недели (7)",
            "Раз в две недели (8)",
            "Раз в две недели (9)",
            "Каждую неделю (14)",
            "Каждую неделю (15)",
            "Каждую неделю (16)",
            "Каждую неделю (17)",
            "Каждую неделю (18)");
    public static int getListWeekLength(){
        return listWeek.size();
    }
    public static List<String> getListWeek() {
        return listWeek;
    }
    public static int getLength(int index){
        if (index == 0)
            return 1;
        if (index < 4)
            return 14;
        return 7;
    }
    public static int getSemestr(int index){//getCount
        String parm = listWeek.get( index );
        int start = parm.indexOf( "(" )+1;
        int end = parm.indexOf( ")" );
        String number = parm.substring( start, end );
        return Integer.parseInt( number );
    }
}
