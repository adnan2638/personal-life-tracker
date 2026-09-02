package com.adnan.personallifetracker

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test fun appContext_hasExpectedPackageName() = assertEquals("com.adnan.personallifetracker", InstrumentationRegistry.getInstrumentation().targetContext.packageName)
}
