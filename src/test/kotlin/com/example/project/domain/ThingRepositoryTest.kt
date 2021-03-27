package com.example.project.domain

import arrow.core.Either
import arrow.core.Left
import com.example.kluent.*
import io.mockk.every
import io.mockk.spyk
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldBeNull
import org.junit.jupiter.api.Test

class ThingRepositoryTest {
    @Test
    internal fun `should be an interface`() {
        ThingRepository::class.shouldBeAnInterface()
    }

    @Test
    internal fun `should be a Repository`() {
        ThingRepository::class.shouldBeARepository()
    }

    @Test
    internal fun `should be a Driven Port`() {
        ThingRepository::class.shouldBeADrivenPort()
    }

    @Test
    internal fun `should declare add method`() {
        ThingRepository::class.shouldDeclareMethod("add", takingTypes = listOf(Thing::class))
    }

    @Test
    internal fun `should declare get method`() {
        ThingRepository::class.shouldDeclareMethod(
            "get",
            takingTypes = listOf(Thing.Id::class),
            returning = Thing::class
        )
    }

    @Test
    internal fun `should declare getAll method`() {
        ThingRepository::class.shouldDeclareMethod("getAll", returning = Set::class)
    }

    @Test
    internal fun `should declare remove method`() {
        ThingRepository::class.shouldDeclareMethod("remove", takingTypes = listOf(Thing.Id::class))
    }

    @Test
    internal fun `should declare update method`() {
        ThingRepository::class.shouldDeclareMethod(
            "update",
            takingTypes = listOf(Thing.Id::class, Function1::class),
            returning = Either::class
        )
    }

    @Test
    internal fun `should declare update with ToMaybeThingError method`() {
        ThingRepository::class.shouldDeclareMethod(
            "update",
            takingTypes = listOf(Thing.Id::class, ToMaybeThingError::class),
            returning = ThingError::class
        )
    }
}

class UpdaterTest {
    @Test
    internal fun `should be an interface`() {
        Updater::class.shouldBeAnInterface()
    }

    @Test
    internal fun `should declare invoke operator`() {
        Updater::class.shouldDeclareMethod("invoke", takingTypes = listOf(Thing::class), returning = Any::class)
    }

    @Test
    internal fun `should declare update method`() {
        Updater::class.shouldDeclareMethod(
            "update",
            takingTypes = listOf(Thing.Id::class, ThingRepository::class),
            returning = Any::class
        )
    }
}

class SimpleUpdaterTest {
    @Test
    internal fun `should be an interface`() {
        SimpleUpdater::class.shouldBeAnInterface()
    }

    @Test
    internal fun `should be an Updater`() {
        object : SimpleUpdater<String> {
            override fun invoke(thing: Thing): String = throw IllegalStateException()
            override fun update(id: Thing.Id, repository: ThingRepository): String = throw IllegalStateException()
        } shouldBeInstanceOf Updater::class
    }
}

class ToMaybeThingErrorTest {
    private val repository = spyk<StubRepository>()

    @Test
    internal fun `should be an interface`() {
        ToMaybeThingError::class.shouldBeAnInterface()
    }

    @Test
    internal fun `should be a function`() {
        ToMaybeThingError::class.shouldBeAFunction()
    }

    @Test
    internal fun `should be a SimpleUpdater`() {
        ToMaybeThingError { throw IllegalStateException() } shouldBeInstanceOf SimpleUpdater::class
    }

    @Test
    internal fun `should return a left when no Thing is found`() {
        val id = Thing.Id("id")
        every { repository.update(eq(id), any<Thing.() -> Either<ThingError, *>>()) } returns Left(ThingNotFound(id))

        repository.update(id, ToMaybeThingError { throw IllegalStateException() }) shouldBeEqualTo ThingNotFound(id)
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    internal fun `should invoke updater on found Thing`() {
        val thing = anThing()
        every { repository.update(eq(thing.id), any<Thing.() -> Either<ThingError, *>>()) } answers {
            (it.invocation.args.last() as Thing.() -> Either<ThingError, *>).invoke(thing)
        }

        repository.update(thing.id, ToMaybeThingError { if (it == thing) null else throw IllegalStateException() })
            .shouldBeNull()
    }
}

class ToThingErrorSetTest {
    private val repository = spyk<StubRepository>()

    @Test
    internal fun `should be an interface`() {
        ToThingErrorSet::class.shouldBeAnInterface()
    }

    @Test
    internal fun `should be a function`() {
        ToThingErrorSet::class.shouldBeAFunction()
    }

    @Test
    internal fun `should be an Updater`() {
        ToThingErrorSet::class shouldBeA Updater::class
    }

    @Test
    internal fun `should return a singleton Set when no Thing is found`() {
        val id = Thing.Id("id")
        every { repository.update(eq(id), any<Thing.() -> Either<ThingError, *>>()) } returns Left(ThingNotFound(id))

        repository.update(id, ToThingErrorSet { error("unreachable") }) shouldBeEqualTo setOf(ThingNotFound(id))
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    internal fun `should execute updater when Thing is found`() {
        val thing = anThing()
        every { repository.update(eq(thing.id), any<Thing.() -> Either<ThingError, *>>()) } answers {
            (it.invocation.args.last() as Thing.() -> Either<ThingError, *>).invoke(thing)
        }

        repository.update(thing.id, ToThingErrorSet { if (it == thing) emptySet() else error("unreachable") })
            .shouldBeEmpty()
    }
}

class StubRepository : ThingRepository {
    override fun add(thing: Thing) = error()
    override fun get(id: Thing.Id): Thing = error()
    override fun getAll(): Set<Thing> = error()
    override fun <R> update(id: Thing.Id, updater: Thing.() -> Either<ThingError, R>): Either<ThingError, R> = error()
    override fun remove(id: Thing.Id) = error()
    private fun error(): Nothing = error("should not be called")
}