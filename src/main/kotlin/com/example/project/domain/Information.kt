package com.example.project.domain

import com.example.ddd.DomainDrivenDesign

interface Information

@DomainDrivenDesign.ValueObject
enum class MandatoryInformation : Information {
    NAME
}

@DomainDrivenDesign.ValueObject
enum class AdditionalInformation : Information {
    DESCRIPTION
}