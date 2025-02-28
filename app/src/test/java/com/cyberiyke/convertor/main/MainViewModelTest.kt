package com.cyberiyke.convertor.main

import com.cyberiyke.convertor.data.remote.model.RatesResult


import com.cyberiyke.convertor.data.repository.MainRepository
import com.cyberiyke.convertor.data.repository.MainRepositoryImpl
import com.cyberiyke.convertor.getOrAwaitValueTest
import com.cyberiyke.convertor.utils.ConvertEvent
import com.cyberiyke.convertor.utils.Resource
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Mock
    private lateinit var mainRepository: MainRepositoryImpl

    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        mainViewModel = MainViewModel(mainRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cleanupTestCoroutines()
    }

    @Test
    fun `getConvertRate should emit Loading and then Success when API call is successful`() = testScope.runBlockingTest {

        val from = "USD"
        val to = "NGN"
        val amount = "100"
        val rates = mapOf("USD" to 1.0, "NGN" to 450.0)
        val ratesResult = RatesResult(success = true, base = "USD", rates = rates, date = "2024-02-28", timestamp = 1740758103)

        `when`(mainRepository.convertRate(from, to)).thenReturn(Resource.Success(ratesResult))

        // Act
        mainViewModel.getConvertRate(from, to, amount)

        // Assert
        val loadingEvent = mainViewModel.conversion.getOrAwaitValueTest()
        assertThat(loadingEvent).isEqualTo(ConvertEvent.Success("45000.00"))

        val successEvent = mainViewModel.conversion.first { it is ConvertEvent.Success }
        assertThat(successEvent).isEqualTo(ConvertEvent.Success("45000.00"))
    }

    @Test
    fun `getConvertRate should emit Loading and then Error when API call fails`() = testScope.runBlockingTest {

        val from = "USD"
        val to = "NGN"
        val amount = "100"
        val errorMessage = "Network error"

        `when`(mainRepository.convertRate(from, to)).thenReturn(Resource.Error(errorMessage))

        mainViewModel.getConvertRate(from, to, amount)

        val errorEvent = mainViewModel.conversion.first { it is ConvertEvent.Error }
        assertThat(errorEvent).isEqualTo(ConvertEvent.Error(errorMessage))
    }

    @Test
    fun `getConvertRate should emit Error when amount is blank`() = testScope.runBlockingTest {
        // Arrange
        val from = "USD"
        val to = "NGN"
        val amount = ""

        `when`(mainRepository.convertRate(from, to)).thenReturn(Resource.Error("Invalid amount"))

        // Act
        mainViewModel.getConvertRate(from, to, amount)

        // Assert
        val errorEvent = mainViewModel.conversion.getOrAwaitValueTest()
        assertThat(errorEvent).isEqualTo(ConvertEvent.Error("Invalid amount"))
    }

    @Test
    fun `getConvertRate should emit Error when currency selection is invalid`() = testScope.runBlockingTest {

        val from = "USD"
        val to = "XYZ"
        val amount = "100"
        val rates = mapOf("USD" to 1.0, "NGN" to 450.0)
        val ratesResult = RatesResult(success = true, base = "USD", rates = rates, date = "2024-02-28", timestamp = 1740758103)

        `when`(mainRepository.convertRate(from, to)).thenReturn(Resource.Success(ratesResult))

        mainViewModel.getConvertRate(from, to, amount)

        val errorEvent = mainViewModel.conversion.first { it is ConvertEvent.Error }
        assertThat(errorEvent).isEqualTo(ConvertEvent.Error("Invalid currency selection"))
    }
}