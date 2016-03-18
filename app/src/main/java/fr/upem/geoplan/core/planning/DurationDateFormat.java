package fr.upem.geoplan.core.planning;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by pierre on 06/03/2016.
 */
public class DurationDateFormat extends DateFormat {
    @Override
    public StringBuffer format(Date date, StringBuffer buffer, FieldPosition field) {
        buffer.append(String.format("%d hours", date.getTime() / (1000 * 60 * 60)));
        return buffer;
    }

    @Override
    public Date parse(String string, ParsePosition position) {
        return null;
    }
}
