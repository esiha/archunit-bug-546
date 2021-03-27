package com.example.project.domain

import com.example.kluent.shouldBeADataClass
import com.example.kluent.shouldBeAValueObject
import org.amshove.kluent.*
import org.junit.jupiter.api.Test

class DescriptionTest {
    @Test
    internal fun `should be a Value Object`() {
        Description::class.shouldBeAValueObject()
    }

    @Test
    internal fun `should be a data class`() {
        Description::class.shouldBeADataClass()
    }

    @Test
    internal fun `should fail to create from 1501-character long value`() {
        with("".padEnd(1_501, 'D')) {
            invoking {
                Description(this)
            } shouldThrow IllegalArgumentException::class
        }
    }

    @Test
    internal fun `should create from 1500-character long value`() {
        invoking {
            Description("".padEnd(1500, 'D'))
        } shouldNotThrow AnyException
    }
}