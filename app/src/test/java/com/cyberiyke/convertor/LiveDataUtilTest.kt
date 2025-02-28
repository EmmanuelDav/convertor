package com.cyberiyke.convertor

/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.annotation.VisibleForTesting


import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * A utility function for testing that collects the first value emitted by a [StateFlow]
 * and returns it. If the [StateFlow] does not emit a value within the specified timeout,
 * a [TimeoutException] is thrown.
 *
 * @param time The maximum time to wait for the value.
 * @param timeUnit The unit of time for the timeout.
 * @return The first value emitted by the [StateFlow].
 * @throws TimeoutException If the [StateFlow] does not emit a value within the timeout.
 */
@VisibleForTesting(otherwise = VisibleForTesting.NONE)
fun <T> StateFlow<T>.getOrAwaitValueTest(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    return runBlocking {
        try {
            // Convert the time to milliseconds for withTimeout
            val timeoutMillis = timeUnit.toMillis(time)
            withTimeout(timeoutMillis) {
                this@getOrAwaitValueTest.first()
            }
        } catch (e: TimeoutException) {
            throw TimeoutException("StateFlow value was never set within the timeout.")
        }
    }
}