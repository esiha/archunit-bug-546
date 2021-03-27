package com.example.project.driven.inmemory

import arrow.core.Left
import arrow.core.right
import com.example.project.domain.*
import org.amshove.kluent.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class InMemoryThingRepositoryTest {
    private val repository = InMemoryThingRepository()

    @Test
    internal fun `should be an ThingRepository`() {
        repository shouldBeInstanceOf ThingRepository::class
    }

    @Test
    internal fun `should not get non-existent Thing`() {
        repository[Thing.Id("id")].shouldBeNull()
    }

    @Test
    internal fun `should get existing Thing`() {
        with(anThing()) {
            repository.add(this)

            repository[id] shouldBe this
        }
    }

    @Nested
    inner class GetAllTest {
        @Test
        internal fun `should return empty when no Thing exist`() {
            repository.getAll().shouldBeEmpty()
        }

        @Test
        internal fun `should get all known Things when any exist`() {
            with(repository) {
                val thing = anThing()
                add(thing)

                getAll() shouldBeEqualTo setOf(thing)
            }
        }
    }

    @Test
    internal fun `should noop when removing non-existing Thing`() {
        invoking { repository.remove(Thing.Id("id")) } shouldNotThrow AnyException
    }

    @Test
    internal fun `should remove existing Thing`() {
        with(anThing()) {
            repository.add(this)

            repository.remove(id)

            repository[id].shouldBeNull()
        }
    }

    @Test
    internal fun `should update existing Thing`() {
        with(anThing()) {
            repository.add(this)

            repository.update(id) {
                modify(Modifications(name = Name("thingy")))
                right()
            } shouldBeEqualTo right()

            repository[id]?.name shouldBeEqualTo Name("thingy")
        }
    }

    @Test
    internal fun `should not update non-existing Thing`() {
        Thing.Id("id").let {
            repository.update(it) {
                modify(Modifications(name = Name("thingy")))
                right()
            } shouldBeEqualTo Left(ThingNotFound(it))
        }
    }

    @Test
    internal fun `should short-circuit only when Thing does not exist`() {
        with(anThing()) {
            repository.add(this)

            repository.update(id) {
                Left(ActionForbiddenForState(state))
            } shouldBeEqualTo Left(ActionForbiddenForState(state))
        }
    }
}