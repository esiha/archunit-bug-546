package com.example.extensions

import java.time.Instant
import java.time.temporal.ChronoUnit.MINUTES

fun Instant.plusMinutes(amount: Long): Instant = plus(amount, MINUTES)
fun Instant.minusMinutes(amount: Long): Instant = minus(amount, MINUTES)
