package com.example.project.domain

import arrow.core.Either
import arrow.core.Right
import com.example.ddd.DomainDrivenDesign
import com.example.extensions.leftIfNotNull
import com.example.hexagonal.HexagonalArchitecture

@HexagonalArchitecture.DrivenPort
@DomainDrivenDesign.Repository
interface ThingRepository {
    fun add(thing: Thing)

    operator fun get(id: Thing.Id): Thing?

    fun getAll(): Set<Thing>

    fun <R> update(id: Thing.Id, updater: Thing.() -> Either<ThingError, R>): Either<ThingError, R>

    // Method overloading is required until https://youtrack.jetbrains.com/issue/KT-41670 is fixed.
    fun update(id: Thing.Id, updater: ToMaybeThingError): ThingError? = updater.update(id, this)
    fun update(id: Thing.Id, updater: ToThingErrorSet): Set<ThingError> = updater.update(id, this)

    fun remove(id: Thing.Id)
}

interface Updater<T, R> {
    operator fun invoke(thing: Thing): T
    fun update(id: Thing.Id, repository: ThingRepository): R
}

interface SimpleUpdater<R> : Updater<R, R>

fun interface ToMaybeThingError : SimpleUpdater<ThingError?> {
    override fun update(id: Thing.Id, repository: ThingRepository) =
        repository.update(id) {
            Either.leftIfNotNull(invoke(this))
        }.leftOrNull()
}

fun interface ToThingErrorSet : SimpleUpdater<Set<ThingError>> {
    override fun update(id: Thing.Id, repository: ThingRepository) =
        repository.update(id) {
            Right(invoke(this))
        }.fold(::setOf) { it }
}

private fun <L> Either<L, *>.leftOrNull(): L? = swap().orNull()
