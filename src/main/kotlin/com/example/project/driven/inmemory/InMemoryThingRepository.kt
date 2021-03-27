package com.example.project.driven.inmemory

import arrow.core.Either
import arrow.core.flatMap
import com.example.project.domain.Thing
import com.example.project.domain.ThingError
import com.example.project.domain.ThingNotFound
import com.example.project.domain.ThingRepository

class InMemoryThingRepository : ThingRepository {
    private val contents = mutableMapOf<Thing.Id, Thing>()

    override fun add(thing: Thing) {
        contents[thing.id] = thing
    }

    override fun get(id: Thing.Id): Thing? = contents[id]

    override fun getAll(): Set<Thing> = contents.values.toSet()

    override fun <R> update(id: Thing.Id, updater: Thing.() -> Either<ThingError, R>): Either<ThingError, R> =
        Either.fromNullable(get(id))
            .mapLeft { ThingNotFound(id) }
            .flatMap { it.updater() }

    override fun remove(id: Thing.Id) {
        contents.remove(id)
    }
}
