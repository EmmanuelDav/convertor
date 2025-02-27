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

    fun getConvertRate(
        from :String,
        to:String,
        amount:String
    ){
        //amount check
        if (amount.isBlank()){
            return
        }
        viewModelScope.launch {
            _conversion.value = ConvertEvent.Loading
            when(val result = mainRepository.convertRate(from ,to ,amount)){
                is Resource.Error -> {
                    _conversion.value = ConvertEvent.Error(result.message)
                }
                is Resource.Success -> {
                    if (result.data != null){
                        _conversion.value = ConvertEvent.Success(result.data)
                    }else{
                        _conversion.value = ConvertEvent.Error(null)
                    }
                }
            }
        }
    }
}