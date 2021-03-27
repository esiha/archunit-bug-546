package com.example.hexagonal

import kotlin.annotation.AnnotationTarget.CLASS

/**
 * The Hexagonal Architecture, also known as Ports & Adapters, aims for long-term maintenance of the Domain Code.
 *
 * It separates the Business from the Technology that supports it so that they can evolve at their own pace.
 * The Business is implemented purely in Business Terms inside the Domain part of a Hexagon. No dependency to any
 * Technology should be allowed inside the Domain so that it can evolve with more ease regardless of what Technology
 * is used to integrate it in the real world.
 *
 * The Domain allows communication with it by exposing Ports. These, still expressed using the Business Terms, define
 * the contract between the Domain and the Technology. There are two flavors of Ports: Driving Ports and Driven Ports.
 *
 * Driving Ports are entry points to the Domain, ways to ask the Domain to perform something related to the Business.
 *
 * Driven Ports are exit points of the Domain, ways for the Domain to ask the Technology to perform something.
 *
 * The Technology is integrated by splitting it into Adapters, each of which will have the task of managing the adaptations
 * required from one particular technology (command-line interface, REST controller, SOAP service, databases, ...) so
 * that it can conform to what the Domain defines. Each Adapter will talk to one or more Ports.
 *
 * There are two rules of upmost importance in a Hexagonal Architecture:
 * 1. The Domain must have no compile-time dependency towards any Technology;
 * 2. The Adapters must be independent one of another.
 *
 * These two rules are here to ensure that the Domain and the different forms of Technology can evolve at their own pace.
 *
 * The first rule could be broken sometimes in order to avoid reimplementing a piece of the Domain that already cleanly
 * exists on the shelf. For example, the Joda Money library could be allowed inside a Bank-related Domain because it is
 * a very small library that pulls no other dependencies, and it avoids reimplementing the notions related to manipulating
 * Money. Cases where this rule is broken should be traced.
 */
object HexagonalArchitecture {
    /**
     * This is a Driving Port, as defined by the Hexagonal Architecture.
     *
     * Usually, this annotation would be applied to an interface, implemented by the Domain.
     */
    @Target(CLASS)
    @MustBeDocumented
    annotation class DrivingPort

    /**
     * This is a Driven Port, as defined by the Hexagonal Architecture.
     *
     * Usually, this annotation would be applied to an interface, implemented by Adapters.
     */
    @Target(CLASS)
    @MustBeDocumented
    annotation class DrivenPort
}

