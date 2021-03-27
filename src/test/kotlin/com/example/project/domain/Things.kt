package com.example.project.domain

import java.time.Instant
import java.time.Instant.now

fun anThing(
    id: Thing.Id = Thing.Id("id"),
    creationDateTime: Instant = now()
) = Thing(id, creationDateTime)

fun aCreatedThing() = anThing()

fun anThingReadyForValidation() = anThing().also { it.modify(mandatoryThingInformation()) }

fun anUnpublishedThing() = anThingReadyForValidation().also { it.validate() }

fun anThingInPublishedState() = anUnpublishedThing().also { it.publish() }

fun mandatoryThingInformation() = Modifications(name = Name("Thingy"))

fun allThingInformation() = mandatoryThingInformation().copy(
    description = Description("Some thing ")
)