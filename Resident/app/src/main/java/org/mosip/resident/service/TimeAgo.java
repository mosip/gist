package org.mosip.resident.service;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeAgo {

        public static final List<Long> times = Arrays.asList(
                TimeUnit.DAYS.toMillis(365),
                TimeUnit.DAYS.toMillis(30),
                TimeUnit.DAYS.toMillis(1),
                TimeUnit.HOURS.toMillis(1),
                TimeUnit.MINUTES.toMillis(1),
                TimeUnit.SECONDS.toMillis(1) );
        public static final List<String> timesString = Arrays.asList("year","month","day","hour","minute","second");

        static String toDuration(long duration) {

            StringBuffer res = new StringBuffer();
            for(int i=0;i< TimeAgo.times.size(); i++) {
                Long current = TimeAgo.times.get(i);
                long temp = duration/current;
                if(temp>0) {
                    res.append(temp).append(" ").append( TimeAgo.timesString.get(i) ).append(temp != 1 ? "s" : "").append(" ago");
                    break;
                }
            }
            if("".equals(res.toString()))
                return "just now";
            else
                return res.toString();
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        static long toDuration(String dateVal, String timeVal) throws ParseException {

            Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(dateVal);
            //11:17:47.295

            Instant instant = Instant.ofEpochMilli( 1_484_063_246L );
            ZoneOffset zoneOffset = ZoneId.systemDefault().getRules().getOffset( instant );
            int offsetInSeconds = zoneOffset.getTotalSeconds() ;
            Log.d("resident -offset=", String.valueOf(offsetInSeconds));
            Date time1 = new SimpleDateFormat("hh:mm:ss").parse(timeVal);
            long recTime = date1.getTime() + time1.getTime() ;//+ (offsetInSeconds *1000) ;
            Log.d("resident -recTime=", String.valueOf(recTime));

            long duration  = new Date().getTime() - recTime;
            Log.d("resident dur(sec)", String.valueOf(duration/1000));

            return( duration);

            //return( (new Date().getTime()-offset) - (date1.getTime() + time1.getTime()));

        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        public static String toTimeAgo(String dateVal, String timeVal) throws ParseException {
            long dur = toDuration(dateVal,timeVal);

            return toDuration(dur);
        }
}
