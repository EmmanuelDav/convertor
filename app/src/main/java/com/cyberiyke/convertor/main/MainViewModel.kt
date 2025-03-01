package com.cyberiyke.convertor.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberiyke.convertor.data.repository.MainRepository
import com.cyberiyke.convertor.utils.ConvertEvent
import com.cyberiyke.convertor.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
): ViewModel(){

    private val _conversion = MutableStateFlow<ConvertEvent>(ConvertEvent.Empty)
    val conversion: StateFlow<ConvertEvent>
        get() = _conversion


    /**
        over here we did something simple but better,
        (we i mean i) we converted it by knowing the value of the currency
     */


    fun getConvertRate(from: String, to: String, amount: String) {
        if (amount.isEmpty()){
            _conversion.value = ConvertEvent.Error("Invalid amount") // Emit an error when amount is blank
        }

        viewModelScope.launch {
            _conversion.value = ConvertEvent.Loading

            when (val result = mainRepository.convertRate(from, to)) {
                is Resource.Error -> {
                    _conversion.value = ConvertEvent.Error(result.message)
                }
                is Resource.Success -> {
                    val rates = result.data?.rates
                    if (rates != null && rates.containsKey(from) && rates.containsKey(to)) {
                        val fromRate = rates[from] ?: return@launch
                        val toRate = rates[to] ?: return@launch
                        val amountValue = amount.toDoubleOrNull() ?: return@launch

                        // Perform the conversion when we have gotten the current rate of the currency
                        // conversion = (amount the user inputted / the rate that the want to convert from) * rate they want to convert to

                        val convertedAmount = (amountValue / fromRate) * toRate

                        _conversion.value = ConvertEvent.Success(getFormatted(convertedAmount))
                    } else {
                        _conversion.value = ConvertEvent.Error("Invalid currency selection")
                    }
                }
            }
        }
    }

    private fun getFormatted(amount: Double): String {
        return String.format("%.2f", amount)
    }

    val currencyToCountryMap = mapOf(
        "USD" to "us", "EUR" to "eu", "GBP" to "gb",
        "AUD" to "au", "CAD" to "ca", "CHF" to "ch",
        "CNY" to "cn", "JPY" to "jp", "NZD" to "nz",
        "SGD" to "sg", "HKD" to "hk", "INR" to "in",
        "BRL" to "br", "ZAR" to "za", "MXN" to "mx",
        "NGN" to "ng", "EGP" to "eg", "ARS" to "ar",
        "COP" to "co", "KES" to "ke", "PKR" to "pk",
        "BDT" to "bd", "GHS" to "gh", "UAH" to "ua",
        "IQD" to "iq"
    )

    fun getFlagUrl(currencyCode: String): String {
        return "https://flagcdn.com/w40/${currencyToCountryMap[currencyCode]}.png"
    }

}

