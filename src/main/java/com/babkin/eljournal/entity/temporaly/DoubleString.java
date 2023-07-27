package com.babkin.eljournal.entity.temporaly;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DoubleString  implements Comparable<DoubleString> {
    private String first;
    private String second;
    public DoubleString(String first,String second){
        this.first = first;
        this.second = second;
    }

    public String getFirst() {
        return first;
    }

    public String getSecond() {
        return second;
    }

    @Override
    public int compareTo(@NotNull DoubleString parm) {
        Date firstDate = null;
        try {
            firstDate = parseDate( first, "dd-MM-yyyy");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date lastDate = null;
        try {
            lastDate = parseDate( parm.first, "dd-MM-yyyy");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int result = firstDate.compareTo( lastDate );
        if (result == 0){
            int res = second.compareTo( parm.second );
            return second.compareTo( parm.second );
        }
        return result;
    }
    private Date parseDate(String date, String format) throws ParseException
    {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.parse(date);
    }

}
