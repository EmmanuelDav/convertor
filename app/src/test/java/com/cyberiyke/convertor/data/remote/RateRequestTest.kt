package com.cyberiyke.convertor.data.remote

import com.cyberiyke.convertor.data.remote.model.RatesResult
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class RateRequestTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var service: RateRequest

    @BeforeEach
    fun setUp() {
        mockWebServer = MockWebServer()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/")) // Use MockWebServer's URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(RateRequest::class.java)
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test convertRate returns expected response`(): Unit = runBlocking {
        // Arrange: Enqueue a mock response
        enqueueResponse("rates-response.json")

        // Act: Call the Retrofit service
        val response: Response<RatesResult> = service.convertRate("your_api_key", "USD,NGN")

        // Assert: Verify the request and response
        val request = mockWebServer.takeRequest()
        assertThat(request.path).isEqualTo("/latest?access_key=your_api_key&symbol=USD,NGN")

        val ratesResult = response.body()
        assertThat(ratesResult).isNotNull()
        assertThat(ratesResult!!.success).isTrue()
        assertThat(ratesResult.base).isEqualTo("USD")
        assertThat(ratesResult.date).isEqualTo("2023-10-01") // Update this to match your mock data
        assertThat(ratesResult.rates).containsExactlyEntriesIn(mapOf("NGN" to 450.0, "USD" to 1.0)) // Update this to match your mock data
    }

    private fun enqueueResponse(fileName: String) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
            .setBody(source.readString(Charsets.UTF_8))
        mockWebServer.enqueue(mockResponse)
    }
}