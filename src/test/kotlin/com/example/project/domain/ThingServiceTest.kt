package com.example.project.domain

import arrow.core.Either
import com.example.project.driven.inmemory.InMemoryThingRepository
import com.example.kluent.shouldBeADrivingPort
import com.example.kluent.shouldBeAService
import io.mockk.spyk
import io.mockk.verify
import org.amshove.kluent.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Clock.fixed
import java.time.Instant.EPOCH
import java.time.Instant.now
import java.time.ZoneId
import java.time.temporal.ChronoUnit.MINUTES

class ThingServiceTest {
    private val thingIdGenerator = NonPredictableThingIdGenerator { Thing.Id("id") }
    private val thingRepository = spyk(InMemoryThingRepository())
    private val clock = fixed(EPOCH, ZoneId.of("Europe/Paris"))
    private val service = ThingService(thingIdGenerator, thingRepository, clock)

    @Test
    internal fun `should be a Service`() {
        ThingService::class.shouldBeAService()
    }

    @Test
    internal fun `should be a Driving Port`() {
        ThingService::class.shouldBeADrivingPort()
    }

    @Nested
    inner class Creation {
        @Test
        internal fun `should create and store new Thing with next Id`() {
            with(service.createThing()) {
                thingRepository[this].shouldNotBeNull()
            }
        }

        @Test
        internal fun `should create Thing using current date and time`() {
            with(service.createThing()) {
                thingRepository[this]?.creationDateTime shouldBeEqualTo now(clock).truncatedTo(MINUTES)
            }
        }
    }

    @Nested
    inner class Get {
        @Test
        internal fun `should not find non-existent Thing`() {
            service.getThing(Thing.Id("id")).shouldBeNull()
        }

        @Test
        internal fun `should find existing Thing`() {
            with(service.createThing()) {
                val thing = service.getThing(this)
                thing.shouldNotBeNull().id shouldBeEqualTo this
            }
        }
    }

    @Nested
    inner class GetAllThingsTest {
        @Test
        internal fun `should return all known Things when any exist`() {
            with(service) {
                val thingId = createThing()
                val allThings = getAllThings()

                assertSoftly {
                    allThings shouldHaveSize 1
                    allThings shouldContain getThing(thingId)!!
                }
            }
        }
    }

    @Nested
    inner class Deletion {
        @Test
        internal fun `should delete Created Thing`() {
            with(service.createThing()) {
                service.deleteThing(this).shouldBeNull()
                thingRepository[this].shouldBeNull()
            }
        }

        @Test
        internal fun `should not call update when deleting a Created Thing`() {
            with(service.createThing()) {
                service.deleteThing(this)

                verify(exactly = 0) {
                    thingRepository.update(any(), any<Thing.() -> Either<ThingError, *>>())
                }
            }
        }

        @Test
        internal fun `should fail to delete Unpublished Thing`() {
            with(service.createThing()) {
                service.modifyThing(this, mandatoryThingInformation())
                service.validateThing(this)

                service.deleteThing(this) shouldBeInstanceOf ActionForbiddenForState::class
                thingRepository[this].shouldNotBeNull()
            }
        }

        @Test
        internal fun `should fail to delete non-existing Thing`() {
            service.deleteThing(Thing.Id("unknown")) shouldBeInstanceOf ThingNotFound::class
        }
    }

    @Nested
    inner class Validation {
        @Test
        internal fun `should validate Created Thing when valid`() {
            with(service.createThing()) {
                service.modifyThing(this, mandatoryThingInformation())
                service.validateThing(this).shouldBeEmpty()
            }
        }

        @Test
        internal fun `should fail to validate non-existing Thing`() {
            with(Thing.Id("unknown")) {
                service.validateThing(this) shouldContain ThingNotFound(this)
            }
        }
    }

    @Nested
    inner class Modification {
        @Test
        internal fun `should modify thing`() {
            with(service.createThing()) {
                service.modifyThing(this, Modifications(name = Name("name"))).shouldBeNull()

                thingRepository[this]?.name shouldBeEqualTo Name("name")
            }
        }

        @Test
        internal fun `should fail to modify non-existing Thing`() {
            with(Thing.Id("unknown")) {
                service.modifyThing(this, Modifications()) shouldBeEqualTo ThingNotFound(this)
            }
        }
    }

    @Nested
    inner class Publish {
        @Test
        internal fun `should publish existing Thing`() {
            with(service.createThing()) {
                service.modifyThing(this, mandatoryThingInformation())
                service.validateThing(this)

                service.publishThing(this).shouldBeNull()
            }
        }

        @Test
        internal fun `should fail to publish non-existing Thing`() {
            with(Thing.Id("unknown")) {
                service.publishThing(this) shouldBeEqualTo ThingNotFound(this)
            }
        }
    }
}
