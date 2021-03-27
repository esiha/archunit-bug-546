package com.example.extensions

import arrow.core.Either
import arrow.core.Left
import arrow.core.right

fun <L> Either.Companion.leftIfNotNull(value: L?): Either<L, Unit> = value?.let(::Left) ?: Unit.right()