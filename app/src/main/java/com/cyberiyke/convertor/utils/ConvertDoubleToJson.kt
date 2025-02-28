package com.cyberiyke.convertor.utils

import androidx.core.animation.TypeConverter
import com.google.gson.Gson


/**
  Since Room can't store Maps, we serialize the rates map into a JSON String and convert it back.
 */

class ConvertDoubleToJson {
//    @TypeConverter
//    fun fromRatesMap(rates: Map<String, Double>): String {
//        return Gson().toJson(rates)
//    }
//
//    @TypeConverter
//    fun toRatesMap(ratesString: String): Map<String, Double> {
//        return Gson().fromJson(ratesString, object : TypeToken<Map<String, Double>>() {}.type)
//    }
}