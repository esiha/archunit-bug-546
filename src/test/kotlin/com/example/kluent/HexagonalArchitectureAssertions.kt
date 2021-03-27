package com.example.kluent

import com.example.hexagonal.HexagonalArchitecture.DrivenPort
import com.example.hexagonal.HexagonalArchitecture.DrivingPort
import org.amshove.kluent.should
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

fun KClass<*>.shouldBeADrivingPort() = this.should("Be a Hexagonal Architecture Driving Port") {
    this.hasAnnotation<DrivingPort>()
}

fun KClass<*>.shouldBeADrivenPort() = this.should("Be a Hexagonal Architecture Driven Port") {
    this.hasAnnotation<DrivenPort>()
}