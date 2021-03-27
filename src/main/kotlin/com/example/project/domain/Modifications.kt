package com.example.project.domain

import com.example.ddd.DomainDrivenDesign
import java.time.Instant
import java.time.ZoneId

@DomainDrivenDesign.ValueObject
data class Modifications(
    val name: Name? = null,
    val description: Description? = null
)