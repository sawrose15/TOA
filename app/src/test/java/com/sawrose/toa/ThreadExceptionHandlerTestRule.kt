package com.sawrose.toa

import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * A custom test watcher that sets the default uncaught exception handler so that any exceptions
 * inside of a viewmodel scope will actually fail a test.
 *
 * Inspiration: https://github.com/Kotlin/kotlinx.coroutines/issues/1205#issuecomment-880411987
 */
class ThreadExceptionHandlerTestRule : TestWatcher() {
    private var previousThread: Thread.UncaughtExceptionHandler? = null

    override fun starting(
        description: Description,
    ) {
        super.starting(description)

        previousThread = Thread.getDefaultUncaughtExceptionHandler()

        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            throw throwable
        }
    }

    override fun finished(
        description: Description,
    ) {
        super.finished(description)
        Thread.setDefaultUncaughtExceptionHandler(previousThread)
    }
}
