package com.example.project.domain

import com.example.ddd.DomainDrivenDesign
import com.example.hexagonal.HexagonalArchitecture
import java.time.Clock
import java.time.Instant.now

@DomainDrivenDesign.Service
@HexagonalArchitecture.DrivingPort
interface ThingService {
    fun createThing(): Thing.Id
    fun getThing(thingId: Thing.Id): Thing?
    fun getAllThings(): Set<Thing>
    fun deleteThing(thingId: Thing.Id): ThingError?
    fun validateThing(thingId: Thing.Id): Set<ThingError>
    fun modifyThing(thingId: Thing.Id, modifications: Modifications): ThingError?
    fun publishThing(thingId: Thing.Id): ThingError?

    companion object {
        operator fun invoke(
            thingIdGenerator: NonPredictableThingIdGenerator,
            thingRepository: ThingRepository,
            clock: Clock
        ): ThingService = Implementation(thingIdGenerator, thingRepository, clock)
    }

    private class Implementation(
        private val thingIdGenerator: NonPredictableThingIdGenerator,
        private val thingRepository: ThingRepository,
        private val clock: Clock
    ) : ThingService {
        override fun createThing() = thingIdGenerator.nextId().also { thingRepository.add(
            Thing(
                it,
                now(clock)
            )
        ) }

        override fun getThing(thingId: Thing.Id) = thingRepository[thingId]

        override fun getAllThings() = thingRepository.getAll()

        override fun deleteThing(thingId: Thing.Id): ThingError? =
            thingRepository[thingId]?.let { thing ->
                return thing.checkCanBeDeleted() ?: run {
                    thingRepository.remove(thingId)
                    null
                }
            } ?: ThingNotFound(thingId)

        override fun validateThing(thingId: Thing.Id) =
            thingRepository.update(thingId, ToThingErrorSet { it.validate() })

        override fun modifyThing(thingId: Thing.Id, modifications: Modifications) =
            thingRepository.update(thingId, ToMaybeThingError { it.modify(modifications) })

        override fun publishThing(thingId: Thing.Id) = thingRepository.update(thingId, ToMaybeThingError {
            it.publish()
        })
    }
}