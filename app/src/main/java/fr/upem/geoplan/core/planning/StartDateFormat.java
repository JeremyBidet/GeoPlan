package fr.upem.geoplan.core.planning;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by pierre on 06/03/2016.
 */
public class StartDateFormat extends DateFormat {

    private long getMilliInDay(int day) {
        final Calendar closeDays = Calendar.getInstance();
        closeDays.set(0, 0, day);

        return closeDays.getTimeInMillis();
    }

    private boolean isCloseDay(Calendar calendar, Calendar now) {
        return Math.abs(calendar.getTimeInMillis() - now.getTimeInMillis()) < getMilliInDay(1);
    }

    private StringBuffer formatCloseDay(Calendar calendar, Calendar now, StringBuffer buffer) {
        final Long delta = calendar.getTimeInMillis() - now.getTimeInMillis();
        final Long deltaDays = delta / getMilliInDay(1);

        switch (deltaDays.intValue()) {
            default:
                return buffer;
            case 0:
                if (delta > 0) {
                    return buffer.append(String.format("tomorrow at %d:%d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
                } else {
                    return buffer.append(String.format("yesterday at %d:%d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
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
                buffer
                        .append(String.format("in %d days", deltaDays));
            } else {
                buffer
                        .append(String.format("there is %d days", deltaDays));
            }
        }

        return buffer;
    }

    @Override
    public Date parse(String string, ParsePosition position) {
        return null;
    }
}
