package com.example.todolist.Utils;

import android.content.Context;
import android.database.DatabaseUtils;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DateUtility {
    private static DateUtility instance;
    private Context mContext;

    private DateUtility(Context context) {
        mContext = context;
    }

    public static DateUtility getInstance(Context context) {
        if (instance == null) {
            instance = new DateUtility(context);
        }
        return instance;
    }

    //    static DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    public LocalDate getCurrentDate() {
        return (LocalDate.now(Clock.systemDefaultZone()));
    }

    public String convertLocalDateToString(LocalDate date) {
        return date.format(dateTimeFormatter);
    }
}
