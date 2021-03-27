package com.example.project.domain

import com.example.ddd.DomainDrivenDesign
import com.example.project.domain.MandatoryInformation.NAME
import com.example.project.domain.Thing.State.*
import java.time.Instant
import java.time.temporal.ChronoUnit.MINUTES

@DomainDrivenDesign.Entity(identifiedBy = Thing.Id::class)
@DomainDrivenDesign.AggregateRoot
class Thing(val id: Id, creationDateTime: Instant) {
    val creationDateTime: Instant = creationDateTime.truncatedTo(MINUTES)
    var state = CREATED
        private set
    var name: Name? = null
        private set
    var description: Description? = null
        private set

    val canBeValidated: Boolean get() = checkCanBeValidated() == null
    val canBeDeleted: Boolean get() = state.allowsDeletion
    val canBePublished: Boolean get() = state.allowsPublication

    fun validate(): Set<ThingError> = checkCanBeValidated() ?: run {
        state = UNPUBLISHED
        emptySet()
    }

    private fun checkCanBeValidated(): Set<ThingError>? =
        sequenceOf(
            ::checkAllowsValidation,
            ::checkAllMandatoryFieldsAreDefined
        ).map { it() }.firstOrNull { it != null }

    private fun checkAllowsValidation() = state.checkAllowsValidation()?.run(::setOf)

    private fun checkAllMandatoryFieldsAreDefined() =
        mapOf(
            NAME to name,
        ).filterValues { it == null }
            .keys
            .map(::MissingMandatoryInformation)
            .takeIf { it.isNotEmpty() }
            ?.toSet()

    fun modify(modifications: Modifications): ActionForbiddenForState? =
        state.checkAllowsValidation() ?: run {
            assign(modifications)
            null
        }

    private fun assign(modifications: Modifications) {
        modifications.name?.let { name = it }
        modifications.description?.let { description = it }
    }

    fun publish(): ActionForbiddenForState? =
        state.checkAllowsPublication() ?: run {
            state = PUBLISHED
            null
        }

    fun checkCanBeDeleted(): ThingError? = state.checkAllowsDeletion()

    override fun equals(other: Any?) = when {
        this === other -> true
        javaClass != other?.javaClass -> false
        else -> id == (other as Thing).id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    @DomainDrivenDesign.ValueObject
    data class Id(val value: String)

    enum class State(
        val allowsDeletion: Boolean = false,
        val allowsValidation: Boolean = false,
        val allowsPublication: Boolean = false
    ) {
        CREATED(allowsDeletion = true, allowsValidation = true),
        UNPUBLISHED(allowsPublication = true),
        PUBLISHED;

        fun checkAllowsPublication() = checkAllows(allowsPublication)
        fun checkAllowsValidation() = checkAllows(allowsValidation)
        fun checkAllowsDeletion() = checkAllows(allowsDeletion)

        private fun checkAllows(allowed: Boolean) = if (allowed) null else ActionForbiddenForState(
            this
        )
    }
}
