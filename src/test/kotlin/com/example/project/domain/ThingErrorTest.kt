package com.example.project.domain

import com.example.kluent.*
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.Test

class ThingErrorTest {
    @Test
    internal fun `should be sealed`() {
        ThingError::class.shouldBeSealed()
    }

    @Test
    internal fun `should be a value object`() {
        ThingError::class.shouldBeAValueObject()
    }
}

class ActionForbiddenForStateTest {
    @Test
    internal fun `should be an Error`() {
        ActionForbiddenForState(Thing.State.CREATED) shouldBeInstanceOf ThingError::class
    }

    @Test
    internal fun `should be a Value Object`() {
        ActionForbiddenForState::class.shouldBeAValueObject()
    }

    @Test
    internal fun `should be a data class`() {
        ActionForbiddenForState::class.shouldBeADataClass()
    }
}

class MissingMandatoryInformationTest {
    @Test
    internal fun `should be an Error`() {
        MissingMandatoryInformation::class shouldBeA ThingError::class
    }

    @Test
    internal fun `should be a Value Object`() {
        MissingMandatoryInformation::class.shouldBeAValueObject()
    }

    @Test
    internal fun `should be a data class`() {
        MissingMandatoryInformation::class.shouldBeADataClass()
    }
}

class ThingNotFoundTest {
    @Test
    internal fun `should be an Error`() {
        ThingNotFound(Thing.Id("unknown")) shouldBeInstanceOf ThingError::class
    }

    @Test
    internal fun `should be a Value Object`() {
        ThingNotFound::class.shouldBeAValueObject()
    }

    @Test
    internal fun `should be a data class`() {
        ThingNotFound::class.shouldBeADataClass()
    }
}

class NameTooLongTest {
    @Test
    internal fun `should be an Error`() {
        NameTooLong shouldBeInstanceOf ThingError::class
    }

    @Test
    internal fun `should be a Value Object`() {
        NameTooLong::class.shouldBeAValueObject()
    }

    @Test
    internal fun `should be closed`() {
        NameTooLong::class.shouldBeClosed()
    }
}