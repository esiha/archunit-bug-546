package com.example.project.domain

import com.example.kluent.shouldBeAValueObject
import com.example.kluent.shouldBeAnAggregateRoot
import com.example.kluent.shouldBeAnEntityIdentifiedBy
import com.example.project.domain.MandatoryInformation.NAME
import com.example.project.domain.Thing.State.PUBLISHED
import com.example.project.domain.Thing.State.UNPUBLISHED
import org.amshove.kluent.*
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import java.time.Instant.now
import java.time.temporal.ChronoUnit.MINUTES

class ThingTest {

    @Test
    internal fun `should be equal to another created with same id`() {
        anThing(id = Thing.Id("id")) shouldBeEqualTo anThing(id = Thing.Id("id"))
    }

    @Test
    internal fun `should be an Entity`() {
        Thing::class.shouldBeAnEntityIdentifiedBy(Thing.Id::class)
    }

    @Test
    internal fun `should be an Aggregate Root`() {
        Thing::class.shouldBeAnAggregateRoot()
    }

    @Test
    internal fun `should keep the creation date and time truncated by the minute`() {
        val now = now()
        with(anThing(creationDateTime = now)) {
            creationDateTime shouldBeEqualTo now.truncatedTo(MINUTES)
        }
    }

    @Nested
    inner class Modify {
        @Test
        internal fun `should modify when Created`() {
            aCreatedThing().modify(Modifications(name = Name("name"))).shouldBeNull()
        }

        @Test
        internal fun `should map fields from provided modifications`() {
            with(aCreatedThing()) {
                val modifications = allThingInformation()
                modify(modifications)
                name shouldBeEqualTo modifications.name
                description shouldBeEqualTo modifications.description
            }
        }

        @Test
        internal fun `should only modify non null fields`() {
            with(aCreatedThing()) {
                modify(Modifications(name = Name("name")))
                modify(Modifications())
                name shouldBeEqualTo Name("name")
            }
        }

        @Test
        internal fun `should not modify when Unpublished`() {
            with(anUnpublishedThing()) {
                modify(Modifications()) shouldBeEqualTo ActionForbiddenForState(state)
            }
        }
    }

    @Nested
    inner class Delete {
        @Test
        internal fun `should verify to success when in state Created`() {
            aCreatedThing().checkCanBeDeleted().shouldBeNull()
        }

        @Test
        internal fun `should verify to failure when in state Unpublished`() {
            with(anUnpublishedThing()) {
                checkCanBeDeleted() shouldBeEqualTo ActionForbiddenForState(state)
            }
        }

        @Test
        internal fun `should tell when it can be deleted`() {
            aCreatedThing().canBeDeleted shouldBe true
        }

        @Test
        internal fun `should tell it cannot be deleted when not in created state`() {
            anUnpublishedThing().canBeDeleted shouldBe false
        }
    }

    @Nested
    inner class Validate {
        @Test
        internal fun `should tell when it can be validated`() {
            anThingReadyForValidation().canBeValidated shouldBe true
        }

        @Test
        internal fun `should tell it cannot be validated when missing mandatory information`() {
            aCreatedThing().canBeValidated shouldBe false
        }

        @Test
        internal fun `should tell it cannot be validated when not in created state`() {
            anUnpublishedThing().canBeValidated shouldBe false
        }

        @Test
        internal fun `should validate when Created and all mandatory information is defined`() {
            with(anThingReadyForValidation()) {
                validate().shouldBeEmpty()
                state shouldBe UNPUBLISHED
            }
        }

        @TestFactory
        internal fun `should fail to validate when Created but mandatory information is missing`() =
            mapOf<MandatoryInformation, (Modifications) -> Modifications>(
                NAME to { it.copy(name = null) }
            ).map {
                dynamicTest(it.key.name) {
                    with(aCreatedThing()) {
                        modify(it.value(mandatoryThingInformation()))
                        validate() shouldBeEqualTo setOf(MissingMandatoryInformation(it.key))
                    }
                }
            }

        @Test
        internal fun `should not Validate when Unpublished`() {
            with(anUnpublishedThing()) {
                validate() shouldBeEqualTo setOf(ActionForbiddenForState(state))
            }
        }

        @Test
        internal fun `should mention all missing fields`() {
            with(aCreatedThing()) {
                validate() shouldContainSame setOf(NAME).map(::MissingMandatoryInformation)
            }
        }
    }

    @Nested
    inner class Publish {
        @Test
        internal fun `should tell when it can be published`() {
            anUnpublishedThing().canBePublished shouldBe true
        }

        @Test
        internal fun `should tell it cannot be published when Created`() {
            anThing().canBePublished shouldBe false
        }

        @Test
        internal fun `should tell it cannot be published when Published`() {
            anThingInPublishedState().canBePublished shouldBe false
        }

        @Test
        internal fun `should publish when Unpublished`() {
            with(anUnpublishedThing()) {
                publish().shouldBeNull()

                state shouldBe PUBLISHED
            }
        }

        @Test
        internal fun `should fail to publish when not Unpublished`() {
            with(aCreatedThing()) {
                publish() shouldBeEqualTo ActionForbiddenForState(state)

                state shouldNotBe PUBLISHED
            }
        }
    }

    @Nested
    inner class ThingIdTest {
        @Test
        internal fun `should be equal to another with same value`() {
            Thing.Id("value") shouldBeEqualTo Thing.Id("value")
        }

        @Test
        internal fun `should be a Value Object`() {
            Thing.Id::class.shouldBeAValueObject()
        }
    }
}
