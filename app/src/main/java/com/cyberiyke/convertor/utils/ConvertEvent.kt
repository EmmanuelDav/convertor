package com.cyberiyke.convertor.utils

import com.cyberiyke.convertor.data.remote.model.RatesResult

sealed class ConvertEvent{
    data class Success(val result: String): ConvertEvent()
    data class Error(val errorMessage:String?): ConvertEvent()
    object Loading: ConvertEvent()
    object Empty: ConvertEvent()
}