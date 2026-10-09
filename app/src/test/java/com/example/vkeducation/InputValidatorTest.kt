package com.example.vkeducation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class InputValidatorTest {
    @Test
    fun formattedInternationalNumberIsNormalized() {
        assertEquals("+79991234567", InputValidator.normalizePhoneNumber(" +7 (999) 123-45-67 "))
    }

    @Test
    fun localAndShortNumbersAreAccepted() {
        assertEquals("89991234567", InputValidator.normalizePhoneNumber("8 999 123 45 67"))
        assertEquals("112", InputValidator.normalizePhoneNumber("112"))
    }

    @Test
    fun emptyAndNonPhoneInputIsRejected() {
        listOf("", "   ", "Привет!", "abc123", "tel:123", "+", "---", "12").forEach {
            assertNull("Should reject: $it", InputValidator.normalizePhoneNumber(it))
        }
    }

    @Test
    fun misplacedPlusAndOverlongNumbersAreRejected() {
        listOf("++79991234567", "7+9991234567", "1234567890123456").forEach {
            assertNull("Should reject: $it", InputValidator.normalizePhoneNumber(it))
        }
        assertEquals("+123456789012345", InputValidator.normalizePhoneNumber("+123456789012345"))
    }
}
