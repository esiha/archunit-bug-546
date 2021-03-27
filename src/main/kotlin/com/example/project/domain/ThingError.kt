package com.example.project.domain

import com.example.ddd.DomainDrivenDesign
import java.time.Instant

@DomainDrivenDesign.ValueObject
sealed class ThingError

@DomainDrivenDesign.ValueObject
data class ActionForbiddenForState(val state: Thing.State) : ThingError()

@DomainDrivenDesign.ValueObject
data class MissingMandatoryInformation(val missingInformation: MandatoryInformation) : ThingError()

@DomainDrivenDesign.ValueObject
data class ThingNotFound(val id: Thing.Id) : ThingError()

@DomainDrivenDesign.ValueObject
object NameTooLong : ThingError()