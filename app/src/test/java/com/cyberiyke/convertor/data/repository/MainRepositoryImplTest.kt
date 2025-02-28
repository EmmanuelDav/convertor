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

        val result = mainRepository.convertRate("USD", "NGN")

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        val errorResult = result as Resource.Error
        assertThat(errorResult.message).isEqualTo("Bad Request")
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