package com.cyberiyke.converter.utils

import com.cyberiyke.converter.data.remote.model.RatesResult

sealed class ConvertEvent{
    data class Success(val result: Double): ConvertEvent()
    data class Error(val errorMessage:String?): ConvertEvent()
    object Loading: ConvertEvent()
    object Empty: ConvertEvent()
}