package com.example.project.domain

import com.example.kluent.shouldBeADataClass
import com.example.kluent.shouldBeAValueObject
import org.amshove.kluent.*
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.api.Test

class NameTest {
    @Test
    internal fun `should be a Value Object`() {
        Name::class.shouldBeAValueObject()
    }

    @Test
    internal fun `should be a data class`() {
        Name::class.shouldBeADataClass()
    }

    @Test
    internal fun `should fail to create from value too long`() {
        with("".padEnd(71, 'A')) {
            invoking {
                Name(this)
            } shouldThrow IllegalArgumentException::class
        }
    }

    @Test
    internal fun `should create with 70 characters`() {
        invoking {
            Name("".padEnd(70, 'A'))
        } shouldNotThrow AnyException
    }
}