package com.example.project.domain

import com.example.ddd.DomainDrivenDesign
import com.example.hexagonal.HexagonalArchitecture

@HexagonalArchitecture.DrivenPort
@DomainDrivenDesign.Service
fun interface NonPredictableThingIdGenerator {
    fun nextId(): Thing.Id
}
