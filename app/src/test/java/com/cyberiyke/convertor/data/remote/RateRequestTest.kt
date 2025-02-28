package com.cyberiyke.convertor.data.remote

import com.cyberiyke.convertor.BuildConfig
import com.cyberiyke.convertor.data.remote.model.RatesResult
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 *
 Over here we test the conversion rate to return expected response
 this is important to simulate the api call in the RateRequest call

 */

class RateRequestTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var service: RateRequest

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/")) // Use MockWebServer's URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(RateRequest::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test convertRate returns expected response`(): Unit = runBlocking {
        // Arrange: Enqueue a mock response
        enqueueResponse("rates-response.json")

        // Act: Call the Retrofit service
        val response: Response<RatesResult> = service.convertRate(BuildConfig.API_KEY, "USD,NGN")

        // Assert: Verify the request and response
        val request = mockWebServer.takeRequest()
        assertThat(request.path).isEqualTo("/latest?access_key=${BuildConfig.API_KEY}&symbol=USD%2CNGN")

        val ratesResult = response.body()
        assertThat(ratesResult).isNotNull()
        assertThat(ratesResult!!.success).isTrue()
        assertThat(ratesResult.base).isEqualTo("EUR")
        assertThat(ratesResult.date).isEqualTo("2025-02-28") // Update this to match your mock data
        assertThat(ratesResult.rates).containsExactlyEntriesIn(mapOf("NGN" to 1560.650703, "USD" to 1.040496))
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