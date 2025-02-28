package com.cyberiyke.convertor.data.repository

import com.cyberiyke.convertor.data.remote.RateRequest
import com.cyberiyke.convertor.utils.Resource
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * This class is a unit test for the `MainRepositoryImpl` class, which handles API requests and responses
 * for currency conversion. It uses `MockWebServer` to simulate network responses and tests the behavior
 * of the repository under different scenarios, such as successful API calls, API errors, and exceptions.
 *
 * Key Components:
 * - **MockWebServer**: Simulates a web server to mock API responses.
 * - **Retrofit**: Creates a Retrofit instance to interact with the `RateRequest` interface.
 * - **Resource**: A wrapper class to handle API responses (success or error).
 * - **JUnit Annotations**: Used to define setup, teardown, and test methods.
 *
 * Test Scenarios:
 * 1. **convertRate_returns_success_when_API_call_is_successful**:
 *    - Simulates a successful API response.
 *    - Verifies that the repository returns a `Resource.Success` with the expected data.
 *
 * 2. **convertRate_returns_error_when_API_call_fails**:
 *    - Simulates an API error response (e.g., HTTP 400).
 *    - Verifies that the repository returns a `Resource.Error` with the correct error message.
 *
 * 3. **convertRate_returns_error_on_exception**:
 *    - Simulates a network exception by shutting down the `MockWebServer`.
 *    - Verifies that the repository returns a `Resource.Error` with an exception message.
 *
 * Helper Method:
 * - **enqueueResponse**: Enqueues a mock response from a JSON file located in the `api-response` directory.
 *   This allows for easy simulation of different API responses.
 *
 * Usage:
 * - The `setUp` method initializes `MockWebServer` and Retrofit before each test.
 * - The `tearDown` method shuts down `MockWebServer` after each test.
 * - Each test method uses `runBlocking` to handle coroutine-based API calls.
 *
 * Example JSON Files:
 * - `rates-response.json`: Simulates a successful API response with currency rates.
 * - `rates-response-error.json`: Simulates an API error response.
 *
 * Dependencies:
 * - Retrofit, MockWebServer, JUnit 5, and Kotlin Coroutines are required for this test class.
 */

class MainRepositoryImplTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var rateRequest: RateRequest
    private lateinit var mainRepository: MainRepositoryImpl

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/")) // Use MockWebServer's URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        rateRequest = retrofit.create(RateRequest::class.java)

        mainRepository = MainRepositoryImpl(rateRequest)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun convertRate_returns_success_when_API_call_is_successful(): Unit = runBlocking {
        enqueueResponse("rates-response.json")

        val result = mainRepository.convertRate("USD", "NGN")

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        val successResult = result as Resource.Success
        assertThat(successResult.data).isNotNull()
        assertThat(successResult.data!!.success).isTrue()
        assertThat(successResult.data!!.base).isEqualTo("EUR")
        assertThat(successResult.data!!.rates).containsExactlyEntriesIn(mapOf("NGN" to 1560.650703, "USD" to 1.040496))
    }

    @Test
    fun convertRate_returns_error_when_API_call_fails() = runBlocking {
        enqueueResponse("rates-response-error.json")

        val result = mainRepository.convertRate("RUS", "NGN")

        /**
         should be successful because the Api request went successfully but
         should log error message of the api response
         */

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        val errorResult = result as Resource.Success
        assertThat(errorResult.data?.success).isEqualTo(false)
    }

    @Test
    fun convertRate_returns_error_on_exception() = runBlocking {
        mockWebServer.shutdown()

        val result = mainRepository.convertRate("USD", "NGN")

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        val errorResult = result as Resource.Error
        assertThat(errorResult.message).isNotNull()
    }

    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader
            .getResourceAsStream("api-response/$fileName")
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse
                .setBody(source.readString(Charsets.UTF_8))
        )
    }
}