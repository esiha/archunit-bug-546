package com.example.extensions

import org.amshove.kluent.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class StringsTest {
    @Nested
    inner class RequireMaximumLengthTest {
        @Test
        internal fun `should throw IllegalArgumentException when length is exceeded`() {
            with("".padEnd(17, 'A')) {
                invoking {
                    this.requireMaximumLength(16)
                } shouldThrow IllegalArgumentException::class withMessage "Value too long (max 16 characters): '$this'"
            }
        }

        @Test
        internal fun `should not throw any exception when maximum length is met`() {
            invoking {
                "".padEnd(16, 'A').requireMaximumLength(16)
            } shouldNotThrow AnyException
        }
    }
}