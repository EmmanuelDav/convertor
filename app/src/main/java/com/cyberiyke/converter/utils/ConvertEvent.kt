package com.cyberiyke.converter.utils

import com.cyberiyke.converter.data.remote.model.ExchangeResponse

sealed class ConvertEvent{
    data class Success(val result: ExchangeResponse): ConvertEvent()
    data class Error(val errorMessage:String?): ConvertEvent()
    object Loading: ConvertEvent()
    object Empty: ConvertEvent()
}