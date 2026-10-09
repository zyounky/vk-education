package com.example.vkeducation

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.intent.matcher.IntentMatchers.hasType
import androidx.test.espresso.matcher.ViewMatchers.hasErrorText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IntentFlowTest {
    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        Intents.init()
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
        Intents.release()
    }

    @Test
    fun explicitIntentDisplaysOriginalTextAndBackPreservesInput() {
        val text = " Привет из VK Education!\nВторая строка "
        enter(text)
        onView(withId(R.id.open_second_button)).perform(scrollTo(), click())
        Intents.intended(allOf(
            hasComponent(SecondActivity::class.java.name),
            hasExtra(SecondActivity.EXTRA_TEXT, text),
        ))
        onView(withId(R.id.received_text)).check(matches(withText(text)))
        onView(withId(R.id.back_button)).perform(scrollTo(), click())
        onView(withId(R.id.input_text)).check(matches(withText(text)))
    }

    @Test
    fun dialIntentContainsNormalizedNumber() {
        Intents.intending(hasAction(Intent.ACTION_DIAL)).respondWith(ActivityResult(Activity.RESULT_OK, null))
        enter("+7 (999) 123-45-67")
        onView(withId(R.id.dial_button)).perform(scrollTo(), click())
        Intents.intended(hasAction(Intent.ACTION_DIAL))
        val dial = Intents.getIntents().single { it.action == Intent.ACTION_DIAL }
        assertEquals("tel", dial.data?.scheme)
        assertEquals("+79991234567", dial.data?.schemeSpecificPart)
    }

    @Test
    fun shareOpensChooserWithPlainTextSendIntent() {
        Intents.intending(hasAction(Intent.ACTION_CHOOSER)).respondWith(ActivityResult(Activity.RESULT_OK, null))
        val text = "Текст для отправки"
        enter(text)
        onView(withId(R.id.share_button)).perform(scrollTo(), click())
        Intents.intended(hasAction(Intent.ACTION_CHOOSER))
        val chooser = Intents.getIntents().single { it.action == Intent.ACTION_CHOOSER }
        @Suppress("DEPRECATION")
        val send = chooser.getParcelableExtra<Intent>(Intent.EXTRA_INTENT)!!
        assertTrue(hasAction(Intent.ACTION_SEND).matches(send))
        assertTrue(hasType("text/plain").matches(send))
        assertEquals(text, send.getStringExtra(Intent.EXTRA_TEXT))
    }

    @Test
    fun blankInputIsRejectedForEveryAction() {
        enter("   ")
        listOf(R.id.open_second_button, R.id.dial_button, R.id.share_button).forEach { button ->
            onView(withId(button)).perform(scrollTo(), click())
            onView(withId(R.id.input_text)).check(matches(hasErrorText(resource(R.string.error_empty_text))))
        }
        assertTrue(Intents.getIntents().none {
            it.action == Intent.ACTION_DIAL || it.action == Intent.ACTION_CHOOSER ||
                it.component?.className == SecondActivity::class.java.name
        })
    }

    @Test
    fun invalidPhoneIsRejectedWithoutLaunchingDialer() {
        enter("Позвони мне")
        onView(withId(R.id.dial_button)).perform(scrollTo(), click())
        onView(withId(R.id.input_text)).check(matches(hasErrorText(resource(R.string.error_invalid_phone))))
        assertTrue(Intents.getIntents().none { it.action == Intent.ACTION_DIAL })
    }

    @Test
    fun inputSurvivesActivityRecreation() {
        enter("Сохранить после поворота")
        scenario.recreate()
        onView(withId(R.id.input_text)).check(matches(withText("Сохранить после поворота")))
    }

    @Test
    fun receivedTextSurvivesActivityRecreation() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(context, SecondActivity::class.java)
            .putExtra(SecondActivity.EXTRA_TEXT, "Сохраняемый текст")
        ActivityScenario.launch<SecondActivity>(intent).use {
            it.recreate()
            onView(withId(R.id.received_text)).check(matches(withText("Сохраняемый текст")))
        }
    }

    private fun enter(text: String) {
        onView(withId(R.id.input_text)).perform(scrollTo(), replaceText(text), closeSoftKeyboard())
    }

    private fun resource(id: Int): String =
        InstrumentationRegistry.getInstrumentation().targetContext.getString(id)
}
