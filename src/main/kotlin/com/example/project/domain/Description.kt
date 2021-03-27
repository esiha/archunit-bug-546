package com.example.project.domain

import com.example.ddd.DomainDrivenDesign
import com.example.extensions.requireMaximumLength

@DomainDrivenDesign.ValueObject
data class Description(val value: String) {
    init {
        value.requireMaximumLength(1_500)
    }
}
