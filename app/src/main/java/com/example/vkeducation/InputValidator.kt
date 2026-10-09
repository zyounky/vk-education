package com.example.vkeducation

internal object InputValidator {
    // Allow common formatting, but reject letters, extra '+' signs and URI syntax.
    private val phoneCharacters = Regex("\\+?[0-9()\\s-]+")
    private val normalizedPhone = Regex("\\+?[0-9]{3,15}")

    fun normalizePhoneNumber(value: String): String? {
        val trimmed = value.trim()
        if (!phoneCharacters.matches(trimmed)) return null
        val normalized = trimmed.filter { it in '0'..'9' || it == '+' }
        return normalized.takeIf { normalizedPhone.matches(it) }
    }
}
