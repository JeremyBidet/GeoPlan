package fr.upem.geoplan.core.planning;

import android.util.Log;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by pierre on 06/03/2016.
 */
public class StartDateFormat extends DateFormat {

    private boolean isCloseDay(Calendar calendar, Calendar now) {
        return Math.abs(calendar.getTimeInMillis() - now.getTimeInMillis()) <  TimeUnit.DAYS.toMillis(1);
    }

    private StringBuffer formatCloseDay(Calendar calendar, Calendar now, StringBuffer buffer) {
        final Long delta = calendar.getTimeInMillis() - now.getTimeInMillis();
        final Long deltaDays = delta / TimeUnit.DAYS.toMillis(1);

        switch (deltaDays.intValue()) {
            default:
                return buffer;
            case 0:
                String date_min=":%d";
                String date_hour="%d";
                if(calendar.get(Calendar.MINUTE) >= 0 && calendar.get(Calendar.MINUTE) <10) {
                    date_min = ":0%d";
                }
                if(calendar.get(Calendar.HOUR_OF_DAY) >= 0 && calendar.get(Calendar.HOUR_OF_DAY) <10) {
                    date_hour = "0%d";
                }
                if (delta > 0) {
                        return  buffer.append(String.format("tomorrow at " + date_hour + date_min, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
                } else {
                        return buffer.append(String.format("yesterday at " + date_hour + date_min, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
                }
        }

    }

    @Override
    public StringBuffer format(Date date, StringBuffer buffer, FieldPosition field) {
        Calendar now = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (year != now.get(Calendar.YEAR)) {
            buffer
                    .append(day)
                    .append('/')
                    .append(month + 1)
                    .append('/')
                    .append(year);
        } else if (isCloseDay(calendar, now)) {
            buffer = formatCloseDay(calendar, now, buffer);
        } else if (month != now.get(Calendar.MONTH)) {
            buffer
                    .append(day)
                    .append('/')
                    .append(month + 1);
        } else {
            long deltaDays = day - now.get(Calendar.DAY_OF_MONTH);
            if (deltaDays > 0) {
                if(deltaDays == 1) {
                    buffer
                            .append(String.format("in %d day", deltaDays));
                }
                else {
                    buffer
                            .append(String.format("in %d days", deltaDays));
                }
            } else {
                if(deltaDays == -1) {
                    buffer
                            .append(String.format("%d day ago", -deltaDays));
                }
                else {
                    buffer
                            .append(String.format("%d days ago", -deltaDays));
                }
            }
        }

        return buffer;
    }

    @Override
    public Date parse(String string, ParsePosition position) {
        return null;
    }
}
