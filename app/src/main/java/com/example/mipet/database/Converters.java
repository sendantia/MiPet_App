package com.example.mipet.database;

import androidx.room.TypeConverter;

import java.sql.Date;
import java.sql.Time;

public class Converters {
    @TypeConverter
    //convierte un objeto lon en un date
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    //convierte un objeto date a un long
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Time fromTimes(Long value) {
        return value == null ? null : new Time(value);
    }

    @TypeConverter
    public static Long timeToTimestamp(Time time) {
        return time == null ? null : time.getTime();
    }
}
