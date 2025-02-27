package com.cyberiyke.converter.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberiyke.converter.data.repository.MainRepository
import com.cyberiyke.converter.utils.ConvertEvent
import com.cyberiyke.converter.utils.Resource
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
        if (amount.isBlank()) return

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
}

