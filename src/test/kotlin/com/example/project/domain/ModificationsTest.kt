package com.example.project.domain

import com.example.kluent.shouldBeADataClass
import com.example.kluent.shouldBeAValueObject
import org.junit.jupiter.api.Test

class ModificationsTest {
    @Test
    internal fun `should be a Value Object`() {
        Modifications::class.shouldBeAValueObject()
    }

    @Test
    internal fun `should be a data class`() {
        Modifications::class.shouldBeADataClass()
    }
}