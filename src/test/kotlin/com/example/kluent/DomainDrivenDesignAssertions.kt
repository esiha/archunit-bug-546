package com.example.kluent

import com.example.ddd.DomainDrivenDesign.AggregateRoot
import com.example.ddd.DomainDrivenDesign.Entity
import com.example.ddd.DomainDrivenDesign.Repository
import com.example.ddd.DomainDrivenDesign.Service
import com.example.ddd.DomainDrivenDesign.ValueObject
import org.amshove.kluent.should
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

fun KClass<*>.shouldBeAValueObject() = this.should("Be a DDD Value Object") {
    this.hasAnnotation<ValueObject>()
}

infix fun KClass<*>.shouldBeAnEntityIdentifiedBy(idType: KClass<*>) =
    this.should("Be a DDD Entity identified by DDD Value Object type ${idType.simpleName}") {
        this.hasAnnotation<Entity>() && with(this.findAnnotation<Entity>()!!) {
            this.identifiedBy == idType && idType.hasAnnotation<ValueObject>()
        }
    }

fun KClass<*>.shouldBeAnAggregateRoot() = this.should("Be a DDD Aggregate Root") {
    this.hasAnnotation<AggregateRoot>()
}

fun KClass<*>.shouldBeAService() = this.should("Be a DDD Service") {
    this.hasAnnotation<Service>()
}

fun KClass<*>.shouldBeARepository() = this.should("Be a DDD Repository") {
    this.hasAnnotation<Repository>()
}