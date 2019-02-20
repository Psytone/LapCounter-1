package io.github.evgeniypetushkov.lapcounter.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.room.TypeConverter;

class RunsConverter {

    private final Gson gson = new Gson();

    @TypeConverter
    public String fromLaps(ArrayList<String> laps) {
        if (laps == null) return null;
        return gson.toJson(laps);
    }

    @TypeConverter
    public ArrayList<String> toLaps(String data) {
        if (data == null) return null;
        Type listType = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(data, listType);
    }
}