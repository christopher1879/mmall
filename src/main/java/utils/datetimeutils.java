package utils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class datetimeutils {
    public static String standard_format="yyyy-MM-dd HH:mm:ss";

    public static Date strtodate(String datetimestr,String formatstr){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatstr);
        DateTime dateTime = dateTimeFormatter.parseDateTime(datetimestr);
        return dateTime.toDate();
    }

    public  static Date strtodate(String datetimestr){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(standard_format);
        DateTime dateTime= dateTimeFormatter.parseDateTime(datetimestr);
        return dateTime.toDate();

    }




    public static String dateToStr(Date date,String formatStr){
        if(date == null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(formatStr);
    }


    public static String dateToStr(Date date){
        if(date == null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(standard_format);
    }


}
