package com.sawrose.toa.preferences

import android.content.SharedPreferences
import com.sawrose.toa.CoroutinesTestRule
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AndroidPreferencesTest {

    @get:Rule
    val coroutineTestRule = CoroutinesTestRule()

    private val mockSharedPreference: SharedPreferences = mockk()
    private val mockEditor: SharedPreferences.Editor = mockk()

    private val androidPreference = AndroidPreferences(
        sharedPreferences = mockSharedPreference
    )

    @Before
    fun setup() {
        every {
            mockSharedPreference.edit()
        } returns mockEditor

        every {
            mockEditor.apply()
        } just runs
    }

    private val testKey = "Test Key"

    @Test
    fun storeNotNullInt() = runTest {
        every {
            mockEditor.putInt(testKey, 5)
        } returns mockEditor

        androidPreference.storeInt(testKey, 5)

        verify {
            mockEditor.putInt(testKey, 5)
            mockEditor.apply()
        }
    }

    @Test
    fun storeNullInt() = runTest {
        every {
            mockEditor.remove(testKey)
        } returns mockEditor

        androidPreference.storeInt(testKey, null)

        verify {
            mockEditor.remove(testKey)
            mockEditor.apply()
        }
    }
}
