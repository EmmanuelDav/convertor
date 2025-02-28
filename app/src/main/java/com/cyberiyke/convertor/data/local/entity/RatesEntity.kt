package com.cyberiyke.convertor.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchange_rates")
data class RatesEntity(
    @PrimaryKey val base: String,
    val success: Boolean,
    val date: String,
    val timestamp: Long,
    val rates: String // Will store the map as a JSON string
)
