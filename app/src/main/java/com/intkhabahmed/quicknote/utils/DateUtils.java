package com.intkhabahmed.quicknote.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static String getFormattedTime(long timeInMillis, long currentTime) {
        return android.text.format.DateUtils.getRelativeTimeSpanString(timeInMillis, currentTime, android.text.format.DateUtils.MINUTE_IN_MILLIS).toString();
    }

    public static String getFormattedTime(long timeInMillis) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/YYYY", SimpleDateFormat.getAvailableLocales()[0]);
        return formatter.format(new Date(timeInMillis));
    }
}
