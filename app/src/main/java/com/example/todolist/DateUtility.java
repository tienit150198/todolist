package com.example.todolist;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DateUtility {
    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(Clock.systemDefaultZone().toString());

    public static LocalDate getCurrentDate() {
        return (LocalDate.now(Clock.systemDefaultZone()));
    }

    public static String convertLocalDateToString(LocalDate date) {
        return dateFormat.format(date);
    }
}
