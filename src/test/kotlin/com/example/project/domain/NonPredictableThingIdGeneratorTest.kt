package com.example.project.domain

import com.example.kluent.shouldBeADrivenPort
import com.example.kluent.shouldBeAService
import com.example.kluent.shouldBeAFunction
import com.example.kluent.shouldBeAnInterface
import com.example.kluent.shouldDeclareMethod
import org.junit.jupiter.api.Test

class NonPredictableThingIdGeneratorTest {
    @Test
    internal fun `should be a functional interface`() {
        NonPredictableThingIdGenerator::class.shouldBeAnInterface()
        NonPredictableThingIdGenerator::class.shouldBeAFunction()
    }

    @Test
    internal fun `should be a Service`() {
        NonPredictableThingIdGenerator::class.shouldBeAService()
    }

    @Test
    internal fun `should be a Driven Port`() {
        NonPredictableThingIdGenerator::class.shouldBeADrivenPort()
    }

    @Test
    internal fun `should declare nextId method`() {
        NonPredictableThingIdGenerator::class.shouldDeclareMethod("nextId", returning = Thing.Id::class)
    }
}