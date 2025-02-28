package com.cyberiyke.convertor.data.repository

import com.cyberiyke.converter.data.remote.RateRequest
import com.cyberiyke.converter.utils.Resource
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainRepositoryImplTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var rateRequest: RateRequest
    private lateinit var mainRepository: MainRepositoryImpl

    @BeforeEach
    fun setUp() {
        mockWebServer = MockWebServer()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/")) // Use MockWebServer's URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        rateRequest = retrofit.create(RateRequest::class.java)

        mainRepository = MainRepositoryImpl(rateRequest)
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `convertRate returns success when API call is successful`(): Unit = runBlocking {
        enqueueResponse("rates-response.json")

        // Act: Call the repository method
        val result = mainRepository.convertRate("USD", "NGN")

        // Assert: Verify the result
        assertThat(result).isInstanceOf(Resource.Success::class.java)
        val successResult = result as Resource.Success
        assertThat(successResult.data).isNotNull()
        assertThat(successResult.data!!.success).isTrue()
        assertThat(successResult.data!!.base).isEqualTo("USD")
        assertThat(successResult.data!!.rates).containsExactlyEntriesIn(mapOf("NGN" to 450.0, "USD" to 1.0))
    }

    @Test
    fun `convertRate returns error when API call fails`() = runBlocking {
        // Arrange: Enqueue a mock error response
        enqueueResponse("rates-response-error.json", responseCode = 400)

        // Act: Call the repository method
        val result = mainRepository.convertRate("USD", "NGN")

        // Assert: Verify the result
        assertThat(result).isInstanceOf(Resource.Error::class.java)
        val errorResult = result as Resource.Error
        assertThat(errorResult.message).isEqualTo("Bad Request")
    }

    @Test
    fun `convertRate returns error on exception`() = runBlocking {
        // Arrange: Simulate a network exception by shutting down the server
        mockWebServer.shutdown()

        // Act: Call the repository method
        val result = mainRepository.convertRate("USD", "NGN")

        // Assert: Verify the result
        assertThat(result).isInstanceOf(Resource.Error::class.java)
        val errorResult = result as Resource.Error
        assertThat(errorResult.message).isNotNull()
    }

    private fun enqueueResponse(fileName: String, responseCode: Int = 200) {
        // Load the mock response JSON file from the resources folder
        val inputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
            .setResponseCode(responseCode)
            .setBody(source.readString(Charsets.UTF_8))
        mockWebServer.enqueue(mockResponse)
    }
}