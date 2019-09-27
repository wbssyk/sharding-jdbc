package com.example.shardingjdbc.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @ClassName DatetimeUtil
 * @Author yakun.shi
 * @Date 2019/5/29 13:11
 * @Version 1.0
 **/
public class DatetimeUtil {

    public static String dateToString(LocalDateTime dateTime){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String format = dateTimeFormatter.format(dateTime);
        return format;
    }


    public static String formatDate(Date keyValue,String logicTable){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(logicTable);
        String format = simpleDateFormat.format(keyValue);
        return format;
    }

    public static Date getDate(String stringDate,String formart){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formart);
        Date parse = null;
        try {
             parse = simpleDateFormat.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse;
    }

    public static void main(String[] args) {
        Date date = DatetimeUtil.getDate("2019-10-10", "yyyy-MM-dd");
        System.out.println(date);
    }
}
