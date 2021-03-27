package com.example.project.domain

import com.example.ddd.DomainDrivenDesign
import com.example.extensions.requireMaximumLength

@DomainDrivenDesign.ValueObject
data class Name(val value: String) {
    init {
        value.requireMaximumLength(70)
    }
}