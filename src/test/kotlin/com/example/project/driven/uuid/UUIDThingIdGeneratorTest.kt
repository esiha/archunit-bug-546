package com.example.project.driven.uuid

import com.example.project.domain.NonPredictableThingIdGenerator
import org.amshove.kluent.AnyException
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldNotThrow
import org.junit.jupiter.api.Test
import java.util.*

class UUIDThingIdGeneratorTest {
    private val generator = UUIDThingIdGenerator()

    @Test
    internal fun `should be a NonPredictableThingIdGenerator`() {
        generator shouldBeInstanceOf NonPredictableThingIdGenerator::class
    }

    @Test
    internal fun `should use UUID to generate Thing Ids`() {
        invoking {
            UUID.fromString(generator.nextId().value)
        } shouldNotThrow AnyException
    }
}