package com.example.extensions

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class LeftIfNotNullTest {
    @Test
    internal fun `should be a Right of Unit when null`() {
        Either.leftIfNotNull(null) shouldBeEqualTo Right(Unit)
    }

    @Test
    internal fun `should be a Left when null`() {
        Either.leftIfNotNull("error") shouldBeEqualTo Left("error")
    }
}