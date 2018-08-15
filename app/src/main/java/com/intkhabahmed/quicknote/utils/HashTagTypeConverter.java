package com.intkhabahmed.quicknote.utils;

import android.arch.persistence.room.TypeConverter;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HashTagTypeConverter {
    @TypeConverter
    public static String toJson(List<String> hashTags) {
        return Arrays.toString(hashTags.toArray());
    }

    @TypeConverter
    public static List<String> toList(String jsonString) {
        JSONArray jsonArray;
        List<String> tags = new ArrayList<>();
        try {
            jsonArray = new JSONArray(jsonString);
            for (int i=0;i<jsonArray.length();i++) {
                tags.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tags;
    }
}
