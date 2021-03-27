package com.example.project.driven.uuid

import com.example.project.domain.Thing
import com.example.project.domain.NonPredictableThingIdGenerator
import java.util.UUID.randomUUID

class UUIDThingIdGenerator: NonPredictableThingIdGenerator {
    override fun nextId(): Thing.Id = Thing.Id(randomUUID().toString())
}
