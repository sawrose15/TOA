package com.sawrose.toa.preferences

import com.google.common.truth.Truth.assertThat
import com.sawrose.toa.fake.FakePreferences
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UserPreferencesTest {
    private val fakePreferences = FakePreferences()
    private lateinit var userPreferences: UserPreferences

    @Before
    fun setUp() {
        userPreferences = UserPreferences(
            preferences = fakePreferences,
        )
    }

    @Test
    fun readWriteNumTasksPerDay() = runTest {
        val testNumTasksPerDay = 5

        userPreferences.setPreferredNumTasksPerDay(testNumTasksPerDay)
        val result = userPreferences.getPreferredNumTasksPerDay()
        assertThat(result).isEqualTo(testNumTasksPerDay)
    }

    @Test
    fun getDefaultNumTasksPerDay() = runTest {
        val preferredNumTasks = userPreferences.getPreferredNumTasksPerDay()
        assertThat(preferredNumTasks).isNull()
    }
}
