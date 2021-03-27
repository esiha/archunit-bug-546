package com.example.project.domain

import com.example.project.domain.AdditionalInformation.*
import com.example.project.domain.MandatoryInformation.*
import com.example.kluent.shouldBeAValueObject
import com.example.kluent.shouldBeAnInterface
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldContainSame
import org.junit.jupiter.api.Test

class InformationTest {
    @Test
    internal fun `should be an interface`() {
        Information::class.shouldBeAnInterface()
    }
}

class MandatoryInformationTest {
    @Test
    internal fun `should have only known values`() {
        MandatoryInformation.values() shouldContainSame arrayOf(NAME)
    }

    @Test
    internal fun `should be a Value Object`() {
        MandatoryInformation::class.shouldBeAValueObject()
    }

    @Test
    internal fun `should be an Information`() {
        NAME shouldBeInstanceOf Information::class
    }
}

class AdditionalInformationTest {
    @Test
    internal fun `should have only known values`() {
        AdditionalInformation.values() shouldContainSame arrayOf(DESCRIPTION)
    }

    @Test
    internal fun `should be a Value Object`() {
        AdditionalInformation::class.shouldBeAValueObject()
    }

    @Test
    internal fun `should be an Information`() {
        DESCRIPTION shouldBeInstanceOf Information::class
    }
}