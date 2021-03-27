package com.example.kluent

import org.amshove.kluent.should
import kotlin.reflect.KClass
import kotlin.reflect.full.allSuperclasses
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure

fun KClass<*>.shouldBeAnInterface() = this.should("an interface") { this.java.isInterface }
fun KClass<*>.shouldBeAFunction() = this.shouldBe("a function", KClass<*>::isFun)
fun KClass<*>.shouldBeADataClass() = this.shouldBe("a data class", KClass<*>::isData)
fun KClass<*>.shouldBeSealed() = this.shouldBe("a sealed class", KClass<*>::isSealed)
fun KClass<*>.shouldBeClosed() = this.shouldBe("closed") { !isOpen }
fun KClass<*>.shouldBeAbstract() = this.shouldBe("abstract", KClass<*>::isAbstract)

fun KClass<*>.shouldBe(message: String, predicate: (KClass<*>) -> Boolean) =
    this.should("$this should be $message") { predicate(this) }

fun KClass<*>.shouldDeclareMethod(
    name: String,
    takingTypes: List<KClass<*>> = emptyList(),
    returning: KClass<*> = Unit::class
) =
    this.should("$this should declare a method named $name, returning $returning and taking types $takingTypes") {
        this.declaredMemberFunctions.any {
            it.name == name
                    && it.returnType.jvmErasure == returning
                    && it.valueParameters.map { it.type.jvmErasure } == takingTypes
        }
    }

infix fun KClass<*>.shouldBeA(type: KClass<*>) =
    this.shouldBe("a ${type.simpleName}") { allSuperclasses.contains(type) }